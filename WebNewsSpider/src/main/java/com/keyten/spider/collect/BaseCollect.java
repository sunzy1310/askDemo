package com.keyten.spider.collect;

import java.net.URI;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;

import com.keyten.base.util.HtmlUtil;

public class BaseCollect {
	/**
	 * 网页提取
	 * @param url
	 * @param charset
	 * @return
	 * @throws Exception
	 */
	public String getHtml(String url,String charset) throws Exception{
		charset = charset.equals("GB2312")?"GBK":charset;
		String body = "";
		//采用绕过验证的方式处理https请求
		SSLContext sslcontext = createIgnoreVerifySSL();
        // 设置协议http和https对应的处理socket链接工厂的对象
        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
            .register("http", PlainConnectionSocketFactory.INSTANCE)
            .register("https", new SSLConnectionSocketFactory(sslcontext))
            .build();
        PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
        HttpClients.custom().setConnectionManager(connManager);
        //创建自定义的httpclient对象
		CloseableHttpClient client = HttpClients.custom().setConnectionManager(connManager).build();
		
		
		//创建get方式请求对象
//		HttpGet httpGet = new HttpGet(url);
		HtmlUtil htmlUtil = new HtmlUtil();
		String uu = htmlUtil.urlEncoder(url, charset);
		URI uri = htmlUtil.fmtUrl2Uri(uu);
		HttpGet httpGet = new HttpGet(uri);
		if(url.contains("%"))
			httpGet = new HttpGet(url);
		//设置header信息
//		httpGet.setHeader("Content-type", "application/x-www-form-urlencoded");
		httpGet.setHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
		//设置超时时间
		RequestConfig requestConfig = RequestConfig.custom()
				.setConnectTimeout(5000).setConnectionRequestTimeout(1000)  
		        .setSocketTimeout(5000).build();  
		httpGet.setConfig(requestConfig);  
		 // 依次是代理地址，代理端口号，协议类型  
//		HttpHost proxy = new HttpHost("127.0.0.1", 49729);  
//		RequestConfig config = RequestConfig.custom().setProxy(proxy).build();  
//		httpGet.setConfig(config);  
		
		//执行请求操作，并拿到结果（同步阻塞）
		CloseableHttpResponse response = client.execute(httpGet);
		//获取结果实体
		HttpEntity entity = response.getEntity();
		if (entity != null) {
			//按指定编码转换结果实体为String类型
			body = EntityUtils.toString(entity, charset);
		}
		EntityUtils.consume(entity);
		//释放链接
		response.close();
		httpGet.abort();
		client.close();
        return body;
	}
	/**
	 * 绕过验证
	 * 	
	 * @return
	 * @throws NoSuchAlgorithmException 
	 * @throws KeyManagementException 
	 */
	public SSLContext createIgnoreVerifySSL() throws NoSuchAlgorithmException, KeyManagementException {
		SSLContext sc = SSLContext.getInstance("SSLv3");
		X509TrustManager trustManager = new X509TrustManager() {
			
			public X509Certificate[] getAcceptedIssuers() {
				// TODO Auto-generated method stub
				return null;
			}
			
			public void checkServerTrusted(X509Certificate[] arg0, String arg1)
					throws java.security.cert.CertificateException {
				// TODO Auto-generated method stub
				
			}
			
			public void checkClientTrusted(X509Certificate[] arg0, String arg1)
					throws java.security.cert.CertificateException {
				// TODO Auto-generated method stub
				
			}
		};

		sc.init(null, new TrustManager[] { trustManager }, null);
		return sc;
	}
}
