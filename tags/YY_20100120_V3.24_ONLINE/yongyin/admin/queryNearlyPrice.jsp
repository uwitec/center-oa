<%@ page contentType="text/html;charset=GBK" language="java"
	errorPage="./error.jsp"%>
<%@include file="./common.jsp"%>
<html>
<head>
<p:link title="最近价格列表" />
<script language="JavaScript" src="../js/common.js"></script>
<script language="JavaScript" src="../js/public.js"></script>
<script language="javascript">

function backs()
{
	window.close();
}
</script>

</head>
<body class="body_class">
<form><input type="hidden" name="productId" value="${productId}">
<p:navigation height="22">
	<td width="550" class="navigation">询价管理 &gt;&gt; 最近价格列表</td>
	<td width="85"></td>
</p:navigation> <p:body width="100%">
	<p:title>
		<td class="caption"><strong>产品图片：</strong></td>
	</p:title>

	<p:subBody width="100%">
		<table width="100%" align="center" cellspacing='1'>
			<tr align="left">
				<td align="left" class="content2"><img
					src="${rootUrl}${product.picPath}?${random}"></td>
			</tr>
		</table>
	</p:subBody>

	<p:title>
		<td class="caption"><strong>最近网上价格：</strong></td>
	</p:title>

	<p:subBody width="100%">
		<table width="100%" align="center" cellspacing='1' class="table0">
			<tr align=center class="content0">
				<td align="center" class="td_class" onclick="tableSort(this, true)"
					width="10%"><strong>时间</strong></td>
				<td align="center" class="td_class" onclick="tableSort(this)"
					width="15%"><strong>产品</strong></td>
				<td align="center" class="td_class" onclick="tableSort(this, true)"
					width="10%"><strong>价额</strong></td>
				<td align="center" class="td_class" onclick="tableSort(this)"
					width="10%"><strong>来源网站</strong></td>
			</tr>

			<c:forEach items="${listWebPrice}" var="item" varStatus="vs">
				<tr class="${vs.index % 2 == 0 ? 'content1' : 'content2'}">
					<td align="center" onclick="hrefAndSelect(this)">${item.logTime}</td>
					<td align="center" onclick="hrefAndSelect(this)">
					${item.productName}【${item.productCode}】</td>
					<td align="center" onclick="hrefAndSelect(this)">${item.price}</td>
					<td align="center" onclick="hrefAndSelect(this)">${item.priceWebName}</td>
				</tr>
			</c:forEach>
		</table>
	</p:subBody>

	<p:title>
		<td class="caption"><strong>最近询价：</strong></td>
	</p:title>

	<p:subBody width="100%">
		<table width="100%" align="center" cellspacing='1' class="table0">
			<tr align=center class="content0">
				<td align="center" class="td_class" onclick="tableSort(this, true)"
					width="10%"><strong>时间</strong></td>
				<td align="center" class="td_class" onclick="tableSort(this)"
					width="15%"><strong>产品</strong></td>
				<td align="center" class="td_class" onclick="tableSort(this, true)"
					width="10%"><strong>价额</strong></td>
				<td align="center" class="td_class" onclick="tableSort(this)"
					width="10%"><strong>询价人</strong></td>
			</tr>

			<c:forEach items="${listAskPrice}" var="item" varStatus="vs">
				<tr class="${vs.index % 2 == 0 ? 'content1' : 'content2'}">
					<td align="center" onclick="hrefAndSelect(this)">${item.logTime}</td>
					<td align="center" onclick="hrefAndSelect(this)">
					${item.productName}【${item.productCode}】</td>
					<td align="center" onclick="hrefAndSelect(this)">${item.price}</td>
					<td align="center" onclick="hrefAndSelect(this)">${item.userName}</td>
				</tr>
			</c:forEach>
		</table>
	</p:subBody>

	<p:title>
		<td class="caption"><strong>最近采购价格：</strong></td>
	</p:title>

	<p:subBody width="100%">
		<table width="100%" align="center" cellspacing='1' class="table0">
			<tr align=center class="content0">
				<td align="center" class="td_class" onclick="tableSort(this, true)"
					width="10%"><strong>时间</strong></td>
				<td align="center" class="td_class" onclick="tableSort(this)"
					width="15%"><strong>产品</strong></td>
				<td align="center" class="td_class" onclick="tableSort(this, true)"
					width="10%"><strong>价额</strong></td>
				<td align="center" class="td_class" onclick="tableSort(this)"
					width="10%"><strong>采购单号</strong></td>
			</tr>

			<c:forEach items="${listStockPrice}" var="item" varStatus="vs">
				<tr class="${vs.index % 2 == 0 ? 'content1' : 'content2'}">
					<td align="center" onclick="hrefAndSelect(this)">${item.logTime}</td>
					<td align="center" onclick="hrefAndSelect(this)">
					${item.productName}【${item.productCode}】</td>
					<td align="center" onclick="hrefAndSelect(this)">${item.price}</td>
					<td align="center" onclick="hrefAndSelect(this)">${item.stockId}</td>
				</tr>
			</c:forEach>
		</table>
	</p:subBody>

	<p:line flag="1" />

	<p:button leftWidth="100%" rightWidth="0%">
		<div align="right"><input type="button"
			class="button_class" name="adds3" style="cursor: pointer"
			value="&nbsp;&nbsp;关闭&nbsp;&nbsp;" onclick="backs()"></div>
	</p:button>

	<p:message />

</p:body></form>
</body>
</html>

