<%@ page contentType="text/html;charset=GBK" language="java"
	errorPage="./error.jsp"%>
<%@include file="./common.jsp"%>
<html>
<head>
<p:link title="ѯ���б�" />
<script language="JavaScript" src="../js/prototype.js"></script>
<script language="JavaScript" src="../js/common.js"></script>
<script language="JavaScript" src="../js/public.js"></script>
<script language="JavaScript" src="../js/fanye.js"></script>
<script language="JavaScript" src="../js/title_div.js"></script>
<script language="javascript">

var ask = 0;

function querys()
{
	formEntry.submit();
}

function addBean()
{
	document.location.href = '../admin/addPriceAsk.jsp';
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
	$('qid').value = '';
	setSelectIndex($('status'), 0);
}

function load()
{
	$f('qid');
	$('qid').select();
}

function del(id)
{
	if (window.confirm('ȷ��ɾ��ѯ��?'))
	{
		$('method').value = 'delPriceAsk';
		$('id').value = id;
		formEntry.submit();
	}
}

function reject(id)
{
	if (window.confirm('ȷ�����ش�ѯ��?'))
	{
		$('method').value = 'rejectPriceAsk';
		$('id').value = id;
		formEntry.submit();
	}
}

function detail(id)
{
	document.location.href = '../admin/price.do?method=findPriceAsk&id=' + id;
}

function selectProduct(index)
{
	ask = index;
	//��ѡ
	window.common.modal("../admin/product.do?method=rptInQueryProduct3&firstLoad=1&type=2");
}

function process(id)
{
	document.location.href = '../admin/price.do?method=preForProcessAskPrice&id=' + id;
}

function end(id)
{
	if (window.confirm('ȷ��������ѯ��?'))
	{
		document.location.href = '../admin/price.do?method=endAskPrice&id=' + id;
	}
}

function getProduct(oo)
{
	var obj = oo;

	if (isNull(obj.value))
	{
		return;
	}

	if (ask == 1)
	{
		document.location.href = '../admin/price.do?method=queryNearlyPrice&productId=' + obj.value;
	}
	else
	{
		$('productId').value = obj.value;
		$('productName').value = obj.productname;
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
</script>

</head>
<body class="body_class" onload="load()" onkeypress="tooltip.bingEsc(event)">
<form name="formEntry" action="../admin/price.do"><input
	type="hidden" name="method" value="queryPriceAsk"> <input
	type="hidden" name="id" value=""> <input type="hidden"
	name="productId" value="${productId}"><input type="hidden"
	value="1" name="firstLoad"> <p:navigation height="22">
	<td width="550" class="navigation">ѯ�۹��� &gt;&gt; ѯ���б�</td>
	<td width="85"></td>
</p:navigation> <br>

<p:body width="100%">

	<p:subBody width="100%">
		<table width="100%" align="center" cellspacing='1' class="table0">
			<tr align=center class="content0">
				<td align="center">��ʼʱ��</td>
				<td align="center" width="35%"><p:plugin name="alogTime"
					value="${pmap.alogTime}" /></td>
				<td align="center">����ʱ��</td>
				<td align="center" width="35%"><p:plugin name="blogTime"
					value="${pmap.blogTime}" /></td>
			</tr>

			<tr align=center class="content1">
				<td align="center">��Ʒ����</td>
				<td align="center" width="35%"><input type="text"
					readonly="readonly" name="productName" value="${pmap.productName}">&nbsp;&nbsp;<input
					type="button" value="&nbsp;...&nbsp;" name="qout"
					class="button_class" onclick="selectProduct(0)"></td>
				<td align="center">״̬</td>
				<td align="center" width="35%"><select name="status"
					class="select_class" values="${pmap.status}">
					<option value="">--</option>
					<option value="0">��ʼ</option>
					<option value="1">ѯ����</option>
					<option value="2">����</option>
					<option value="3">����</option>
				</select></td>
			</tr>

			<tr align=center class="content0">
				<td align="center">�����̶�</td>
				<td align="center" width="35%"><select name="instancy"
					class="select_class" values="${pmap.instancy}">
					<option value="">--</option>
					<option value="0">һ��</option>
					<option value="1">����</option>
					<option value="2">�ǳ�����</option>
				</select></td>
				<td align="center">ѯ�۵���</td>
				<td align="center" width="35%"><input type="text" onkeydown="press()"
					 name="qid" value="${pmap.qid}"></td>
			</tr>

			<tr align=center class="content1">
				<td align="center">�Ƿ�����</td>
				<td align="center" width="35%"><select name="overTime"
					class="select_class" values="${pmap.overTime}">
					<option value="">--</option>
					<option value="1">��</option>
					<option value="0">��</option>
				</select></td>
				<td align="right" colspan="2"><input type="button"
					class="button_class" value="&nbsp;&nbsp;�� ѯ&nbsp;&nbsp;"
					onclick="querys()">&nbsp;&nbsp; <input type="button"
					class="button_class" value="&nbsp;&nbsp;�� ��&nbsp;&nbsp;"
					onclick="resets()"></td>
			</tr>
		</table>

	</p:subBody>


	<p:title>
		<td class="caption"><strong>ѯ���б���</strong></td>
	</p:title>

	<p:line flag="0" />

	<p:subBody width="100%">
		<table width="100%" align="center" cellspacing='1' class="table0"
			id="result">
			<tr align=center class="content0">
				<td align="center" class="td_class" onclick="tableSort(this)"
					width="15%"><strong>ѯ�۵���</strong></td>
				<td align="center" class="td_class" onclick="tableSort(this)"
					width="5%"><strong>����</strong></td>
				<td align="center" class="td_class" onclick="tableSort(this)"
					width="15%"><strong>��Ʒ</strong></td>
				<td align="center" class="td_class" onclick="tableSort(this, true)"
					width="5%"><strong>����</strong></td>
				<td align="center" class="td_class" onclick="tableSort(this, true)"
					width="5%"><strong>�۸�</strong></td>
				<td align="center" class="td_class" onclick="tableSort(this)"
					width="5%"><strong>״̬</strong></td>
				<td align="center" class="td_class" onclick="tableSort(this)"
                    width="5%"><strong>����</strong></td>
				<td align="center" class="td_class" onclick="tableSort(this)"
					width="5%"><strong>�����̶�</strong></td>
				<td align="center" class="td_class" onclick="tableSort(this)"
					width="5%"><strong>����</strong></td>
				<td align="center" class="td_class" onclick="tableSort(this)"
					width="10%"><strong>¼��ʱ��</strong></td>
				<td align="center" class="td_class" onclick="tableSort(this)"
					width="10%"><strong>����ʱ��</strong></td>
			</tr>

			<c:forEach items="${list}" var="item" varStatus="vs">
				<tr class="${vs.index % 2 == 0 ? 'content1' : 'content2'}">
					<td align="center" onclick="hrefAndSelect(this)"
					onMouseOver="showDiv('${item.id}')" onmousemove="tooltip.move()" onmouseout="tooltip.hide()"
					>
					<a onclick="hrefAndSelect(this)" href="../admin/price.do?method=findPriceAsk&id=${item.id}">
					${item.id}
					</a></td>
					<td align="center" onclick="hrefAndSelect(this)">
					<c:if test="${user.role == 'STOCK' && item.status == 1}">
						<a title="����ѯ��" href="javascript:end('${item.id}')"> <img
							src="../images/realse.gif" border="0" height="15" width="15"></a>
					</c:if>
					
					<c:if test="${(user.role == 'PRICE' || user.role == 'STOCK' ) && (item.status == 0 || item.status == 1 )}">

						<a title="����ѯ��" href="javascript:process('${item.id}')"> <img
							src="../images/change.gif" border="0" height="15" width="15"></a>
					</c:if>

					<c:if test="${user.role == 'COMMON' && item.status != 1}">
						<a title="ɾ��ѯ��" href="javascript:del('${item.id}')"> <img
							src="../images/del.gif" border="0" height="15" width="15"></a>
					</c:if>

					<c:if test="${item.status == 1}">
						<a title="ѯ����ϸ" href="javascript:detail('${item.id}')"> <img
							src="../images/edit.gif" border="0" height="15" width="15"></a>
					</c:if>

					</td>
					<td align="center" onclick="hrefAndSelect(this)">
					<a href="../admin/product.do?method=findProduct&productId=${item.productId}&detail=1">${item.productName}</a></td>
					<td align="center" onclick="hrefAndSelect(this, true)">${item.amount}</td>
					<td align="center" onclick="hrefAndSelect(this, true)">${item.price}</td>
					<td align="center" onclick="hrefAndSelect(this)">${my:get('priceAskStatus', item.status)}</td>
					<td align="center" onclick="hrefAndSelect(this)">${my:get('priceAskType', item.type)}</td>
					<td align="center" onclick="hrefAndSelect(this)">${my:get('priceAskInstancy',
					item.instancy)}</td>
					<td align="center" onclick="hrefAndSelect(this)">${item.locationName}</td>
					<td align="center" onclick="hrefAndSelect(this)">${item.logTime}</td>
					<c:if test="${item.overTime == 0}">
					<td align="center" onclick="hrefAndSelect(this)"><font color=blue>${item.processTime}</font></td>
					</c:if>
					<c:if test="${item.overTime == 1}">
					<td align="center" onclick="hrefAndSelect(this)"><font color=red>${item.processTime}</font></td>
					</c:if>
				</tr>
			</c:forEach>
		</table>

		<p:formTurning form="formEntry" method="queryPriceAsk"></p:formTurning>
	</p:subBody>

	<p:line flag="1" />

	<c:if test="${user.role == 'COMMON'}">
	<p:button leftWidth="100%" rightWidth="0%">
		<div align="right"><input
			type="button" class="button_class" name="adds1"
			style="cursor: pointer" value="&nbsp;&nbsp;ѯ������&nbsp;&nbsp;"
			onclick="addBean()"></div>
	</p:button>
	</c:if>

	<p:message />

</p:body></form>
</body>
</html>
