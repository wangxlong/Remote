	package sport.user.register.controller;
	
	import java.io.File;
import java.io.IOException;
	import java.io.OutputStream;
	import java.io.UnsupportedEncodingException;
	import java.net.URLEncoder;
import java.nio.file.Files;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
	import java.util.List;
	import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
	import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.binary.Base64;
	import org.apache.poi.hssf.usermodel.HSSFRow;
	import org.apache.poi.hssf.usermodel.HSSFSheet;
	import org.apache.poi.hssf.usermodel.HSSFWorkbook;
	import org.springframework.beans.factory.annotation.Autowired;
	import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
	import org.springframework.web.bind.annotation.RequestMapping;
	import org.springframework.web.bind.annotation.RequestMethod;
	import org.springframework.web.bind.annotation.RequestParam;
	import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.gson.Gson;

import sport.user.register.dao.ForcePaymentsDao;
import sport.user.register.dao.UserOperatorDao;
import sport.user.register.dao.webOperatorDao;
import sport.user.register.domain.AnnouncementBean;
import sport.user.register.domain.RecordsFilteredBean;
	import sport.user.register.domain.ShowUserInfoBean;
import sport.user.register.domain.ShowWebUserAliPaymentsBean;
import sport.user.register.domain.ShowWebUserLogBean;
import sport.user.register.domain.ShowWebUserPaymentsBean;
import sport.user.register.domain.ShowWebUserWxPaymentsBean;
import sport.user.register.domain.TotalShowWebUserAliPaymentsBean;
import sport.user.register.domain.UserVipBean;
import sport.user.register.util.BootstrapPar;
import sport.user.register.util.TimeConverter;
	
	@Controller
	public class ShowInWeb {
		
	
		String concatZero="0000000";
		@Autowired(required=true)  
		   private UserOperatorDao userDao;
		@Autowired(required=true)  
		   private ForcePaymentsDao forcePaymentsDao;
		@Autowired(required=true)  
		   private webOperatorDao weboperatordao;
		
		//web上显示用户信息的bootstarp-table
		@RequestMapping(value="/wuser/InfoShow")
		public String userLogShow(){
			return "userInfoDatetable";
		}
		@ResponseBody
		@RequestMapping(value="/user/dealInfoShow",method=RequestMethod.GET)
		public void dealLogShow(HttpServletRequest request, HttpServletResponse response) throws IOException{
			BootstrapPar bootstrapPar=new BootstrapPar();
			RecordsFilteredBean recordsFilteredSql=new RecordsFilteredBean();
		    request.setCharacterEncoding("UTF-8");	    
		    //获取请求次数
		    String draw = "0";
		    draw = request.getParameter("draw");
		    bootstrapPar.setDraw(draw);
		    //数据起始位置
		    String start = request.getParameter("start");
		    bootstrapPar.setStart(start);
		    System.out.println("1数据起始位置："+start);
		    //数据长度
		    String length = request.getParameter("length");
		    bootstrapPar.setLength(length);
		    System.out.println("2数据长度："+length);
		    //总记录数
		    String recordsTotal = "0";
		    //过滤后记录数
		    String recordsFiltered = "";
		    //定义列名
		    String[] cols = {"id", "user_name", "phone", "email", "age", "gender", "address", "created_at","is_iphone","recommener"};
		    //获取客户端需要那一列排序
		    String orderColumn = "0";
		    orderColumn = request.getParameter("order[0][column]");
		    orderColumn = cols[Integer.parseInt(orderColumn)];
		    bootstrapPar.setOrderColumn(orderColumn);
		    recordsFilteredSql.setOrderColumn(orderColumn);
		    //获取排序方式 默认为asc
		    String orderDir = "asc";
		    orderDir = request.getParameter("order[0][dir]");
		    recordsFilteredSql.setOrderDir(orderDir);
		    bootstrapPar.setOrderDir(orderDir);
		    //获取用户过滤框里的字符
		    String searchValue=request.getParameter("search[value]");
		    List<String> sArray = new ArrayList<String>();
		    if (!searchValue.equals("") && !searchValue.contains("'")) {
		    	if(searchValue.equals("男")){
		    		sArray.add("gender like '%" + 'm' + "%'");
		    	}
		    	if(searchValue.equals("女")){
		    		sArray.add("gender like '%" + 'f' + "%'");	
		    	}
		    	if(searchValue.equals("安卓")){
		    		sArray.add("is_iphone=0");	
		    	}
		    	if(searchValue.equals("苹果")){
		    		sArray.add("is_iphone=1");	
		    	}
		        sArray.add("id like '%" + searchValue + "%'");
		        sArray.add("user_name like '%" + searchValue + "%'");
		        sArray.add("phone like '%" + searchValue + "%'");
		        sArray.add("email like '%" + searchValue + "%'");
		        sArray.add("age like '%" + searchValue + "%'");
		        //sArray.add("gender like '%" + searchValue + "%'");
		        sArray.add("address like '%" + searchValue + "%'");
		        //sArray.add("is_iphone like '%" + searchValue + "%'");
		        sArray.add("recommener like '%" + searchValue + "%'");
		       // sArray.add("created_at like '%" + searchValue + "%'");
		    }
		    String individualSearch = "";
		    if (sArray.size() == 1) {
		        individualSearch = sArray.get(0);
		    } else if (sArray.size() > 1) {
		        for (int i = 0; i < sArray.size() - 1; i++) {
		            individualSearch += sArray.get(i) + " or ";
		        }
		        individualSearch += sArray.get(sArray.size() - 1);
		    }
		        //获取过滤前数据库总记录数	      
		        recordsTotal = String.valueOf(userDao.countUsers());
		        System.out.println("recordsTotal为："+recordsTotal);
		        String searchSQL = "";
		        if (individualSearch != "") {
		            searchSQL = " where " + individualSearch;
		        }
		        bootstrapPar.setIndividualSearch(searchSQL);
		        recordsFilteredSql.setSearchSQL(searchSQL);
		        List<ShowUserInfoBean> users = userDao.selectAllWebUserInfo(bootstrapPar);
		       if (searchValue != "") { 
		            recordsFiltered = userDao.selectAllWebUserInfoFiltered(recordsFilteredSql);
		            
		        } else {
		            recordsFiltered = recordsTotal;
		        }
		       for(int i=0;i<users.size();i++){
		    	   ShowUserInfoBean showUserInfoBean=users.get(i);
		    	   showUserInfoBean.setId(concatZero.substring(showUserInfoBean.getId().length())+showUserInfoBean.getId());
		    	   if(showUserInfoBean.getIs_iphone().equals("1")){
		    		   showUserInfoBean.setIs_iphone("苹果");
		    	   }else{
		    		   showUserInfoBean.setIs_iphone("安卓");
		    	   }
		    	   if(showUserInfoBean.getGender()!=null&&showUserInfoBean.getGender().equals("f")){
		    		   showUserInfoBean.setGender("女");
		    	   }if(showUserInfoBean.getGender()!=null&&showUserInfoBean.getGender().equals("m")){
		    		   showUserInfoBean.setGender("男");
		    	   }
		    	   
		       }
		    Map<Object, Object> info = new HashMap<Object, Object>();
		    info.put("data", users);
		    info.put("recordsTotal", recordsTotal);
		    info.put("recordsFiltered", recordsFiltered);
		    info.put("draw", draw);
		    String json = new Gson().toJson(info);
		    //----------------------------
		      System.out.println(json);
		    //----------------------------
		   response.setCharacterEncoding("UTF-8");
		   response.setHeader("Content-type", "text/json;charset=UTF-8");
		   response.getWriter().write(json);
		}
		public String encodeFileName(HttpServletRequest request, String fileName) throws UnsupportedEncodingException {  
			   String agent = request.getHeader("USER-AGENT");  
			   if (null != agent && -1 != agent.indexOf("MSIE")) {  
			       return URLEncoder.encode(fileName, "UTF-8");  
			   } else if (null != agent && -1 != agent.indexOf("Mozilla")) {  
			       return "=?UTF-8?B?" + (new String(Base64.encodeBase64(fileName.getBytes("UTF-8")))) + "?=";  
			   } else {  
			       return fileName;  
			   }  
			}
		@RequestMapping(value="/userInfo/download")
		public void downloadUserInfoExcel(HttpServletRequest request, HttpServletResponse response) throws IOException{
			response.setCharacterEncoding("utf-8");		
			//String table = request.getParameter("table");
			//System.out.println("table: "+table);		
			//String chineseFileName = getChineseFileName(table);
			//输出excel表的准备工作
			response.setHeader("Connection", "close");  
	        response.setHeader("Content-Type", "application/vnd.ms-excel;charset=utf-8");  
	        String filename = "用户基本信息.xls";
	        System.out.println("filename: "+filename);
	        filename = encodeFileName(request, filename);
	        response.setHeader("Content-Disposition", "attachment;filename=" + filename);          
	        HSSFWorkbook wb = new HSSFWorkbook();  
	        HSSFSheet sheet = wb.createSheet("用户基本信息表"); 
	        //往excel表中填入数据库中的数据
				String columnNames[] = {"用户编号","用户名称","手机号","邮箱","年龄","性别","住址","注册时间","机子类型","推荐人"};
				int rowNumber = 0;	//记录表格的行数
				HSSFRow header = sheet.createRow(rowNumber++);
		        for (int i=0; i<10; i++) {  
		        	header.createCell(i).setCellValue(columnNames[i]);  
		        }        
		        //////将数据填入表格       
		        HSSFRow tempRow;
		        List<ShowUserInfoBean> downloadUsers=userDao.downloadUsers();
				for(int j=0;j<downloadUsers.size();j++)
				{
					tempRow = sheet.createRow(rowNumber++);
					tempRow.createCell(0).setCellValue(downloadUsers.get(j).getId());
					tempRow.createCell(1).setCellValue(downloadUsers.get(j).getUser_name());
					tempRow.createCell(2).setCellValue(downloadUsers.get(j).getPhone());
					tempRow.createCell(3).setCellValue(downloadUsers.get(j).getEmail());
					tempRow.createCell(4).setCellValue(downloadUsers.get(j).getAge());
					tempRow.createCell(5).setCellValue(downloadUsers.get(j).getGender());
					tempRow.createCell(6).setCellValue(downloadUsers.get(j).getAddress());
					tempRow.createCell(7).setCellValue(downloadUsers.get(j).getCreated_at());
					tempRow.createCell(8).setCellValue(downloadUsers.get(j).getIs_iphone());
					tempRow.createCell(9).setCellValue(downloadUsers.get(j).getRecommener());
				}
		        
			            
	        OutputStream out=response.getOutputStream();  
	        wb.write(out);  
	        out.flush();
	        out.close(); 	
		}
		
		//删除用户
		@RequestMapping(value="/user/deleteUser/{id}")
		public String deleteUser(@PathVariable String id, Model model){
			
			for(int i=0;i<7;i++){
				if(id.substring(0, 1).equals("0"))
					id=id.substring(1);
				else break;
			}
			System.out.println("要删除的id--是："+id);
			//List<ShowWebUserLogBean> showWebUserLogBeanList=userDao.webShowUserLog(id);
			//System.out.println(showWebUserLogBeanList);
			//model.addAttribute(showWebUserLogBeanList);
			//return "userLog";
			userDao.deleteUser(id);
			return "redirect:/wuser/InfoShow";
		}
		
		//web上显示用户登录日志
		@RequestMapping(value="/wuser/showLogs/{id}")
		public String showUserLogs(@PathVariable String id, Model model){
			System.out.println("id--是："+id);
			List<ShowWebUserLogBean> showWebUserLogBeanList=userDao.webShowUserLog(id);
			System.out.println(showWebUserLogBeanList);
			model.addAttribute(showWebUserLogBeanList);
			return "userLog";
		}
		
		//web上显示用户付款记录
		@RequestMapping(value="/wuser/showPayments/{id}")
		public String showUserPayments(@PathVariable String id, Model model){
			
			String id1=id.replaceAll("^(0+)", "");;
			System.out.println("id1--是："+id1);
			List<TotalShowWebUserAliPaymentsBean> totalShowWebUserAliPaymentsBeanList=new ArrayList<TotalShowWebUserAliPaymentsBean>();
			List<ShowWebUserPaymentsBean> showWebUserPaymentsBeanList=userDao.webShowUserPayments(id1);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");			
			for(int i=0;i<showWebUserPaymentsBeanList.size();i++){
				ShowWebUserPaymentsBean showWebUserPaymentsBean=showWebUserPaymentsBeanList.get(i);
				TotalShowWebUserAliPaymentsBean totalShowWebUserAliPaymentsBean=new TotalShowWebUserAliPaymentsBean();
				totalShowWebUserAliPaymentsBean.setId(showWebUserPaymentsBean.getId());
				totalShowWebUserAliPaymentsBean.setFee(Double.toString(showWebUserPaymentsBean.getFee()/100));
				totalShowWebUserAliPaymentsBean.setCreated_at(sdf.format(showWebUserPaymentsBean.getCreated_at()));
				totalShowWebUserAliPaymentsBean.setAssociationUserPayments(showWebUserPaymentsBean.getAssociationUserPayments());
				totalShowWebUserAliPaymentsBeanList.add(totalShowWebUserAliPaymentsBean);
				//System.out.println("最少执行一次");
			}
			//支付宝
			List<ShowWebUserAliPaymentsBean> showWebUserAliPaymentsBeanList=userDao.webShowUserAliPayments(id1);
			for(int i=0;i<showWebUserAliPaymentsBeanList.size();i++){
				System.out.println("showWebUserAliPaymentsBeanList为："+i+"  "+showWebUserAliPaymentsBeanList.get(i));
			}
			for(int i=0;i<showWebUserAliPaymentsBeanList.size();i++){
				ShowWebUserAliPaymentsBean showWebUserAliPaymentsBean=showWebUserAliPaymentsBeanList.get(i);
				TotalShowWebUserAliPaymentsBean totalShowWebUserAliPaymentsBean=new TotalShowWebUserAliPaymentsBean();
				totalShowWebUserAliPaymentsBean.setId(showWebUserAliPaymentsBean.getId());
				totalShowWebUserAliPaymentsBean.setFee(showWebUserAliPaymentsBean.getFee());
				totalShowWebUserAliPaymentsBean.setCreated_at(showWebUserAliPaymentsBean.getCreated_at());
				totalShowWebUserAliPaymentsBean.setAssociationUserPayments(showWebUserAliPaymentsBean.getAssociationUserPayments());
				totalShowWebUserAliPaymentsBeanList.add(totalShowWebUserAliPaymentsBean);
			}
			//微信
			List<ShowWebUserWxPaymentsBean> showWebUserWxPaymentsBeanList=userDao.webShowUserWxPayments(id1);
			for(int i=0;i<showWebUserWxPaymentsBeanList.size();i++){
				System.out.println("showWebUserAliPaymentsBeanList为："+i+"  "+showWebUserWxPaymentsBeanList.get(i));
			}
			for(int i=0;i<showWebUserWxPaymentsBeanList.size();i++){
				ShowWebUserWxPaymentsBean showWebUserWxPaymentsBean=showWebUserWxPaymentsBeanList.get(i);
				TotalShowWebUserAliPaymentsBean totalShowWebUserWxPaymentsBean=new TotalShowWebUserAliPaymentsBean();
				totalShowWebUserWxPaymentsBean.setId(showWebUserWxPaymentsBean.getId());
				totalShowWebUserWxPaymentsBean.setFee(Double.toString(Double.valueOf(showWebUserWxPaymentsBean.getFee())/100));
				totalShowWebUserWxPaymentsBean.setCreated_at(TimeConverter.timeformattrv(showWebUserWxPaymentsBean.getCreated_at()));
				totalShowWebUserWxPaymentsBean.setAssociationUserPayments(showWebUserWxPaymentsBean.getAssociationUserPayments());
				totalShowWebUserAliPaymentsBeanList.add(totalShowWebUserWxPaymentsBean);
			}
			//
			model.addAttribute(totalShowWebUserAliPaymentsBeanList);
			for(int i=0;i<totalShowWebUserAliPaymentsBeanList.size();i++)
				System.out.println("totalShowWebUserAliPaymentsBeanList为："+i+" "+totalShowWebUserAliPaymentsBeanList.get(i));
			return "userPayDetail";
		}
		
		@RequestMapping(value="/admin/login.json")
		public String adminLog(HttpServletRequest request,HttpSession session,RedirectAttributes redirectAttributes){
			System.out.println("进入方法…………");			
			String username=request.getParameter("username");
	        String password=request.getParameter("password");
	        System.out.println("user_name_phone："+username+" encrypted_password:"+password);
	        String loginId=userDao.webAdminLogin(username, password);
	        if(loginId!=null){
	        	session.setAttribute("userId", loginId);
	        	return "userInfoDatetable";
	        }
	        else{
	        	session.setAttribute("errorMsg", "1");
	        	return "redirect:/index.jsp";
	      }
		}
		
		//映射到上传图片界面
		@RequestMapping(value="/wuser/toUploadPiction")
		public String toUploadPiction(){
			System.out.println("执行进来");
			return "uploadPiction";
		}
		
		//上传图片
	    @RequestMapping(value="/uploadPiction")
		public  String uploadPiction(HttpServletRequest request, HttpServletResponse response){			
			System.out.println("输出去");					  
	        MultipartResolver resolver = new CommonsMultipartResolver(request
	            .getSession().getServletContext());
		    MultipartHttpServletRequest multipartRequest = resolver
		        .resolveMultipart(request);
		    MultipartFile multipartFile =  multipartRequest.getFile("fileList");
		    if(multipartFile!=null){
		    	//for(MultipartFile multipartFile:files){
		    		String fileName=multipartFile.getOriginalFilename();
		    		//File imageFile=new File(multipartRequest.getServletContext().getRealPath("/scollePiction"),fileName);
		    		File imageFile=new File("/opt/ylImage",fileName);
		    		try {
						multipartFile.transferTo(imageFile);
					} catch (IllegalStateException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					//}
		    	}
		    }
		    return "uploadPiction";
	    } 
		
	    //web上设置VIP
	    @RequestMapping(value="/wuser/toSetVip")
	    public String toSetVip(){
	    	return "setVip";
	    }
	    @RequestMapping(value="/user/isVip")
	    public void showVip(HttpServletRequest request, HttpServletResponse response) throws IOException, ParseException{
	    	BootstrapPar bootstrapPar=new BootstrapPar();
			RecordsFilteredBean recordsFilteredSql=new RecordsFilteredBean();
		    request.setCharacterEncoding("UTF-8");	    
		    //获取请求次数
		    String draw = "0";
		    draw = request.getParameter("draw");
		    bootstrapPar.setDraw(draw);
		    //数据起始位置
		    String start = request.getParameter("offset");
		    bootstrapPar.setStart(start);
		    System.out.println("1数据起始位置："+start);
		    //数据长度
		    String length = request.getParameter("limit");
		    if(length.equals("10")){
		    	length="15";
		    }
		    bootstrapPar.setLength(length);
		    System.out.println("2数据长度："+length);
		    //总记录数
		    String recordsTotal = "0";
		    //过滤后记录数
		    String recordsFiltered = "";
		    //定义列名
		    //String[] cols = {"user_name", "vip_to"};
		    //获取客户端需要那一列排序
		    String orderColumn = "user_name";
		    String forderColumn = request.getParameter("sort");
		    if(forderColumn!=null){
		    	
		    	orderColumn=forderColumn;
		    	if(forderColumn.equals("is_vip")){
			    	orderColumn="vip_to";
			    }
		    }
		    
		    //orderColumn = cols[Integer.parseInt(orderColumn)];
		    bootstrapPar.setOrderColumn(orderColumn);
		    recordsFilteredSql.setOrderColumn(orderColumn);
		    //获取排序方式 默认为asc
		    String orderDir = "asc";
		    orderDir = request.getParameter("order");
		    recordsFilteredSql.setOrderDir(orderDir);
		    bootstrapPar.setOrderDir(orderDir);
		    //获取用户过滤框里的字符
		    String searchValue=request.getParameter("search");
		    List<String> sArray = new ArrayList<String>();
		    if (searchValue!=null&&!searchValue.equals("") && !searchValue.contains("'")) {
		        if(searchValue.equals("是"))
		        	sArray.add("vip_to>sysdate");
		        if(searchValue.equals("否")){
		        	sArray.add("vip_to<=sysdate");
		        }
		    	sArray.add("user_name like '%" + searchValue + "%'");
		       // sArray.add("created_at like '%" + searchValue + "%'");
		    }
		    String individualSearch = "";
		    if (sArray.size() == 1) {
		        individualSearch = sArray.get(0);
		    } else if (sArray.size() > 1) {
		        for (int i = 0; i < sArray.size() - 1; i++) {
		            individualSearch += sArray.get(i) + " or ";
		        }
		        individualSearch += sArray.get(sArray.size() - 1);
		    }
		        //获取过滤前数据库总记录数	      
		        recordsTotal = forcePaymentsDao.vipCountUsers();
		        System.out.println("recordsTotal为："+recordsTotal);
		        String searchSQL = "";
		        if (individualSearch != "") {
		            searchSQL = " where " + individualSearch;
		        }
		        bootstrapPar.setIndividualSearch(searchSQL);
		        recordsFilteredSql.setSearchSQL(searchSQL);
		        List<UserVipBean> users = forcePaymentsDao.selectAllWebUserVip(bootstrapPar);
		       for(int j=0;j<users.size();j++){
		    	   System.out.println(users.get(j).toString());
		       }
		        
		        if (searchValue != "") { 
		            recordsFiltered = forcePaymentsDao.selectAllWebUserVipFiltered(recordsFilteredSql);
		            
		        } else {
		            recordsFiltered = recordsTotal;
		        }
		       
		      
		      
		       
		       //格式转换
		       java.util.Date nowdate=new java.util.Date();
		       for(int i=0;i<users.size();i++){
		    	   UserVipBean userVipBean=users.get(i);
		    	   if(userVipBean.getVip_to()!=null){
		    		   Date vip_date=(Date) TimeConverter.stringToDate(userVipBean.getVip_to());
		    		   userVipBean.setVip_to(TimeConverter.dateToString(vip_date));
		    		   if(vip_date.before(nowdate))
		    		   		userVipBean.setIs_vip("否");
		    		   else
		    			   userVipBean.setIs_vip("是");
		    		   //userVipBean.setVip_to(TimeConverter.dateToString());
		    	   }else{
		    		   userVipBean.setIs_vip("否");
		    		   //userVipBean.setVip_to(TimeConverter.dateToString(nowdate));
		    	   }
		       }
		    Map<Object, Object> info = new HashMap<Object, Object>();
		    info.put("total", recordsTotal);
		    info.put("rows", users);
		    //info.put("recordsTotal", recordsTotal);
		    //info.put("recordsFiltered", recordsFiltered);
		    //info.put("draw", draw);
		    String json = new Gson().toJson(info);
		    //----------------------------
		      System.out.println(json);
		    //----------------------------
		   response.setCharacterEncoding("UTF-8");
		   response.setHeader("Content-type", "text/json;charset=UTF-8");
		   response.getWriter().write(json);
	    }
	 
	 @RequestMapping(value="/wuser/setAnnouncement")
	 public String toAnnouncement(Model model){
		 List<AnnouncementBean> announcementList=weboperatordao.selectThisMonthAnnouncement();
		 //for(int i=0;i<announcementList.size();i++){
			// System.out.println(announcementList.get(i).toString());
		 //}
		 model.addAttribute("thismouthannouncement",announcementList);
		 return "announcement";
	 }
	 @RequestMapping(value="/user/dealAnnouncement")
	 public String dealWithAnnouncement(HttpServletRequest request ,Model model,RedirectAttributes redirectAttributes){
		 //System.out.println("处理公告栏提交");
		 String raidovalue=(String) request.getParameter("optionsRadios");
		 String headercon=(String) request.getParameter("headercon");
		 String bodycont=(String) request.getParameter("bodycont");
		 //System.out.println("raidovalue+"+raidovalue);
		 //System.out.println("headercon+"+headercon);		 
		 //System.out.println("bodycont+"+bodycont);
		 if(raidovalue.equals("option1")&&!headercon.equals("")&&!bodycont.equals("")){
			 weboperatordao.writeAnnouncement(headercon,bodycont);
		 }else{
			 ServletContext application = request.getServletContext();
			 if(raidovalue.equals("option2")){
				 application.setAttribute("todayOvelRecomend", "1");//今日推送完毕--todayOvelRecomend
				 application.setAttribute("todayNoRecomend", "0");
			 }
			 if(raidovalue.equals("option3")){
				 application.setAttribute("todayNoRecomend", "1");//今日无推送--todayNoRecomend
				 application.setAttribute("todayOvelRecomend", "0");
			 }		 
		 }
		 
		 return "redirect:/wuser/setAnnouncement";
	 }
	 
	 @RequestMapping(value="/wuser/toDetailAnnouncement/{id}")
	 public String todetailannouncement(@PathVariable String id,Model model){
		 //System.out.println("图片id："+id);
		 AnnouncementBean announcementBean=weboperatordao.getIdAnnouncementBean(id);
		 System.out.println(announcementBean.toString());
		 model.addAttribute("announcementBean",announcementBean);
		 return "detailAnnouncement";
	 }
	 @RequestMapping(value="/user/deleteAnnouncement/{id}")
	 public String deleteAnnouncement(@PathVariable String id){
		 //System.out.println("图片id："+id);
		 //AnnouncementBean announcementBean=weboperatordao.getIdAnnouncementBean(id);
		 System.out.println("删除announcement"+id);
		 weboperatordao.deleteAnnouncement(id);
		 return "redirect:/wuser/setAnnouncement";
	 }
	
}
