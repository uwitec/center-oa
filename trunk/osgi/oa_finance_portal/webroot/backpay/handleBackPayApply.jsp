<%@ page contentType="text/html;charset=UTF-8" language="java"
	errorPage="../common/error.jsp"%>
<%@include file="../common/common.jsp"%>
<html>
<head>
<p:link title="退款申请" />
<script language="JavaScript" src="../js/JCheck.js"></script>
<script language="JavaScript" src="../js/common.js"></script>
<script language="JavaScript" src="../js/public.js"></script>
<script language="JavaScript" src="../js/key.js"></script>
<script language="javascript">

function passBean()
{
    $O('method').value = 'passBackPayApply';
    
	submit('确定通过退款申请?', null, null);
}

function rejectBean()
{
    $O('method').value = 'rejectBackPayApply';
    
    submit('确定驳回退款申请?', null, null);
}

function endBean()
{
    $O('method').value = 'endBackPayApply';
    
    submit('确定付款给客户?(自动生成付款单)付款金额:' + $$('backPay'), null, null);
}

</script>

</head>
<body class="body_class">
<form name="formEntry" action="../finance/backpay.do" method="post">
<input type="hidden" name="method" value=""> 
<input type="hidden" name="id" value="${bean.id}"> 
<input type="hidden" name="mode" value="${mode}"> 
<p:navigation
	height="22">
	<td width="550" class="navigation">
	<span style="cursor: pointer;"
        onclick="javascript:history.go(-1)">资金管理</span> &gt;&gt;
	<span>退款申请处理</span></td>
	<td width="85"></td>
</p:navigation> <br>

<p:body width="98%">

	<p:title>
		<td class="caption"><strong>退款基本信息：</strong></td>
	</p:title>

	<p:line flag="0" />

	<p:subBody width="100%">

		<p:class value="com.china.center.oa.finance.bean.BackPayApplyBean" />

		<p:table cells="2">

            <c:if test="${bean.type == 0}">
			<p:cell title="销售标识">
                <a href="../sail/out.do?method=findOut&outId=${out.fullId}">${out.fullId}</a>
            </p:cell>

            <p:cell title="总金额">
                ${my:formatNum(out.total)}
            </p:cell>
            
            <p:cell title="已支付">
                ${my:formatNum(out.hadPay)}
            </p:cell>
            
            <p:cell title="坏账金额">
                ${my:formatNum(out.badDebts)}
            </p:cell>
            
            <p:cell title="退货实物价值">
                ${my:formatNum(backTotal)}
            </p:cell>
            
            <p:cell title="申请退款金额">
                ${my:formatNum(bean.backPay)}
            </p:cell>
            
            <p:cell title="转预收">
                ${my:formatNum(bean.changePayment)}
            </p:cell>
            </c:if>
    
            <c:if test="${bean.type == 1}">
	            <p:cell title="预收标识">
	                <a href="../finance/bill.do?method=findInBill&id=${bean.billId}">${bean.billId}</a>
	            </p:cell>
	
	            <p:cell title="申请退款金额">
	                ${my:formatNum(bean.backPay)}
	            </p:cell>
            </c:if>
            
            <p:cell title="客户">
                 ${bean.customerName}
            </p:cell>
            
            <p:cell title="申请人">
                 ${bean.stafferName}
            </p:cell>
            
            <p:cell title="备注" end="true">
               ${bean.description}
            </p:cell>
            
            <p:cell title="时间" end="true">
               ${bean.logTime}
            </p:cell>

            <c:if test="${bean.status == 2 && mode != 0}">
	            <p:cell title="申请退款金额" end="true">
	                <input type="text" oncheck="notNone;isFloat;" readonly="readonly" name="backPay" value="${my:formatNum(bean.backPay)}">
	                <font color="red">*</font>
	            </p:cell> 
	            
	            <p:cell title="银行帐户" end="true">
	                <select name="bankId" style="width: 400px" class="select_class" oncheck="notNone">
	                <p:option type="bankList"></p:option>
	                </select>
	                <font color="red">*</font>
	            </p:cell> 
	            
	            <p:cell title="付款方式" end="true">
	                <select name="payType" style="width: 400px" class="select_class" oncheck="notNone">
	                <p:option type="outbillPayType"></p:option>
	                </select>
	                <font color="red">*</font>
	            </p:cell>
	            
            </c:if>         
            
            <c:if test="${bean.status != 0 && bean.status != 98 && bean.status != 99 && mode != 0}">
				<p:cell title="审批意见" end="true">
					<textarea rows="3" cols="55" oncheck="notNone;maxLength(200);" name="reason"></textarea>
					<font color="red">*</font>
				</p:cell>
			</c:if>

		</p:table>

	</p:subBody>
	
	<p:line flag="0" />

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

			<c:forEach items="${loglist}" var="item" varStatus="vs">
				<tr class='${vs.index % 2 == 0 ? "content1" : "content2"}'>
					<td align="center">${item.actor}</td>

					<td align="center">${my:get('oprMode', item.oprMode)}</td>

					<td align="center">${my:get('backPayApplyStatus', item.preStatus)}</td>

					<td align="center">${my:get('backPayApplyStatus', item.afterStatus)}</td>

					<td align="center">${item.description}</td>

					<td align="center">${item.logTime}</td>

				</tr>
			</c:forEach>
		</table>
	</p:subBody>

	<p:line flag="1" />

	<p:button leftWidth="100%" rightWidth="0%">
		<div align="right">
			
			<c:if test="${bean.status == 1 && mode != 0}">
	            <input type="button" class="button_class"
	                id="ok_p" style="cursor: pointer" value="&nbsp;&nbsp;通 过&nbsp;&nbsp;"
	                onclick="passBean()">&nbsp;&nbsp;
	            <input type="button" class="button_class"
	            id="re_b" style="cursor: pointer" value="&nbsp;&nbsp;驳 回&nbsp;&nbsp;"
	            onclick="rejectBean()">&nbsp;&nbsp;
            </c:if>
            
            <c:if test="${bean.status == 2 && mode != 0}">
	            <input type="button" class="button_class"
	                id="ok_p2" style="cursor: pointer" value="&nbsp;&nbsp;付 款&nbsp;&nbsp;"
	                onclick="endBean()">&nbsp;&nbsp;
	            <input type="button" class="button_class"
	            id="re_b" style="cursor: pointer" value="&nbsp;&nbsp;驳 回&nbsp;&nbsp;"
	            onclick="rejectBean()">&nbsp;&nbsp;
            </c:if>
            
            <input
	            type="button" name="ba" class="button_class"
	            onclick="javascript:history.go(-1)"
	            value="&nbsp;&nbsp;返 回&nbsp;&nbsp;">
			</div>
	</p:button>

	<p:message2 />
</p:body></form>
</body>
</html>

