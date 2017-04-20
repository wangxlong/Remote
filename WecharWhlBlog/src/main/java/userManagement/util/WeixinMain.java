package userManagement.util;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

import net.sf.json.JSONObject;
import userManagement.service.ButtonService;
import userManagement.service.ButtonServiceImp;

public class WeixinMain {

	public static void main(String [] args) throws KeyManagementException, NoSuchAlgorithmException, NoSuchProviderException, IOException{
		//System.out.print("1");
		//上传临时素材，获得media_id
		//String media_id=MessageUtil.upload("C:\\Users\\whl\\Desktop\\2.jpeg",GET_ACCESS_TOKEN.retAccess_Token(),"image");
		
		//上传菜单
		
		String url=WeixinReqUtil.addMenu.replace("ACCESS_TOKEN", GET_ACCESS_TOKEN.retAccess_Token());
		ButtonServiceImp buttonService=new ButtonService();
		String msg=buttonService.retButtonStruction();
		JSONObject jsonObject=WeixinReqUtil.doPostStr(url,msg);
		System.out.println(jsonObject.get("errcode").toString());
		
		//SendMsgToAllUtil.upload("C:\\Users\\whl\\Desktop\\1.jpg",GET_ACCESS_TOKEN.retAccess_Token());
	}
}
