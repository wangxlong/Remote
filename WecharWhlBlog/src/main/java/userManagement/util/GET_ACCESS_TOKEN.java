package userManagement.util;

import javax.servlet.ServletContext;

import org.springframework.web.context.ContextLoader;

import net.sf.json.JSONObject;

public class GET_ACCESS_TOKEN {

	private static String getTokenUrl="https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
	/**
	 * 周期性调用，返回access_token
	 */
	public static void execute(){
		String url=getTokenUrl.replaceAll("APPID", WeixinReqUtil.AppID).replaceAll("APPSECRET", WeixinReqUtil.AppSecret);
		JSONObject jsonObject=WeixinReqUtil.doGetStr(url);
		//System.out.println(jsonObject.get("access_token"));
		//System.out.println(jsonObject.get("expires_in"));
		ServletContext application =ContextLoader.getCurrentWebApplicationContext().getServletContext();
		application.setAttribute("access_token", (String) jsonObject.get("access_token"));
		//return (String) jsonObject.get("access_token");
	}
	/**
	 * 执行函数返回access_token
	 * @return
	 */
	public static String  retAccess_Token(){
		String url=getTokenUrl.replaceAll("APPID", WeixinReqUtil.AppID).replaceAll("APPSECRET", WeixinReqUtil.AppSecret);
		JSONObject jsonObject=WeixinReqUtil.doGetStr(url);
		//System.out.println(jsonObject.get("access_token"));
		//System.out.println(jsonObject.get("expires_in"));
		//ServletContext application =ContextLoader.getCurrentWebApplicationContext().getServletContext();
		return (String) jsonObject.get("access_token");
		//return (String) jsonObject.get("access_token");
	}
}
