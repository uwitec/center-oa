<%@ page contentType="text/html;charset=GBK" language="java"
	errorPage="./error.jsp"%>
<%@include file="./common.jsp"%>
<html>
<head>
<p:link title="增加货物异常处理单" />
<script language="JavaScript" src="../js/common.js"></script>
<script language="JavaScript" src="../js/public.js"></script>
<script language="JavaScript" src="../js/JCheck.js"></script>

<script language="javascript">
function addApplys()
{
	submit('确定提交货物异常处理单到总经理处?');
}

function openProduct()
{
	window.common.modal('./product.do?method=rptInQueryProduct&firstLoad=1&locationInner='+ ${currentLocationId});
}

function getProduct(ox, selectLocationId)
{
	if (ox.length <= 0)
	{
		return;
	}
	
	$('productName').value = ox[0].productName;
}

function load()
{
	loadForm();
}

</script>

</head>
<body class="body_class" onload="load()">
<form name="addApply"
	action="../admin/proException.do?method=addProctException"
	enctype="multipart/form-data" method="post"><input type="hidden"
	name="id" value="${bean.id}"> <p:navigation height="22">
	<td width="550" class="navigation"><span style="cursor: hand"
		onclick="javascript:history.go(-1)">货物异常处理管理</span> &gt;&gt; 增加货物异常处理</td>
	<td width="85"></td>
</p:navigation> <br>

<p:body width="80%">

	<p:title>
		<td class="caption"><strong>货物异常处理信息：</strong></td>
	</p:title>

	<p:line flag="0" />

	<p:subBody width="75%">
		<p:class value="com.china.centet.yongyin.bean.ProctExceptionBean" />

		<p:table cells="1">

			<p:pro field="productName" value="${bean.productName}"
				innerString="readonly">
			&nbsp;&nbsp;<input type="button" class="button_class"
					value="&nbsp;&nbsp;选择产品&nbsp;&nbsp;" onclick="openProduct()" />
			</p:pro>

			<p:pro field="amount" value="${bean.amount}" />

			<p:pro field="applyer" value="${bean.applyer}" />

			<p:pro field="description" value="${bean.description}"
				innerString="cols=55 rows=3" />

			<p:cell title="附件">
				<input type="file" name="attachment" class="button_class">
			</p:cell>

		</p:table>
	</p:subBody>

	<p:line flag="1" />

	<p:button leftWidth="87%" rightWidth="13%">
		<div align="right"><input type="button" class="button_class"
			style="cursor: pointer" value="&nbsp;&nbsp;提 交&nbsp;&nbsp;"
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

