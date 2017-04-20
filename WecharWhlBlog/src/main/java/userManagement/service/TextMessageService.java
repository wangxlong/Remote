package userManagement.service;

import java.util.Date;

import org.springframework.stereotype.Service;

import userManagement.domain.TextMessage;
import userManagement.util.MessageUtil;

@Service
public class TextMessageService implements TextMessageServiceImp{

	public String retTextMessage(String ToUserName, String FromUserName, String Content) {
		// TODO Auto-generated method stub
		TextMessage text=new TextMessage();
		text.setFromUserName(FromUserName);
		text.setToUserName(ToUserName);
		text.setContent(Content);
		text.setCreateTime(new Date().toString());
		text.setMsgType("text");
		//System.out.println("text:"+text);
		String msgxml=MessageUtil.TextMessageToXml(text);	
		return msgxml;
	}

}
