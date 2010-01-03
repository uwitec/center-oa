<%@ page contentType="text/html;charset=GBK" language="java"
	errorPage="./error.jsp"%>
<%@include file="./common.jsp"%>
<html>
<head>

<p:link title="增加业务员" />
<script language="JavaScript" src="../js/prototype.js"></script>
<script language="JavaScript" src="../js/public.js"></script>
<script language="JavaScript" src="../js/common.js"></script>
<script language="JavaScript" src="../js/cnchina.js"></script>
<script language="JavaScript" src="../js/buffalo.js"></script>
<script language="javascript">
var END_POINT="${pageContext.request.contextPath}/bfapp";

var buffalo = new Buffalo(END_POINT);

function callbacks()
{
	if (trim($("ywyName").value) == '')
	{
		alert("名称不能为空，请输入");
        return false;
	}

	if (trim($("stafferId").value) == '')
	{
		alert("职员名称不能为空，请输入");
        return false;
	}

	buffalo.remoteCall("userDAO.findUserByLoginName",[$("ywyName").value], function(reply) {
		        var result = reply.getResult();
		        if (result != null)
		        {
		        	alert("此人员已经存在,请重新填写");
		        	return null;
		        }

		        if (window.confirm("确定增加此人员"))
		        modifyPassword.submit();
		});

}

function load()
{
	//loadForm();
}
</script>

</head>
<body class="body_class" onload="load()">
<form name="modifyPassword" action="./checkuser.do"><input
	type="hidden" name="method" value="addYWY">
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
				<td width="550" class="navigation">人员管理 &gt;&gt; <span
					style="cursor:hand" onclick="javascript:history.go(-1)">浏览人员</span>
				&gt;&gt; 增加人员</td>
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

<br>
<table width="85%" border="0" cellpadding="0" cellspacing="0"
	align="center">
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
								<td width="15">&nbsp;</td>
								<td width="6"><img src="../images/dot_r.gif" width="6"
									height="6"></td>
								<td class="caption"><strong>人员信息:</strong></td>
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
		<td colspan='2' align='center'>
		<table width="65%" border="0" cellpadding="0" cellspacing="0"
			class="border">
			<tr>
				<td>
				<table width="100%" border="0" cellspacing='1'>
					<tr class="content2">
						<td width="30%">登陆名称:</td>
						<td><input type="text" name="ywyName" maxlength="16" ime=true><font
							color="#FF0000">*</font><input type="text" size="1" style="visibility: hidden"></td>
					</tr>

					<tr class="content1">
						<td width="30%">职员名称:</td>
						<td><select name="stafferId" quick="true">
							<option value="">--</option>
							<c:forEach items="${staffers}" var="item">
								<option value="${item.id}">${item.name}</option>
							</c:forEach>
						</select><font color="#FF0000">*</font></td>
					</tr>

					<tr class="content2">
						<td width="30%">职员角色:</td>
						<td><select name="role">
							<c:forEach items="${roleList}" var="item">
								<option value="${item.id}">${item.roleName}</option>
							</c:forEach>
						</select><font color="#FF0000">*</font></td>
					</tr>
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
		<td width="82%">
		<div align="right"><input name="adds" type="button" id="adds"
			class="button_class" value="&nbsp;&nbsp;确 定&nbsp;&nbsp;"
			onclick="callbacks()">&nbsp; <input name="cancel"
			type="reset" class="button_class" value="&nbsp;&nbsp;重 置&nbsp;&nbsp;">&nbsp;
		<input type="button" class="button_class"
			onclick="javascript:history.go(-1)"
			value="&nbsp;&nbsp;返 回&nbsp;&nbsp;"></div>
		</td>
		<td width="18%"></td>
	</tr>

	<tr height="10">
		<td height="10" colspan='2'></td>
	</tr>


	<tr>
		<td colspan='2' align="center"><FONT color="blue">${MESSAGE_INFO}</FONT><FONT
			color="red">${errorInfo}</FONT></td>
	</tr>


</table>


</form>
</body>
</html>

