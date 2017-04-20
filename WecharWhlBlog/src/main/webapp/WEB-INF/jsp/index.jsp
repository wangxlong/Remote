<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE HTML>
<html>
<head>
	<script type="text/javascript" src="/WecharWhlBlog/js/jquery-2.1.4.min.js"></script>
	<link href="/WecharWhlBlog/css/bootstrap.min.css" rel="stylesheet" media="screen">
	<script type="text/javascript" src="/WecharWhlBlog/js/bootstrap.min.js"></script>
	
	<title>aa</title>
</head>
<body>
	<div>
		<button type="button" class="btn btn-info" data-toggle="modal" data-target="#myModal">审批单</button>
	</div>
	<%
		
	
	%>
<!-- Modal -->
<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true" style="width: 100%;height:100%;">
    <div class="modal-dialog" style="width: 100%;height:100%;">
        <div class="modal-content" style="width: 100%;height:100%;">
          <!--    <div class="modal-header">
                <h4 class="modal-title" id="myModalLabel">新增</h4>
            </div> 
          -->       
            <div class="modal-body" style="width: 100%;height:100%;">                        
	                <div class="form-group" style="width: 100%;height:100%;">						
						<iframe src="http://67845e0e.ngrok.natapp.cn/portal/r/w?sid=c246c2fa-a215-471b-9b7c-a1972a0cce0b&cmd=CLIENT_BPM_WORKLIST_PROCESSINST_CREATE_PREPARE&processGroupId=obj_65fd5562ceb9411083d2b379ed2ce239&processDefId=obj_cffb0637c51749c5b4346c803b7e6637" frameborder="0" scrolling="yes" style="width: 100%;height:100%;">	
						</iframe>			               
	                </div>
	                <!--         
	                <div class="modal-footer">
	                <button type="button" class="btn btn-default" data-dismiss="modal" id="closeWindow">关闭</button>
	                <button type="button" class="btn btn-primary" id="save">保存</button>                        
	            	</div>
	            	-->                 
            </div>
        </div>
    </div>
</div>

 <!-- <div style="display:none" id="formid">
		<iframe src="https://www.baidu.com/" width="900" height="500" frameborder="0" scrolling="no">
			<a href="https://www.baidu.com/">你的浏览器不支持iframe页面嵌套，请点击这里访问页面内容。</a>
		</iframe>
	</div>
-->
</body>
</html>
