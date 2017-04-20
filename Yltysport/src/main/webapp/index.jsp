<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ page isELIgnored="false" %>
<!DOCTYPE html>
<html lang="en" class="no-js">
    <head>
        <meta charset="utf-8">
        <title>登录</title>
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <meta name="description" content="">
        <meta name="author" content="">

        <!-- CSS -->
        <link rel="stylesheet" href="/Yltysport/css/reset.css">
        <link rel="stylesheet" href="/Yltysport/css/supersized.css">
        <link rel="stylesheet" href="/Yltysport/css/style.css">

        <!-- HTML5 shim, for IE6-8 support of HTML5 elements -->
        <!--[if lt IE 9]>
            <script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
        <![endif]-->

    </head>

   <body oncontextmenu="return false">

        <div class="page-container">
            <h1>Login</h1>
            <form  id="form" name="form" action="admin/login.json" method="post">
            	<input type="hidden" name="errorMsg">
				<div>
					<input type="text" name="username" class="username" placeholder="Username" autocomplete="off"/>
				</div>
                <div>
					<input type="password" name="password" class="password" placeholder="Password" oncontextmenu="return false" onpaste="return false" />
                </div>
                <button id="sbutton" type="button">Sign in</button>
            </form>
            <div class="connect">
                <p>Y L T Y S P O R T</p>
				<p style="margin-top:20px;">盈郎体育   </p>
            </div>
        </div>
		<div class="alert" style="display:none">
			<h2>消息</h2>
			<div class="alert_con">
				<p id="ts"></p>
				<p style="line-height:70px"><a class="btn">确定</a></p>
			</div>
		</div>

        <!-- Javascript -->
		<script src="/Yltysport/js/jquery-2.1.4.min.js" type="text/javascript"></script>
        <script src="/Yltysport/js/supersized.3.2.7.min.js"></script>
        <script src="/Yltysport/js/supersized-init.js"></script>
        <script>
		$(".btn").click(function(){
			is_hide();
		})
		var u = $("input[name=username]");
		var p = $("input[name=password]");		
		var errorMsg = $("input[name=errorMsg]");
		errorMsg=<%=session.getAttribute("errorMsg")%>;
		<%session.removeAttribute("errorMsg");%>
		//alert(errorMsg);
		if(errorMsg=="1"){
			$("#ts").html("用户名或密码错误");
			is_show();
		}
		$("#sbutton").bind('click',function(){
			if(u.val() == '' || p.val() =='')
			{
				$("#ts").html("用户名或密码不能为空");
				is_show();
				return false;
			}else{
				var reg = /^[0-9A-Za-z]+$/;			
				if(!reg.exec(u.val()))
			     {				
				  $("#ts").html("用户名错误");
				  is_show();
				  return false;
			     }else{
			    	// alert("haha");
				 $("#form").submit();
				  return true;
			   }	
			} 
		});
		window.onload = function()
		{
			$(".connect p").eq(0).animate({"left":"0%"}, 600);
			$(".connect p").eq(1).animate({"left":"0%"}, 400);
		}
		function is_hide(){ $(".alert").animate({"top":"-40%"}, 300) }
		function is_show(){ $(".alert").show().animate({"top":"45%"}, 300) }
		</script>
    </body>
</html>
