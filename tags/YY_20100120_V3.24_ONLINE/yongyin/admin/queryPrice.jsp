<%@ page contentType="text/html;charset=GBK" language="java"
	errorPage="./error.jsp"%>
<%@include file="./common.jsp"%>
<html>
<head>
<p:link title="网上价格列表" />
<script language="JavaScript" src="../js/common.js"></script>
<script language="JavaScript" src="../js/public.js"></script>
<script language="JavaScript" src="../js/fanye.js"></script>
<script language="javascript">
function querys()
{
	formEntry.submit();
}

function addBean()
{
	document.location.href = '../admin/price.do?method=preForAddPrice';
}

function press()
{
	if (window.common.getEvent().keyCode == 13)
	{
		querys();
	}
}

function resets()
{
	formEntry.reset();

	$('productName').value = '';
	$('productId').value = '';
	setSelectIndex($('status'), 0);
}

function load()
{
	loadForm();
}

function dels(id)
{
	if (window.confirm('确定删除网上价格?'))
	{
		$('method').value = 'delPrice';
		$('id').value = id;
		formEntry.submit();
	}
}

function reject(id)
{
	if (window.confirm('确定驳回此网上价格?'))
	{
		$('method').value = 'rejectPrice';
		$('id').value = id;
		formEntry.submit();
	}
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
<form name="formEntry" action="../admin/price.do"><input
	type="hidden" name="method" value="queryPrice">
	<input
	type="hidden" name="id" value="">
	<input
	type="hidden" name="productId" value="${productId}"><input
	type="hidden" value="1" name="firstLoad"> <p:navigation
	height="22">
	<td width="550" class="navigation">询价管理 &gt;&gt; 网上价格列表</td>
	<td width="85"></td>
</p:navigation> <br>

<p:body width="100%">

	<p:subBody width="100%">
		<table width="100%" align="center" cellspacing='1' class="table0">
			<tr align=center class="content0">
				<td align="center" >开始时间</td>
				<td align="center" width="35%"><p:plugin name="alogTime" value="${alogTime}" /></td>
				<td align="center" >结束时间</td>
				<td align="center" width="35%"><p:plugin name="blogTime" value="${blogTime}" /></td>
			</tr>

			<tr align=center class="content1">
				<td align="center">产品名称</td>
				<td align="center" width="35%"><input type="text" readonly="readonly"
					name="productName" value="${productName}">&nbsp;&nbsp;<input
					type="button" value="&nbsp;...&nbsp;" name="qout"
					class="button_class" onclick="selectProduct()"></td>
				<td align="center">价格状态</td>
				<td align="center" width="35%"><select name="status" readonly="${readonly}"
					class="select_class" values="${status}">
					<option value="">--</option>
					<option value="0">发布</option>
					<option value="1">驳回</option>
				</select></td>
			</tr>

			<tr align=center class="content0">
				<td align="right" colspan="4"><input type="button"
					class="button_class" value="&nbsp;&nbsp;查 询&nbsp;&nbsp;"
					onclick="querys()">&nbsp;&nbsp; <input type="button"
					class="button_class" value="&nbsp;&nbsp;重 置&nbsp;&nbsp;"
					onclick="resets()"></td>
			</tr>
		</table>

	</p:subBody>


	<p:title>
		<td class="caption"><strong>网上价格列表：</strong></td>
	</p:title>

	<p:line flag="0" />

	<p:subBody width="100%">
		<table width="100%" align="center" cellspacing='1' class="table0"
			id="result">
			<tr align=center class="content0">
				<td align="center" class="td_class" onclick="tableSort(this)" width="15%"><strong>产品</strong></td>
				<td align="center" class="td_class" onclick="tableSort(this)" width="15%"><strong>编码</strong></td>
				<td align="center" class="td_class" onclick="tableSort(this, true)" width="10%"><strong>价额</strong></td>
				<td align="center" class="td_class" onclick="tableSort(this)" width="10%"><strong>来源网站</strong></td>
				<td align="center" class="td_class" onclick="tableSort(this, true)" width="10%"><strong>录入时间</strong></td>
				<td align="center" class="td_class" onclick="tableSort(this)" width="10%"><strong>状态</strong></td>
				<td align="center" class="td_class" onclick="tableSort(this)" width="10%"><strong>操作</strong></td>
			</tr>

			<c:forEach items="${list}" var="item" varStatus="vs">
				<tr class="${vs.index % 2 == 0 ? 'content1' : 'content2'}">
					<td align="center" onclick="hrefAndSelect(this)">
					${item.productName}
					</td>
					<td align="center" onclick="hrefAndSelect(this)">${item.productCode}</td>
					<td align="center" onclick="hrefAndSelect(this)">${item.price}</td>
					<td align="center" onclick="hrefAndSelect(this)">${item.priceWebName}</td>
					<td align="center" onclick="hrefAndSelect(this)">${item.logTime}</td>
					<td align="center" onclick="hrefAndSelect(this)">${my:get('priceStatus', item.status)}</td>
					<td align="center" onclick="hrefAndSelect(this)">

					<c:if test="${user.role == 'REPRICE'}">
					<a
						title="驳回网上价格"
						href="javascript:reject('${item.id}')">
					<img src="../images/begin.gif" border="0" height="15" width="15"></a>
					</c:if>

					<c:if test="${item.status == 1}">
					<a
						title="删除网上价格"
						href="javascript:dels('${item.id}')">
					<img src="../images/del.gif" border="0" height="15" width="15"></a>
					</c:if>

					</td>
				</tr>
			</c:forEach>
		</table>

		<p:formTurning form="formEntry" method="queryPrice"></p:formTurning>
	</p:subBody>

	<p:line flag="1" />

	<p:message />

</p:body></form>
</body>
</html>

