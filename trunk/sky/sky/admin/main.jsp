<%@ page contentType="text/html;charset=GBK" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
<html>
<head>
<title>SKY-OA系统</title>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<script language="JavaScript" type="text/JavaScript">
function Logout()
{
}
</script>
<frameset rows="63,*" cols="*" frameborder="NO" border="0"
	framespacing="0" onUnload="Logout()">
	<frame src="top.jsp" name="topFrame" scrolling="NO" id="topFrame">
	<frameset cols="191,*" framespacing="0" frameborder="no" border="0">

		<frame src="shousuo.jsp" name="fun" noresize scrolling="auto">

		<frame src="../pers/persional.do?method=queryPersionalDeskTop" name="main">
	</frameset>
</frameset>
<noframes>
<body>
你使用的浏览器不支持框架。
</body>
</noframes>
</html>
