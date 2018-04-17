package com.keyten.spider.collect;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.keyten.base.bean.TBLCollectWeb;
import com.keyten.base.util.LogUtil;
import com.keyten.spider.collect.IKAnalyzer.CosineSimilarAlgorithm;
import com.keyten.spider.collect.IKAnalyzer.IKAnalyzerUtil;
import com.keyten.spider.service.TBLCollectWebService;
import com.keyten.spider.service.TBLHotNewsService;

/**
 * 采集各个新闻站，并对比新闻是否相似度是否符合条件，符合条件的生成事件，并存储url和标题
 * @author liym
 * 2018-03-26
 * 1 分别采集本目标下的新闻信息别分别存储到不同的map中
 * 2 每次取一个网站标杆，与其他往后在哪中的新闻对比相似度，分析到热点新闻并暂存
 * 3 根据热点新闻 进行分词 并计算出本次热点事件
 * 4 存储
 */
@Component
public class SpreadNewsAnalysis{
	@Autowired
	TBLCollectWebService collectWebService;
	@Autowired
	TBLHotNewsService hotNewsService;
	@Autowired
	BaseCollectUrl collectUrl;
	//用于存储每次新闻对比的相似新闻信息  String：url  string[]{title,columnid}
	private Map<String,String[]> titles = new HashMap<String,String[]>();
	//最少在   （0.5）  的网站中找到了相似度较高的文章
	private double minLike = 0.5;
	//当前匹配站点数
	private double count = 0;
	
	//分别用于存储每个网站采集到的新闻信息   string：url   string[]:newsinfo
	private Map<String,String[]> wy = null;
	private Map<String,String[]> sina = null;
	private Map<String,String[]> sohu = null;
	private Map<String,String[]> ifeng = null;
	private Map<String,String[]> paper = null;
	private Map<String,String[]> tencent = null;
	public void compare(String targetId){
		List<TBLCollectWeb> weblist = collectWebService.getCollectWebByTargetId(targetId);
		if(weblist.size()>0){
			//保存目标下每个网站对应的 规则信息  （比如澎湃就又两个地址，两种规则）采集的时候回循环 每一种规则
			List<TBLCollectWeb> wyColumns = new ArrayList<>();
			List<TBLCollectWeb> sinaColumns = new ArrayList<>();
			List<TBLCollectWeb> sohuColumns = new ArrayList<>();
			List<TBLCollectWeb> ifengColumns = new ArrayList<>();
			List<TBLCollectWeb> paperColumns = new ArrayList<>();
			List<TBLCollectWeb> tencentColumns = new ArrayList<>();
			List<TBLCollectWeb> wyJsonColumn = new ArrayList<>();
			for(TBLCollectWeb column : weblist){
				String webtype = column.getWebtype();
				if(webtype.equals("SINA")){
					sinaColumns.add(column);
				}else if(webtype.equals("THEPAPER")){
					paperColumns.add(column);
				}else if(webtype.equals("WANGYI")){
					wyColumns.add(column);
				}else if(webtype.equals("SOHU")){
					sohuColumns.add(column);
				}else if(webtype.equals("IFENG")){
					ifengColumns.add(column);
				}else if(webtype.equals("TENCENT")){
					tencentColumns.add(column);
				}else if(webtype.equals("WANGYIJSON")){
					wyJsonColumn.add(column);
				}
			}
			if(wyJsonColumn.size()>0){
				collectUrl.setColumns(wyJsonColumn);
				collectUrl.setCharSet("GBK");
				collectUrl.setContentType("json");
				count++;
				wy = collectUrl.collectIndex();
			}
			collectUrl.setContentType("html");
			if(wyColumns.size()>0){
				collectUrl.setColumns(wyColumns);
				collectUrl.setCharSet("GBK");
				count++;
				wy = collectUrl.collectIndex();
			}
			if(sinaColumns.size()>0){
				collectUrl.setColumns(sinaColumns);
				collectUrl.setCharSet("UTF-8");
				count++;
				sina = collectUrl.collectIndex();
			}
			if(sohuColumns.size()>0){
				collectUrl.setColumns(sohuColumns);
				collectUrl.setCharSet("GBK");
				count++;
				sohu = collectUrl.collectIndex();
			}
			if(ifengColumns.size()>0){
				collectUrl.setColumns(ifengColumns);
				collectUrl.setCharSet("UTF-8");
				count++;
				ifeng = collectUrl.collectIndex();
			}
			if(paperColumns.size()>0){
				collectUrl.setColumns(paperColumns);
				collectUrl.setCharSet("UTF-8");
				count++;
				paper = collectUrl.collectIndex();
			}
			if(tencentColumns.size()>0){
				collectUrl.setColumns(tencentColumns);
				collectUrl.setCharSet("GBK");
				count++;
				tencent = collectUrl.collectIndex();
			}
			//采集完成后开始执行匹配对比返回 key：事件   list：新闻列表
			Map<String,List<String[]>> newsmap = compareIndex();
			if(newsmap.size()>0){
				//持久化保存新闻和事件
				saveData(newsmap,targetId);
			}
			count = 0;
		}else{
			System.out.println("该热点目标未关联网站栏目信息");
		}
	}
	
	/**
	 * 对比相似度   找到热点新闻   分析热事件
	 * @return map<事件，本事件新闻列表>
	 */
	private Map<String,List<String[]>> compareIndex(){
		//用于保存返回数据
		Map<String,List<String[]>> newsMap = new HashMap<>();
		try {
			//标杆map
			Set<Map<String,String[]>> mset = new HashSet<>();
			String setStr = "";
			while(true){
				mset.clear();
				//用于判断有没有作为标杆比对过
				boolean ifadd = false;
				//这些判断是将没有作为过标杆的    全部存到mset中
				if(setStr.indexOf("sina")<0&&sina!=null&&sina.size()>0)mset.add(sina);
				if(setStr.indexOf("163")<0&&wy!=null&&wy.size()>0)mset.add(wy);
				if(setStr.indexOf("sohu")<0&&sohu!=null&&sohu.size()>0)mset.add(sohu);
				if(setStr.indexOf("ifeng")<0&&ifeng!=null&&ifeng.size()>0)mset.add(ifeng);
				if(setStr.indexOf("paper")<0&&paper!=null&&paper.size()>0)mset.add(paper);
				if(setStr.indexOf("tencent")<0&&tencent!=null&&tencent.size()>0)mset.add(tencent);
				//拿到本次的标杆列表信息 mset剩余不作为标杆的集合   用于和标杆信息对比
				Map<String,String[]> map = mset.iterator().next();
				mset.remove(map);
				//循环遍历本次标杆集合中的每一条url与未作为标杆的集合做对比，查找相似文章
				for(String url:map.keySet()){
					if(!ifadd){
						//新的一次匹配，将本次标杆列表的网站类型保存下来，表示已经匹配过
						if(url.indexOf("sina.com")>-1)setStr += "sina;";
						if(url.indexOf("163.com")>-1)setStr += "163;";
						if(url.indexOf("sohu.com")>-1)setStr += "sohu;";
						if(url.indexOf("ifeng.com")>-1)setStr += "ifeng;";
						if(url.indexOf("thepaper.cn")>-1)setStr += "thepaper;";
						if(url.indexOf("qq.com")>-1)setStr += "tencent;";
						ifadd = true;
					}
					//每次循环标杆中的一条新闻信息
					String[] newsinfo = map.get(url);
					String title = newsinfo[0];
					String columnid = newsinfo[1];
					titles.put(url, new String[]{title,columnid});
					//获取相似文章，保存到titles中，    mset每次会修改，
					Set<Map<String,String[]>> cset = new HashSet<Map<String,String[]>>();
					cset.addAll(mset);
					getSameNews(cset);
					
					//判断相同的个数
					double sameCount = titles.size();
					//具有相似新闻的网站数/总网站数 = 相似新闻同时在多大比例的网站中出现过
					double same = sameCount/count;
					/* 比例大于等于 minLike设置的阈值，则判定为热点新闻，
					 * 比如0.6说明一半以上出现过阈值为0.5，那么就可以判定为热点新闻
					 * */
					if(same>=minLike&&sameCount>1){
						//调用热点事件分析方法，组装出 map<热点事件,新闻[]>
						newsMap.putAll(getHotThing());
					}
					//清空titles
					titles.clear();
				}
				double shengxia = mset.size();
				if(shengxia/count<0.5||shengxia==1){
					System.out.println("剩余的站点不足50%，匹配不到热点新闻！剩余"+shengxia);
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return newsMap;
	}
	/*
	 * 递归或者迭代的方式计算新闻相似度
	 * 每次拿出一个列表和titles中已经存在的新闻作对比，
	 * 如果和titles中任意一条新闻相似度较高，则认为匹配到相似文章
	 * 将文章从对应的集合删除，并加入到titles中
	 */
	private void getSameNews(Set<Map<String,String[]>> mset){
		if(mset.size()>0){
			//取出一个文章列表
			Map<String,String[]> cmap = mset.iterator().next();
			mset.remove(cmap);
			label:for(String u:cmap.keySet()){
				String[] news = cmap.get(u);
				String t = news[0];
				for(String url:titles.keySet()){
					String title = titles.get(url)[0];
					//计算相似度
					double samedu = CosineSimilarAlgorithm.getSimilarity(title,t);
					if(samedu>0.50){
						titles.put(u,news);
						if(u.indexOf("sina.com")>-1){
							sina.remove(u);
						}else if(u.indexOf("sohu.com")>-1){
							sohu.remove(u);
						}else if(u.indexOf("163.com")>-1){
							wy.remove(u);
						}else if(u.indexOf("ifeng.com")>-1){
							ifeng.remove(u);
						}else if(u.indexOf("qq.com")>-1){
							tencent.remove(u);
						}else if(u.indexOf("thepaper.cn")>-1){
							paper.remove(u);
						}
						break label;
					}
				}
				
			}
			this.getSameNews(mset);
		}
		
	}
	/**
	 * 根据titles中的热点新闻分析对应的热点事件
	 * @throws IOException 
	 */
	private Map<String,List<String[]>> getHotThing() throws IOException {
		Map<String,List<String[]>> newsMap = new HashMap<>();
		//此处应该将titles中的内容保存下来
		List<String[]> slist = new ArrayList<String[]>();
		for(String str:titles.keySet()){
			String[] nowInfo = new String[3];
			nowInfo[0] = str;//url
			nowInfo[1] = titles.get(str)[0];//title
			nowInfo[2] = titles.get(str)[1];//columnid
			slist.add(nowInfo);
		}
		//IK分词结果
		List<String> first  = IKAnalyzerUtil.segWord(slist.get(0)[1]);
		int sCount = slist.size();
		StringBuffer sb = new StringBuffer();
		for(String str:first){
			int allCount = 1;
			for(int c = 1 ; c<sCount ;c++){
				String ss = slist.get(c)[1];
				if(ss.indexOf(str)>-1){
					allCount++;
				}
			}
			if(sCount<=(allCount+1)){
				sb.append(str);
			}
		}
		newsMap.put(sb.toString(),slist);
		return newsMap;
	}
	/**
	 * 热点新闻分析完毕后的信息存储
	 * 只有需要保存才会进入此方法
	 * @param newsmap <thing,String[]{url,title,columnid}>
	 * @param targetId
	 */
	private void saveData(Map<String,List<String[]>> newsmap,String targetId){
		Iterator<String> it = newsmap.keySet().iterator();
		while(it.hasNext()){
			try {
				//得到本次的事件标题
				String thing = (String) it.next();
				List<String[]> newslist = newsmap.get(thing);
				hotNewsService.saveHotNewsAndThing(thing,newslist,targetId);
			}catch (Exception e) {
				e.printStackTrace();
				LogUtil.addLogError("热点信息存储失败",this.getClass().getName());
			}
			
		}
	}
}
