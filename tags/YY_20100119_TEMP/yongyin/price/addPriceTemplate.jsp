<%@ page contentType="text/html;charset=GBK" language="java"
	errorPage="./error.jsp"%>
<%@include file="./common.jsp"%>
<html>
<head>
<p:link title="增加网站价格模板" />
<script language="JavaScript" src="../js/common.js"></script>
<script language="JavaScript" src="../js/public.js"></script>
<script language="JavaScript" src="../js/JCheck.js"></script>
<script language="javascript">
function addBean()
{
	submit('确定增加网上价格模板?');
}

function load()
{
	loadForm();
}

function selectProduct()
{
	//单选
	window.common.modal("../admin/product.do?method=rptInQueryProduct2&firstLoad=1&type=2");
}

function getProduct(oo)
{
	var obj = oo[0];

	$('productId').value = obj.value;
	$('productName').value = obj.productName;
}

</script>

</head>
<body class="body_class" onload="load()">
<form name="addApply" action="../admin/price.do"><input
	type="hidden" name="method" value="addPriceTemplate"> <input
	type="hidden" name="productId" value="" /> <input type="hidden"
	name="oprMode" value="0" /> <p:navigation height="22">
	<td width="550" class="navigation"><span style="cursor: hand"
		onclick="javascript:history.go(-1)">网站价格模板管理</span> &gt;&gt; 增加网站价格模板</td>
	<td width="85"></td>
</p:navigation> <br>

<p:body width="100%">

	<p:title>
		<td class="caption"><strong>网站价格模板信息：</strong></td>
	</p:title>

	<p:line flag="0" />

	<p:subBody width="100%">
		<p:class value="com.china.centet.yongyin.bean.PriceTemplateBean" />

		<p:table cells="1">
			<p:pro field="productId">&nbsp;&nbsp;<input type="button"
					value="&nbsp;...&nbsp;" name="qout" class="button_class"
					onclick="selectProduct()">
			</p:pro>

			<p:cell title="网站">
				<c:forEach items="${list}" var="item">
					<input type="checkbox" name="priceWebIds" value="${item.id}">${item.name}<br>
				</c:forEach>
			</p:cell>

		</p:table>
	</p:subBody>

	<p:line flag="1" />

	<p:button leftWidth="100%" rightWidth="0%">
		<div align="right"><input type="button" class="button_class"
			name="adds" style="cursor: pointer"
			value="&nbsp;&nbsp;确 定&nbsp;&nbsp;" onclick="addBean()">&nbsp;&nbsp;
		<input type="button" class="button_class"
			onclick="javascript:history.go(-1)"
			value="&nbsp;&nbsp;返 回&nbsp;&nbsp;"></div>
	</p:button>

	<tr>
		<td colspan='2' align="center"><FONT color="blue">${MESSAGE_INFO}</FONT><FONT
			color="red">${errorInfo}</FONT></td>
	</tr>
</p:body></form>
</body>
</html>

