<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>VIP情况查询</title>
    <link rel="stylesheet" href="/Yltysport/css/default.css">
    <link rel="stylesheet" href="/Yltysport/css/sidebarfonts.css">
    <link rel="stylesheet" href="/Yltysport/css/style.min.css">
    <link rel="stylesheet" href="/Yltysport/css/bootstrap.min.css">
    <link rel="stylesheet" href="/Yltysport/css/bootstrap-table.css">
    
    <link rel="stylesheet" href="/Yltysport/css/datetimepicker.css">
    <link rel="stylesheet" href="http://rawgit.com/vitalets/x-editable/master/dist/bootstrap3-editable/css/bootstrap-editable.css">
    <link rel="stylesheet" href="/Yltysport/css/edit-bootstapexamples.css">
    <script src="/Yltysport/js/jquery-2.1.4.min.js"></script>
    <script src="/Yltysport/js/bootstrap.min.js"></script>
    <script src="/Yltysport/js/bootstrap-datetimepicker.min.js"></script>   
    <script src="/Yltysport/js/ga.js"></script>
    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
    <script src="//cdnjs.cloudflare.com/ajax/libs/html5shiv/3.7.2/html5shiv.min.js"></script>
    <script src="//cdnjs.cloudflare.com/ajax/libs/respond.js/1.4.2/respond.min.js"></script>
    <script src="//cdnjs.cloudflare.com/ajax/libs/json2/20140204/json2.min.js"></script>
    <![endif]-->
</head>
<body>
<div id="wrapper" class="wrapper">
<main>
<br>
<div class="container">

	 <ol class="breadcrumb">
	  <li><a href="/Yltysport/wuser/toSetVip">VIP情况查询</a></li>
	 <!--  <li><a href="/FMP/data_management/dataManagement.jsp">数据管理</a></li>
	  <li class="active">班级</li>
	   -->
    </ol> 
  <!--   <div id="toolbar">
       <button id="remove" class="btn btn-danger" disabled>
            <i class="glyphicon glyphicon-remove"></i> Delete
        </button>
    </div>
    -->
    <table id="table"
           data-toolbar="#toolbar"
           data-search="true"
           data-show-refresh="true"
           data-show-toggle="true"
           data-show-columns="true"
           data-show-export="false"
           data-detail-view="true"
           data-detail-formatter="detailFormatter"
           data-minimum-count-columns="2"
           data-show-pagination-switch="true"
           data-pagination="true"
           data-id-field="id"
           data-page-list="[15, 25, 50, 100, ALL]"
           data-show-footer="false"
           data-side-pagination="server"
           data-url="/Yltysport/user/isVip"
           data-response-handler="responseHandler">
    </table>
</div>
</main>
</div>
<!-- 侧边菜单 -->
<%@include file="navigation.jsp" %>

<script>
    var $table = $('#table'),
        $remove = $('#remove'),
        selections = [];

    function initTable() {
        $table.bootstrapTable({
            height: getHeight(),
             columns: [
                [
                   // {
                    //    field: 'state',
                     //   checkbox: true,
                      //  align: 'center',
                      //  valign: 'middle'
                   // }, 
                    {
                        title: '用户名',
                        field: 'user_name',
                        align: 'center',
                        valign: 'middle',
                        sortable: true,
                        footerFormatter: totalTextFormatter
                    },
					 {
                        field: 'is_vip',
                        title: '是否是VIP',
                        sortable: true,
                        editable: false,
                        footerFormatter: totalNameFormatter,
                        align: 'center'
                    }, {
                        field: 'vip_to',
                        title: 'VIP有效期',
                        sortable: true,
                        align: 'center',                        
                        editable: {
                            type: 'datetime',
                            title: 'choose&date',
                            placement:'top|right|bottom|left',
                            emptytext:'无'
                            //条件判断功能
                            /*validate: function (value) {
                                value = $.trim(value);
                                var dB = new Date(value.replace(/-/g, "/"));
                                if (value==""||value==null) {
                                    return '请选择有效日期';
                                }
                                if (new Date() > Date.parse(dB)) {
                                    return '选择的有效时间不能小于当前时间'
                                }
                                var data = $table.bootstrapTable('getData'),
                                    index = $(this).parents('tr').data('index');
                                console.log(data[index]);
                                return '';
                            }*/
                        },
                        footerFormatter: totalPriceFormatter
                    }, {
                        field: 'operate',
                        title: '操作',
                        align: 'center',
                        events: operateEvents,
                        formatter: operateFormatter
                    }
					
                ]
            ]
        });
        // sometimes footer render error.
        setTimeout(function () {
            $table.bootstrapTable('resetView');
        }, 200);
        $table.on('check.bs.table uncheck.bs.table ' +
                'check-all.bs.table uncheck-all.bs.table', function () {
            $remove.prop('disabled', !$table.bootstrapTable('getSelections').length);

            // save your data, here just save the current page
            selections = getIdSelections();
            // push or splice the selections if you want to save all data selections
        });
        $table.on('expand-row.bs.table', function (e, index, row, $detail) {
           // if (index % 2 == 1) {
           //     $detail.html('Loading from ajax request...');
           //     $.get('LICENSE', function (res) {
           //         $detail.html(res.replace(/\n/g, '<br>'));
           //     });
           // }
        });
        $table.on('all.bs.table', function (e, name, args) {
            console.log(name, args);
        });
        $remove.click(function () {
            var ids = getIdSelections();
            $table.bootstrapTable('remove', {
                field: 'id',
                values: ids
            });
            $remove.prop('disabled', true);
        });
        $(window).resize(function () {
            $table.bootstrapTable('resetView', {
                height: getHeight()
            });
        });
    }

    function getIdSelections() {
        return $.map($table.bootstrapTable('getSelections'), function (row) {
            return row.id
        });
    }

    function responseHandler(res) {
        $.each(res.rows, function (i, row) {
            row.state = $.inArray(row.id, selections) !== -1;
        });
        return res;
    }

    function detailFormatter(index, row) {
        var html = [];
        $.each(row, function (key, value) {
            html.push('<p><b>' + key + ':</b> ' + value + '</p>');
        });
        return html.join('');
    }

    function operateFormatter(value, row, index) {
        return [
            '<a class="like" href="javascript:void(0)" title="确定">',
            '<i class="glyphicon glyphicon-heart"></i>',
            '</a>  '
            //显示删除按钮的js语句
            //, 
            //'<a class="remove" href="javascript:void(0)" title="Remove">',
            //'<i class="glyphicon glyphicon-remove"></i>',
            //'</a>'
        ].join('');
    }

    window.operateEvents = {
        'click .like': function (e, value, row, index) {
            //alert('You click like action, row: ' + JSON.stringify(row)+' row:'+row.user_name+' index:'+index);
        	if(window.confirm('确定修改？')){
                //alert("确定");
               window.location.href= '/Yltysport/user/updateVip_to/'+row.user_name+'?vip_to='+row.vip_to;
                return true;
             }else{
                //alert("取消");
                return false;
            }            
        },
        'click .remove': function (e, value, row, index) {
            $table.bootstrapTable('remove', {
                field: 'id',
                values: [row.id]
            });
        }
    };

    function totalTextFormatter(data) {
        return 'Total';
    }

    function totalNameFormatter(data) {
        return data.length;
    }

    function totalPriceFormatter(data) {
        var total = 0;
        $.each(data, function (i, row) {
            total += +(row.price.substring(1));
        });
        return '$' + total;
    }

    function getHeight() {
        return $(window).height() - $('h1').outerHeight(true);
    }

    $(function () {
        var scripts = [
                location.search.substring(1) || '/Yltysport/js/bootstrap-table.js',
                '/Yltysport/js/bootstrap-table-export.js',
                '/Yltysport/js/tableExport.js',
                '/Yltysport/js/bootstrap-editable.js',
                '/Yltysport/js/bootstrap-table-editable.js'
                
            ],
            eachSeries = function (arr, iterator, callback) {
                callback = callback || function () {};
                if (!arr.length) {
                    return callback();
                }
                var completed = 0;
                var iterate = function () {
                    iterator(arr[completed], function (err) {
                        if (err) {
                            callback(err);
                            callback = function () {};
                        }
                        else {
                            completed += 1;
                            if (completed >= arr.length) {
                                callback(null);
                            }
                            else {
                                iterate();
                            }
                        }
                    });
                };
                iterate();
            };

        eachSeries(scripts, getScript, initTable);
    });
    function getScript(url, callback) {
        var head = document.getElementsByTagName('head')[0];
        var script = document.createElement('script');
        script.src = url;

        var done = false;
        // Attach handlers for all browsers
        script.onload = script.onreadystatechange = function() {
            if (!done && (!this.readyState ||
                    this.readyState == 'loaded' || this.readyState == 'complete')) {
                done = true;
                if (callback)
                    callback();

                // Handle memory leak in IE
                script.onload = script.onreadystatechange = null;
            }
        };

        head.appendChild(script);

        // We handle everything using the script element injection
        return undefined;
    }
</script>
</body>
</html>
