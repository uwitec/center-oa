<%@ page contentType="text/html;charset=UTF-8" language="java"
	errorPage="../common/error.jsp"%>
<%@include file="../common/common.jsp"%>
<html>
<head>
<p:link title="增加帐户" guid="true"/>
<script language="JavaScript" src="../js/JCheck.js"></script>
<script language="JavaScript" src="../js/common.js"></script>
<script language="JavaScript" src="../js/public.js"></script>
<script language="javascript">
function addBean()
{
	submit('确定增加帐户?');
}

function selectTax()
{
    window.common.modal('../tax/tax.do?method=rptQueryTax&load=1&selectMode=1');
}

function getTax(oos)
{
    var obj = oos[0];
    
    if (obj.pbottomflag == 1)
    {
        alert('只能选择父级科目');
        
        return false;
    }
    
    $("input[name='parentTaxId']").val(obj.value);
    
    $("input[name='parentTaxName']").val(obj.pname);
}

</script>

</head>
<body class="body_class">
<form name="formEntry" action="../finance/bank.do" method="post">
<input type="hidden" name="method" value="addBank">
<input type="hidden" name="parentTaxId" value="">

<p:navigation
	height="22">
	<td width="550" class="navigation"><span style="cursor: pointer;"
		onclick="javascript:history.go(-1)">帐户管理</span> &gt;&gt; 增加帐户</td>
	<td width="85"></td>
</p:navigation> <br>

<p:body width="98%">

	<p:title>
		<td class="caption"><strong>帐户基本信息：</strong></td>
	</p:title>

	<p:line flag="0" />

	<p:subBody width="100%">
		<p:class value="com.china.center.oa.finance.bean.BankBean" />

		<p:table cells="1">

			<p:pro field="name" innerString="size=60"/>
			
			<p:pro field="code" innerString="size=60"/>
			
			<p:pro field="type">
				<p:option type="bankType"/>
			</p:pro>
			
			<p:pro field="dutyId">
                <p:option type="dutyList"/>
            </p:pro>
            
            <p:cell title="父级科目">
                <input type="text" name="parentTaxName" value="" readonly="readonly" size="60">
                <input type="button" value="&nbsp;...&nbsp;" name="qout" id="qout"
                    class="button_class" onclick="selectTax()">&nbsp;&nbsp;
            </p:cell>
            
            <p:cell title="辅助核算项">
				<input type="checkbox" name="unit" value="1">单位<br>
				<input type="checkbox" name="department" value="1">部门<br>
				<input type="checkbox" name="staffer" value="1">职员
			</p:cell>

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

