package userManagement.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import userManagement.domain.TextMessage;
import userManagement.service.ImageMessageServiceImp;
import userManagement.service.PicArticleMessageServiceImp;
import userManagement.service.TextMessageServiceImp;
import userManagement.util.CheckUtil;
import userManagement.util.GET_ACCESS_TOKEN;
import userManagement.util.MessageUtil;

@Controller
public class WechatConnection {

	@Autowired
	TextMessageServiceImp textMessageService;
	
	@Autowired
	PicArticleMessageServiceImp picArticleMessageServiceImp;
	
	@Autowired
	ImageMessageServiceImp imageMessageServiceImp;
	
	@RequestMapping(value="/wechar/blog.json",method=RequestMethod.GET)
	public void weCharCheck(HttpServletRequest request,HttpServletResponse response) throws IOException{
		
		PrintWriter out=response.getWriter();
		String signature=request.getParameter("signature");
		String timestamp=request.getParameter("timestamp");
		String nonce=request.getParameter("nonce");
		String echostr=request.getParameter("echostr");		
		/*
		 * System.out.println("signature:"+signature);
		 * System.out.println("timestamp:"+timestamp);
		 * System.out.println("nonce:"+nonce);
		 * System.out.println("echostr:"+echostr);
		 */		 
		if(CheckUtil.checkUrl(signature, timestamp, nonce)){
			out.print(echostr);
		}		
	}
  
	@RequestMapping(value="/wechar/blog.json",method=RequestMethod.POST)
	public void dealMessage(HttpServletRequest request,HttpServletResponse response) throws IOException, DocumentException{
		//request.setCharacterEncoding("utf-8");
		//response.setCharacterEncoding("utf-8");
		//System.out.println("deal:"+"哈哈");
		
		Map<String,String> map=MessageUtil.xmlToMap(request);
		System.out.println(map);
		String toUserName=map.get("ToUserName");
		String fromUserName=map.get("FromUserName");
		String createTime=map.get("CreateTime");
		String msgType=map.get("MsgType");
		String content=map.get("Content");
		String msgId=map.get("MsgId");
		String msgxml=null;
		if("text".equals(msgType)){
			if(content.equals("1")){
				msgxml=textMessageService.retTextMessage(fromUserName, toUserName, "哈哈");
			}
			if(content.equals("2")){
				msgxml=picArticleMessageServiceImp.retPicArticleMessage(fromUserName, toUserName);
				//GET_ACCESS_TOKEN.execute();
				//ServletContext application = request.getServletContext();
				//System.out.println(application.getAttribute("access_token"));
			}
			if(content.equals("3")){
				msgxml=imageMessageServiceImp.retImageMessage(fromUserName, toUserName);
			}
		}if("event".equals(msgType)){
			if("click1".equals(map.get("EventKey"))){
				msgxml=textMessageService.retTextMessage(fromUserName, toUserName, "click1");
			}
			if("location_select".equals(map.get("Event"))){
				
				//msgxml=textMessageService.retTextMessage(fromUserName, toUserName, "click1");
			}
			if("subscribe".equals(map.get("Event"))){
				System.out.println("订阅");
			}
			if("unsubscribe".equals(map.get("Event"))){
				System.out.println("取消订阅");
			}
		}
		System.out.println(msgxml);
		response.getWriter().print(msgxml);
		
		
		
		
	}
	
}
