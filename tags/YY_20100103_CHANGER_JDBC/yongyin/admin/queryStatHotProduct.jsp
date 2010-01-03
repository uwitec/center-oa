<%@ page contentType="text/html;charset=GBK" language="java"
	errorPage="./error.jsp"%>
<%@include file="./common.jsp"%>
<html>
<head>
<p:link title="热点产品统计" />
<script language="JavaScript" src="../js/common.js"></script>
<script language="JavaScript" src="../js/public.js"></script>
<script language="javascript">
function querys()
{
	formEntry.submit();
}

function press()
{
	if (window.common.getEvent().keyCode == 13)
	{
		querys();
	}
}

function load()
{
	loadForm();
}

</script>

</head>
<body class="body_class" onload="load()"
	onkeypress="tooltip.bingEsc(event)">
<form name="formEntry" action="../admin/product.do">
<input type="hidden" name="method" value="queryStatHotProduct">
<input type="hidden" value="1" name="firstLoad">

<p:navigation height="22">
	<td width="550" class="navigation">热点产品管理 &gt;&gt; 热点产品统计</td>
	<td width="85"></td>
</p:navigation> <br>

<p:body width="100%">

	<p:subBody width="100%">
		<table width="100%" align="center" cellspacing='1' class="table0">
			<tr align=center class="content0">
				<td align="center">开始时间</td>
				<td align="center" width="35%"><p:plugin name="alogTime" type="1"
					value="${alogTime}" /></td>
				<td align="center">结束时间</td>
				<td align="center" width="35%"><p:plugin name="blogTime" type="1"
					value="${blogTime}" /></td>
			</tr>

			<tr align=center class="content1">
				<td align="right" colspan="4"><input type="button"
					class="button_class" value="&nbsp;&nbsp;查 询&nbsp;&nbsp;"
					onclick="querys()">&nbsp;&nbsp; <input type="button"
					class="button_class" value="&nbsp;&nbsp;重 置&nbsp;&nbsp;"
					onclick="resets()"></td>
			</tr>
		</table>

	</p:subBody>

	<p:title>
		<td class="caption"><strong>热点产品统计：</strong></td>
	</p:title>

	<p:line flag="0" />

	<p:subBody width="100%">
		<table width="100%" align="center" cellspacing='1' class="table0"
			id="result">
			<tr align=center class="content0">
				<td align="center" class="td_class" onclick="tableSort(this)"
					width="30%"><strong>产品名称</strong></td>
				<td align="center" class="td_class" onclick="tableSort(this)"
					width="20%"><strong>产品编码</strong></td>
				<td align="center" class="td_class" onclick="tableSort(this, true)"
					width="30%"><strong>统计数量</strong></td>
			</tr>

			<c:forEach items="${list}" var="item" varStatus="vs">
				<tr class="${vs.index % 2 == 0 ? 'content1' : 'content2'}">
					<td align="center" onclick="hrefAndSelect(this)">${item.productName}</td>
					<td align="center" onclick="hrefAndSelect(this)">${item.productCode}</td>
					<td align="center" onclick="hrefAndSelect(this)">${item.amount}</td>
				</tr>
			</c:forEach>
		</table>

		<p:formTurning form="formEntry" method="queryStatHotProduct"></p:formTurning>
	</p:subBody>

	<p:line flag="1" />

	<p:message />

</p:body></form>
</body>
</html>

