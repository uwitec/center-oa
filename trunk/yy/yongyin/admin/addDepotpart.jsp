<%@ page contentType="text/html;charset=GBK" language="java"
	errorPage="./error.jsp"%>
<%@include file="./common.jsp"%>
<html>
<head>
<p:link title="增加仓区" />
<script language="JavaScript" src="../js/common.js"></script>
<script language="JavaScript" src="../js/JCheck.js"></script>
<c:if test='${param.add != "1"}'>
	<c:set var="dis" value="修改" />
	<c:set var="red" value="readonly=true" />
</c:if>
<c:if test='${param.add == "1"}'>
	<c:set var="dis" value="增加" />
	<c:set var="red" value="" />
</c:if>
<script language="javascript">
function addApplys()
{
	submit('确定${dis}仓区[' + $$('name') + ']');
}

function load()
{
	loadForm();
	$f($('name'));
}

</script>

</head>
<body class="body_class" onload="load()">
<form name="addApply" action="../admin/das.do"><input
	type="hidden" name="id" value="${depotpart.id}"> <c:if
	test='${param.add != "1"}'>
	<input type="hidden" name="method" value="updateDepotpart">
</c:if> <c:if test='${param.add == "1"}'>
	<input type="hidden" name="method" value="addDepotpart">
</c:if> <p:navigation height="22">
	<td width="550" class="navigation"><span style="cursor: hand"
		onclick="javascript:history.go(-1)">仓区管理</span> &gt;&gt; ${dis}仓区</td>
	<td width="85"></td>
</p:navigation> <br>

<p:body width="80%">

	<p:title>
		<td class="caption"><strong>仓区信息：</strong></td>
	</p:title>

	<p:line flag="0" />

	<p:subBody width="75%">
	
		<p:class value="com.china.centet.yongyin.bean.DepotpartBean" />

		<p:table cells="1">

			<p:pro field="name" value="${depotpart.name}" />
			
			<p:pro field="type" value="${depotpart.type}" innerString="${red}">
				<option value="1">良品仓</option>
				<option value="0">次品仓</option>
				<option value="2">报废仓</option>
			</p:pro>

			<p:pro field="description" value="${depotpart.description}" />

		</p:table>
	</p:subBody>

	<p:line flag="1" />

	<p:button leftWidth="87%" rightWidth="13%">
		<div align="right"><input type="button" class="button_class"
			style="cursor: pointer" value="&nbsp;&nbsp;确 定&nbsp;&nbsp;"
			onclick="addApplys()">&nbsp;&nbsp; <input type="button"
			class="button_class" onclick="javascript:history.go(-1)"
			value="&nbsp;&nbsp;返 回&nbsp;&nbsp;"></div>
	</p:button>

	<tr>
		<td colspan='2' align="center"><FONT color="blue">${MESSAGE_INFO}</FONT><FONT
			color="red">${errorInfo}</FONT></td>
	</tr>
</p:body></form>
</body>
</html>

