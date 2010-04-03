<%@ page contentType="text/html;charset=GBK" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
<html>
<head>
<title>-=手机开单[V1.0]=-</title>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<META HTTP-EQUIV="Pragma" CONTENT="no-cache">
<META HTTP-EQUIV="Cache-Control" CONTENT="no-cache">
<META HTTP-EQUIV="Expires" CONTENT="0">
<script language="JavaScript">
function nexts()
{
    formd.method.value = 'step3';
    
    formd.submit();
}

function querys()
{
    formd.method.value = 'queryCustomer';
    
    formd.submit();
}

function pres()
{
    document.location.href = './step1.jsp';
}
</script>
</HEAD>

<BODY>
<FORM name=formd action="../wap/out.do" method=post><input
	type="hidden" name="method" value="queryCustomer" />
<table align="center">
	<tr>
		<td align="center"><b><font color="blue">查询客户</font></b>
		</td>
	</tr>

	<tr height="5%">
		<td align="center"></td>
	</tr>

	<tr>
		<td>
		<table width="100% border=" 0" cellpadding="0"
			cellspacing="0">

			<tr>
				<td align="left">
				客户名称
				</td>
				<td align="left"><input name="cname" type="text" value="${cname}"></td>
			</tr>

			<tr height="20">
				<td align="right" />
				<td align="left"></td>
			</tr>

			<tr>
				<td align="left">
				客户编码
				</td>
				<td align="left"><input name="ccode" value="${ccode}"
					type="text" class="input"></td>
			</tr>

			<tr height="20">
				<td align="right" />
				<td align="left"></td>
			</tr>

			<tr>
				<td align="left"></td>
				<td align="left"><input name="logins" type="button" value="查询(显示10条)" onclick="querys()"></td>
			</tr>
			
			<tr height="20">
                <td align="right" />
                <td align="left"></td>
            </tr>
            
            <c:forEach items="${resultCustomerList}" var="item">
            <tr>
                <td colspan="2"><input name="customerName" type="radio" value="${item.name}">${item.name}(${item.code})</td>
            </tr>
            
            <tr height="20">
                <td align="right" />
                <td align="left"></td>
            </tr>
            </c:forEach>

            <tr>
                <td align="left"></td>
                <td align="left">
                <input name="loginss" type="button" onclick="nexts()" value="(2)下一步">
                <input name="pre" type="button" onclick="pres()" value="上一步"> 
                </td>
            </tr>
            
             
            <tr height="20">
                <td align="right" />
                <td align="left"></td>
            </tr>
            
            <tr>
                <td align="left" colspan='2'><font color=red>${errorInfo}</font></td>
                <c:remove var="errorInfo" scope="session" />
            </tr>
            
            <tr>
                <td align="left" colspan="2">
                <a href="./menu.jsp">首 页</a>&nbsp;&nbsp;<a href="../wap/checkuser.do?method=logout">退 出</a>
                </td>
            </tr>
			
		</table>
		</td>
	</tr>

</table>
</form>
</body>
</html>
