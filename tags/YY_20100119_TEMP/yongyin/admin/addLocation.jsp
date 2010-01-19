<%@ page contentType="text/html;charset=GBK" language="java"
	errorPage="./error.jsp"%>
<%@include file="./common.jsp"%>
<html>
<head>
<p:link title="增加收付款" />
<script language="JavaScript" src="../js/JCheck.js"></script>
<script language="javascript">
function addApplys()
{
	submit('确定增加区域?');
}

</script>

</head>
<body class="body_class">
<form name="addApply" action="../admin/common.do"><input
	type="hidden" name="method" value="addLocation"> <p:navigation
	height="22">
	<td width="550" class="navigation"><span style="cursor: hand"
		onclick="javascript:history.go(-1)">区域管理</span> &gt;&gt; 增加区域</td>
	<td width="85"></td>
</p:navigation> <br>

<p:body width="80%">

	<p:title>
		<td class="caption"><strong>区域信息：</strong></td>
	</p:title>

	<p:line flag="0" />

	<p:subBody width="75%">
		<p:class value="com.china.centet.yongyin.bean.LocationBean" />

		<p:table cells="2">

			<p:pro field="locationName" />

			<p:pro field="locationCode" />

			<p:cell title="管理员" width="15">
				<input type="text" name="name" maxLength="20" oncheck="notNone;">
				<font color="#FF0000">*</font>
			</p:cell>

			<p:cell title="职员" width="15">
				<select name="stafferName" oncheck="notNone;">
					<option value="">--</option>
					<c:forEach items="${staffers}" var="item">
						<option value="${item}">${item}</option>
					</c:forEach>
				</select>
				<font color="#FF0000">*</font>
			</p:cell>

			<p:pro field="description" cell="0" innerString="rows=3 cols=55" />

		</p:table>
	</p:subBody>

	<p:line flag="1" />

	<p:button leftWidth="87%" rightWidth="13%">
		<div align="right"><input type="button" class="button_class"
			style="cursor: pointer" value="&nbsp;&nbsp;确 定&nbsp;&nbsp;"
			onclick="addApplys()"></div>
	</p:button>

	<tr>
		<td colspan='2' align="center"><FONT color="blue">${MESSAGE_INFO}</FONT><FONT
			color="red">${errorInfo}</FONT></td>
	</tr>
</p:body></form>
</body>
</html>

