<%@ page contentType="text/html;charset=UTF-8" language="java"
    errorPage="../common/error.jsp"%>
<%@include file="../common/common.jsp"%>
<html>
<head>
<p:link title="科目余额" cal="true" guid="true"/>
<script language="JavaScript" src="../js/common.js"></script>
<script language="JavaScript" src="../js/public.js"></script>
<script language="JavaScript" src="../js/JCheck.js"></script>
<script language="JavaScript" src="../js/tableSort.js"></script>
<script language="javascript">
function exports()
{
	document.location.href = '../finance/finance.do?method=exportFinanceItem';
}

function load()
{
	loadForm();
	
	bingTable("senfe");
}

function cc()
{
	if (compareDays($$('beginDate'), $$('endDate')) > 90)
	{
		alert('跨度不能大于90天');
		return false;
	}
	
	return true;
}

function query()
{
	submit(null, null, cc);
}

function selectTax()
{
    window.common.modal('../tax/tax.do?method=rptQueryTax&load=1&selectMode=1');
}

function getTax(oos)
{
    var obj = oos[0];
    
    $("input[name='taxId']").val(obj.value);
    $("input[name='taxName']").val(obj.value + ' ' + obj.pname);
}

function selectStaffer()
{
    window.common.modal('../admin/pop.do?method=rptQueryStaffer&load=1&selectMode=1');
}

function getStaffers(oos)
{
    var obj = oos[0];
    
    $("input[name='stafferId']").val(obj.value);
    $("input[name='stafferName']").val(obj.pname);
}

function selectDepartment()
{
    window.common.modal('../admin/org.do?method=popOrg');
}

function setOrgFromPop(id, name, level, pname)
{
    var showName = '';
    
    if (pname)
    showName = pname + '->' + '[' + level + ']' + name;
    else
    showName = '[' + level + ']' + name;
    
    $("input[name='departmentId']").val(id);
    $("input[name='departmentName']").val(showName);
}

function resetAll()
{
    $("input[name='taxId']").val('');
    $("input[name='stafferId']").val('');
    $("input[name='departmentId']").val('');
    $("input[name='taxName']").val('');
    $("input[name='stafferName']").val('');
    $("input[name='departmentName']").val('');
}
</script>

</head>
<body class="body_class" onload="load()">
<form name="formEntry" action="../finance/finance.do">
<input type="hidden" name="method" value="queryTaxFinance2"> 
<input type="hidden" value="1" name="firstLoad">
<input type="hidden" name="taxId" value="${taxId}"> 
<input type="hidden" name="stafferId" value="${stafferId}"> 
<input type="hidden" name="departmentId" value="${departmentId}"> 
<p:navigation
	height="22">
	<td width="550" class="navigation">科目余额</td>
	<td width="85"></td>
</p:navigation> <br>

<p:body width="100%">

	<p:subBody width="98%">
		<table width="100%" align="center" cellspacing='1' class="table0">
			<tr class="content1">
				<td width="15%" align="center">开始日期</td>
				<td align="left">
				<p:plugin name="beginDate" size="20" value="${beginDate}"  oncheck="notNone"/>
				<font color="#FF0000">*</font>
				</td>
				<td width="15%" align="center">结束日期</td>
				<td align="left">
				<p:plugin name="endDate" size="20" value="${endDate}"  oncheck="notNone"/>
				<font color="#FF0000">*</font>
				</td>
			</tr>

			<tr class="content2">
                <td width="15%" align="center">科目</td>
                <td align="left" colspan="1"><input type="text" name="taxName" style="width: 80%" value="${taxName}" oncheck="notNone;" readonly="readonly">
                <input type="button" value="&nbsp;...&nbsp;" name="qout" id="qout"
                    class="button_class" onclick="selectTax()">
                <font color="#FF0000">*</font>
                </td>
                
                <td width="15%" align="center"></td>
                <td align="left" colspan="1"></td>
            </tr>

			<tr class="content1">
				<td colspan="4" align="right"><input type="button" onclick="query()"
					class="button_class" value="&nbsp;&nbsp;查 询&nbsp;&nbsp;">&nbsp;&nbsp;<input
					type="button" class="button_class" onclick="resetAll()"
					value="&nbsp;&nbsp;重 置&nbsp;&nbsp;"></td>
			</tr>
		</table>

	</p:subBody>

	<p:title>
		<td class="caption"><strong>科目余额：</strong></td>
	</p:title>

	<p:line flag="0" />

	<p:subBody width="98%">
		<table width="100%" align="center" cellspacing='1' class="table0" id="senfe">
			<tr align=center class="content0">
				<td align="center" width="8%" class="td_class" onclick="tableSort(this)"><strong>日期</strong></td>
				<td align="center" class="td_class" onclick="tableSort(this)"><strong>凭证</strong></td>
				<td align="center" class="td_class" onclick="tableSort(this)"><strong>科目</strong></td>
				<td align="center" class="td_class" onclick="tableSort(this)"><strong>摘要</strong></td>
				<td align="center" class="td_class" onclick="tableSort(this, true)"><strong>借方金额</strong></td>
				<td align="center" class="td_class" onclick="tableSort(this, true)"><strong>贷方金额</strong></td>
				<td align="center" class="td_class" onclick="tableSort(this)"><strong>借/贷</strong></td>
				<td align="center" class="td_class" onclick="tableSort(this, true)"><strong>余额</strong></td>
				<td align="center" class="td_class" onclick="tableSort(this)"><strong>辅助</strong></td>
			</tr>

			<c:forEach items="${resultList}" var="item"
				varStatus="vs">
				<tr class="${vs.index % 2 == 0 ? 'content1' : 'content2'}">
					<td align="left" width="8%"  onclick="hrefAndSelect(this)">${item.financeDate}</td>
					<td align="left" onclick="hrefAndSelect(this)">
					<a href="../finance/finance.do?method=findFinance&id=${item.pid}">${item.pid}</a>
					</td>
					<td align="left" onclick="hrefAndSelect(this)">${item.taxId} ${item.taxName}</td>
					<td align="left" onclick="hrefAndSelect(this)">${item.description}</td>
					<td align="left" width="8%" onclick="hrefAndSelect(this)" >${item.showInmoney}</td>
					<td align="left" width="8%" onclick="hrefAndSelect(this)">${item.showOutmoney}</td>
					<td align="left" onclick="hrefAndSelect(this)">${item.forwardName}</td>
					<td align="left" width="8%" onclick="hrefAndSelect(this)">${item.showLastmoney}</td>
					<td align="left" onclick="hrefAndSelect(this)">${item.departmentName}/${item.stafferName}/${item.unitName}/${item.productName}/${item.depotName}/${item.duty2Name}</td>
				</tr>
			</c:forEach>
		</table>
		
	</p:subBody>

	<p:line flag="1" />
	
	<p:button leftWidth="98%" rightWidth="2%">
        <div align="right"><input type="button" class="button_class"
            value="&nbsp;&nbsp;导出明细&nbsp;&nbsp;" onclick="exports()">&nbsp;&nbsp;
        </div>
    </p:button>

	<p:message2 />
	
</p:body>
</form>
</body>
</html>

