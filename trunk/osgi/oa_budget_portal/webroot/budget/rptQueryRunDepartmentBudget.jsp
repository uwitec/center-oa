<%@ page contentType="text/html;charset=UTF-8" language="java"
	errorPage="../common/error.jsp"%>
<%@include file="../common/common.jsp"%>
<html>
<head>
<p:link title="部门预算列表" />
<base target="_self">
<script language="JavaScript" src="../js/common.js"></script>
<script language="JavaScript" src="../js/public.js"></script>
<script language="javascript">
function querys()
{
	formEntry.submit();
}

function sures()
{
	add();
    
    closes();
}

function add()
{
    var opener = window.common.opener();
    
    var oo = getCheckBox("beans");
    
    if (oo && oo.length == 0)
    {
        alert('请选择部门预算');
        return;
    }
    
    if (oo)
    opener.getRunDepartmentBudgets(oo);
}

function closes()
{
	opener = null;
	window.close();
}

function load()
{
	loadForm();
}

function press()
{
    window.common.enter(querys);
} 

function closesd()
{
    var opener = window.common.opener();
    
    opener = null;
    window.close();
}

function subBudget(id)
{
    window.common.modal('../budget/budget.do?method=queryReference&pid=' + id);
}

</script>

</head>
<body class="body_class" onload="load()">
<form name="formEntry" action="../budget/budget.do" method="post"><input
	type="hidden" name="method" value="rptQueryRunDepartmentBudget"><input
	type="hidden" value="1" name="load"><input
    type="hidden" value="${selectMode}" name="selectMode"> <p:navigation
	height="22">
	<td width="550" class="navigation">部门当前执行预算</td>
	<td width="85"></td>
</p:navigation> <br>

<p:body width="100%">
	<p:subBody width="90%">
		<table width="100%" align="center" cellspacing='1' class="table0"
			id="result">
			<tr class="content1">
				<td width="15%" align="center">预算名称</td>
				<td align="center"><input type="text" name="name" onkeypress="press()"
					value="${name}"></td>
				<td width="15%" align="center">部门名称</td>
				<td align="center"><input type="text" name="departmentName" onkeypress="press()"
					value="${departmentName}"></td>
			</tr>
			
			<tr class="content2">
                <td width="15%" align="center">权签人</td>
                <td align="center"><input type="text" name="stafferName" onkeypress="press()"
                    value="${stafferName}"></td>
                <td width="15%" align="center"></td>
                <td align="center"></td>
            </tr>

			<tr class="content1">
				<td colspan="4" align="right"><input type="button"
					onclick="querys()" class="button_class"
					value="&nbsp;&nbsp;查 询&nbsp;&nbsp;"></td>
		</table>

	</p:subBody>

	<p:title>
		<td class="caption"><strong>部门预算列表：</strong></td>
	</p:title>

	<p:line flag="0" />

	<p:subBody width="90%">
		<table width="100%" align="center" cellspacing='1' class="table0"
			id="result">
			<tr align=center class="content0">
				<td align="center">选择</td>
				<td align="center"><strong>部门预算</strong></td>
				<td align="center"><strong>权签人</strong></td>
				<td align="center"><strong>预算/使用</strong></td>
				<td align="center"><strong>预算时间</strong></td>
				<td align="center"><strong>预算部门</strong></td>
				<td align="center"><strong>预算项</strong></td>
			</tr>

			<c:forEach items="${beanList}" var="item" varStatus="vs">
				<tr class="${vs.index % 2 == 0 ? 'content1' : 'content2'}">
					<td align="center"><input type='${selectMode == 1 ? "radio" : "checkbox"}' name="beans"
						pname="${item.name}" pdid="${item.budgetDepartment}" pdname='${item.budgetFullDepartmentName}' value="${item.id}"
						psignername="${item.signerName}"
						psignerid="${item.signer}"
						${vs.index == 0 ? 'checked' : ''} /></td>
					<td align="center" onclick="hrefAndSelect(this)">${item.name}</td>
					<td align="center" onclick="hrefAndSelect(this)">${item.signerName}</td>
					<td align="center" onclick="hrefAndSelect(this)">${item.stotal}/${item.srealMonery}</td>
					<td align="center" onclick="hrefAndSelect(this)">${item.beginDate}至${item.endDate}</td>
					<td align="center" onclick="hrefAndSelect(this)">${item.budgetFullDepartmentName}</td>
					<td align="center" onclick="hrefAndSelect(this)">
					<input type="button"
                    onclick="subBudget('${item.id}')" class="button_class"
                    value="&nbsp;预算项明细&nbsp;">
					</td>
				</tr>
			</c:forEach>
		</table>
			
		<p:formTurning form="formEntry" method="rptQueryRunDepartmentBudget"></p:formTurning>

	</p:subBody>

	<p:line flag="1" />

	<p:button leftWidth="100%" rightWidth="0%">
		<div align="right">
		<c:if test="${selectMode != 1}">
		<input type="button" class="button_class" id="adds"
            value="&nbsp;&nbsp;选 择&nbsp;&nbsp;" onClick="add()">&nbsp;&nbsp;
        </c:if>
        <input type="button" class="button_class" id="sure1"
            value="&nbsp;&nbsp;确 定&nbsp;&nbsp;" onClick="sures()" id="sures">&nbsp;&nbsp;<input type="button" class="button_class"
            value="&nbsp;&nbsp;关 闭&nbsp;&nbsp;" onClick="closesd()" id="clo">&nbsp;&nbsp;
		</div>
	</p:button>

	<p:message />

</p:body></form>
</body>
</html>

