<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script type="text/javascript" src="/Yltysport/js/jquery-2.1.4.min.js"></script>
<script type="text/javascript" src="/Yltysport/js/jquery-ui.min.js"></script>
<link href="/Yltysport/css/bootstrap-combined.min.css" rel="stylesheet" media="screen">
<link href="/Yltysport/css/jquery-ui.min.css" rel="stylesheet">
<script type="text/javascript" src="/Yltysport/js/bootstrap.min.js"></script>
</head>
<body>
<br><br><br>
<div class="container">	
    <ol class="breadcrumb">
	  <li><a href="/Yltysport/wuser/InfoShow">用户基本信息</a>/</li>
	  <li><a href="">用户付款明细</a></li>
	 <!--  <li><a href="/FMP/data_management/dataManagement.jsp">数据管理</a></li>
	  <li class="active">班级</li>
	   -->
    </ol>
    <br>
<div class="container-fluid" id="LG">
	<div class="row-fluid">
		<div class="span1">
		</div>
		<div class="span10">
			<table class="table table-bordered table-hover">
				<thead>
					<tr>
						<th>
							用户名
						</th>
						<th>
							付款金额
						</th>
						<th>
							付款时间
						</th>					
					</tr>
				</thead>
				<tbody>
					<c:set var="colorIndex" value="1" scope="page"></c:set>
					<c:forEach items="${totalShowWebUserAliPaymentsBeanList}" var="totalShowWebUserAliPaymentsBeanList" varStatus="status">
					  <!-- 选择颜色操作 -->
					  <c:choose>
					  	<c:when test="${status.count%5==1}">
					  		<c:set var="color" value="class=\"error\"" scope="page"></c:set>
					  	</c:when>
					  	<c:when test="${status.count%5==2}">
					  		<c:set var="color" value="class=\"success\"" scope="page"></c:set>
					  	</c:when>
					  	<c:when test="${status.count%5==3}">
					  		<c:set var="color" value="" scope="page"></c:set>
					  	</c:when>
					  	<c:when test="${status.count%5==4}">
					  		<c:set var="color" value="class=\"warning\"" scope="page"></c:set>
					  	</c:when>
					  	<c:when test="${status.count%5==0}">
					  		<c:set var="color" value="class=\"info\"" scope="page"></c:set>
					  	</c:when>
					  </c:choose>
					  <!-- 遍历集合操作 -->					 
					  <tr ${color}>
					  	<td>${totalShowWebUserAliPaymentsBeanList.associationUserPayments.user_name }</td>						
						<td>${totalShowWebUserAliPaymentsBeanList.fee}</td>
						<td>${totalShowWebUserAliPaymentsBeanList.created_at}</td>
					  </tr>					  
					</c:forEach>
					
				</tbody>
			</table>
		</div>
		<div class="span1">
		</div>
	</div>
</div>
</div>
</body>
</html>