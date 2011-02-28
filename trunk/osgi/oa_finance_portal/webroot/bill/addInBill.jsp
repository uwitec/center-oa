<%@ page contentType="text/html;charset=UTF-8" language="java"
	errorPage="../common/error.jsp"%>
<%@include file="../common/common.jsp"%>
<html>
<head>
<p:link title="增加收款单" />
<script language="JavaScript" src="../js/JCheck.js"></script>
<script language="JavaScript" src="../js/common.js"></script>
<script language="JavaScript" src="../js/public.js"></script>
<script language="javascript">
function addBean()
{
	submit('确定增加收款单?');
}

function selectCus()
{
    window.common.modal('../customer/customer.do?method=rptQueryAllCustomer&load=1');
}

function getCustomer(obj)
{
    $O('customerId').value = obj.value;
    $O('customerName').value = obj.pname;
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
</script>

</head>
<body class="body_class">
<form name="formEntry" action="../finance/bill.do" method="post">
<input type="hidden" name="method" value="addInBill">
<input type="hidden" name="customerId" value="">
<input type="hidden" name="ownerId" value="">

<p:navigation
	height="22">
	<td width="550" class="navigation"><span style="cursor: pointer;"
		onclick="javascript:history.go(-1)">收款单管理</span> &gt;&gt; 增加收款单</td>
	<td width="85"></td>
</p:navigation> <br>

<p:body width="98%">

	<p:title>
		<td class="caption"><strong>收款单基本信息：</strong></td>
	</p:title>

	<p:line flag="0" />

	<p:subBody width="100%">
		<p:class value="com.china.center.oa.finance.bean.InBillBean" />

		<p:table cells="1">

			<p:pro field="bankId" innerString="style='width: 300px'">
			    <option value="">--</option> 
                <p:option type="bankList"/>
            </p:pro>
			
			<p:pro field="type">
				<p:option type="inbillType"/>
			</p:pro>
            
            <!--  
            <p:pro field="customerId" innerString="size=60 style='display: none'">
                <input type="button" value="&nbsp;选 择&nbsp;" name="qout1" id="qout1"
                    class="button_class" onclick="selectCus()">&nbsp;
            </p:pro>
            
            <p:pro field="ownerId" innerString="size=60 style='display: none'">
                <input type="button" value="&nbsp;选 择&nbsp;" name="qout2" id="qout2"
                    class="button_class" onclick="selectStaffer()">&nbsp;
                <input type="button" value="&nbsp;清 空&nbsp;" name="qout3" id="qout3"
                        class="button_class" onclick="clears()">&nbsp;
            </p:pro>
            
            -->
            
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

