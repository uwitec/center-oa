<%@ page contentType="text/html;charset=GBK" language="java"
	errorPage="./error.jsp"%>
<%@include file="./common.jsp"%>
<html>
<head>
<p:link title="增加热点" />
<script language="JavaScript" src="../js/common.js"></script>
<script language="JavaScript" src="../js/JCheck.js"></script>
<script language="JavaScript" src="../js/public.js"></script>
<script language="javascript">

function addBean(opr)
{
	submit('确定增加此热点产品?', null, null);
}


function load()
{
	loadForm();
}

function selectProduct()
{
	window.common.modal("../admin/product.do?method=rptInQueryProduct2&firstLoad=1");
}

function getProduct(oo)
{
	oo = oo[0];

	$("productName").value = oo.productName;
	$("productId").value = oo.value;
}
</script>

</head>
<body class="body_class" onload="load()">
<form name="addApply" action="../admin/product.do" method="post"><input
	type="hidden" name="method" value="addHotProduct"> <input
	type="hidden" name="productId" value=""> <p:navigation
	height="22">
	<td width="550" class="navigation"><span style="cursor: hand"
		onclick="javascript:history.go(-1)">热点产品管理</span> &gt;&gt; 增加热点产品</td>
	<td width="85"></td>
</p:navigation> <br>

<p:body width="100%">

	<p:title>
		<td class="caption"><strong>热点产品信息：</strong></td>
	</p:title>

	<p:line flag="0" />

	<p:subBody width="100%">
		<p:class value="com.china.centet.yongyin.bean.HotProductBean" />

		<p:table cells="1">
			<p:pro field="productId" innerString="size=60">&nbsp;
			<input type="button" value="&nbsp;选 择&nbsp;" name="qout_0"
					class="button_class" onclick="selectProduct()">
			</p:pro>

			<p:pro field="orders" />

		</p:table>
	</p:subBody>

	<p:line flag="1" />

	<p:button leftWidth="100%" rightWidth="0%">
		<div align="right"><input type="button" class="button_class"
			name="adds" style="cursor: pointer"
			value="&nbsp;&nbsp;增 加&nbsp;&nbsp;" onclick="addBean()">&nbsp;&nbsp;
		<input type="button" class="button_class"
			onclick="javascript:history.go(-1)"
			value="&nbsp;&nbsp;返 回&nbsp;&nbsp;"></div>
	</p:button>

	<p:message />
</p:body></form>
</body>
</html>

