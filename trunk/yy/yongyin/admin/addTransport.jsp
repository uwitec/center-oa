<%@ page contentType="text/html;charset=GBK" language="java"
	errorPage="./error.jsp"%>
<%@include file="./common.jsp"%>
<html>
<head>
<p:link title="增加运输" />
<script language="JavaScript" src="../js/common.js"></script>
<script language="JavaScript" src="../js/JCheck.js"></script>

<c:set var="dis" value="增加" />

<script language="javascript">
function addApplys()
{
	submit('确定${dis}运输[' + $$('name') + ']');
}

function load()
{
	loadForm();
	change();
}

function change()
{
	if ($$('type') == 0)
	{
		$d('parent', true);
	}
	else
	{
		$d('parent', false);
	}
}
</script>

</head>
<body class="body_class" onload="load()">
<form name="addApply" action="../admin/transport.do"><input
	type="hidden" name="method" value="addTransport"> <p:navigation
	height="22">
	<td width="550" class="navigation"><span style="cursor: hand"
		onclick="javascript:history.go(-1)">运输管理</span> &gt;&gt; ${dis}运输</td>
	<td width="85"></td>
</p:navigation> <br>

<p:body width="80%">

	<p:title>
		<td class="caption"><strong>运输信息：</strong></td>
	</p:title>

	<p:line flag="0" />

	<p:subBody width="75%">
		<p:class value="com.china.centet.yongyin.bean.TransportBean" />

		<p:table cells="1">
			<p:cell title="类型">
				<select name="type" onchange="change()" oncheck="notNone;">
					<option value="1">运输方式</option>
					<option value="0">运输分类</option>
				</select>
				<font color="#FF0000">*</font>
			</p:cell>

			<p:pro field="name" />

			<p:pro field="parent">
				<option value="">--</option>
				<c:forEach items="${transportList}" var="item">
					<option value="${item.id}">${item.name}</option>
				</c:forEach>
			</p:pro>

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

