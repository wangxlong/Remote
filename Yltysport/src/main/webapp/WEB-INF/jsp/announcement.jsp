<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ page isELIgnored="false"%>
<%@ page import="sport.user.register.domain.AnnouncementBean,java.util.*" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>公告栏</title>
  <!-- 侧边导航栏需要导入的包 -->
  <link rel="stylesheet" href="/Yltysport/css/default.css">
  <link rel="stylesheet" href="/Yltysport/css/sidebarfonts.css">
  <link rel="stylesheet" href="/Yltysport/css/style.min.css">
  
  
  <link rel="stylesheet" type="text/css" href="/Yltysport/css/normalize.css" />
  <link rel="stylesheet" type="text/css" href="/Yltysport/css/newdefault.css" />
  <link href="/Yltysport/css/bootstrap-theme.min.css" rel="stylesheet" type="text/css" />
  <link href="/Yltysport/css/site.css" rel="stylesheet" type="text/css" />
  <link rel="stylesheet" href="/Yltysport/css/bootstrap.min.css">
  <script src="/Yltysport/js/jquery-2.1.4.min.js"></script>
  <script src="/Yltysport/js/bootstrap.min.js"></script>
  <!-- 标题的css -->
   <style>
   	 body {
			background-color: #EAEAEA;
		}
     * { margin: 0; padding: 0; border-width: 1px; }
	 .parentCls {margin:0px;}
	 .js-max-input {border: solid 1px #ffd2b2; position:relative;background: #fffae5;padding: 0 10px 0 10px;font-size:20px;color: #ff4400}
	 .inputElem4{ width: 300px; height: 36px; border: 1px solid #E0E0E0; padding-left: 10px; line-height: 36px; font-size: 14px; }
  </style>
  <!-- textarea的css -->
  <style>
			textarea {
				padding: 10px;
				vertical-align: top;
				width: 200px;
				height: 200px;
			}
			textarea:focus {
				outline-style: solid;
				outline-width: 2px;
			}
   </style>
</head>
<body>
<div id="wrapper" class="wrapper">
<div class="container">

<header class="htmleaf-header bgcolor-10">
  <span><h1 style="text-align:center;">公告栏</h1></span>
   <br>
</header>
<br><br><br>
	<div class="row">
		<div class="col-xs-6">
		<!-- 左半边 -->			
			<form action="/Yltysport/user/dealAnnouncement" method="post">
				<div class="radio">
				    <label>
				      <input type="radio" name="optionsRadios" id="optionsRadios1" 
				         value="option1" checked="checked" onclick="javascript:visiableText()"> 内容
				    </label> &nbsp;&nbsp;&nbsp;&nbsp;
				    <label>
				   	  <input type="radio" name="optionsRadios" id="optionsRadios2" 
				         value="option2" onclick="hiddenTextx()"> 今日推送完毕
				    </label>&nbsp;&nbsp;&nbsp;&nbsp;
				    <label>
				      <input type="radio" name="optionsRadios" id="optionsRadios3" 
				         value="option3" onclick="hiddenTextx()"> 今日无推送
				    </label>
				</div>			
				<br><br>
				<div class="parentCls" id="headercontent">
					标题：<input type="text" name="headercon" class="inputElem4" autocomplete = "off" maxlength="18"/>
				</div>
				<br>
				<div id="bodycontent">
				           内容：<textarea  name="bodycont" style='resize: none;width: 85%;min-height: 150px'></textarea>
				</div>
			    <br><br><br>
			    <span style="display:block; text-align:center;color: red;">
			    	<input type="submit" value="提交" class="btn" >&nbsp;&nbsp;&nbsp;&nbsp;
			    	<input type="reset" value="重置" class="btn" >
			    </span>		  
			    		 
			
			</form>
		</div>
		<!-- 中间 -->
		<div class="col-xs-1">
		<!-- 栅格的中间 -->
		
		</div>
		<div class="col-xs-5">
		<!-- 右半边 -->
		<div class="panel panel-danger">
						<div class="panel-heading ">
							<span class="glyphicon glyphicon-list-alt"></span><b>Announcement</b></div>
						<div class="panel-body">
							<div class="row">
								<div class="col-xs-12">
									<ul class="announcementListclass">
									
									  <c:forEach items="${thismouthannouncement}" var="announcement">
										<li class="news-item">
											<table cellpadding="4">
												<tr>
													<!-- <td><img src="images/1.png" width="60" class="img-circle" /></td>  -->
													 <td>${announcement.created_at}</td>
													 
													<td>&nbsp;&nbsp;标题：${announcement.title} &nbsp;&nbsp;<a href="/Yltysport/wuser/toDetailAnnouncement/${announcement.id}">Read more...</a></td>																																																			
													<td>
													 	<span style="margin-left: 70px;">
															<a href="/Yltysport/user/deleteAnnouncement/${announcement.id}">删除</a>
														</span>
													</td>
													
												</tr>
											</table>
										</li>
									 </c:forEach>
									 
									</ul>
								</div>
							</div>
						</div>
						<div class="panel-footer">

						</div>
		    </div>
		</div>
	</div>
</div>
</div>
 <script src="/Yltysport/js/jqfangdawenzi.js"></script>
	 <script>
		 // 初始化
		 $(function(){
			
			new TextMagnifier({
				inputElem: '.inputElem4',
				align: 'bottom',
				splitType: [6,4,4,4]
			});
		 });
		 	
 </script>
<script type="text/javascript">
	function visiableText(){
		//alert("哈哈");
		document.getElementById('bodycontent').style.display='';		
		document.getElementById('headercontent').style.display='';
	}
	function hiddenTextx(){
		//alert("hiden");
		document.getElementById('bodycontent').style.display='none';		
		document.getElementById('headercontent').style.display='none';
	}
</script>

<script src="/Yltysport/js/jquery.bootstrap.newsbox.min.js" type="text/javascript"></script>
<script type="text/javascript">
    $(function () {
        $(".announcementListclass").bootstrapNews({
            newsPerPage: 10,
            autoplay: true,
			pauseOnHover:true,
            direction: 'up',
            newsTickerInterval: 4000,
            onToDo: function () {
                //console.log(this);
            }
        });
    });
</script>
<script src='/Yltysport/js/autosize.js'></script>
	<script>
		autosize(document.querySelectorAll('textarea'));
	</script>

<%@include file="navigation.jsp" %>
</body>
</html>