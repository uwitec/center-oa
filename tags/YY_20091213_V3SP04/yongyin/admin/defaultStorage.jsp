<%@ page contentType="text/html;charset=GBK" language="java"
	errorPage="./error.jsp"%>
<%@include file="./common.jsp"%>
<html>
<head>
<p:link title="增加储位" />
<script language="JavaScript" src="../js/common.js"></script>
<script language="JavaScript" src="../js/JCheck.js"></script>
<script language="JavaScript" src="../js/public.js"></script>
<script language="JavaScript" src="../js/string.js"></script>
<c:set var="dis" value='${param.add != "1" ? "修改" : "增加"}' />
<script language="javascript">

function load()
{
	loadForm();
}


function changes()
{
	fromSelect = $('productName');

	$('productId').value = '';

	for (i = fromSelect.options.length-1; i >= 0; i--)
	{
		var opp = fromSelect.options[i];
		if (opp.selected == true)
		{
			$('productId').value += opp.value + '#';
		}
	}

	if ($('productId').value == '')
	{
		alert('请选择需要转移储位的产品');
		return false;
	}


	submit('确定转移产品到--' + getOptionText($('dirStorage')));
}

</script>

</head>
<body class="body_class" onload="load()">
<form name="addApply" action="../admin/das.do" method="post"><input
	type="hidden" name="id" value="${bean.id}"> <input
	type="hidden" name="productId" value=""> <input type="hidden"
	name="depotpartId"
	value="${my:show(param.depotpartId, bean.depotpartId)}"> <input
	type="hidden" name="method" value='changeDefaultStorage'> <p:navigation
	height="22">
	<td width="550" class="navigation"><span style="cursor: hand"
		onclick="javascript:history.go(-1)">储位管理</span> &gt;&gt; 默认储位</td>
	<td width="85"></td>
</p:navigation> <br>

<p:body width="80%">

	<p:title>
		<td class="caption"><strong>储位信息：</strong></td>
	</p:title>

	<p:line flag="0" />

	<p:subBody width="75%">

		<p:class value="com.china.centet.yongyin.bean.StorageBean" />

		<p:table cells="1">

			<p:pro field="name" innerString="readonly=true" value="${bean.name}" />

			<p:cell title="目的储位">
				<select class="select_class" name="dirStorage" oncheck="notNone;"
					head='目的储位'>
					<option value="">--</option>
					<c:forEach items="${list}" var="item">
						<option value="${item.id}">${item.name}</option>
					</c:forEach>
				</select>
				<font color="#FF0000">*</font>
			</p:cell>

			<p:pro field="productId"
				innerString="multiple=true size=12 style='width: 220px'">
				<c:forEach items="${relations}" var="item">
					<option value="${item.productId}" amount="${item.amount}">${item.productName}</option>
				</c:forEach>
			</p:pro>

		</p:table>
	</p:subBody>

	<p:line flag="1" />

	<p:button leftWidth="87%" rightWidth="13%">
		<div align="right"><input type="button" class="button_class"
			style="cursor: pointer" value="&nbsp;产品转移储位&nbsp;"
			onclick="changes()">&nbsp;&nbsp; <input type="button"
			class="button_class" onclick="javascript:history.go(-1)"
			value="&nbsp;&nbsp;返 回&nbsp;&nbsp;"></div>
	</p:button>

	<p:message />
</p:body></form>
</body>
</html>

