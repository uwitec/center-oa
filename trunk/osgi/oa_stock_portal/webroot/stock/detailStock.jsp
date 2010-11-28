<%@ page contentType="text/html;charset=UTF-8" language="java"
	errorPage="../common/error.jsp"%>
<%@include file="../common/common.jsp"%>
<html>
<head>
<p:link title="采购单" />
<script language="JavaScript" src="../js/prototype.js"></script>
<script language="JavaScript" src="../js/common.js"></script>
<script language="JavaScript" src="../js/public.js"></script>
<script language="JavaScript" src="../js/key.js"></script>
<script language="JavaScript" src="../js/title_div.js"></script>

<script language="javascript">
function load()
{
	//tooltip.init();
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

function out(id)
{
	if (window.confirm('确定把此采购的产品调出?'))
	{
		var sss = window.prompt('请输入运输单号：', '');

		$('tranNo').value = sss;

		if (!(sss == null || sss == ''))
		{
			$('method').value = 'stockItemChangeToOut';
			$('id').value = id;
			formEntry.submit();
		}
		else
		{
			alert('请输入运输单号');
		}
	}
}
</script>

</head>
<body class="body_class" onload="load()" onkeydown="tooltip.bingEsc(event)">
<form action="../stock/stock.do" name="formEntry">
<input type="hidden" name="method" value="">
<input type="hidden" name="tranNo" value="">
<input type="hidden" name="id" value="">
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
			<p:class value="com.china.center.oa.stock.bean.StockBean" opr="2"/>
			
			<p:cell title="单号">
			${bean.id}
			</p:cell>

			<p:cell title="采购人">
			${bean.userName}
			</p:cell>
			
			<p:cell title="提交人">
			${bean.owerName}
			</p:cell>

			<p:cell title="区域">
			${bean.locationName}
			</p:cell>

			<p:cell title="状态">
			${my:get('stockStatus', bean.status)}
			</p:cell>
			
			<p:pro field="stockType">
				<option value="">--</option>
               <p:option type="stockSailType"></p:option>
            </p:pro>
            
            <p:pro field="stype">
				<option value="">--</option>
               <p:option type="stockStype"></p:option>
            </p:pro>
			
            <p:pro field="invoiceType" innerString="style='width: 300px'">
                <option value="">没有发票</option>
                <c:forEach items="${invoiceList}" var="item">
				<option value="${item.id}">${item.fullName}</option>
				</c:forEach>
            </p:pro>

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

			<p:cells celspan="1" title="异常状态">
			${my:get('stockExceptStatus', bean.exceptStatus)}
			</p:cells>
			
			<p:cells celspan="1" title="询价类型">
            ${my:get('priceAskType', bean.type)}
            </p:cells>
            
            <p:pro field="willDate"/>
            
            <p:pro field="nearlyPayDate" cell="1"/>
            
            <p:cells celspan="2" title="纳税实体">
            ${bean.dutyName}
            </p:cells>

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
				<td width="5%" align="center">是否询价</td>
				<td width="10%" align="center">参考价格</td>
				<td width="10%" align="center">实际价格</td>
				
				<c:if test="${user.role != 'COMMON' && user.role != 'MANAGER'}">
				<td width="15%" align="center">供应商</td>
				</c:if>
				<td width="5%" align="center">是否调出</td>
				<td width="10%" align="center">合计金额</td>
				<c:if test="${out == 1}">
				<td width="10%" align="center">操作</td>
				</c:if>
			</tr>

			<c:forEach items="${bean.itemVO}" var="item" varStatus="vs">
				<tr class='${vs.index % 2 == 0 ? "content1" : "content2"}'>
					<td align="center" style="cursor: pointer;"
					onMouseOver="showDiv('${item.id}')" onmousemove="tooltip.move()" onmouseout="tooltip.hide()"
					><a href="../product/product.do?method=findProduct&id=${item.productId}&detail=1">${item.productName}</a></td>

					<td align="center">${item.amount}</td>
					
					<td align="center">${item.productNum}</td>

					<td align="center">${item.status == 0 ? "否" : "是"}</td>

					<td align="center">${my:formatNum(item.prePrice)}</td>

					<td align="center">${my:formatNum(item.price)}</td>

					<c:if test="${user.role != 'COMMON' && user.role != 'MANAGER'}">
					<td align="center">${item.providerName}</td>
					</c:if>

					<td align="center">${item.hasRef == 0 ? "<font color=red>否</font>" : "是"}</td>

					<td align="center">${my:formatNum(item.total)}</td>

					<c:if test="${out == 1}">
						<c:if test="${item.hasRef == 0}">
							<td align="center">
							<a title="采购调出" href="javascript:out('${item.id}')"> <img
										src="../images/change.gif" border="0" height="15" width="15">
							</a>
							</td>
						</c:if>
					</c:if>
				</tr>
			</c:forEach>
		</table>
	</p:subBody>

	<p:tr />

	<p:subBody width="100%">
		<table width="100%" border="0" cellspacing='1' id="tables">
			<tr align="center" class="content0">
				<td width="10%" align="center">审批人</td>
				<td width="10%" align="center">审批动作</td>
				<td width="10%" align="center">前状态</td>
				<td width="10%" align="center">后状态</td>
				<td width="45%" align="center">意见</td>
				<td width="15%" align="center">时间</td>
			</tr>

			<c:forEach items="${logs}" var="item" varStatus="vs">
				<tr class='${vs.index % 2 == 0 ? "content1" : "content2"}'>
					<td align="center">${item.actor}</td>

					<td align="center">${item.oprModeName}</td>

					<td align="center">${item.preStatusName}</td>

					<td align="center">${item.afterStatusName}</td>

					<td align="center">${item.description}</td>

					<td align="center">${item.logTime}</td>

				</tr>
			</c:forEach>
		</table>
	</p:subBody>

	<p:line flag="1" />

	<p:button leftWidth="100%" rightWidth="0%">
		<div align="right"><input type="button" class="button_class"
			onclick="javascript:history.go(-1)"
			value="&nbsp;&nbsp;返 回&nbsp;&nbsp;"></div>
	</p:button>

	<p:message2></p:message2>
</p:body></form>
</body>
</html>

