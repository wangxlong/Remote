package userManagement.service;



import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import net.sf.json.JSONObject;
import userManagement.domain.Article;
import userManagement.domain.Button;
import userManagement.domain.ClickButton;
import userManagement.domain.ImageMessage;
import userManagement.domain.ImageMessageItem;
import userManagement.domain.Menu;
import userManagement.domain.PicArticleMessage;
import userManagement.domain.ViewButton;
import userManagement.util.MessageUtil;
import userManagement.util.WeixinReqUtil;

@Service
public class ButtonService implements ButtonServiceImp{

	public String retButtonStruction() {
		// TODO Auto-generated method stub
		//clickbutton1
		ClickButton clickButton1=new ClickButton();
		clickButton1.setName("简介");
		clickButton1.setType("click");
		clickButton1.setKey("click1");
		//viewButton1
		ViewButton viewButton1=new ViewButton();
		viewButton1.setName("博客园");
		viewButton1.setType("view");
		String url="https://www.baidu.com";
		viewButton1.setUrl(url);
		//clickButton2的子菜单1
		ClickButton subclickButton1=new ClickButton();
		subclickButton1.setName("11subbutton1");
		subclickButton1.setType("scancode_push");
		subclickButton1.setKey("subclick1");
		//clickButton2的子菜单1
		ClickButton subclickButton2=new ClickButton();
		subclickButton2.setName("点击subbutton2");
		subclickButton2.setType("location_select");
		subclickButton2.setKey("subclick2");
		//clickButton2的子菜单1
		ClickButton subclickButton3=new ClickButton();
		subclickButton3.setName("点击subbutton3");
		subclickButton3.setType("pic_sysphoto");
		subclickButton3.setKey("subclick3");
		//clickButton2
		ClickButton clickButton2=new ClickButton();
		clickButton2.setName("点击button2");
		clickButton2.setKey("click2");
		clickButton2.setType("click");
		clickButton2.setSub_button(new Button[]{subclickButton1,subclickButton2,subclickButton3});
		
		//建立菜单
		Menu menu=new Menu();
		menu.setButton(new Button[]{clickButton1,viewButton1,clickButton2});
		System.out.println(JSONObject.fromObject(menu).toString());
		return JSONObject.fromObject(menu).toString();
		//return null;
	}

	

}
