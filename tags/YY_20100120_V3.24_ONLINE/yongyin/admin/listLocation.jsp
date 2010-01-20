<%@ page contentType="text/html;charset=GBK" language="java"
	errorPage="./error.jsp"%>
<%@include file="./common.jsp"%>
<html>
<head>
<p:link title="区域列表" />
<script language="javascript">
function addApplys()
{
	locationList.submit();
}

</script>

</head>
<body class="body_class">
<form name="locationList" action="../admin/common.do"><input
	type="hidden" name="method" value="preForaddLocation"> <p:navigation
	height="22">
	<td width="550" class="navigation">区域管理</td>
	<td width="85"></td>
</p:navigation> <br>

<p:body width="85%">

	<p:title>
		<td class="caption"><strong>区域列表：</strong></td>
	</p:title>

	<p:line flag="0" />

	<p:subBody width="85%">
		<table width="100%" align="center" cellspacing='1' class="table0">
			<tr align=center class="content0">
				<td align="center" class="td_class" ><strong>区域名称</strong></td>
				<td align="center" class="td_class"><strong>区域标识</strong></td>
			</tr>
			<c:forEach items="${requestScope.locationList}" var="item"
				varStatus="s">
				<tr class="${s.index % 2 == 0 ? 'content1' : 'content2'}">
					
					<td align="center"><c:out value="${item.locationName}" /></td>
					<td align="center"><c:out value="${item.locationCode}" /></td>
				</tr>
			</c:forEach>
		</table>

	</p:subBody>

	<p:line flag="1" />

	<p:button leftWidth="87%" rightWidth="13%">
		<div align="right"><input type="button" class="button_class"
			style="cursor: pointer" value="&nbsp;&nbsp;增加区域&nbsp;&nbsp;"
			onclick="addApplys()"></div>
	</p:button>

	<tr>
		<td colspan='2' align="center"><FONT color="blue">${MESSAGE_INFO}</FONT><FONT
			color="red">${errorInfo}</FONT></td>
	</tr>
</p:body></form>
</body>
</html>

