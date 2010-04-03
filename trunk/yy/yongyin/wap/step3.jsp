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
    formd.method.value = 'total';
    
    formd.submit();
}

function querys()
{
    formd.method.value = 'queryProduct';
    
    formd.submit();
}

function pres()
{
    document.location.href = './step2.jsp';
}
</script>
</HEAD>

<BODY bgcolor="#FFFFFF" leftmargin="0" topmargin="0" marginwidth="0"
	marginheight="0">
<FORM name=formd action="../wap/out.do" method=post><input
	type="hidden" name="method" value="queryProduct" />
<table align="center">
	<tr>
		<td align="center"><b><font color="blue">查询产品</font></b></td>
	</tr>

	<tr height="5%">
		<td align="center"></td>
	</tr>

	<tr>
		<td>
		<table width="100% border="0" cellpadding="0"
			cellspacing="0">
			<tr>
				<td align="left">
				产品名称
				</td>
				<td align="left"><input name="pname" type="text" value="${pname}"></td>
			</tr>

			<tr height="20">
				<td align="right" />
				<td align="left"></td>
			</tr>

			<tr>
				<td align="left">
				产品编码
				</td>
				<td align="left"><input name="pcode" value="${pcode}"
					type="text" class="input"></td>
			</tr>

			<tr height="20">
				<td align="right" />
				<td align="left"></td>
			</tr>

			<tr>
				<td align="left"></td>
				<td align="left"><input name="query_b" type="button" value="查询(显示10条)" onclick="querys()">
				</td>
			</tr>
			
			<tr height="20">
                <td align="right" />
                <td align="left"></td>
            </tr>
            
            <c:forEach items="${resultProductList}" var="item">
            <tr>
                <td colspan="2"><input name="productNames" type="checkbox" value="${item.name}">${item.name}(${item.code})(数量:${item.num})</td>
            </tr>
            
            <tr height="20">
                <td align="right" />
                <td align="left"></td>
            </tr>
            </c:forEach>
            
            <tr>
                <td align="left" colspan="2">已经选择销售的产品:</td>
            </tr>
            
             
            <tr height="20">
                <td align="right" />
                <td align="left"></td>
            </tr>
            
            <c:forEach items="${gwapmap.productSet}" var="item">
            <tr>
                <td colspan="2"><input name="hasSelectProductNames" type="checkbox" checked="checked" value="${item}">${item}</td>
            </tr>
            
            <tr height="20">
                <td align="right" />
                <td align="left"></td>
            </tr>
            </c:forEach>
            
           <tr>
                <td align="left"></td>
                <td align="left">
                <input name="add1" type="button" onclick="querys()" value="增加销售列表">
                <input name="next_b" type="button" onclick="nexts()" value="(3)下一步">
                <input name="pre_b" type="button" onclick="pres()" value="上一步"> 
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
