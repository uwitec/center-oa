<%@ page contentType="text/html;charset=GBK" language="java"
	errorPage="../common/error.jsp"%>
<%@include file="../common/common.jsp"%>
<html>
<head>
<p:link title="增加订货" />
<script language="JavaScript" src="../js/common.js"></script>
<script language="JavaScript" src="../js/JCheck.js"></script>
<script language="JavaScript" src="../js/public.js"></script>
<script language="javascript">
function addBean()
{
	submit('确定修改订货?');
}

function selectCus()
{
    window.common.modal('../admin/pop.do?method=rptQueryProdcut&load=1');
}

function getProduct(obj)
{
    $O('productId').value = obj.value;
    $O('productName').value = obj.pname;
}

</script>

</head>
<body class="body_class">
<form name="formEntry" action="../product/product.do"><input
	type="hidden" name="method" value="updateOutOrder">
<input type="hidden" name="id" value="${bean.id}"> 
<input type="hidden" name="productId" value="${bean.productId}"> 
<input type="hidden" name="logTime" value="${bean.logTime}"> 
<p:navigation
	height="22">
	<td width="550" class="navigation"><span style="cursor: pointer;"
		onclick="javascript:history.go(-1)">定制产品管理</span> &gt;&gt; 修改订货</td>
	<td width="85"></td>
</p:navigation> <br>

<p:body width="100%">

	<p:title>
		<td class="caption"><strong>订货信息：</strong></td>
	</p:title>

	<p:line flag="0" />

	<p:subBody width="98%">
		<p:class value="com.china.center.oa.product.bean.OutOrderBean" opr="1"/>

		<p:table cells="1">
		    
			<p:cell title="产品">${bean.productName}</p:cell>

			<p:pro field="orderAmount" innerString="oncheck=isMathNumber"/>
			
			<p:pro field="endTime"/>
			
			<p:pro field="description" cell="0" innerString="rows=3 cols=55" />
			
		</p:table>
	</p:subBody>

	<p:line flag="1" />

	<p:button leftWidth="100%" rightWidth="0%">
		<div align="right"><input type="button" class="button_class" id="ok_b"
		 value="&nbsp;&nbsp;确 定&nbsp;&nbsp;"
			onclick="addBean()"></div>
	</p:button>
</p:body></form>
</body>
</html>

