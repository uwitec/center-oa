<%@ page contentType="text/html;charset=GBK" language="java"
	errorPage="./error.jsp"%>
<%@include file="./common.jsp"%>
<html>

<head>
<p:link title="人员列表" />
<script src="../js/compatible.js"></script>
<script src="../js/prototype.js"></script>
<script src="../js/common.js"></script>
<script src="../js/public.js"></script>
<script src="../js/fanye.js"></script>
<script language="javascript">
function addywy()
{
	document.location.href = '../admin/checkuser.do?method=addForAdmin';
}

function resetPassword()
{
	if (window.confirm("确定初始化" + getRadioValue("userName") + "密码?"))
	{
		document.location.href = '../admin/checkuser.do?method=initPassword&userName=' + getRadioValue("userName");
	}
}

function unlock()
{
	if (window.confirm("确定强制解锁" + getRadioValue("userName") + "?"))
	{
		document.location.href = '../admin/checkuser.do?method=unlock&userName=' + getRadioValue("userName");
	}
}

function deleteywy()
{
	if (getRadioValue("userName") == '')
	{
		alert('请选择一个人员');
		return;
	}

	if (window.confirm("您确认需要删除人员?"))
	if (window.confirm("确定删除" + getRadioValue("userName")))
	{
		document.location.href = '../admin/checkuser.do?method=delYWY&userName=' + getRadioValue("userName");
	}
}

function load()
{
	initPage(15, $("result"), "tr", 1, 0, true);

	$f('con');
}

function press()
{
	if (window.common.getEvent().keyCode == 13)
	{
		resetRadio('userName');
		queryInner($$('con'), 1);
	}
}

function resets()
{
	resetRadio('userName');

	initPage(15, $('result'), "tr", 1, 1, false);

	$('con').value = '';
}


</script>
<title>管理员列表</title>

</head>
<body class="body_class" onload="load()">
<form action="./login.do" name="adminForm"><input type="hidden"
	name="page">
<table width="100%" border="0" cellpadding="0" cellspacing="0">
	<tr>
		<td height="22" valign="bottom">
		<table width="100%" height="22" border="0" cellpadding="0"
			cellspacing="0">
			<tr valign="middle">
				<td width="8"></td>
				<td width="30">
				<div align="center"><img src="../images/dot_a.gif" width="9"
					height="9"></div>
				</td>
				<td width="550" class="navigation">人员管理 &gt;&gt; 浏览人员</td>
				<td width="85"></td>
			</tr>
		</table>
		</td>
	</tr>

	<tr>
		<td height="6" valign="top">
		<table width="100%" border="0" cellpadding="0" cellspacing="0">
			<!--DWLayoutTable-->
			<tr>
				<td width="8" height="6"
					background="../images/index_sp_welcome_center_10.gif"><img
					src="../images/index_sp_welcome_center_07.gif" width="8" height="6"></td>
				<td width="190"
					background="../images/index_sp_welcome_center_08.gif"></td>
				<td width="486"
					background="../images/index_sp_welcome_center_10.gif"></td>
				<td align="right"
					background="../images/index_sp_welcome_center_10.gif">
				<div align="right"><img
					src="../images/index_sp_welcome_center_12.gif" width="23"
					height="6"></div>
				</td>
			</tr>
		</table>
		</td>
	</tr>
</table>


<table width="85%" border="0" cellpadding="0" cellspacing="0"
	align="center">

		<tr>
		<td height="20" colspan='2'></td>
	</tr>

		<p:subBody width="85%">
		<table width="100%" align="center" cellspacing='1' class="table0">
			<tr align=center class="content0">
				<td align="center" >人员名称</td>
				<td align="center"
					width="35%"><input type="text" name="con" onkeypress="press()"><input type="text" name="con1" style="visibility: hidden;" size=1></td>
				<td align="center" colspan="2"><input type="button" class="button_class"
				value="&nbsp;&nbsp;查 询&nbsp;&nbsp;" onclick="queryInner($$('con'), 1)">&nbsp;&nbsp;
				<input type="button" class="button_class"
				value="&nbsp;&nbsp;全 部&nbsp;&nbsp;" onclick="resets()">
				</td>
			</tr>
		</table>

	</p:subBody>

	<tr>
		<td valign="top" colspan='2'>
		<table width="100%" border="0" cellpadding="0" cellspacing="0">
			<!--DWLayoutTable-->
			<tr>
				<td width="784" height="6"></td>
			</tr>
			<tr>
				<td align="center" valign="top">
				<div align="left">
				<table width="90%" border="0" cellspacing="2">
					<tr>
						<td>
						<table width="100%" border="0" cellpadding="0" cellspacing="10">
							<tr>
								<td width="35">&nbsp;</td>
								<td width="6"><img src="../images/dot_r.gif" width="6"
									height="6"></td>
								<td class="caption"><strong>浏览人员:</strong></td>
							</tr>
						</table>
						</td>
					</tr>
				</table>
				</td>
			</tr>
		</table>
		</td>
	</tr>


	<tr>
		<td background="../images/dot_line.gif" colspan='2'></td>
	</tr>

	<tr>
		<td height="10" colspan='2'></td>
	</tr>

	<tr>
		<td align='center' colspan='2'>
		<table width="85%" border="0" cellpadding="0" cellspacing="0"
			class="border">
			<tr>
				<td>
				<table width="100%" border="0" cellspacing='1' id="result">
					<tr align="center" class="content0">
						<td align="center" width="8%" align="center">选择</td>
						<td align="center">登陆名称</td>
						<td align="center">职员</td>
						<td align="center">角色</td>
						<td align="center">区域</td>
						<td align="center">状态</td>
					</tr>

					<c:forEach items="${userList}" var="item" varStatus="vs">
						<tr class='${vs.index % 2 == 0 ? "content1" : "content2"}'>
							<td align="center"><input type="radio" name="userName"
								value="${item.name}" ${vs.index== 0 ? "checked" : ""}/></td>
							<td align="center">${item.name}</td>
							<td align="center">${item.stafferName}</td>
							<td align="center">${item.role}</td>
							<td align="center">${item.locationName}</td>
							<td align="center">${item.status == 0 ? "正常" : "锁定"}</td>
						</tr>
					</c:forEach>
				</table>
				</td>
			</tr>
		</table>
		</td>
	</tr>

	<tr>
		<td height="10" colspan='2'></td>
	</tr>


	<tr>
		<td background="../images/dot_line.gif" colspan='2'></td>
	</tr>

	<tr>
		<td height="10" colspan='2'></td>
	</tr>

	<tr>
		<td width="93%">
		<div align="right"><c:if test='${user.role != "TOP"}'>
			<input type="button" class="button_class" value="&nbsp;增加人员&nbsp;" name="adds" id="adds"
				onClick="addywy()">&nbsp;&nbsp;
			</c:if> <input name="resetPwd" type="button" class="button_class"
			value="&nbsp;初始化密码&nbsp;" onClick="resetPassword()">&nbsp;&nbsp;
		<input type="button" class="button_class" value="&nbsp;强制解锁&nbsp;"
			onClick="unlock()">&nbsp;&nbsp; <input name="delete"
			type="button" class="button_class" value="&nbsp;删除人员&nbsp;"
			onClick="deleteywy()">&nbsp;&nbsp;</div>
		</td>
		<td width="7%"></td>
	</tr>

	<tr height="10">
		<td height="10" colspan='2'></td>
	</tr>

	<tr>
		<td colspan='2' align="center"><FONT color="blue">${MESSAGE_INFO}</FONT>
		</td>
	</tr>
</table>

</form>
</body>
</html>
