<%@ page contentType="text/html;charset=UTF-8" language="java"
	errorPage="../common/error.jsp"%>
<%@include file="../common/common.jsp"%>
<html>
<head>
<p:link title="增加付款单" />
<script language="JavaScript" src="../js/JCheck.js"></script>
<script language="JavaScript" src="../js/common.js"></script>
<script language="JavaScript" src="../js/public.js"></script>
<script language="javascript">
function addBean()
{
	submit('确定增加付款单?');
}

function selectCus()
{
    window.common.modal('../provider/provider.do?method=rptQueryProvider&load=1');
}

function selectStockItem()
{
	if ($$('type') != 0)
	{
		alert('请选择采购付款');
        
        return false;
	}
	
	if ($O('provideId').value == '')
    {
        alert('请选择供应商');
        
        return false;
    }
    
    window.common.modal('../stock/stock.do?method=rptQueryStockItem&load=1&providerId=' + $$('provideId'));
}

function getProvider(id, name)
{
    $O('provideId').value = id;
    $O('provideName').value = name;
}

function selectStaffer()
{
    window.common.modal('../admin/pop.do?method=rptQueryStaffer&load=1&selectMode=1');
}

function getStaffers(oo)
{
    var obj = oo[0];
    
    $O('ownerId').value = obj.value;
    $O('ownerName').value = obj.pname;
}

function getStockItem(oo)
{
    var obj = oo[0];
    
    $O('stockItemId').value = obj.value;
    $O('stockId').value = obj.pid;
    $O('moneys').value = obj.ptotal;
}

function changeAll()
{
    $O('moneys').value = '';
    
    if ($$('type') == 0)
    {
    	$O('moneys').readOnly = false;
    }
    else
    {
    	$O('moneys').readOnly = false;
    }
}
</script>

</head>
<body class="body_class" onload="changeAll()">
<form name="formEntry" action="../finance/bill.do" method="post">
<input type="hidden" name="method" value="addOutBill">
<input type="hidden" name="provideId" value="">
<input type="hidden" name="ownerId" value="">
<input type="hidden" name="stockId" value="">

<p:navigation
	height="22">
	<td width="550" class="navigation"><span style="cursor: pointer;"
		onclick="javascript:history.go(-1)">付款单管理</span> &gt;&gt; 增加付款单</td>
	<td width="85"></td>
</p:navigation> <br>

<p:body width="98%">

	<p:title>
		<td class="caption"><strong>付款单基本信息：</strong></td>
	</p:title>

	<p:line flag="0" />

	<p:subBody width="100%">
		<p:class value="com.china.center.oa.finance.bean.OutBillBean" />

		<p:table cells="1">

			<p:pro field="bankId" innerString="style='width: 300px'">
			    <option value="">--</option> 
                <p:option type="bankList"/>
            </p:pro>
            
            <p:pro field="payType">
                <p:option type="outbillPayType"/>
            </p:pro>
			
			<p:pro field="type" innerString="onchange=changeAll()">
				<p:option type="outbillType"/>
			</p:pro>
			
			<p:pro field="invoiceId" innerString="style='width: 300px'">
			    <option value="">--</option>
			    <c:forEach items="${invoiceList}" var="item">
			     <option value="${item.id}">${item.fullName}</option>
			    </c:forEach>
            </p:pro>

            <p:pro field="provideId" innerString="size=60">
                <input type="button" value="&nbsp;选 择&nbsp;" name="qout1" id="qout1"
                    class="button_class" onclick="selectCus()">&nbsp;
            </p:pro>
            
            <p:pro field="ownerId" innerString="size=60">
                <input type="button" value="&nbsp;选 择&nbsp;" name="qout2" id="qout2"
                    class="button_class" onclick="selectStaffer()">&nbsp;
                <input type="button" value="&nbsp;清 空&nbsp;" name="qout3" id="qout3"
                        class="button_class" onclick="clears()">&nbsp;
            </p:pro>
            
            <p:pro field="moneys"/>

			<p:pro field="description" cell="0" innerString="rows=3 cols=55" />

		</p:table>
	</p:subBody>

	<p:line flag="1" />

	<p:button leftWidth="100%" rightWidth="0%">
		<div align="right"><input type="button" class="button_class" id="ok_b"
			style="cursor: pointer" value="&nbsp;&nbsp;确 定&nbsp;&nbsp;"
			onclick="addBean()"></div>
	</p:button>

	<p:message/>
</p:body></form>
</body>
</html>

