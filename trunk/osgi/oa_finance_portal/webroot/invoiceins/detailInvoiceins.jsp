<%@ page contentType="text/html;charset=UTF-8" language="java"
	errorPage="../common/error.jsp"%>
<%@include file="../common/common.jsp"%>
<html>
<head>
<p:link title="发票" link="true" guid="true" cal="true" dialog="true" />
<script language="JavaScript" src="../js/JCheck.js"></script>
<script language="JavaScript" src="../js/common.js"></script>
<script language="JavaScript" src="../js/public.js"></script>
<script language="JavaScript" src="../js/key.js"></script>
<script src="../stockapply_js/scheck.js"></script>
<script language="javascript">

function passBean()
{
    $O('method').value = 'passInvoiceins';
    
    submit('确定通过?', null, null);
}

function rejectBean()
{
    $O('method').value = 'rejectInvoiceins';
    
    submit('确定驳回?', null, null);
}

function checkSubmit(checks, checkrefId)
{
    if (checks == '' || checkrefId == '')
    {
        alert('意见和关联单据不能为空');
        
        return false;
    }
    
    closeCheckDiv();
    
    $ajax2('../finance/invoiceins.do?method=checkInvoiceins&id=${bean.id}', {'checks' : checks, 'checkrefId' : checkrefId}, 
                        callBackFun1);
}

function callBackFun1(data)
{
    alert(data.msg);
    
    if (data.ret == 0)
    {
        $('#checkCell_SEC').html('已核对 / ' + $('#checks').val() + ' / ' + $('#checkrefId').val());
    }
}

function checkBean()
{
    openCheckDiv();
}


</script>

</head>
<body class="body_class">
<form name="formEntry" action="../finance/invoiceins.do" method="post">
<input type="hidden" name="method" value=""> 
<input type="hidden" name="id" value="${bean.id}"> 
<input type="hidden" name="mode" value="${mode}"> 
<p:navigation
	height="22">
	<td width="550" class="navigation"><span style="cursor: pointer;"
		onclick="javascript:history.go(-1)">发票管理</span> &gt;&gt; 发票明细</td>
	<td width="85"></td>
</p:navigation> <br>

<p:body width="98%">

	<p:title>
		<td class="caption"><strong>发票基本信息：</strong></td>
	</p:title>

	<p:line flag="0" />

	<p:subBody width="100%">

		<p:class value="com.china.center.oa.finance.bean.InvoiceinsBean" opr="2"/>

		<p:table cells="1">
		    <p:cell title="标识">
               ${bean.id}
            </p:cell>

			<p:pro field="invoiceDate"/>
			
			<p:pro field="unit" innerString="size=60" />

			<p:pro field="invoiceId" innerString="style='WIDTH: 340px;'">
			    <c:forEach items="${invoiceList}" var="item">
			    <option value="${item.id}">${item.fullName}</option>
			    </c:forEach>
			</p:pro>

			<p:pro field="dutyId" innerString="onchange=loadShow()">
				<p:option type="dutyList" />
			</p:pro>
			
			<p:cell title="客户/分公司">
               ${bean.customerName}
            </p:cell>
			
			<p:cell title="关联单据">
			    <c:forEach items="${bean.vsList}" var="item">
			    ${item.outId};
			    </c:forEach>
            </p:cell>
            
            <p:cell title="开票人">
               ${bean.stafferName}
            </p:cell>
            
            <p:cell title="审核人">
               ${bean.processName}
            </p:cell>
            
            <p:cell title="总金额">
               ${my:formatNum(bean.moneys)}
            </p:cell>
            
            <p:cell title="关注类型">
               <font color="red">${my:get('pubVtype', bean.vtype)}</font>
            </p:cell>
            
            <p:cell title="特殊类型">
               <font color="red">${my:get('invoiceinssType', bean.stype)}</font>
            </p:cell>
            
            <p:cell title="开票时间">
               ${bean.logTime}
            </p:cell>
            
            <p:cell title="核对信息" id="checkCell">
               ${my:get('pubCheckStatus', bean.checkStatus)} / ${bean.checks} / ${bean.checkrefId}
            </p:cell>

			<p:pro field="description" cell="0" innerString="rows=3 cols=55" />
			
			 <c:if test="${(bean.status == 1 || bean.status == 2) && (mode == 1 || mode == 3)}">
                <p:cell title="审批意见" end="true">
                    <textarea rows="3" cols="55" oncheck="maxLength(200);" name="reason"></textarea>
                </p:cell>
            </c:if>

		</p:table>

	</p:subBody>

	<p:tr />


	<p:subBody width="100%">

		<p:table cells="1">

			<tr align="center" class="content0">
				<td width="30%" align="center">品名</td>
				<td width="30%" align="center">规格</td>
                <td width="10%" align="center">单位</td>
				<td width="10%" align="center">数量</td>
				<td width="10%" align="center">单价</td>
				<td width="10%" align="center">合计</td>
			</tr>
			
			<c:forEach items="${bean.itemList}" var="item">
			<tr align="center" class="content0">
                <td align="center">${item.showName}</td>
                <td align="center">${item.special}</td>
                <td align="center">${item.unit}</td>
                <td align="center">${item.amount}</td>
                <td align="center">${my:formatNum(item.price)}</td>
                <td align="center">${my:formatNum(item.price * item.amount)}</td>
            </tr>
            </c:forEach>

		</p:table>

	</p:subBody>
	
	<p:tr />
	
	<p:subBody width="100%">

		<p:table cells="1">

			<tr align="center" class="content0">
				<td width="20%" align="center">类型</td>
				<td width="40%" align="center">单据</td>
				<td width="20%" align="center">子项</td>
				<td width="30%" align="center">开票金额</td>
			</tr>
			
			<c:forEach items="${bean.vsList}" var="item">
			<tr align="center" class="content0">
                <td align="center">${my:get('invsoutType', item.type)}</td>
                <td align="center">
                <c:if test="${item.type == 0}">
                <a href="../sail/out.do?method=findOut&outId=${item.outId}">${item.outId}</a>
                </c:if>
                <c:if test="${item.type == 1}">
                <a href="../sail/out.do?method=findOutBalance&id=${item.outId}">${item.outId}</a>
                </c:if>
                </td>
                <td align="center">${item.baseId}</td>
                <td align="center">${my:formatNum(item.moneys)}</td>
            </tr>
            </c:forEach>

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

            <c:forEach items="${logList}" var="item" varStatus="vs">
                <tr class='${vs.index % 2 == 0 ? "content1" : "content2"}'>
                    <td align="center">${item.actor}</td>

                    <td align="center">${my:get('oprMode', item.oprMode)}</td>

                    <td align="center">${my:get('invoiceinsStatus', item.preStatus)}</td>

                    <td align="center">${my:get('invoiceinsStatus', item.afterStatus)}</td>

                    <td align="center">${item.description}</td>

                    <td align="center">${item.logTime}</td>

                </tr>
            </c:forEach>
        </table>
    </p:subBody>
	
	<p:line flag="1" />

	<p:button leftWidth="100%" rightWidth="0%">
		<div align="right">
		
		<c:if test="${(bean.status == 1 || bean.status == 2) && (mode == 1 || mode == 3)}">
                <input type="button" class="button_class"
                    id="ok_p" style="cursor: pointer" value="&nbsp;&nbsp;通 过&nbsp;&nbsp;"
                    onclick="passBean()">&nbsp;&nbsp;
                <input type="button" class="button_class"
                id="re_b" style="cursor: pointer" value="&nbsp;&nbsp;驳 回&nbsp;&nbsp;"
                onclick="rejectBean()">&nbsp;&nbsp;
         </c:if>
         
         <c:if test="${bean.status == 99}">
	        <input
	            type="button" name="ba" class="button_class"
	            onclick="checkBean()"
	            value="&nbsp;&nbsp;总部核对&nbsp;&nbsp;">&nbsp;&nbsp;    
         </c:if>
       
		<input
            type="button" name="ba" class="button_class"
            onclick="javascript:history.go(-1)"
            value="&nbsp;&nbsp;返 回&nbsp;&nbsp;"></div>
	</p:button>

	<p:message2 />
</p:body></form>
</body>
</html>

