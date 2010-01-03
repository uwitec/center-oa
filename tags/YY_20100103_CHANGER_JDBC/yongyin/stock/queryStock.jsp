<%@ page contentType="text/html;charset=GBK" language="java"
	errorPage="./error.jsp"%>
<%@include file="./common.jsp"%>
<html>
<head>
<p:link title="采购单列表" />
<script language="JavaScript" src="../js/prototype.js"></script>
<script language="JavaScript" src="../js/common.js"></script>
<script language="JavaScript" src="../js/public.js"></script>
<script language="JavaScript" src="../js/title_div.js"></script>
<script language="javascript">
function querys()
{
	$('method').value = 'queryStock';
	formEntry.submit();
}

function addBean()
{
	document.location.href = '../stock/stock.do?method=preForAddStock';
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

	$('ids').value = '';
	setSelectIndex($('status'), 0);
	setSelectIndex($('pay'), 0);
	setSelectIndex($('locationId'), 0);
}

function load()
{
	loadForm();

	$f('ids');

	$('update').value = '';
}

function del(id)
{
	if (window.confirm('确定删除采购单?'))
	{
		$('method').value = 'delStock';
		$('id').value = id;
		formEntry.submit();
	}
}

function reject(id)
{
	if (window.confirm('确定驳回此采购单?'))
	{
		var sss = window.prompt('请输入驳回销售单原因：', '');

		$('reason').value = sss;

		if (!(sss == null || sss == ''))
		{
			$('method').value = 'updateStockStatus';
			$('id').value = id;
			$('reject').value = '1';
			$('pass').value = '';
			formEntry.submit();
		}
		else
		{
			alert('请输入驳回原因');
		}
	}
}

var jmap = {};
<c:forEach items="${map}" var="item">
	jmap['${item.key}'] = "${item.value}";
</c:forEach>

function showDiv(id)
{
	if (jmap[id] != null && jmap[id] != '')
	tooltip.showTable(jmap[id]);
}

function passTo(id)
{
	if (window.confirm('确定通过此采购单?'))
	{
		$('method').value = 'updateStockStatus';
		$('id').value = id;
		$('reject').value = '';
		$('pass').value = '1';
		formEntry.submit();
	}
}

function sub(id)
{
	if (window.confirm('确定提交此采购单?'))
	{
		$('method').value = 'updateStockStatus';
		$('id').value = id;
		$('reject').value = '';
		$('pass').value = '1';
		formEntry.submit();
	}
}

function update(id)
{
	$('method').value = 'findStock';
	$('id').value = id;
	$('stockAskChange').value = '';
	$('process').value = '';
	$('update').value = '1';
	formEntry.submit();
}

function ask(id)
{
	$('method').value = 'findStock';
	$('id').value = id;
	$('stockAskChange').value = '';
	$('process').value = '1';
	$('update').value = '';
	formEntry.submit();
}

function askChange(id)
{
	$('method').value = 'findStock';
	$('id').value = id;
	$('stockAskChange').value = '1';
	$('process').value = '';
	$('update').value = '';
	formEntry.submit();
}

function end(id)
{
	if (window.confirm('确定采购的物品已到，结束采购单并自动生成入库单?'))
	{
		$('method').value = 'endStock';
		$('id').value = id;
		formEntry.submit();
	}
}

function pay(id, status)
{
	var tipMap = {"1": "确定批准采购主管的采购付款申请?", "2": "确定向总经理申请采购付款?", "3": "确定驳回采购经理采购付款申请?"};

	if (window.confirm(tipMap[status]))
	{
		$('method').value = 'payStock';
		$('id').value = id;
		$('payStatus').value = status;
		formEntry.submit();
	}
}

function out(id)
{
	document.location.href = '../stock/stock.do?method=findStock&id=' + id + "&out=1";
}

function exports()
{
	document.location.href = '../stock/stock.do?method=exportStock';
}
</script>

</head>
<body class="body_class" onload="load()"
	onkeypress="tooltip.bingEsc(event)">
<form name="formEntry" action="../stock/stock.do"><input
	type="hidden" name="method" value="queryStock"> <input
	type="hidden" value="1" name="firstLoad"> <input type="hidden"
	value="" name="id"> <input type="hidden" value="" name="pass">
<input type="hidden" value="" name="payStatus"> <input
	type="hidden" value="" name="reject"> <input type="hidden"
	value="" name="reason"> <input type="hidden" value=""
	name="update"><input type="hidden" value=""
	name="stockAskChange"> <input type="hidden" value=""
	name="process"> <p:navigation height="22">
	<td width="550" class="navigation">采购单管理 &gt;&gt; 采购单列表</td>
	<td width="85"></td>
</p:navigation> <br>

<p:body width="100%">

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
				<td align="center">单据</td>
				<td align="center" width="35%"><input type="text"
					onkeydown="press()" name="ids" value="${ids}"></td>
				<td align="center">状态</td>
				<td align="center" width="35%"><select name="status"
					class="select_class" values="${status}">
					<option value="">--</option>
					<option value="0">保存</option>
					<option value="1">提交</option>
					<option value="2">驳回</option>
					<option value="3">区域经理通过</option>
					<option value="4">核价员通过</option>
					<option value="5">采购主管通过</option>
					<option value="6">采购经理通过</option>
					<option value="7">采购中</option>
					<option value="8">采购到货</option>
				</select></td>
			</tr>

			<tr align=center class="content0">
				<td align="center">采购区域</td>
				<td align="center" width="35%"><select name="locationId"
					class="select_class" values="${locationId}">
					<option value="">--</option>
					<c:forEach items="${locations}" var="item">
						<option value="${item.id}">${item.locationName}</option>
					</c:forEach>
				</select></td>
				<td align="center">付款</td>
				<td align="center" width="35%"><select name="pay"
					class="select_class" values="${pay}">
					<option value="">--</option>
					<option value="0">未付款</option>
					<option value="1">已付款</option>
					<option value="2">付款申请</option>
					<option value="3">申请驳回</option>
				</select></td>
			</tr>

			<tr align=center class="content0">
				<td align="center">是否逾期</td>
				<td align="center" width="35%"><select name="over"
					class="select_class" values="${over}">
					<option value="">--</option>
					<option value="0">未逾期</option>
					<option value="1">已逾期</option>
				</select></td>
				<td align="right" colspan="2"><input type="button" id="b_query"
					class="button_class" value="&nbsp;&nbsp;查 询&nbsp;&nbsp;"
					onclick="querys()">&nbsp;&nbsp; <input type="button"
					class="button_class" value="&nbsp;&nbsp;重 置&nbsp;&nbsp;" id="b_reset"
					onclick="resets()"></td>
			</tr>
		</table>

	</p:subBody>


	<p:title>
		<td class="caption"><strong>采购单列表：</strong></td>
	</p:title>

	<p:line flag="0" />

	<p:subBody width="100%">
		<table width="100%" align="center" cellspacing='1' class="table0"
			id="result">
			<tr align=center class="content0">
				<td align="center" class="td_class" onclick="tableSort(this)"
					width="10%"><strong>单据</strong></td>
				<td align="center" class="td_class" onclick="tableSort(this)"
					width="5%"><strong>采购人</strong></td>
				<td align="center" class="td_class" onclick="tableSort(this)"
					width="5%"><strong>状态</strong></td>
				<td align="center" class="td_class" onclick="tableSort(this)"
					width="5%"><strong>采购区域</strong></td>
				<td align="center" class="td_class" onclick="tableSort(this)"
					width="10%"><strong>到货日期</strong></td>
				<td align="center" class="td_class" onclick="tableSort(this, true)"
					width="10%"><strong>合计金额</strong></td>
				<td align="center" class="td_class" onclick="tableSort(this)"
					width="5%"><strong>付款</strong></td>
				<td align="center" class="td_class" onclick="tableSort(this)"
					width="10%"><strong>更新时间</strong></td>
				<td align="center" class="td_class" onclick="tableSort(this)"
					width="5%"><strong>操作</strong></td>
			</tr>

			<c:forEach items="${list}" var="item" varStatus="vs">
				<tr class="${vs.index % 2 == 0 ? 'content1' : 'content2'}">
					<td align="center" onclick="hrefAndSelect(this)"
						onMouseOver="showDiv('${item.id}')" onmousemove="tooltip.move()"
						onmouseout="tooltip.hide()"><a onclick="hrefAndSelect(this)"
						href="../stock/stock.do?method=findStock&id=${item.id}">
					${item.id} </a></td>
					<td align="center" onclick="hrefAndSelect(this)">${item.userName}</td>
					<td align="center" onclick="hrefAndSelect(this)">${my:get('stockStatus',
					item.status)}</td>
					<td align="center" onclick="hrefAndSelect(this)">${item.locationName}</td>
					<c:if test="${item.overTime == 0}">
						<td align="center" onclick="hrefAndSelect(this)"><font
							color=blue>${item.needTime}</font></td>
					</c:if>
					<c:if test="${item.overTime == 1}">
						<td align="center" onclick="hrefAndSelect(this)"><font
							color=red>${item.needTime}</font></td>
					</c:if>
					<td align="center" onclick="hrefAndSelect(this)">${my:formatNum(item.total)}</td>
					<td align="center" onclick="hrefAndSelect(this)">${my:get('stockPay', item.pay)}</td>
					<td align="center" onclick="hrefAndSelect(this)">${item.logTime}</td>
					<td align="center" onclick="hrefAndSelect(this)">
					<c:if test="${item.display == 0}">
						<c:if test="${user.role == 'COMMON'}">
							<a title="提交采购单" href="javascript:sub('${item.id}')"> <img
								src="../images/realse.gif" border="0" height="15" width="15"></a>

							<a title="修改采购单" href="javascript:update('${item.id}')"> <img
								src="../images/edit.gif" border="0" height="15" width="15"></a>

							<a title="删除采购单" href="javascript:del('${item.id}')"> <img
								src="../images/del.gif" border="0" height="15" width="15"></a>
						</c:if>

						<c:if test="${user.role != 'COMMON'  && user.role != 'PRICE'}">
							<a title="审批通过采购单" href="javascript:passTo('${item.id}')"> <img id="img_${vs.index}"
								src="../images/realse.gif" border="0" height="15" width="15"></a>

							<a title="驳回采购单" href="javascript:reject('${item.id}')"> <img
								src="../images/reject.gif" border="0" height="15" width="15"></a>
						</c:if>

						<c:if test="${user.role == 'PRICE'}">
							<a title="采购单询价" href="javascript:ask('${item.id}')"> <img id="ask_img_${vs.index}"
								src="../images/change.gif" border="0" height="15" width="15"></a>

							<a title="审批通过采购单" href="javascript:passTo('${item.id}')"> <img
								src="../images/realse.gif" border="0" height="15" width="15"></a>

							<a title="驳回采购单" href="javascript:reject('${item.id}')"> <img
								src="../images/reject.gif" border="0" height="15" width="15"></a>
						</c:if>

						<c:if test="${user.role == 'STOCK'}">
							<a title="询价变动" href="javascript:askChange('${item.id}')"> <img
								src="../images/change.gif" border="0" height="15" width="15"></a>
							<c:if test="${item.status == 7}">
								<a title="采购结束" href="javascript:end('${item.id}')"> <img id="end_img_${vs.index}"
									src="../images/end.gif" border="0" height="15" width="15"></a>
							</c:if>
						</c:if>
					</c:if> 
					
					<c:if test="${user.role == 'STOCK'}">
						<c:if test="${item.status == 7}">
							<a title="采购结束" href="javascript:end('${item.id}')"> <img id="end_img_${vs.index}"
								src="../images/end.gif" border="0" height="15" width="15"></a>
						</c:if>

						<c:if test="${item.status == 8}">
							<c:if test="${item.pay == 0 || item.pay == 3}">
								<a title="申请付款" href="javascript:pay('${item.id}', '2')"> <img id="pay_img_${vs.index}"
									src="../images/pay.gif" border="0" height="15" width="15"></a>
							</c:if>
						</c:if>
					</c:if> 
					
					<c:if test="${user.role == 'ADMIN' && item.status == 8}">
						<a title="采购生成调出" href="javascript:out('${item.id}')"> <img
							src="../images/change.gif" border="0" height="15" width="15"></a>
					</c:if> <c:if
						test="${user.role == 'MANAGER' && item.pay == 2 && user.locationID == '0'}">
						<a title="批准付款" href="javascript:pay('${item.id}', '1')"> <img id="pay_approve_img_${vs.index}"
							src="../images/pay.gif" border="0" height="15" width="15"></a>
						<a title="驳回付款" href="javascript:pay('${item.id}', '3')"> <img id="pay_reject_img_${vs.index}"
							src="../images/reject.gif" border="0" height="15" width="15"></a>
					</c:if>
					
					</td>
				</tr>
			</c:forEach>
		</table>

		<p:formTurning form="formEntry" method="queryStock"></p:formTurning>
	</p:subBody>

	<p:line flag="1" />

	<p:button leftWidth="100%" rightWidth="0%">
		<div align="right">
		<c:if test="${user.role == 'MANAGER'}">
		<input type="button" class="button_class"
			id="b_export" style="cursor: pointer"
			value="&nbsp;&nbsp;导出采购单&nbsp;&nbsp;" onclick="exports()">
		</c:if>
			 <c:if test="${user.role == 'COMMON'}">
			<input type="button" class="button_class" name="adds"
				style="cursor: pointer" value="&nbsp;&nbsp;增加采购单&nbsp;&nbsp;"
				onclick="addBean()">
		</c:if>
		</div>
	</p:button>

	<p:message />

</p:body></form>
</body>
</html>

