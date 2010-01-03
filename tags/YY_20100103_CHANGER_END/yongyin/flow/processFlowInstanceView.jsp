<%@ page contentType="text/html;charset=GBK" language="java"
	errorPage="./error.jsp"%>
<%@include file="./common.jsp"%>
<html>
<head>
<p:link title="查看流程查阅" />
<script language="JavaScript" src="../js/common.js"></script>
<script language="JavaScript" src="../js/JCheck.js"></script>
<script language="JavaScript" src="../js/public.js"></script>
<script language="javascript">

var cindex = -1;
function addBean(opr)
{
	submit('确定提交此流程的查阅者?', null, lverify);
}

function lverify()
{
	var checkArr = document.getElementsByName('check_init');

	var isSelect = 0;

	var imap = {};

	for (var i = 0; i < checkArr.length; i++)
	{
		var obj = checkArr[i];

		var index = obj.value;

		if (obj.checked)
		{
			isSelect++;
			if ($('tokenType_' + i).value == '' )
			{
				alert('查看类型不能为空');
				return false;
			}

			if ($$('tokenType_' + i)  != "2")
			{
				if ($$('processerId_' + i) == '')
				{
					alert('请选择查阅者');
					return false;
				}
			}
		}
	}

	if (isSelect == 0)
	{
		alert('请选择查阅者');
		return false;
	}

	return true;
}
function load()
{
	loadForm();

	init();
}

function init()
{
	var checkArr = document.getElementsByName('check_init');

	for (var i = 0; i < checkArr.length; i++)
	{
		var obj = checkArr[i];

		var index = obj.value;

		if (obj.checked)
		{
			$d('tokenType_' + index, false);
			$d('processer_' + index, false);
		}
		else
		{
			$('tokenType_' + index).value = '';
			$('processer_' + index).value = '';
			$d('tokenType_' + index);
			$d('processer_' + index);
		}
	}
}


var urlMap = 
{
"0" : "../admin/common.do?method=rptUser&firstLoad=1",
"1" : "../admin/common.do?method=rptRole",
"3" : "../admin/common.do?method=rptStaffer&firstLoad=1"
};

function selectProcesser(index)
{
	cindex = index;

	if ($$('tokenType_' +cindex) == "")
	{
		alert("请选择查看类型");
		return;
	}

	if ($$('tokenType_' +cindex) == "2")
	{
		return;
	}
	
	window.common.modal(urlMap[$$('tokenType_' +cindex)]);
}

function getUserOrRole(oo)
{
	if (cindex != -1)
	{
		$("processer_" + cindex).value = oo.processerName;
		$("processerId_" + cindex).value = oo.value;
	}
}

function getStaffer(oo)
{
	if (cindex != -1)
	{
		$("processer_" + cindex).value = oo.processerName;
		$("processerId_" + cindex).value = oo.value;
	}
}

function change(index, obj)
{
	$('processer_' + index).value = '';
	$('processerId_' + index).value = '';
}

</script>

</head>
<body class="body_class" onload="load()">
<form name="formEntry" action="../flow/flow.do" method="post"><input
	type="hidden" name="method" value="processFlowViewer">
	<input type="hidden" name="oprMode" value="">
	<input
	type="hidden" name="flowId" value="${id}"> <p:navigation
	height="22">
	<td width="550" class="navigation"><span style="cursor: hand"
		onclick="javascript:history.go(-1)">流程管理</span> &gt;&gt; 查看流程查阅</td>
	<td width="85"></td>
</p:navigation> <br>

<p:body width="100%">

	<p:title>
		<td class="caption"><strong>流程查阅信息：</strong></td>
	</p:title>

	<p:line flag="0" />

	<p:subBody width="100%">
		<p:table cells="1">
			<p:cells celspan="2" title="帮助说明">
			查看类型分四类<br>
			1、人员查看就是流程结束后具体某一人员可以查看<br>
			2、区域角色查看就是流程结束后由区域下相关角色查看,除了超级管理员之外<br>
			3、全部就是所有人可以查看<br>
			4、职员就是就是流程中结束后由某一职员可以查看，只要登录的人员从属于这个职员都可以查看
			</p:cells>
			
			<p:cells id="names" celspan="2" title="流程名称">${bean.name}</p:cells>

			<p:cells id="selects" celspan="2" title="流程查阅">
				<table id="mselect">
					<c:forEach begin="0" end="9" var="item" >
					<tr>
						<td>
						<input type="checkbox" name="check_init" value="${item}" onclick="init()" ${view[item].id > 0 ? "checked='checked'" : ""}>
						查看类型:
							<select class="select_class" name="tokenType_${item}" onchange="change(${item}, this)" values="${view[item].type}">
								<option value="">--</option>
								<option value="3">职员</option>
								<option value="0">人员</option>
								<option value="1">区域角色</option>
								<option value="2">全部</option>
							</select>&nbsp;
							查看者:<input
							type="text" name="processer_${item}" value="${view[item].processerName}" size="15" readonly="readonly">&nbsp;
							<input type="button" value="&nbsp;...&nbsp;" name="qout"
							class="button_class" onclick="selectProcesser(${item})">
							<input type="hidden" name="processerId_${item}" value="${view[item].processer}">
							</td>
					</tr>
					</c:forEach>
					
				</table>
			</p:cells>

		</p:table>
	</p:subBody>

	<p:line flag="1" />

	<p:button leftWidth="100%" rightWidth="0%">
		<div align="right">
		<input type="button" class="button_class"
			name="adds" style="cursor: pointer"
			value="&nbsp;&nbsp;提 交&nbsp;&nbsp;" onclick="addBean(1)">&nbsp;&nbsp;
		<input type="button" class="button_class"
			onclick="javascript:history.go(-1)"
			value="&nbsp;&nbsp;返 回&nbsp;&nbsp;"></div>
	</p:button>

	<tr>
		<td colspan='2' align="center"><FONT color="blue">${MESSAGE_INFO}</FONT><FONT
			color="red">${errorInfo}</FONT></td>
	</tr>
</p:body></form>
</body>
</html>

