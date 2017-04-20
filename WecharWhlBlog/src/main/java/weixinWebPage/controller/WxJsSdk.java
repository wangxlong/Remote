package weixinWebPage.controller;

import java.util.Date;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import weixinWebPage.util.FieldUtil;
import weixinWebPage.util.WebPageReqUtil;

@Controller
public class WxJsSdk {

	@RequestMapping(value="/toWxJsFunction")
	public String WxJsSdkconfig(Model model){
		
		String appid=FieldUtil.appID;
		String timestamp=Long.toString(new Date().getTime());
		String nonceStr=WebPageReqUtil.geneNonceStr(16);
		String url="http://41ec1a0e.ngrok.natapp.cn/WecharWhlBlog/toWxJsFunction"; //当前网页的url,用来加密生成signature
		String signature=WebPageReqUtil.getsignature(nonceStr, timestamp, url);
		model.addAttribute("appid", appid);
		model.addAttribute("timestamp", timestamp);
		model.addAttribute("nonceStr", nonceStr);
		model.addAttribute("signature", signature);
		return "WxJsSdk";
	}
}
