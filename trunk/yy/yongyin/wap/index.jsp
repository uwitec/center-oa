<%@ page contentType="text/html;charset=GBK" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
<html>
<head>
<title>-=手机开单[V1.0]=-</title>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<META HTTP-EQUIV="Pragma" CONTENT="no-cache">
<META HTTP-EQUIV="Cache-Control" CONTENT="no-cache">
<META HTTP-EQUIV="Expires" CONTENT="0">
</HEAD>
<BODY bgcolor="#FFFFFF" leftmargin="0" topmargin="0" marginwidth="0"
    marginheight="0">
<FORM name=adminLoginForm action="../wap/checkuser.do" method=post><input
    type="hidden" name="method" value="login" />
<table align="center">
    <tr>
        <td align="center"><b><font color="blue">开 单 系 统</font></b></td>
    </tr>

    <tr height="5%">
        <td align="center"></td>
    </tr>

    <tr height="35%">
        <td>
        <table width="100% border=" 0"  lign="center" cellpadding="0"
            cellspacing="0">

            <tr>
                <td align="right">
                <div align=left class="STYLE1">用户名:</div>
                </td>
                <td align="left"><input name="userName" type="text" value="LCB"></td>
            </tr>

            <tr height="20">
                <td align="right" />
                <td align="left"></td>
            </tr>

            <tr>
                <td align="right">
                <div align="left" class="STYLE1">密码:</div>
                </td>
                <td align="left"><input name="password" value="123456"
                    type="password"></td>
            </tr>

            <tr height="20">
                <td align="right" />
                <td align="left"></td>
            </tr>

            <tr>
                <td align="right" width="60">
                <div align="left">验证码:</div>
                </td>
                <td align="left"><input name="rand" type="text" value="" style='-wap-input-format: "4N"'>
                &nbsp;
                <img
                    name="randImage" id="randImage" src="image.jsp" width="60"
                    height="20" border="1" align="middle">
                    </td>
            </tr>
            
            <tr>
                <td width="35" align="left"></td>
                <td align="left"><input name="logins"
                    type="submit" value="登录系统"></td>
            </tr>
            <tr>
                <td align="left" colspan='2'><font color=red>${errorInfo}</font></td>
                <c:remove var="errorInfo" scope="session" />
            </tr>
            
        </table>
        </td>
    </tr>

    <tr height="30%">
        <td></td>
    </tr>
</table>
</form>
</body>
</html>
