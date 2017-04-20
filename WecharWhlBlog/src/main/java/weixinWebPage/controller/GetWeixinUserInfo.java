package weixinWebPage.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import net.sf.json.JSONObject;
import weixinWebPage.util.FieldUtil;
import weixinWebPage.util.WebPageReqUtil;

@Controller
public class GetWeixinUserInfo {

	
	@RequestMapping(value="/wxWebpage/userConfirm")
	public String userConfirm(RedirectAttributes redirectAttributes) throws UnsupportedEncodingException{
		System.out.println("wwwwwww");
		String redirect_url="http://41ec1a0e.ngrok.natapp.cn/WecharWhlBlog/wxWebpage/getCode";
		String confirm_url=FieldUtil.CONFIRM_URL.replace("APPID", FieldUtil.appID).replace("REDIRECT_URI",URLEncoder.encode(redirect_url,"utf-8")).replace("SCOPE","snsapi_userinfo");
		//WebPageReqUtil.doConnection(confirm_url);
		return "redirect:"+confirm_url;
	}
	
	@RequestMapping(value="/wxWebpage/getCode")
	public String getCode(HttpServletRequest request,Model model){
		String code=request.getParameter("code");
		String get_access_token_url=FieldUtil.GET_WXPAGE_ACCESS_TOKEN.replace("APPID",FieldUtil.appID).replace("SECRET",FieldUtil.appsecret).replace("CODE", code);	
		JSONObject jsonObject=WebPageReqUtil.getUserConfirmedDate(get_access_token_url);
		String access_token=jsonObject.get("access_token").toString();
		String openid=jsonObject.get("openid").toString();
		String get_user_info_url=FieldUtil.GET_USER_INFO.replace("ACCESS_TOKEN",access_token).replace("OPENID", openid);
		JSONObject userinfo_jsonObject=WebPageReqUtil.doConnection(get_user_info_url);
		System.out.println("userinfo_jsonObject:"+userinfo_jsonObject);
		model.addAttribute("userinfo_jsonObject", userinfo_jsonObject);
		return "userinfo";
	}
}
