<%@ page contentType="text/html;charset=GBK" language="java"
	errorPage="./error.jsp"%>
<%@include file="./common.jsp"%>
<html>
<head>
<p:link title="热点产品列表" />
<script language="JavaScript" src="../js/common.js"></script>
<script language="JavaScript" src="../js/public.js"></script>
<script language="javascript">
function addBean()
{
	document.location.href = '../admin/addHotProduct.jsp';
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

function del(id)
{
	if (window.confirm('确定删除热点产品?'))
	{
		$('method').value = 'delHotProduct';
		$('id').value = id;
		formEntry.submit();
	}
}

function queryStat()
{
	document.location.href = '../admin/product.do?method=queryStatHotProduct&load=1';
}

</script>

</head>
<body class="body_class" onload="load()"
	onkeypress="tooltip.bingEsc(event)">
<form name="formEntry" action="../admin/product.do"><input
	type="hidden" name="id" value=""> <input type="hidden"
	name="method" value=""> <p:navigation height="22">
	<td width="550" class="navigation">热点产品管理 &gt;&gt; 热点产品列表</td>
	<td width="85"></td>
</p:navigation> <br>

<p:body width="100%">

	<p:title>
		<td class="caption"><strong>热点产品列表：</strong></td>
	</p:title>

	<p:line flag="0" />

	<p:subBody width="100%">
		<table width="100%" align="center" cellspacing='1' class="table0"
			id="result">
			<tr align=center class="content0">
				<td align="center" class="td_class" onclick="tableSort(this, true)"
					width="10%"><strong>排名</strong></td>
				<td align="center" class="td_class" onclick="tableSort(this)"
					width="30%"><strong>产品名称</strong></td>
				<td align="center" class="td_class" onclick="tableSort(this)"
					width="20%"><strong>产品编码</strong></td>
				<td align="center" class="td_class" onclick="tableSort(this)"
					width="30%"><strong>时间</strong></td>
				<td align="center" class="td_class" onclick="tableSort(this)"
					width="10%"><strong>操作</strong></td>
			</tr>

			<c:forEach items="${list}" var="item" varStatus="vs">
				<tr class="${vs.index % 2 == 0 ? 'content1' : 'content2'}">
					<td align="center" onclick="hrefAndSelect(this)">
					${item.orders}</td>
					<td align="center" onclick="hrefAndSelect(this)">${item.productName}</td>
					<td align="center" onclick="hrefAndSelect(this)">${item.productCode}</td>
					<td align="center" onclick="hrefAndSelect(this)">${item.logTime}</td>
					<td align="center" onclick="hrefAndSelect(this)"><c:if
						test="${user.role == 'STOCK'}">
						<a title="删除热点" href="javascript:del('${item.id}')"> <img
							src="../images/del.gif" border="0" height="15" width="15"></a>
					</c:if></td>
				</tr>
			</c:forEach>
		</table>
	</p:subBody>

	<p:line flag="1" />

	<p:button leftWidth="100%" rightWidth="0%">
		<div align="right">
		<c:if test="${user.role == 'STOCK'}">
			<input type="button" class="button_class" name="adds"
				style="cursor: pointer" value="&nbsp;&nbsp;增加热点产品&nbsp;&nbsp;"
				onclick="addBean()">
		</c:if>
		&nbsp;&nbsp;
		<input type="button" class="button_class" name="adds"
			style="cursor: pointer" value="&nbsp;&nbsp;热点产品统计&nbsp;&nbsp;"
			onclick="queryStat()"></div>
	</p:button>

	<p:message />

</p:body></form>
</body>
</html>

