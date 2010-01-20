<%@ page contentType="text/html;charset=GBK" language="java"
	errorPage="./error.jsp"%>
<%@include file="./common.jsp"%>
<html>
<head>
<p:link title="采购单" />
<script language="JavaScript" src="../js/key.js"></script>
<script language="JavaScript" src="../js/common.js"></script>
<script language="JavaScript" src="../js/public.js"></script>

<script language="javascript">
function ask(id)
{
	document.location.href = '../stock/stock.do?method=preForSockAsk&id=${bean.id}&itemId=' + id;
}

function passTO()
{
	if (window.confirm('确定通过此采购单?'))
	{
		$('pass').value = '1';
		formEntry.submit();
	}
}
</script>

</head>
<body class="body_class">
<form name="formEntry" action="../stock/stock.do" method="post">
<input type="hidden" name="method" value="updateStockStatus">
<input type="hidden" name="id" value="${bean.id}">
<input type="hidden" name="pass" value="1">
<p:navigation height="22">
	<td width="550" class="navigation">采购单明细</td>
	<td width="85"></td>
</p:navigation> <br>

<p:body width="98%">

	<p:title>
		<td class="caption"><strong>采购单信息：</strong></td>
	</p:title>

	<p:line flag="0" />

	<p:subBody width="100%">
		<p:table cells="2">
			<p:cell title="单号">
			${bean.id}
			</p:cell>

			<p:cell title="采购人">
			${bean.userName}
			</p:cell>

			<p:cell title="区域">
			${bean.locationName}
			</p:cell>

			<p:cell title="状态">
			${my:get('stockStatus', bean.status)}
			</p:cell>

			<p:cell title="录入时间">
			${bean.logTime}
			</p:cell>

			<p:cell title="需到货时间">
			${bean.needTime}
			</p:cell>

			<p:cell title="物流">
			${bean.flow}
			</p:cell>

			<p:cell title="总计金额">
			${my:formatNum(bean.total)}
			</p:cell>

			<p:cells celspan="2" title="备注">
			${bean.description}
			</p:cells>
		</p:table>
	</p:subBody>

	<p:tr />

	<p:subBody width="100%">
		<table width="100%" border="0" cellspacing='1' id="tables">
			<tr align="center" class="content0">
				<td width="15%" align="center">采购产品</td>
				<td width="10%" align="center">采购数量</td>
				<td width="10%" align="center">当前数量</td>
				<td width="10%" align="center">是否询价</td>
				<td width="10%" align="center">参考价格</td>
				<td width="10%" align="center">实际价格</td>
				<td width="20%" align="center">供应商</td>
				<td width="5%" align="center">合计金额</td>
				<td width="10%" align="center">询 价</td>
			</tr>

			<c:forEach items="${bean.itemVO}" var="item" varStatus="vs">
				<tr class='${vs.index % 2 == 0 ? "content1" : "content2"}'>
					<td align="center">${item.productName}</td>

					<td align="center">${item.amount}</td>
					
					<td align="center">${item.productNum}</td>

					<td align="center">${item.status == 0 ? "<font color=red>否</font>" : "<font color=blue>是</font>"}</td>

					<td align="center">${my:formatNum(item.prePrice)}</td>

					<td align="center">${my:formatNum(item.price)}</td>

					<td align="center">${item.providerName}</td>

					<td align="center">${my:formatNum(item.total)}</td>

					<td align="center">
					<c:if test="${item.status == 0}">
					<a title="采购询价"
						href="javascript:ask('${item.id}')">
					<img src="../images/change.gif" border="0" height="15" width="15"></a>
					</c:if>
					</td>
				</tr>
			</c:forEach>
		</table>
	</p:subBody>

	<p:line flag="1" />

	<p:button leftWidth="100%" rightWidth="0%">
		<div align="right"><input type="button" class="button_class"
			name="sub1" style="cursor: pointer"
			value="&nbsp;&nbsp;通 过&nbsp;&nbsp;" onclick="passTO()">
			&nbsp;&nbsp;
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

