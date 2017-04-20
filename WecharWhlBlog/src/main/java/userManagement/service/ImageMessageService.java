package userManagement.service;



import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import userManagement.domain.Article;
import userManagement.domain.ImageMessage;
import userManagement.domain.ImageMessageItem;
import userManagement.domain.PicArticleMessage;
import userManagement.util.MessageUtil;
import userManagement.util.WeixinReqUtil;

@Service
public class ImageMessageService implements ImageMessageServiceImp{

	public String retImageMessage(String ToUserName, String FromUserName) {
		// TODO Auto-generated method stub
		ImageMessage imageMessage=new ImageMessage();
		imageMessage.setToUserName(ToUserName);
		imageMessage.setFromUserName(FromUserName);
		imageMessage.setCreateTime(new Date().toString());
		imageMessage.setMsgType("image");
		ImageMessageItem imageMessageItem=new ImageMessageItem();
		imageMessageItem.setMediaId(WeixinReqUtil.media_id_2);
		imageMessage.setImage(imageMessageItem);
		String msgxml=MessageUtil.ImageMessagetoXml(imageMessage);
		return msgxml;
	}

}
