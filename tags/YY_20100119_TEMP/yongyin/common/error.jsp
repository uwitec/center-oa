<%@ page contentType="text/html;charset=GBK" language="java" isErrorPage ="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
<%@ page import="org.apache.commons.logging.Log, org.apache.commons.logging.LogFactory" %>

<html>
<head>
<title>错误提示</title>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">

</head>
<body bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0"
	background="../images/special/bg.gif">
<table width="100%" height="508" border="0" cellpadding="0"
	cellspacing="0">
	<tr>
		<td height="26" valign="bottom">
		<table width="100%" height="26" border="0" cellpadding="0"
			cellspacing="0">
			<tr>
				<td width="21"></td>
				<td width="162"><font color="blue" size="2">SKY办公系统</font></td>
				<td align="right"></td>
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
	<tr>
		<td height="477" valign="top">
		<table width="100%" border="0" cellpadding="0" cellspacing="0">
			<!--DWLayoutTable-->
			<tr>
				<td width="784" height="159"></td>
			</tr>
			<tr>
				<td height="318" align="center" valign="top" class="background">
				<table border="0" cellspacing="8">
					<tr>
						<td>
						<div align="center" style="cursor:hand"><img
							src="../images/error.gif" width="84" height="35">
							</div>
							<% 
						if (exception != null)
						{
							exception.printStackTrace(new java.io.PrintWriter(out));
						}
					%>
						</td>
					</tr>
					<tr>
						<td align="center"><span class="style1"><font size="3">
						<strong>${errorInfo}</strong> </font></span></td>
					</tr>
				</table>
				</td>
			</tr>
		</table>
		</td>
	</tr>
</table>
</body>
</html>
