<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
String besePath = request.getScheme() + "://" +
request.getServerName() + ":" + request.getServerPort() +
request.getContextPath() + "/";
%>
<!DOCTYPE html>
<html>
<head>
	<base href = "<%=besePath%>">
<meta charset="UTF-8">

<link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
<link href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css" type="text/css" rel="stylesheet" />

<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>

<link rel="stylesheet" type="text/css" href="jquery/bs_pagination/jquery.bs_pagination.min.css">
<script type="text/javascript" src="jquery/bs_pagination/jquery.bs_pagination.min.js"></script>
<script type="text/javascript" src="jquery/bs_pagination/en.js"></script>

<script type="text/javascript">

	$(function(){
		//页面加载完毕之后触发一个方法
		//默认展开列表的第一页，每页展现两条记录
		pageList(1,2);

		//为创建按钮绑定事件，打开添加操作的模态窗口
		$("#addBtn").click(function (){
			$(".time").datetimepicker({
				minView:"month",
				language:'zh-CN',
				format:'yyyy-mm-dd',
				autoclose:true,
				todayBtn:true,
				pickerPosition: "bottom-left"
			});

			/*
			操作模态窗口的方式:
				需要操作的模态窗口的jquery对象，调用modal方法，为该方法传递参数
				show：打开模态窗口  hide：关闭模态窗口
			 */
			//走后台，目的是为了取得用户信息列表，为所有者下拉框铺值
			$.ajax({
				url:"activity/getUserList.do",
				type:"get",
				dataType:"json",
				success:function (data){
					//alert(data)
					var html = "";
					//遍历出来的每一个n，就是每一个user对象
					$.each(data,function (i,n){
						//也使用append()来拼接数据
						html += "<option value='"+ n.id +"'>" + n.name + "</option>";
					})

					$("#create-marketActivityOwner").html(html);

					//将当前登录的用户，设置为下拉框默认的选项
					//中js中使用el表达式，el表达式一定要套用在字符串中
					var id = "${user.id}";
					$("#create-marketActivityOwner").val(id);

					//所有者下拉框处理完毕后，展现模态窗口
					$("#createActivityModal").modal("show");
				}

			})

		})
		//为保存按钮绑定事件，执行添加操作, $.trim()去空格
		$("#saveBtn").click(function (){
			$.ajax({
				url:"activity/save.do",
				data:{
					"owner" :$.trim($("#create-marketActivityOwner").val()),
					"name" :$.trim($("#create-marketActivityName").val()),
					"startDate" :$.trim($("#create-startTime").val()),
					"endDate" :$.trim($("#create-endTime").val()),
					"cost" :$.trim($("#create-cost").val()),
					"description" :$.trim($("#create-describe").val())
				},
				type:"post",
				dataType:"json",
				success:function (data){
					/*
						data：{“success”:true /false}
					 */

					if(data.sucess){
						//添加成功后，刷新市场活动信息列表
						//pageList(1,2);
						/*
						$("#activityPage").bs_pagination('getOption', 'currentPage')
						表示操作后停留在当前页
						$("#activityPage").bs_pagination('getOption', 'rowsPerPage')
						表示操作后维持已经设置好的每页展现的记录数
						*/
						pageList(1,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));

						//清空添加操作模态窗口中的数据
						$("#activityAddForm")[0].reset();

						//关闭添加操作的模态窗口
						$("#createActivityModal").modal("hide");
					}else {
						alert("添加市场活动失败")
					}
				}
			})
		})



		//为查询按钮绑定事件
		$("#searchBtn").click(function (){
			/*
				点击查询按钮的时候，我们应该将搜索框中的信息保存起来
				保存在隐藏域当中
			 */
			$("#hidden-name").val($.trim($("#search-name").val()));
			$("#hidden-owner").val($.trim($("#search-owner").val()));
			$("#hidden-startDate").val($.trim($("#search-startTime").val()));
			$("#hidden-endDate").val($.trim($("#search-endTime").val()));
			pageList(1,2);
		})

		//为全选的复选框绑定事件，触发全选操作
		$("#qx").click(function (){
			$("input[name=xz]").prop("checked",this.checked);
		})
		//以下这种做法是不行的，因为动态生成的元素，是不能
		//够以普通绑定事件的形式来进行操作的
		/*$("input[name=xz]").click(function (){

		})*/
		/*
		动态生产的元素，我们要以on方法的形式来触发事件
		语法：
			$(需要绑定元素的有效的外层元素).on(绑定事件的方式,需要绑定的元素的jquery对象,回调函数)
		 */
		$("#activityBody").on("click", $("input[name=xz]"),function (){
			$("#qx").prop("checked",$("input[name=xz]").length==$("input[name=xz]:checked").length);
		})

		//为删除按钮绑定事件，执行市场活动删除操作
		$("#deleteBtn").click(function (){
			//找到复选框中所有选中的复选框的jquery对象
			var $xz = $("input[name=xz]:checked");
			if($xz.length ==0){
				alert("请选择需要删除的记录")
			}else{
				//弹出一个窗口，询问用户是否要删除 ：confirm方法
				if(confirm("确定删除选中的记录吗？")){
					//肯定选了，可能1条，可能多条
					//url：delete.do?id=xxx&id=xxx...
					//拼接参数：
					var param = "";
					//将$xz中的每一个dom对象遍历出来，取其value值
					for(var i=0; i<$xz.length; i++){
						param += "id=" + $($xz[i]).val();
						//如果不是最后一个元素，需要在后面追加一个&符
						if(i<$xz.length-1){
							param += "&";
						}
					}
					//alert(param)
					$.ajax({
						url:"activity/delete.do",
						data:param,
						type:"post",
						dataType:"json",
						success:function (data){
							/*
                                data:
                                {"sucess":true/false}
                             */
							if(data.sucess){
								//删除成功后
								//pageList(1,2);
								pageList(1,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));
							}else{
								alert("删除市场活动失败");
							}
						}
					})
				}
			}
		})

		//为修改按钮绑定事件，打开修改操作的模态窗口
		$("#editBtn").click(function (){
			var $xz = $("input[name=xz]:checked");

			if($xz.length==0){
				alert("请选择需要修改的记录")
			}else if($xz.length > 1){
				alert("只能选择一条记录进行修改")
			}else {
				//肯定只选了一条
				var id = $($xz).val();
				$.ajax({
					url:"activity/getUserListAndActivity.do",
					data:{
						"id":id
					},
					type:"get",
					dataType:"json",
					success:function (data){
						/*
						data:
						用户列表
						市场活动对象
						{“uList":[{用户1},{},{}],"a":{市场活动}}
						 */

						//处理所有者的下拉框
						var html = "";
						$.each(data.uList, function (i,n){
							html += "<option value='" + n.id + "'>"+ n.name +"</option>";
						})
						$("#edit-marketActivityOwner").html(html);

						//处理单条activity
						$("#edit-id").val(data.a.id);
						$("#edit-marketActivityName").val(data.a.name);
						$("#edit-marketActivityOwner").val(data.a.owner);
						$("#edit-startTime").val(data.a.startDate);
						$("#edit-endTime").val(data.a.endDate);
						$("#edit-cost").val(data.a.cost);
						$("#edit-describe").val(data.a.description);

						//所有的值都填写之后，打开修改操作的模态窗口
						$("#editActivityModal").modal("show")

					}
				})
			}
		})
		/*
			在实际项目开发中，一定是先做添加，在做修改
			修改操作一般都是copy添加操作
		 */
		//为更新按钮绑定事件，执行市场活动的修改操作
		$("#updateBtn").click(function (){
			if(confirm("确认修改记录吗？")){
				$.ajax({
					url:"activity/update.do",
					data:{
						"id":$.trim($("#edit-id").val()),
						"owner" :$.trim($("#edit-marketActivityOwner").val()),
						"name" :$.trim($("#edit-marketActivityName").val()),
						"startDate" :$.trim($("#edit-startTime").val()),
						"endDate" :$.trim($("#edit-endTime").val()),
						"cost" :$.trim($("#edit-cost").val()),
						"description" :$.trim($("#edit-describe").val())
					},
					type:"post",
					dataType:"json",
					success:function (data){
						/*
                            data：{“success”:true /false}
                         */

						if(data.sucess){
							//修改成功后，刷新市场活动信息列表
							//pageList(1,2)
							pageList($("#activityPage").bs_pagination('getOption', 'currentPage')
									,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));
							//关闭添加操作的模态窗口
							$("#editActivityModal").modal("hide");
						}else {
							alert("修改市场活动失败")
						}
					}
				})
			}
		})

	});
	/*
	对于所有的关系型数据库，做前端的分页相关操作的基础组件
	就是pageNo和pageSize
	pageNo：页码
	pageSize：每页展现的记录数

	pageList方法，就是发出ajax请求到后台，从后台取得最新数据
				通过响应回来的数据，局部刷新活动信息列表

	我们什么时候使用pageList方法：
		1）点击左侧菜单中的“市场活动”超链接
		2）添加，修改，删除后，需要刷新市场活动列表
		3）点击查询按钮的时候
		4）点击分页组件的时候

	以上指定了6个入口，中6个操作完成后，必须调用pageList方法，刷新市场活动列表
	 */
	function pageList(pageNo,pageSize){
		//将全选的复选框的 勾  干掉
		$("#qx").prop("checked",false);

		//查询前，将隐藏域中保存的信息取出，重新赋予到搜索框中
		$("#search-name").val($.trim($("#hidden-name").val()));
		$("#search-owner").val($.trim($("#hidden-owner").val()));
		$("#search-startTime").val($.trim($("#hidden-startDate").val()));
		$("#search-endTime").val($.trim($("#hidden-endDate").val()));


		$.ajax({
			url:"activity/pageList.do",
			data:{
				"pageNo":pageNo,
				"pageSize":pageSize,
				"name":$.trim($("#search-name").val()),
				"owner":$.trim($("#search-owner").val()),
				"startDate":$.trim($("#search-startTime").val()),
				"endDate":$.trim($("#search-endTime").val())
			},
			type:"get",
			dataType:"json",
			success:function (data){
				/*
					data:{"total":100,"dataList":[{市场活动1}，{市场活动2}，{市场活动3}]}
						我们需要的
						[{市场活动1}，{市场活动2}，{市场活动3}]
						一会分页插件需要的：查询出来的总记录数
						{"total":100}
				 */
				var html = "";
				$.each(data.dataList, function (i,n){
						html +=	'<tr class="active" >'
						html += '<td><input type="checkbox" name="xz" value="'+ n.id +'"/></td>'
						html += '<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href=\'activity/detail.do?id='+ n.id +'\';">'+n.name +'</a></td>'
						html += '<td>'+ n.owner +'</td>'
						html += '<td>'+ n.startDate +'</td>'
						html += '<td>'+ n.endDate +'</td>'
						html +=	'</tr>'
				})
				$("#activityBody").html(html);

				//计算总页数
				var totalPages = data.total%pageSize==0?data.total/pageSize:parseInt((data.total/pageSize)+1)

				//数据处理完毕后，结合分页查询，对前端展现分页信息
				$("#activityPage").bs_pagination({
					currentPage: pageNo, // 页码
					rowsPerPage: pageSize, // 每页显示的记录条数
					maxRowsPerPage: 20, // 每页最多显示的记录条数
					totalPages: totalPages, // 总页数
					totalRows: data.total, // 总记录条数

					visiblePageLinks: 3, // 显示几个卡片

					showGoToPage: true,
					showRowsPerPage: true,
					showRowsInfo: true,
					showRowsDefaultInfo: true,

					//该回调函数是在点击分页组件的时候触发的
					onChangePage : function(event, data){
						pageList(data.currentPage , data.rowsPerPage);
					}
				});
			}
		})
	}
</script>
</head>
<body>
	<!--隐藏域-->
	<input type="hidden" id="hidden-name"/>
	<input type="hidden" id="hidden-owner"/>
	<input type="hidden" id="hidden-startDate"/>
	<input type="hidden" id="hidden-endDate"/>
	<!-- 创建市场活动的模态窗口 -->
	<div class="modal fade" id="createActivityModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel1">创建市场活动</h4>
				</div>
				<div class="modal-body">
				
					<form id="activityAddForm" class="form-horizontal" role="form">
					
						<div class="form-group">
							<label for="create-marketActivityOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="create-marketActivityOwner">

								</select>
							</div>
                            <label for="create-marketActivityName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="create-marketActivityName">
                            </div>
						</div>
						
						<div class="form-group">
							<label for="create-startTime" class="col-sm-2 control-label">开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="create-startTime" readonly>
							</div>
							<label for="create-endTime" class="col-sm-2 control-label">结束日期</label>
							<div class="col-sm-10 " style="width: 300px;">
								<input type="text" class="form-control time" id="create-endTime" readonly>
							</div>
						</div>
                        <div class="form-group">

                            <label for="create-cost" class="col-sm-2 control-label">成本</label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="create-cost">
                            </div>
                        </div>
						<div class="form-group">
							<label for="create-describe" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="create-describe"></textarea>
							</div>
						</div>
						
					</form>
					
				</div>
				<div class="modal-footer">
					<!--data-dismiss="modal":表示关闭模态窗口-->
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="saveBtn">保存</button>
				</div>
			</div>
		</div>
	</div>
	
	<!-- 修改市场活动的模态窗口 -->
	<div class="modal fade" id="editActivityModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel2">修改市场活动</h4>
				</div>
				<div class="modal-body">
				
					<form class="form-horizontal" role="form">
						<input type="hidden" id="edit-id"/>
						<div class="form-group">
							<label for="edit-marketActivityOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="edit-marketActivityOwner">

								</select>
							</div>
                            <label for="edit-marketActivityName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="edit-marketActivityName" >
                            </div>
						</div>

						<div class="form-group">
							<label for="edit-startTime" class="col-sm-2 control-label">开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="edit-startTime">
							</div>
							<label for="edit-endTime" class="col-sm-2 control-label">结束日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="edit-endTime">
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-cost" class="col-sm-2 control-label">成本</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-cost" >
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-describe" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<!--
								关于文本域textarea：
								（1）一定是要以标签对的形式来呈现，正常状态下标签对要紧紧的挨着
								（2）textarea虽然是以标签的形式来呈现的，但是它也是属于表达元素范畴
									我们所有的对于textarea的取值和赋值操作，应该统一使用val()方法，而不是html()方法
								-->
								<textarea class="form-control" rows="3" id="edit-describe"></textarea>
							</div>
						</div>
						
					</form>
					
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="updateBtn">更新</button>
				</div>
			</div>
		</div>
	</div>
	
	
	
	
	<div>
		<div style="position: relative; left: 10px; top: -10px;">
			<div class="page-header">
				<h3>市场活动列表</h3>
			</div>
		</div>
	</div>
	<div style="position: relative; top: -20px; left: 0px; width: 100%; height: 100%;">
		<div style="width: 100%; position: absolute;top: 5px; left: 10px;">
		
			<div class="btn-toolbar" role="toolbar" style="height: 80px;">
				<form class="form-inline" role="form" style="position: relative;top: 8%; left: 5px;">
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">名称</div>
				      <input class="form-control" type="text" id="search-name">
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">所有者</div>
				      <input class="form-control" type="text" id="search-owner">
				    </div>
				  </div>


				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">开始日期</div>
					  <input class="form-control" type="text" id="search-startTime"/>
				    </div>
				  </div>
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">结束日期</div>
					  <input class="form-control" type="text" id="search-endTime">
				    </div>
				  </div>
				  
				  <button type="button" id="searchBtn" class="btn btn-default">查询</button>
				  
				</form>
			</div>
			<div class="btn-toolbar" role="toolbar" style="background-color: #F7F7F7; height: 50px; position: relative;top: 5px;">
				<div class="btn-group" style="position: relative; top: 18%;">
				  <button type="button" class="btn btn-primary" id="addBtn"><span class="glyphicon glyphicon-plus"></span> 创建</button>
				  <button type="button" class="btn btn-default" id="editBtn"><span class="glyphicon glyphicon-pencil"></span> 修改</button>
				  <button type="button" class="btn btn-danger" id="deleteBtn"><span class="glyphicon glyphicon-minus"></span> 删除</button>
				</div>
				
			</div>
			<div style="position: relative;top: 10px;">
				<table class="table table-hover">
					<thead>
						<tr style="color: #B3B3B3;">
							<td><input type="checkbox" id="qx"/></td>
							<td>名称</td>
                            <td>所有者</td>
							<td>开始日期</td>
							<td>结束日期</td>
						</tr>
					</thead>
					<tbody id="activityBody">

					</tbody>
				</table>
			</div>
			
			<div style="height: 50px; position: relative;top: 30px;">
				<div id="activityPage"></div>

			</div>
			
		</div>
		
	</div>
</body>
</html>