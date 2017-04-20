package sport.user.register.service;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.util.Random;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.methods.GetMethod;
import org.springframework.stereotype.Service;


import sport.user.register.domain.Message;

@Service
public class SendmessageServiceImpl implements SendmessageService {
	String url = "http://222.73.117.158/msg/";// Ӧ�õ�ַ
	String account = "ylty888";// �˺�
	String pswd = "Ylty123456";// ����
	String mobile = "";// �ֻ����룬�������ʹ��","�ָ�
	String sendmsg = "";// ��������
	boolean needstatus = true;// �Ƿ���Ҫ״̬���棬��Ҫtrue������Ҫfalse
	String extno = null;// ��չ��      
	public Message sendMessage(String phone) {
		Message msg=new Message();
		mobile=phone.trim();
		String verifycodetophone=generateVerifyCode();//�������6λ����֤��
		sendmsg="您好，您的验证码是："+verifycodetophone;
		// TODO Auto-generated method stub		
		//msg.setRetCode("13143");
		//msg.setVertify_code(1);
		//File file=new File("file.txt");
		//BufferedWriter bf;
		try {
			//bf = new BufferedWriter(new PrintWriter(file));
			//bf.append("����������£�");
			//bf.flush();
			String returnString = SendmessageServiceImpl.batchSend(url, account, pswd, mobile, sendmsg, needstatus, extno);
			System.out.println("-------"+returnString);
			int returncode=Integer.parseInt(returnString.substring(returnString.indexOf(",")+1,returnString.indexOf(",")+2));
			if(returncode==0){
			msg.setRetCode(returncode);	
			//加密返回
			String ascii=toAscii(verifycodetophone);
			/*
			 * String firstsixrandomString=ascii.substring(0,6);
			 * int circulation =(int)(ascii.substring(6,7).hashCode()-97);
			 * System.out.println("circulation为："+circulation);
			 * String verifycode=numberEncryption(firstsixrandomString,circulation);
			 * msg.setVertify_code(verifycode+ascii.substring(6,8));
			 */			
			msg.setVertify_code(ascii);
			}
			if(returncode==1){
			msg.setRetCode(returncode);		
			msg.setVertify_code("手机号码错误,请重新输入");	
			}
			//bf.append(returnString);
			//bf.flush();
		} catch ( Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return msg;
	}

	public String generateVerifyCode(){
		String randomverifycode="";
	    for(int i=0;i<6;i++){
	    	Random random=new Random();
	    	int intNumber=random.nextInt(10);    	
	    	randomverifycode+=intNumber;
	   }
		return randomverifycode;		
	}
	//将生成的6个数字的随机数，变成8个数字的ascii码，前6个是用来转化的，第七个表示循环次数，第八个没用
	public static String toAscii(String a){
		String password="";
		Random random=new Random();
    	int intNumber=random.nextInt(10); 
	    for(int i=0;i<6;i++){
	    	password+=String.valueOf((char)(Integer.parseInt(a.substring(i, i+1))+97)); //进行ascii码的转化
	    }
	    password+=String.valueOf((char)(random.nextInt(10)+97));
	    password+=String.valueOf((char)(random.nextInt(10)+97));
	    System.out.println("完整的密码为："+password);
	    return password;
	}
	//加密
	public static String exchangeLeftRight(String password,int circulation){	
		password=password.substring(0,1)+password.substring(2,3)+
				password.substring(4,5)+password.substring(1,2)+
				password.substring(3,4)+password.substring(5,6);
		System.out.println("第"+circulation+"次转化："+password);
		return password;
	}
	//加密
	public static String numberEncryption(String password,int circulation){		
		   String newPassword="";
		   for(int j=1;j<=circulation;j++){
			   newPassword=exchangeLeftRight(password,j);
			   password=newPassword;
		  }
		   return newPassword;
	}
	public static String batchSend(String url,String account,String pswd,String mobile,String msg,boolean needstatus,
			String extno) throws Exception{
		HttpClient client=new HttpClient();
		GetMethod method=new GetMethod();
		System.out.println("��ʼ����batchsender");
		try{
			URI base=new URI(url,false);
			method.setURI(new URI(base,"HttpBatchSendSM",false));
			method.setQueryString(new NameValuePair[]{new NameValuePair("account",account),
					new NameValuePair("pswd",pswd),new NameValuePair("mobile",mobile),
					new NameValuePair("needstatus",String.valueOf(needstatus)),new NameValuePair("msg",msg),
					new NameValuePair("extno",extno),});
			int result=client.executeMethod(method);
			System.out.println("�ύ��������");
			if(result==HttpStatus.SC_OK) {
				InputStream in=method.getResponseBodyAsStream();
				ByteArrayOutputStream baos=new ByteArrayOutputStream();
				byte[] buffer=new byte[1024];
				int len=0;
				while((len=in.read(buffer))!=-1){
					baos.write(buffer,0,len);
				}
				return URLDecoder.decode(baos.toString(),"UTF-8");
			}else{
				throw new Exception("HTTP ERROR Status: "+method.getStatusCode()+":"+method.getStatusText());
			}
		}finally{
			method.releaseConnection();
		}
	}
}
