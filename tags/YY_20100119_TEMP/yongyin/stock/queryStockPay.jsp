<%@ page contentType="text/html;charset=GBK" language="java"
	errorPage="./error.jsp"%>
<%@include file="./common.jsp"%>
<html>
<head>
<p:link title="付款单汇总列表" />
<script language="JavaScript" src="../js/prototype.js"></script>
<script language="JavaScript" src="../js/common.js"></script>
<script language="JavaScript" src="../js/public.js"></script>
<script language="JavaScript" src="../js/title_div.js"></script>
<script language="javascript">
function querys()
{
	$('method').value = 'queryStockPay';
	formEntry.submit();
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

	$('providerName').value = '';
	$('providerCode').value = '';
	$('id').value = '';
}

function load()
{
	loadForm();

	$f('providerName');
}


function collectToPay()
{
    
}
</script>

</head>
<body class="body_class" onload="load()"
	onkeypress="tooltip.bingEsc(event)">
<form name="formEntry" action="../stock/stock.do"><input
	type="hidden" name="method" value="queryStockPay"> <input
	type="hidden" value="1" name="firstLoad"> <p:navigation height="22">
	<td width="550" class="navigation">采购单管理 &gt;&gt; 付款单汇总列表</td>
	<td width="85"></td>
</p:navigation> <br>

<p:body width="98%">

	<p:subBody width="100%">
		<table width="100%" align="center" cellspacing='1' class="table0">
			<tr align=center class="content0">
				<td align="center">开始时间</td>
				<td align="center" width="35%"><p:plugin name="alogTime"
					value="${alogTime}" /></td>
				<td align="center">结束时间</td>
				<td align="center" width="35%"><p:plugin name="blogTime"
					value="${blogTime}" /></td>
			</tr>

			<tr align=center class="content1">
				<td align="center">供应商名称</td>
                <td align="center" width="35%"><input type="text"
                    onkeydown="press()" name="providerName" value="${providerName}"></td>
				<td align="center">供应商编码</td>
                <td align="center" width="35%"><input type="text"
                    onkeydown="press()" name="providerCode" value="${providerCode}"></td>
			</tr>
			
			<tr align=center class="content1">
                <td align="center">单据标识</td>
                <td align="center" width="35%"><input type="text"
                    onkeydown="press()" name="id" value="${id}"></td>
                <td align="center"></td>
                <td align="center" width="35%"></td>
            </tr>
			
			<tr align=center class="content1">
                <td align="right" colspan="4"><input type="button" id="b_query"
                    class="button_class" value="&nbsp;&nbsp;查 询&nbsp;&nbsp;"
                    onclick="querys()">&nbsp;&nbsp; <input type="button"
                    class="button_class" value="&nbsp;&nbsp;重 置&nbsp;&nbsp;" id="b_reset"
                    onclick="resets()"></td>
            </tr>
		</table>

	</p:subBody>


	<p:title>
		<td class="caption"><strong>付款单汇总列表：</strong></td>
	</p:title>

	<p:line flag="0" />

	<p:subBody width="100%">
		<table width="100%" align="center" cellspacing='1' class="table0"
			id="result">
			<tr align=center class="content0">
				<td align="center" class="td_class" onclick="tableSort(this)"
					width="10%"><strong>时间</strong></td>
				<td align="center" class="td_class" onclick="tableSort(this)"
                    width="10%"><strong>标识</strong></td>
				<td align="center" class="td_class" onclick="tableSort(this)"
					width="15%"><strong>供应商</strong></td>
				<td align="center" class="td_class" onclick="tableSort(this)"
                    width="15%"><strong>汇总人</strong></td>
				<td align="center" class="td_class" onclick="tableSort(this)"
					width="10%"><strong>付款金额</strong></td>
			</tr>

			<c:forEach items="${list}" var="item" varStatus="vs">
				<tr class="${vs.index % 2 == 0 ? 'content1' : 'content2'}">
					<td align="center" onclick="hrefAndSelect(this)">${item.logTime}</td>
					<td align="center" onclick="hrefAndSelect(this)">${item.id}</td>
					<td align="center" onclick="hrefAndSelect(this)">${item.providerName}(${item.providerCode})</td>
					<td align="center" onclick="hrefAndSelect(this)">${item.stafferName}</td>
					<td align="center" onclick="hrefAndSelect(this)">${my:formatNum(item.total)}</td>
				</tr>
			</c:forEach>
		</table>

		<p:formTurning form="formEntry" method="queryStockPay"></p:formTurning>
	</p:subBody>

	<p:line flag="1" />

	<p:button leftWidth="100%" rightWidth="0%">
		<div align="right">
		<c:if test="${user.role == 'STOCKMANAGER'}">
		<input type="button" class="button_class"
			id="b_export" style="cursor: pointer"
			value="&nbsp;&nbsp;汇总付款&nbsp;&nbsp;" onclick="collectToPay()">
		</c:if>
		</div>
	</p:button>

	<p:message />

</p:body></form>
</body>
</html>

