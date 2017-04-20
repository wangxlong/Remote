package userManagement.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import net.sf.json.JSONObject;



public class WeixinReqUtil {

	//JHC
	public  static final String AppID="wxf45302271e8879be";
	public  static final String AppSecret="2f38e88017ab54ef207012f8e9216547";
	
	//测试号
	//public  static final String AppID="wx5f97a2035b7413a0";
	//public  static final String AppSecret="bf3ff33282704e5d7c9fa45efcf9457e";
	
	public static final String media_id_2="2rVUfa2yrhuerZJXoX-gOvJ2eH3LMsvmAWT-Y6FYT7cPp9GOTfIrJsuxMQbPGOxW";
	public static final String addMenu="https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";
	
	/**
	 * doget请求url，返回参数值
	 * @param url
	 * @return
	 */
	public static JSONObject doGetStr(String url){
		@SuppressWarnings("deprecation")
		HttpClient httpClient=new DefaultHttpClient();
		HttpGet httpGet=new HttpGet(url);
		JSONObject jsonObject=null;
		
		try {
			HttpResponse response=httpClient.execute(httpGet);
			HttpEntity entity=response.getEntity();
			if(entity!=null){
				String result=EntityUtils.toString(entity, "utf-8");
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
	 * dopost 请求url，返回参数值
	 * @param url
	 * @param outstr
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static JSONObject doPostStr(String url,String outstr){
		HttpClient httpClient=new DefaultHttpClient();
		HttpPost httpPost=new HttpPost(url);
		JSONObject jsonObject=null;		
		try {
			httpPost.setEntity(new StringEntity(outstr,"UTF-8"));
			try {
				HttpResponse response=httpClient.execute(httpPost);
				HttpEntity entity=response.getEntity();
				if(entity!=null){
					String result=EntityUtils.toString(entity, "utf-8");
					jsonObject=JSONObject.fromObject(result);					
				}					
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		
		}finally{
			
		}
	
		return jsonObject;
	}
	
}
