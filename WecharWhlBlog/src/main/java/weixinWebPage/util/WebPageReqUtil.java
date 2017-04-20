package weixinWebPage.util;

import java.io.IOException;
import java.util.Date;
import java.util.Random;

import javax.servlet.ServletContext;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.web.context.ContextLoader;

import net.sf.json.JSONObject;
import userManagement.util.GET_ACCESS_TOKEN;
import userManagement.util.SHA1;

public class WebPageReqUtil {
	
	public static final String jsapi_ticket_url="https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=ACCESS_TOKEN&type=jsapi";

	/**
	 * get 请求
	 * @param url
	 * @return
	 */
	public static JSONObject doConnection(String url){
		@SuppressWarnings("deprecation")
		HttpClient httpClient=new DefaultHttpClient();
		HttpGet httpGet=new HttpGet(url);
		JSONObject jsonObject=null;
		try {
			HttpResponse response=httpClient.execute(httpGet);
			HttpEntity entity=response.getEntity();
			if(entity!=null){
				String result=EntityUtils.toString(entity, "utf-8");
				//System.out.println("result:"+result);
				jsonObject=JSONObject.fromObject(result);
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jsonObject;
	}
	
	/**
	 * 随机生成nonceStr
	 * @param numberSize
	 * @return
	 */
	public static String geneNonceStr(int numberSize){
		StringBuffer noncestr=new StringBuffer();
		char[] alpha=new char[]{'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z',
				'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z',
				'1','2','3','4','5','6','7','8','9','0'};
		Random r = new Random();
		for(int j=0;j<numberSize;j++){
			noncestr.append(r.nextInt(66));
		}
		return noncestr.toString();
	}
	
	/**
	 * 获得用户授权后的access_token和openid
	 * @return
	 */
	public static JSONObject getUserConfirmedDate(String url){
		JSONObject jsonObject=WebPageReqUtil.doConnection(url);
		//System.out.println(jsonObject);
		return jsonObject;
		
	}
	
	public static String getAccess_token(){
		ServletContext application =ContextLoader.getCurrentWebApplicationContext().getServletContext();
		return application.getAttribute("access_token").toString();
	}
	
	/**
	 * 获得jsapi_ticket
	 * @return
	 */
	public static String getjsapi_ticket(){
		String access_token=GET_ACCESS_TOKEN.retAccess_Token();
		String new_jspai_ticke_url=WebPageReqUtil.jsapi_ticket_url.replace("ACCESS_TOKEN", access_token);
		JSONObject jsonObject=doConnection(new_jspai_ticke_url);
		System.out.println(jsonObject);
		String jsapi_ticket=jsonObject.get("ticket").toString();
		return jsapi_ticket;		
	}
	
	/**
	 * 加密,得到signature
	 * @return
	 */
	public static String getsignature(String nonceStr,String timestamp,String url){
		String jsapi_ticket=getjsapi_ticket();
		//String url="";
		//String nonceStr=geneNonceStr(16);
		String sortValue="jsapi_ticket=JSAPI_TICKET&noncestr=NONCESTR&timestamp=TIMESTAMP&url=URL";
		String signature=sortValue.replace("JSAPI_TICKET", jsapi_ticket).replace("NONCESTR", nonceStr).replace("TIMESTAMP", timestamp).replace("URL", url);
		return new SHA1().getDigestOfString(signature.getBytes());
	}
}
