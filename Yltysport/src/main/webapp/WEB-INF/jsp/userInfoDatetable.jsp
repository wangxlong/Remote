<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8"/>
    <title>用户资料列表</title>
    <!-- 侧边导航栏需要导入的包 -->
    <link rel="stylesheet" href="/Yltysport/css/default.css">
    <link rel="stylesheet" href="/Yltysport/css/sidebarfonts.css">
  	<link rel="stylesheet" href="/Yltysport/css/style.min.css">
  	
 	<link rel="stylesheet" type="text/css" href="/Yltysport/css/bootstrap.min.css"/>
    <link rel="stylesheet" type="text/css" href="/Yltysport/css/dataTables.bootstrap.css"/>
    <link href="/Yltysport/css/upload.css" rel="stylesheet">
    <link href="/Yltysport/css/dashboard.css" rel="stylesheet">
    <script src="/Yltysport/js/jquery-2.1.4.min.js" type="text/javascript"></script>
    <script src="/Yltysport/js/jquery.dataTables.min.js" charset="utf-8"></script>	
    <script type="text/javascript"  src="/Yltysport/js/dataTables.bootstrap.js" charset="utf-8"></script>
    <script src="/Yltysport/js/bootstrap.min.js"></script>
    <script src="/Yltysport/js/bootstrap-datetimepicker.min.js"></script>
    <script src="/Yltysport/js/handlebars-v3.0.1.js"></script>
    <!--定义操作列按钮模板-->
<script id="tpl" type="text/x-handlebars-template" type="text/javascript" charset="utf-8">
    {{#each func}}
    <button type="button" class="btn btn-{{this.type}} btn-sm" onclick="{{this.fn}}">{{this.name}}</button>
    {{/each}}
</script>
<script type="text/javascript" charset="utf-8">
    var table;
    var editFlag = false;
    $(function () {
       // $('#start_date').datetimepicker();

        var tpl = $("#tpl").html();
        //预编译模板
        var template = Handlebars.compile(tpl);
		
        
        table = $('#example').DataTable({
            ajax: {
                url: "/Yltysport/user/dealInfoShow",
                contentType: "application/x-www-form-urlencoded; charset=-utf-8"
            },
            serverSide: true,
            columns: [
                {"data": "id"},
                {"data": "user_name"},
                {"data": "phone"},
                {"data": "email",  "defaultContent": ""},
                {"data": "age",  "defaultContent": ""},
                {"data": "gender",  "defaultContent": ""},
                {"data": "address",  "defaultContent": ""},
                {"data": "created_at",  "defaultContent": ""},
                {"data": "is_iphone",  "defaultContent": ""},
                {"data": "recommener",  "defaultContent": ""},
                {"data": null},
                {"data": null}
                                       
            ],
            
            columnDefs: [
                {
                    targets: 10,
                    "bSortable": false,
                    render: function (a, b, c, d) {
                        var context =
                        {
                            func: [
                                {"name": "登录日志", "fn": "toLog(\'" + c.id+ "\')", "type": "primary"},
                                {"name": "付款情况", "fn": "toPayment(\'" + c.id+ "\')", "type": "danger"}                               
                            ]
                        };
                        var html = template(context);
                        return html;
                    }
                },
               {
                    targets: 11,
                    "bSortable": false,
                    render: function (a, b, c, d) {
                        var context =
                        {
                            func: [
                                {"name": "删除", "fn": "deleteUser(\'" + c.id+ "\')", "type": "danger"}
                            ]
                        };
                        var html = template(context);
                        return html;
                    }
                } 
            ],
                         
            'bProcessing':true,
            "language": {
                "lengthMenu": "_MENU_ 条记录每页",
                "zeroRecords": "没有找到记录",
                "info": "第 _PAGE_ 页 ( 总共 _PAGES_ 页 )",
                "infoEmpty": "无记录",
                "infoFiltered": "(从 _MAX_ 条记录过滤)",
                "paginate": {
                    "previous": "上一页",
                    "next": "下一页"
                }
            },
            "dom": "<'row'<'col-xs-2'l><'#mytool.col-xs-4'><'col-xs-6'f>r>" +
                    "t" +
                    "<'row'<'col-xs-6'i><'col-xs-6'p>>",
            initComplete: function () {            
   				$("#mytool").append('<input  type="button" class="btn btn-success btn-sm"  onclick="location.href=\'/Yltysport/userInfo/download\';" value="下载" />');   				
   				//$("#mytool").append('&nbsp;&nbsp;&nbsp;&nbsp;<input  type="button" class="btn btn-warning btn-sm"  onclick="location.href=\'/Yltysport/\';" value="退出" />');
            }

        });

    });
    
    function toLog(id) {
       //alert(id);
       window.location.href= '/Yltysport/wuser/showLogs/'+id;
    }
    function toPayment(id) {
        //alert(id);
        window.location.href= '/Yltysport/wuser/showPayments/'+id;
     }
    //删除用户
    function deleteUser(id){
    	if(window.confirm('确定要删除？')){
            //alert("确定");
            window.location.href= '/Yltysport/user/deleteUser/'+id;
            return true;
         }else{
            //alert("取消");
            return false;
        }
    }
    </script>
</head>
<body>
<%
	response.setCharacterEncoding("UTF-8");
%>
<div id="wrapper" class="wrapper">
<main>
<div class="container">	
    <ol class="breadcrumb">
	  <li><a href="/Yltysport/wuser/InfoShow">用户基本信息</a></li>
	 <!--  <li><a href="/FMP/data_management/dataManagement.jsp">数据管理</a></li>
	  <li class="active">班级</li>
	   -->
    </ol>   			
	
	<div align="center"><label class="text-danger" id="deleteFeedback"></label></div>

    <table id="example" class="table table-striped table-bordered">
        <thead>
        <tr>
            <th>用户编号</th>
            <th>用户名称</th>
            <th>手机号</th>
            <th>邮箱</th>
            <th>年龄</th>
            <th>性别</th>
            <th>住址</th>
            <th>注册时间</th>
            <th>机型</th>
            <th>推荐人</th>
            <th>操作</th>
            <th>删除</th>    
        </tr>
        </thead>
        <tbody></tbody>
        <!-- tbody是必须的 -->
    </table>	
</div>
</main>
</div>
<%@include file="navigation.jsp" %>
</body>
</html>