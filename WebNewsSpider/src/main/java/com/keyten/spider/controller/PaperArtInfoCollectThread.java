package com.keyten.spider.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.keyten.base.bean.TBLNewspaperWebConfig;
import com.keyten.base.bean.TBLPost;
import com.keyten.base.util.LogUtil;
import com.keyten.base.util.SpringContextUtil;
import com.keyten.spider.collect.PaperArtCollect;
import com.keyten.spider.service.TBLNewspaperWebConfigService;
/**
 * 报刊部分文章内容采集调度线程
 * @author liym
 * 2018-04-09
 */
@Component
public class PaperArtInfoCollectThread implements Runnable{
	@Autowired
	SpringContextUtil springContextUtil;
	@Autowired
	TBLNewspaperWebConfigService newspaperWebCfgService;
	
//	private String preId = "";
//	private List<TBLPost> list = new ArrayList<>();
	@Override
	public void run() {
		while(true){
			try{
				if(CollectPaperHelper.artQueue.size()>0){
					TBLPost post = CollectPaperHelper.artQueue.take();
					collect(post);
				}else{
					LogUtil.addLogInfo("暂无【文章内容】信息采集任务",this.getClass().getName());
					Thread.sleep(2000);
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	/**
	 * 这种方法在没有代理Ip的时候可以一定程度上缓解同时访问一个网站的压力，
	 * 但是如果有代理IP，则没有必要这么做了
	 * 
	 * 如果使用这个方法，那么就不调用collect了，直接调用这个方法
	 * 
	 * 采集文章内容具体方法
	 * 为保证两次获取到的地址是不在同一个栏目下的（防止封禁，变相增加每个栏目的访问间隔）
	 * 在本类中加入一个列表 list（list作为因重复栏目放出去的信息，拥有优先采集权）
	 * 每次先判断list里是否有数据
	 *  1 有数据
	 *  	取得第一条信息同preId中保存的上一次采集地址的栏目id对比
	 *  	相同
	 *  		与 （2 没有数据处理方式一致）
	 *  	不相同
	 *  		采集本条信息
	 *  2 没有数据
	 *  	从artTaskList取得第一条信息同preId中保存的上一次采集地址的栏目id对比
	 *  	相同
	 *  		将本地址保存到list中
	 *  	不相同
	 *   		采集本条信息
	 *   
	 */
	/*private void getArtInfo(){
		try {
			TBLPost post = null;
			if(list.size() > 0){
				post = list.get(0);
				if(post.getColumnid().equals(preId)){
					getArtByArtTaskList();
				}else{
					collect(post);
					list.remove(0);
				}
			}else{
				getArtByArtTaskList();
			}
		}catch(Exception e){
			e.printStackTrace();
		} 
	}
	private void getArtByArtTaskList() throws Exception{
		TBLPost post = CollectHelper.artQueue.take();
		if(post.getColumnid().equals(preId)){
			list.add(post);
		}else{
			collect(post);
		}
	}*/
	private void collect(TBLPost post){
		try {
			String artUrl = post.getArturl();
			String columnid = post.getColumnid();
			TBLNewspaperWebConfig newspaperWebCfg = CollectPaperHelper.allColAndRules.get(columnid);
			if(null == newspaperWebCfg) {
				newspaperWebCfg = newspaperWebCfgService.getWebCfgAndRulesById(columnid);
				CollectPaperHelper.allColAndRules.put(columnid, newspaperWebCfg);
			}
			PaperArtCollect paperArtCollect = 
					(PaperArtCollect) springContextUtil.getBean("newsArtCollect");
			paperArtCollect.setArtUrl(artUrl);
			paperArtCollect.setNewspaperWebCfg(newspaperWebCfg);
			//扔到线程池里去执行吧
			CollectPaperHelper.executor.execute(paperArtCollect);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void test() {
		System.out.println("into this thread!!!");
	}
}
