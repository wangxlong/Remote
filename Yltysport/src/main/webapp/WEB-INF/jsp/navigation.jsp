<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!-- 侧边菜单 -->
<button id="mm-menu-toggle" class="mm-menu-toggle">Toggle Menu</button>
	<nav id="mm-menu" class="mm-menu">
	  <div class="mm-menu__header">
	    <h2 class="mm-menu__title">Function</h2>
	  </div>

	  <ul class="mm-menu__items">
	  	  <li class="mm-menu__item">
	      <a class="mm-menu__link" href="/Yltysport/wuser/InfoShow">
	        <span class="mm-menu__link-text"><i class="md md-person"></i>用户基本信息</span>
	      </a>
	    </li>
	   	  
	    <li class="mm-menu__item">
	      <a class="mm-menu__link" href="/Yltysport/wuser/toSetVip">
	        <span class="mm-menu__link-text"><i class="md md-settings"></i>设置VIP</span>
	      </a>
	    </li>
	    
	     <li class="mm-menu__item">
	      <a class="mm-menu__link" href="/Yltysport/wuser/toUploadPiction">
	        <span class="mm-menu__link-text"><i class="md md-inbox"></i>上传图片</span>
	      </a>
	    </li>
	     <li class="mm-menu__item">
	      <a class="mm-menu__link" href="/Yltysport/wuser/setAnnouncement">
	        <span class="mm-menu__link-text"><i class="md md-inbox"></i>公告栏</span>
	      </a>
	    </li>
	    <!--
	    <li class="mm-menu__item">
	      <a class="mm-menu__link" href="#">
	        <span class="mm-menu__link-text"><i class="md md-inbox"></i> Inbox</span>
	      </a>
	    </li>
	    <li class="mm-menu__item">
	      <a class="mm-menu__link" href="#">
	        <span class="mm-menu__link-text"><i class="md md-favorite"></i> Favourites</span>
	      </a>
	    </li>
	    -->
	    <li class="mm-menu__item">
	      <a class="mm-menu__link" href="/Yltysport/">
	        <span class="mm-menu__link-text"><i class="md md-home"></i>退出</span>
	      </a>
	    </li>
	  </ul>
	</nav>
<!-- 侧边菜单js文件 -->
<script src="/Yltysport/js/materialMenu.min.js"></script>
	<script>
	  var menu = new Menu;
    </script>