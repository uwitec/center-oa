<%@ page contentType="text/html;charset=GBK" language="java"
	errorPage="./error.jsp"%>
<%@include file="./common.jsp"%>
<HTML>
<HEAD>
<p:link title="系统配置菜单" cal="false"/>
<style type="text/css">
</style>
</HEAD>
<BODY class="tree_class">
<table>
	<tr height="10">
		<td colspan="2"></td>
	</tr>

	<tr height="10">
		<td width="15"></td>
		<td><font color="blue"><B>[${GLocationName}]
		${user.stafferName} 登陆[3.0使用版]</B></font></td>
	</tr>

	<tr height="10">
		<td colspan="2"></td>
	</tr>

	<tr>
		<td width="15"></td>
		<td><font color="blue"><B>今天：${GTime}</B></font></td>
	</tr>



	<tr height="10">
		<td colspan="2"></td>
	</tr>

	<c:forEach items="${menuItemList}" var="item">
		<tr>
			<td width="15"></td>
			<td><a href="${item.url}" target="main">${item.menuItemName}</a></td>
		</tr>

		<tr height="10">
			<td colspan="2"></td>
		</tr>
	</c:forEach>

	<tr>
		<td width="15"></td>
		<td><a href="../admin/modifyPassword.jsp" target="main">修改密码</a></td>
	</tr>

	<tr height="10">
		<td colspan="2"></td>
	</tr>

	<tr>
		<td width="15"></td>
		<td><a href="../admin/logout.do" target="_parent">退出登陆</a></td>
	</tr>

	<tr height="10">
		<td colspan="2"></td>
	</tr>

</table>
</BODY>
</HTML>
