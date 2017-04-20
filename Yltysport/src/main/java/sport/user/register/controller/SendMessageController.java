package sport.user.register.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import net.sf.json.JSONObject;
import sport.user.register.domain.Message;
import sport.user.register.service.SendmessageServiceImpl;

@Controller
public class SendMessageController {

	private static final Log logger=LogFactory.getLog(SendMessageController.class);
	@Autowired
	private SendmessageServiceImpl sendmsgservice;
	
	@RequestMapping(value="/sendMessage.json",method=RequestMethod.GET)
	public void sendMessage(HttpServletRequest request, HttpServletResponse response) throws IOException{
		//System.out.println("IP地址为："+request.getRemoteAddr());
		String phone=request.getParameter("phone");
		Message returnMesg=sendmsgservice.sendMessage(phone);
		JSONObject jsonMsg = JSONObject.fromObject(returnMesg);
		//logger.info("message�����json��ʽ�ǣ�"+jsonMsg);
		response.setHeader("Content-type", "text/json;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(jsonMsg.toString());
	}
}
