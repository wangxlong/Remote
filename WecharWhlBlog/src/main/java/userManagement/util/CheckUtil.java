package userManagement.util;

import java.lang.reflect.Array;
import java.util.Arrays;

public class CheckUtil {
	
	public static final String token="whlBlog";

	public static boolean checkUrl(String signature,String timestamp,String nonce){
		String checkurlArray[]=new String[]{token,timestamp,nonce};
		//排序
		Arrays.sort(checkurlArray);
		//拼接成字符串
		StringBuffer checkContent=new StringBuffer();
		
		for(int i=0;i<checkurlArray.length;i++){
			checkContent.append(checkurlArray[i]);
		}
		String digest = new SHA1().getDigestOfString(checkContent.toString().getBytes()); 
		//System.out.println("digest:"+digest);
		return (digest.toLowerCase()).equals(signature);
	}
}
