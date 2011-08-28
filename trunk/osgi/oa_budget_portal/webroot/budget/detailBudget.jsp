<%@page contentType="text/html; charset=UTF-8"%>
<%@include file="../common/common.jsp"%>
<html>
<head>
<p:link title="修改预算" />
<script language="JavaScript" src="../js/common.js"></script>
<script language="JavaScript" src="../js/public.js"></script>
<script language="JavaScript" src="../js/JCheck.js"></script>
<script language="JavaScript" src="../js/key.js"></script>
<script language="JavaScript" src="../budget_js/budget.js"></script>
<script language="javascript">
function load()
{
    $detail($O('tables'));
}

</script>

</head>
<body class="body_class" onload="load()">
<form name="formEntry" action="../budget/budget.do"><input
	type="hidden" name="method" value="updateBudget"> <input
	type="hidden" name="opr" value="0"> <input type="hidden"
	name="id" value="${bean.id}"> <p:navigation height="22">
	<td width="550" class="navigation">预算详细</td>
	<td width="85"></td>
</p:navigation> <br>

<p:body width="100%">

	<p:title>
		<td class="caption"><strong>预算基本信息：</strong></td>
	</p:title>

	<p:line flag="0" />

	<p:subBody width="98%">
		<p:class value="com.china.center.oa.budget.vo.BudgetVO" opr="2" />

		<p:table cells="2">
			<p:pro field="name" cell="2" innerString="size=60"/>
            
            <p:pro field="type">
            <p:option type="budgetType"></p:option>
            </p:pro>
            <p:pro field="level">
            <p:option type="budgetLevel"></p:option>
            </p:pro>
            
            <p:pro field="year">
                <option value="">--</option>
                <c:forEach begin="2000" end="2100" var="item">
                    <option value="${item}">${item}</option>
                </c:forEach>
            </p:pro>
            
            <p:pro field="status">
                <p:option type="budgetStatus"></p:option>
            </p:pro>
            
             <p:pro field="carryStatus">
                <p:option type="budgetCarry"></p:option>
            </p:pro>
            
            <p:cells celspan="1" title="预算部门">
            ${bean.budgetFullDepartmentName}
            </p:cells>
            
            <p:pro field="beginDate" />
            <p:pro field="endDate" />
            
            <p:pro field="sail" />
            <p:pro field="orgProfit" />
            
            <p:pro field="realProfit" />
            <p:pro field="outSave" />
            
            <p:pro field="outMoney"/>
            <p:pro field="inMoney" />
            
            <p:cell title="预算合计">
               ${my:formatNum(bean.total)}
            </p:cell>
            
            <p:cell title="权签人">
               ${bean.signerName}
            </p:cell>
            
            <p:cells celspan="2" title="提交人">
            ${bean.stafferName}
            </p:cells>
            
            <p:pro field="description" cell="2" innerString="rows=4 cols=60"/>
		</p:table>
	</p:subBody>

	<p:tr></p:tr>

	<tr>
		<td colspan='2' align='center'>
		<table width="98%" border="0" cellpadding="0" cellspacing="0"
			class="border">
			<tr>
				<td>
				<table width="100%" border="0" cellspacing='1' id="tables">
					<tr align="center" class="content0">
						<td width="20%" align="center">预算项</td>
						<td width="15%" align="center">预算金额</td>
						<td width="15%" align="center">使用金额</td>
						<td width="15%" align="center">预用金额</td>
						<td width="50%" align="center">描述</td>
					</tr>

					<c:forEach items="${bean.itemVOs}" var="item" varStatus="vs">
						<tr class="content1" id="trCopy${vs.index}">
							<td width="20%"><input type="text" name="item_name"
								id="f_item_name" maxlength="20" value="${item.feeItemName}" oncheck="notNone" head="预算项名称"
								style="width: 100%;"></td>

							<td width="20%" align="left"><input type="text"
								id="f_item_budget" style="width: 100%;" oncheck="isFloat"
								head="预算金额" value="${item.sbudget}" maxlength="12"
								name="item_budget"></td>
								
							<td width="15%" align="left"><input type="text"
                                id="f_item_budget" style="width: 100%;" oncheck="isFloat"
                                head="预算金额" value="${item.srealMonery}" maxlength="12"
                                name="item_budget"></td>
                                
                            <td width="15%" align="left"><input type="text"
                                id="f_item_budget" style="width: 100%;" oncheck="isFloat"
                                head="预用金额" value="${item.suseMonery}" maxlength="12"
                                name="item_budget"></td>

							<td width="40%" align="left"><textarea
								name="item_description" rows="2" style="width: 100%;"
								id="f_item_description" oncheck="maxLength(200)">${item.description}</textarea></td>
						</tr>
					</c:forEach>
				</table>
				</td>
			</tr>
		</table>

		</td>
	</tr>
	
	<p:tr></p:tr>
	
	<tr>
        <td colspan='2' align='center'>
        <div id="desc1" style="display: block;">
        <table width="98%" border="0" cellpadding="0" cellspacing="0"
            class="border">
            <tr>
                <td>
                <table width="100%" border="0" cellspacing='1' id="tables2">
                    <tr align="center" class="content0">
                        <td width="10%" align="center">操作人</td>
                        <td width="10%" align="center">动作</td>
                        <td width="45%" align="center">日志</td>
                        <td width="15%" align="center">时间</td>
                    </tr>

                    <c:forEach items="${logs}" var="item" varStatus="vs">
                        <tr class='${vs.index % 2 == 0 ? "content1" : "content2"}'>
                            <td align="center">${item.stafferName}</td>

                            <td  align="center">${item.operation}</td>

                            <td  align="center">${item.log}</td>

                            <td  align="center">${item.logTime}</td>

                        </tr>
                    </c:forEach>
                </table>
                </td>
            </tr>
        </table>
        </div>
        </td>
    </tr>

	<p:line flag="1" />

	<p:button leftWidth="100%" rightWidth="0%">
		<div align="right"><input type="button" class="button_class"
			id="ok_b" style="cursor: pointer" value="&nbsp;&nbsp;返 回&nbsp;&nbsp;"
			onclick="javaScript:window.history.go(-1);"></div>
	</p:button>
</p:body></form>
</body>
</html>

