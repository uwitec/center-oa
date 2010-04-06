<%@ page contentType="text/html;charset=GBK" language="java"
	errorPage="./error.jsp"%>
<%@include file="./common.jsp"%>
<html>
<head>
<p:link title="询价列表" />
<link href="../js/plugin/dialog/css/dialog.css" type="text/css" rel="stylesheet"/>
<script language="JavaScript" src="../js/common.js"></script>
<script language="JavaScript" src="../js/public.js"></script>
<script language="JavaScript" src="../js/fanye.js"></script>
<script language="JavaScript" src="../js/title_div.js"></script>
<script src="../js/jquery/jquery.js"></script>
<script src="../js/plugin/dialog/jquery.dialog.js"></script>
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

	$O('productName').value = '';
	$O('productId').value = '';
	$O('qid').value = '';
	setSelectIndex($O('status'), 0);
}

function load()
{
    <c:if test="${user.role != 'NETASK'}">
	$f('qid');
	$O('qid').select();
	</c:if>
	
	tooltip.init();
}

function del(id)
{
	if (window.confirm('确定删除询价?'))
	{
		$O('method').value = 'delPriceAsk';
		$O('id').value = id;
		formEntry.submit();
	}
}

function reject(id)
{
	if (window.confirm('确定驳回此询价?'))
	{
		$O('method').value = 'rejectPriceAsk';
		$O('id').value = id;
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
	//单选
	window.common.modal("../admin/product.do?method=rptInQueryProduct3&firstLoad=1&type=2");
}

function process(id)
{
	document.location.href = '../admin/price.do?method=preForProcessAskPrice&id=' + id;
}

function end(id)
{
	if (window.confirm('确定结束此询价?'))
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
		$O('productId').value = obj.value;
		$O('productName').value = obj.productname;
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

function updateMaxAmount(id, amount)
{
    $.messager.prompt('更新询价数量', '请询价数量', amount, function(r){
                if (r)
                {
                    $Dbuttons(true);
                    
                    var sss = r;
        
                    if (!(sss == null || sss == ''))
                    {
                        document.location.href = '../admin/price.do?method=updatePriceAskAmount&updateMax=1&id=' + id + '&newAmount=' + sss;
                    }
                    else
                    {
                        $Dbuttons(false);
                    }
                }
                else
                {
                    alert('请输询价数量');
                }
            });
}

function updatePass(id)
{
    //updatePriceAskAmountStatus
    if (window.confirm('确定放行此询价?'))
    {
        $Dbuttons(true);
        document.location.href = '../admin/price.do?method=updatePriceAskAmountStatus&updateMax=1&id=' + id;
    }
}
</script>

</head>
<body class="body_class" onload="load()" onkeypress="tooltip.bingEsc(event)">
<form name="formEntry" action="../admin/price.do"><input
	type="hidden" name="method" value="queryPriceAsk"> <input
	type="hidden" name="id" value=""> <input type="hidden"
	name="productId" value="${productId}"><input type="hidden"
	value="1" name="firstLoad">
	<input type="hidden" value="${updateMax}" name="updateMax">
	 <p:navigation height="22">
	<td width="550" class="navigation">询价管理 &gt;&gt; 询价列表</td>
	<td width="85"></td>
</p:navigation> <br>

<p:body width="100%">

    <c:if test="${user.role != 'NETASK'}">
	<p:subBody width="98%">
		<table width="100%" align="center" cellspacing='1' class="table0">
			<tr align=center class="content0">
				<td align="center">开始时间</td>
				<td align="center" width="35%"><p:plugin name="alogTime"
					value="${pmap.alogTime}" /></td>
				<td align="center">结束时间</td>
				<td align="center" width="35%"><p:plugin name="blogTime"
					value="${pmap.blogTime}" /></td>
			</tr>

			<tr align=center class="content1">
				<td align="center">产品名称</td>
				<td align="center" width="35%"><input type="text"
					readonly="readonly" name="productName" value="${pmap.productName}">&nbsp;&nbsp;<input
					type="button" value="&nbsp;...&nbsp;" name="qout"
					class="button_class" onclick="selectProduct(0)"></td>
				<td align="center">状态</td>
				<td align="center" width="35%"><select name="status"
					class="select_class" values="${pmap.status}">
					<option value="">--</option>
					<option value="0">开始</option>
					<option value="1">询价中</option>
					<option value="2">驳回</option>
					<option value="3">结束</option>
				</select></td>
			</tr>

			<tr align=center class="content0">
				<td align="center">紧急程度</td>
				<td align="center" width="35%"><select name="instancy"
					class="select_class" values="${pmap.instancy}">
					<option value="">--</option>
					<option value="0">一般</option>
					<option value="1">紧急</option>
					<option value="2">非常紧急</option>
				</select></td>
				<td align="center">询价单号</td>
				<td align="center" width="35%"><input type="text" onkeydown="press()"
					 name="qid" value="${pmap.qid}"></td>
			</tr>

			<tr align=center class="content1">
				<td align="center">是否逾期</td>
				<td align="center" width="35%"><select name="overTime"
					class="select_class" values="${pmap.overTime}">
					<option value="">--</option>
					<option value="1">是</option>
					<option value="0">否</option>
				</select></td>
				<td align="center">询价类型</td>
                <td align="center" width="35%"><select name="type"
                    class="select_class" values="${pmap.type}">
                    <option value="">--</option>
                    <option value="0">内部询价</option>
                    <option value="1">外网询价</option>
                    <option value="2">内外网询价</option>
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
	</c:if>


	<p:title>
		<td class="caption"><strong>询价列表：</strong></td>
	</p:title>

	<p:line flag="0" />

	<p:subBody width="98%">
		<table width="100%" align="center" cellspacing='1' class="table0"
			id="result">
			<tr align=center class="content0">
				<td align="center" class="td_class" onclick="tableSort(this)"
					width="15%"><strong>询价单号</strong></td>
				<td align="center" class="td_class" onclick="tableSort(this)"
					width="5%"><strong>操作</strong></td>
				<td align="center" class="td_class" onclick="tableSort(this)"
					width="15%"><strong>产品</strong></td>
				<td align="center" class="td_class" onclick="tableSort(this, true)"
					width="5%"><strong>数量</strong></td>
				<td align="center" class="td_class" onclick="tableSort(this, true)"
					width="5%"><strong>价格</strong></td>
				<td align="center" class="td_class" onclick="tableSort(this)"
					width="5%"><strong>状态</strong></td>
				<td align="center" class="td_class" onclick="tableSort(this)"
                    width="5%"><strong>类型</strong></td>
				<td align="center" class="td_class" onclick="tableSort(this)"
					width="5%"><strong>紧急程度</strong></td>
				<td align="center" class="td_class" onclick="tableSort(this)"
					width="5%"><strong>区域</strong></td>
				<td align="center" class="td_class" onclick="tableSort(this)"
					width="10%"><strong>录入时间</strong></td>
				<td align="center" class="td_class" onclick="tableSort(this)"
					width="10%"><strong>处理时间</strong></td>
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
						<a title="结束询价" href="javascript:end('${item.id}')"> <img
							src="../images/realse.gif" border="0" height="15" width="15"></a>
					</c:if>
					
					<c:if test="${(user.role == 'PRICE' || user.role == 'STOCK'
					 || user.role == 'NETASK' || user.role == 'NETCOMMON') && (item.status == 0 || item.status == 1 )}">

						<a title="处理询价" href="javascript:process('${item.id}')"> <img
							src="../images/change.gif" border="0" height="15" width="15"></a>
					</c:if>

					<c:if test="${user.role == 'COMMON' && (item.status == 0 || item.status == 2)}">
						<a title="删除询价" href="javascript:del('${item.id}')"> <img
							src="../images/del.gif" border="0" height="15" width="15"></a>
					</c:if>

					<c:if test="${item.status == 1 && user.role != 'NETASK'}">
						<a title="询价详细" href="javascript:detail('${item.id}')"> <img
							src="../images/edit.gif" border="0" height="15" width="15"></a>
					</c:if>
					
					<c:if test="${(user.role == 'NETSTOCK') && (item.status == 0 || item.status == 1 ) && updateMax == '1'}">
                        <a title="修改最大询价数量" href="javascript:updateMaxAmount('${item.id}', ${item.amount})"> <img
                            src="../images/edit.gif" border="0" height="15" width="15"></a>
                        <a title="直接放行" href="javascript:updatePass('${item.id}')"> <img
                            src="../images/change.gif" border="0" height="15" width="15"></a>
                    </c:if>

					</td>
					<td align="center" onclick="hrefAndSelect(this)">
					<a href="../admin/product.do?method=findProduct&productId=${item.productId}&detail=1">${item.productName}</a></td>
					<td align="center" onclick="hrefAndSelect(this, true)">${item.amount}</td>
					<c:if test="${user.role != 'NETASK'}">
					<td align="center" onclick="hrefAndSelect(this, true)">${item.price}</td>
					</c:if>
					<c:if test="${user.role == 'NETASK'}">
                    <td align="center" onclick="hrefAndSelect(this, true)"></td>
                    </c:if>
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
			style="cursor: pointer" value="&nbsp;&nbsp;询价申请&nbsp;&nbsp;"
			onclick="addBean()"></div>
	</p:button>
	</c:if>

	<p:message />

</p:body></form>
</body>
</html>

