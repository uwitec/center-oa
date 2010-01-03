<%@ page contentType="text/html;charset=GBK" language="java"
	errorPage="./error.jsp"%>
<%@include file="./common.jsp"%>
<html>
<head>
<p:link title="修改网络订阅" />
<script language="JavaScript" src="../js/common.js"></script>
<script language="JavaScript" src="../js/public.js"></script>
<script language="JavaScript" src="../js/JCheck.js"></script>

<script language="javascript">
function updateBean()
{
	submit('确定修网络订阅?');
}

function clears()
{
	if (window.confirm('确定清除缓存的链接?'))
	document.location.href = '../admin/net.do?method=clearUrl';
}


function load()
{
	loadForm();
}

</script>

</head>
<body class="body_class" onload="load()">
<form name="addApply" action="../admin/net.do" method="post"><input
	type="hidden" name="method" value="updateNetAccess"> <p:navigation
	height="22">
	<td width="550" class="navigation"><span style="cursor: hand"
		onclick="javascript:history.go(-1)">网络订阅管理</span> &gt;&gt; 修改网络订阅</td>
	<td width="85"></td>
</p:navigation> <br>

<p:body width="80%">

	<p:title>
		<td class="caption"><strong>网络订阅信息：</strong></td>
	</p:title>

	<p:line flag="0" />

	<p:subBody width="75%">
		<p:table cells="1">
			<p:cell title="smtp">
				<input name="smtp" value="${bean.smtp}" oncheck="notNone" size="70">
			</p:cell>

			<p:cell title="用户">
				<input name="user" value="${bean.user}" oncheck="notNone" size="70">
			</p:cell>

			<p:cell title="密码">
				<input type="password" name="password" value="" oncheck="notNone" size="70">
			</p:cell>

			<p:cell title="发送地址">
				<input name="fromUser" value="${bean.fromUser}" oncheck="notNone" size="70">
			</p:cell>

			<p:cell title="显示名称">
				<input name="displayName" value="${bean.displayName}" size="70"
					oncheck="notNone">
			</p:cell>

			<p:cell title="收件人">
				<textarea name="to"  oncheck="notNone" cols="70" rows="4">${to}</textarea>
			</p:cell>

		</p:table>
	</p:subBody>

	<p:line flag="1" />

	<p:button leftWidth="87%" rightWidth="13%">
		<div align="right"><input type="button" class="button_class"
			style="cursor: pointer" value="&nbsp;&nbsp;提 交&nbsp;&nbsp;"
			onclick="updateBean()">&nbsp;&nbsp; <input type="button"
			class="button_class" onclick="clears()"
			value="&nbsp;&nbsp;清空链接&nbsp;&nbsp;"></div>
	</p:button>

	<tr>
		<td colspan='2' align="center"><FONT color="blue">${MESSAGE_INFO}</FONT><FONT
			color="red">${errorInfo}</FONT></td>
	</tr>
</p:body></form>
</body>
</html>

