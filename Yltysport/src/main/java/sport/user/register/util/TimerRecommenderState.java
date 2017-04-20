package sport.user.register.util;

import java.util.Date;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;

import org.springframework.web.context.ContextLoader;

public class TimerRecommenderState extends HttpServlet {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5034241273472933070L;
	private static int counter = 0;
	
    protected void execute()  { 
    	ServletContext application =ContextLoader.getCurrentWebApplicationContext().getServletContext();
		application.setAttribute("todayOvelRecomend", "0");//今日推送完毕--todayOvelRecomend
		application.setAttribute("todayNoRecomend", "0");
        //long ms = System.currentTimeMillis();  
        //System.out.println(new Date(ms));  
        //System.out.println("(" + counter++ + ")");  
    }  
}
