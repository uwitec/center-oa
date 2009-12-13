<%@ page contentType="text/html;charset=GBK" language="java"
	errorPage="./error.jsp"%>
<%@include file="./common.jsp"%>
<html>
<head>
<p:link title="供应商采购明细计" />
<script language="JavaScript" src="../js/common.js"></script>
<script language="JavaScript" src="../js/public.js"></script>
<script language="JavaScript" src="../js/key.js"></script>
<script language="javascript">
function load()
{
}

</script>
</head>

<body class="body_class" onload="load()">
<form name="formEntry" action="../admin/stat.do"><input
	type="hidden" name="method" value="queryStockItemForStacProvider">
<input type="hidden" value="${alogTime}" name="alogTime">
<input type="hidden" value="${blogTime}" name="blogTime">
<input type="hidden" value="${providerId}" name="providerId">
<p:navigation
	height="22">
	<td width="550" class="navigation">统计管理 &gt;&gt; 供应商采购明细</td>
	<td width="85"></td>
</p:navigation> <br>

<p:body width="100%">

	<p:title>
		<td class="caption"><strong>供应商【${customer.name}】采购明细[时间:${alogTime}至${blogTime}]：</strong></td>
	</p:title>

	<p:line flag="0" />

	<p:subBody width="100%">
		<table width="100%" align="center" cellspacing='1' class="table0"
			id="result">
			<tr align=center class="content0">
				<td align="center" class="td_class" onclick="tableSort(this)"
					width="30%"><strong>采购产品</strong></td>
				<td align="center" class="td_class" onclick="tableSort(this, true)"
					width="5%"><strong>数量</strong></td>
				<td align="center" class="td_class" onclick="tableSort(this, true)"
					width="5%"><strong>单价</strong></td>
				<td align="center" class="td_class" onclick="tableSort(this, true)"
					width="10%"><strong>金额</strong></td>
				<td align="center" class="td_class" onclick="tableSort(this)"
					width="25%"><strong>时间</strong></td>
			</tr>

			<c:forEach items="${list}" var="item" varStatus="vs">
				<tr class="${vs.index % 2 == 0 ? 'content1' : 'content2'}">
					<td align="center" onclick="hrefAndSelect(this)">${item.productName}</td>
					<td align="center" onclick="hrefAndSelect(this)">${item.amount}</td>
					<td align="center" onclick="hrefAndSelect(this)">${my:formatNum(item.price)}</td>
					<td align="center" onclick="hrefAndSelect(this)">${my:formatNum(item.total)}</td>
					<td align="center" onclick="hrefAndSelect(this)">${item.logTime}</td>
					</td>
				</tr>
			</c:forEach>
		</table>

		<p:formTurning form="formEntry" method="queryStockItemForStacProvider"></p:formTurning>
	</p:subBody>

	<p:line flag="1" />

	<p:button leftWidth="100%" rightWidth="0%">
		<div align="right"><input type="button" class="button_class"
			onclick="javascript:history.go(-1)"
			value="&nbsp;&nbsp;返 回&nbsp;&nbsp;"></div>
	</p:button>

	<p:message />

</p:body></form>
</body>
</html>

