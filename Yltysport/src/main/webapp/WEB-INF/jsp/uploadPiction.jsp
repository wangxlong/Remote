<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ page import="java.io.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>图片上传</title>
<link rel="stylesheet" href="/Yltysport/css/default.css">
<link rel="stylesheet" href="/Yltysport/css/sidebarfonts.css">
<link rel="stylesheet" href="/Yltysport/css/style.min.css">
<!-- 引用控制层插件样式 -->
<link rel="stylesheet" href="/Yltysport/css/zyUpload.css" type="text/css">
<script src="/Yltysport/js/jquery-2.1.4.min.js" type="text/javascript"></script>
<!-- 引用核心层插件 -->
<script src="/Yltysport/js/zyFile.js"></script>
<!-- 引用控制层插件 -->
<script src="/Yltysport/js/zyUpload.js"></script>
<!-- 引用初始化JS -->
<script src="/Yltysport/js/jq22.js"></script>

<!-- 滚动图片预览 -->
<link rel="stylesheet" href="/Yltysport/css/shoulaqin.css" type="text/css">
<script type="text/javascript"  src="/Yltysport/js/shoulaqin.js"></script>
</head>
<body>
<div id="wrapper" class="wrapper">
<h1 style="text-align:center;">滚动图片上传</h1>
<div id="demo" class="demo"></div>

<!-- 预览图片 -->
<div id="shoulaoqin">
<div id="center">
   
	<div id="slider">	
		<div class="slide">
			<img class="diapo" src="/Yltysport/scollimage/1.jpg" alt="">
			<div class="text">
				  <span class="title">PICTURE 1</span>				 
			</div>
		</div>
		<div class="slide">
			<img class="diapo" src="/Yltysport/scollimage/2.jpg" alt="">
			<div class="text">
				 <span class="title">PICTURE 2</span>
				
			</div>
		</div>
		<div class="slide">
			<img class="diapo" src="/Yltysport/scollimage/3.jpg" alt="">
			<div class="text">
				   <span class="title">PICTURE 3</span>
				 
			</div>
		</div>
		<div class="slide">
			<img class="diapo" src="/Yltysport/scollimage/4.jpg" alt="">
			<div class="text">
				 <span class="title">PICTURE 4</span>
			</div>
		</div>
		<div class="slide">
			<img class="diapo" src="/Yltysport/scollimage/5.jpg" alt="">
			<div class="text">
				 <span class="title">PICTURE 5</span>
			</div>
		</div>
		
	</div>
</div>
</div>
</div>
<!-- 侧边菜单 -->
<%@include file="navigation.jsp" %>
<!-- 图片展示的js -->
 <script type="text/javascript">
/* ==== start script ==== */
slider.init();
</script>
</body>
</html>