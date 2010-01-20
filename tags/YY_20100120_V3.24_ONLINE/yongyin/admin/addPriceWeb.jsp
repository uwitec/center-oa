<%@ page contentType="text/html;charset=GBK" language="java"
	errorPage="./error.jsp"%>
<%@include file="./common.jsp"%>
<html>
<head>
<p:link title="增加网站" />
<script language="JavaScript" src="../js/common.js"></script>
<script language="JavaScript" src="../js/JCheck.js"></script>
<script language="javascript">
function addBean()
{
	submit('确定' + $$('name'));
}

function load()
{
	loadForm();
}

</script>

</head>
<body class="body_class" onload="load()">
<form name="addApply" action="../admin/price.do">
	<input type="hidden" name="method" value="addPriceWeb">
<p:navigation height="22">
	<td width="550" class="navigation"><span style="cursor: hand"
		onclick="javascript:history.go(-1)">询价管理</span> &gt;&gt; 增加网站</td>
	<td width="85"></td>
</p:navigation> <br>

<p:body width="100%">

	<p:title>
		<td class="caption"><strong>网站信息：</strong></td>
	</p:title>

	<p:line flag="0" />

	<p:subBody width="85%">
		<p:class value="com.china.centet.yongyin.bean.PriceWebBean" />

		<p:table cells="1">
			<p:pro field="name" />

			<p:pro field="url" innerString="size=60" />
		</p:table>
	</p:subBody>

	<p:line flag="1" />

	<p:button leftWidth="87%" rightWidth="13%">
		<div align="right"><input type="button" class="button_class" name="adds"
			style="cursor: pointer" value="&nbsp;&nbsp;确 定&nbsp;&nbsp;"
			onclick="addBean()">&nbsp;&nbsp; <input type="button"
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

