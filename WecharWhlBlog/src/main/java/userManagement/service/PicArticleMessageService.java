package userManagement.service;



import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import userManagement.domain.Article;
import userManagement.domain.PicArticleMessage;
import userManagement.util.MessageUtil;

@Service
public class PicArticleMessageService implements PicArticleMessageServiceImp{

	public String retPicArticleMessage(String ToUserName, String FromUserName) {
		// TODO Auto-generated method stub
		PicArticleMessage picArticleMessage=new PicArticleMessage();
		picArticleMessage.setToUserName(ToUserName);
		picArticleMessage.setFromUserName(FromUserName);
		picArticleMessage.setCreateTime(new Date().toString());
		picArticleMessage.setMsgType("news");
		picArticleMessage.setArticleCount("1");
		
		List <Article> articles=new ArrayList<Article>();
		Article article=new Article();
		article.setDescription("原来是姜慧聪-。-");
		article.setTitle("快看，那是谁");
		article.setPicUrl("http://18161186.ngrok.natapp.cn/WecharWhlBlog/images/1.jpg");
		article.setUrl("https://www.baidu.com/");
		articles.add(article);
		
		picArticleMessage.setArticles(articles);
		String msgxml=MessageUtil.PicArticleMessagetoXml(picArticleMessage);
		return msgxml;
	}

}
