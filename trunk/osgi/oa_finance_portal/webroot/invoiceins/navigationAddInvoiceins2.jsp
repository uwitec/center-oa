<%@ page contentType="text/html;charset=UTF-8" language="java"
	errorPage="../common/error.jsp"%>
<%@include file="../common/common.jsp"%>
<html>
<head>
<p:link title="增加发票" />
<script language="JavaScript" src="../js/JCheck.js"></script>
<script language="JavaScript" src="../js/common.js"></script>
<script language="JavaScript" src="../js/public.js"></script>
<script language="JavaScript" src="../js/json.js"></script>
<script language="javascript">

var invoicesJSON = JSON.parse('${invoicesJSON}');
var vsJSON = JSON.parse('${vsJSON}');

var invMap = {};
var invFullMap = {};
<c:forEach items="${invoiceList}" var="item">
  invFullMap['${item.id}'] = '${item.fullName}';
</c:forEach>

<c:forEach items="${dutyList}" var="item">
  invMap['${item.id}'] = '${item.type}';
</c:forEach>

function addBean()
{
	submit('确定申请增加发票?', null, check);
}

function check()
{
    var totalArr = document.getElementsByName('total');

    var totals = 0.0;
    
    for (var i = 0; i < totalArr.length; i++)
    {
        totals += parseFloat(totalArr[i].value);        
    }
    
    $O('hasMoney').value = totals;
    
    if (totals > parseFloat($$('mayMoney')))
    {
        alert('开票金额不能大于单据的可开票金额');
        
        return false;
    }
    
    return true;
}

function loadShow()
{
    
}

</script>

</head>
<body class="body_class" onload="loadShow()">
<form name="formEntry" action="../finance/invoiceins.do" method="post">
<input type="hidden" name="method" value="addInvoiceinsInNavigation"> 
<input type="hidden" name="customerId" value=""> 
<input type="hidden" name="type" value="0"> 
<input type="hidden" name="outId" value="${pmap.outId}"> 
<input type="hidden" name="mode" value="0"> 
<p:navigation
	height="22">
	<td width="550" class="navigation"><span style="cursor: pointer;"
		onclick="javascript:history.go(-1)">发票管理</span> &gt;&gt; 开票申请向导(2)</td>
	<td width="85"></td>
</p:navigation> <br>

<p:body width="98%">

	<p:title>
		<td class="caption"><strong>发票基本信息：(关联多个单据默认依次填充单据的可开票金额,如果有委托代销最大开票金额需要人为减去退货金额,否则有误)</strong></td>
	</p:title>

	<p:line flag="0" />

	<p:subBody width="100%">

		<p:class value="com.china.center.oa.finance.bean.InvoiceinsBean" opr="2"/>

		<p:table cells="1">

			<p:pro field="invoiceDate"/>
			
			<p:pro field="unit" innerString="size=60" />
			
			<p:pro field="dutyId" innerString="onchange=loadShow()">
                <p:option type="dutyList" />
            </p:pro>

			<p:pro field="invoiceId" innerString="style='WIDTH: 340px;'">
			    <c:forEach items="${invoiceList}" var="item">
			    <option value="${item.id}">${item.fullName}</option>
			    </c:forEach>
			</p:pro>

			<p:cell title="开票客户" end="true">
                ${pmap.cname}
            </p:cell>
			
			<p:cell title="关联单据" end="true">
			    ${pmap.outId}
            </p:cell>
            
            <p:cell title="金额">
                可开票金额：<input type="text" size="20" readonly="readonly" name="mayMoney" value="${pmap.mayMoney}"> 
                &nbsp;&nbsp;
                当前开票金额：<input type="text" size="20" readonly="readonly" name="hasMoney" value="0.0"> 
            </p:cell>
            
            <p:pro field="processer">
                <p:option type="stafferList" empty="true"/>
            </p:pro>

			<p:pro field="description" cell="0" innerString="rows=3 cols=55" />

		</p:table>

	</p:subBody>

	<p:tr />


	<p:subBody width="100%">

		<p:table cells="1">

			<tr align="center" class="content0">
				<td width="15%" align="center">品名</td>
				<td width="30%" align="center">规格</td>
				<td width="10%" align="center">单位</td>
				<td width="20%" align="center">最大开票金额</td>
				<td width="20%" align="center">开票金额</td>
			</tr>
			
			<c:forEach items="${showList}" var="item">
			<tr align="center" class="content0">
                <td align="center"><input type="text" name="showName" style="width: 100%" value="${item.name}">
                <input type="hidden" name="showId" value="${item.id}"> 
                </td>
                <td align="center"><input type="text" name="special" style="width: 100%" oncheck="notNone;"></td>
                <td align="center"><input type="text" name="sunit" style="width: 100%" oncheck="notNone;"></td>
                <td align="center">
                <input type="text" name="e_total" id="e_total_${vs}" style="width: 100%" readonly="readonly" oncheck="notNone;isFloat2" value="${item.description}">
                </td>
                 <td align="center">
                <input type="text" name="total" style="width: 100%" oncheck="notNone;isFloat2" value="">
                </td>
            </tr>
            </c:forEach>

		</p:table>

	</p:subBody>
	
	<p:line flag="1" />

	<p:button leftWidth="100%" rightWidth="0%">
		<div align="right"><input type="button" class="button_class"
			id="ok_b" style="cursor: pointer" value="&nbsp;&nbsp;确 定&nbsp;&nbsp;"
			onclick="addBean()"></div>
	</p:button>

	<p:message />
</p:body></form>
</body>
</html>

