<%@ page contentType="text/html;charset=UTF-8" language="java"
	errorPage="../common/error.jsp"%>
<%@include file="../common/common.jsp"%>

<html>
<head>
<p:link title="委托代销结算清单" />
<script language="JavaScript" src="../js/common.js"></script>
<script language="JavaScript" src="../js/public.js"></script>
<script language="JavaScript" src="../js/JCheck.js"></script>
<script language="JavaScript" src="../js/key.js"></script>
<script language="JavaScript" src="../js/json.js"></script>
<script language="javascript">
function load()
{
    $detail($O('viewTable'), ['ba']);
}

function addBean()
{
    submit('确定提交委托代销结算?');
}

</script>
</head>
<body class="body_class" onload="load()">
<form name="formBean" method=post action="../sail/out.do">
<p:navigation
    height="22">
    <td width="550" class="navigation">结算清单详细</td>
    <td width="85"></td>
</p:navigation> <br>

<table width="95%" border="0" cellpadding="0" cellspacing="0" id="viewTable"
	align="center">
	
	<p:title>
        <td class="caption"><strong>结算清单基本信息：</strong></td>
    </p:title>

    <p:line flag="0" />
    
	<tr>
		<td colspan='2' align='center'>
		<table width="100%" border="0" cellpadding="0" cellspacing="0" 
			class="border">
			<tr>
				<td colspan="2">
				<table width="100%" border="0" cellspacing='1' id="tables">
					<tr align="center" class="content0">
						<td width="20%" align="center">品名</td>
						<td width="5%" align="center">数量</td>
						<td width="10%" align="center">销售价</td>
						<td width="10%" align="center">成本</td>
						<td width="10%" align="center">已结数量</td>
						<td width="10%" align="center">结算数量</td>
						<td width="10%" align="center">结算单价</td>
					</tr>
					
					<c:forEach items="${baseList}" var="fristBase" varStatus="vs">
                    <tr class="content1">
                        <td align="center">${fristBase.productName}
                        </td>

                        <td align="center">${fristBase.amount}</td>

                        <td align="center">${my:formatNum(fristBase.price)}</td>

                        <td align="center">${fristBase.description}</td>
                        
                        <td align="center">${fristBase.inway}</td>
                        
                        <td align="center"><input type="text" value="${fristBase.unit}"  style="width: 100%" name="amount" ></td>
                        
                        <td align="center"><input type="text" value="${fristBase.showName}"  style="width: 100%" name="price"></td>
                    </tr>
                    </c:forEach>
				</table>
				</td>
			</tr>
			
		</table>

		</td>
	</tr>
	
	<tr>
        <td height="10" colspan='2'></td>
    </tr>
	
	<tr>
        <td colspan='2' align='center'>
        <table width="100%" border="0" cellpadding="0" cellspacing="0" id="mainTable"
            class="border">
            <tr>
                <td>
                <table width="100%" border="0" cellspacing='1'>
                    <tr class="content1">
                        <td width="15%" align="right">退货仓库：</td>

                        <td width="35%">${bean.dirDepotName}</td>

                            <td width="15%" align="right">状态：</td>
                            <td width="35%">${my:get('outBalanceStatus', bean.status)}</td>
                    </tr>
                    
                    <tr class="content2">
                        <td width="15%" align="right">备注：</td>

                        <td width="35%">${bean.description}</td>
                        <td width="15%" align="right">审批意见：</td>
                        <td width="35%">${bean.reason}</td>
                    </tr>

                </table>
                </td>
            </tr>
        </table>

        </td>
    </tr>
    
    <tr>
        <td height="10" colspan='2'></td>
    </tr>
	
	<tr>
        <td colspan='2' align='center'>
        <div id="desc1" style="display: block;">
        <table width="100%" border="0" cellpadding="0" cellspacing="0"
            class="border">
            <tr>
                <td>
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

                            <td  align="center">${my:get('oprMode', item.oprMode)}</td>

                            <td  align="center">${my:get('outBalanceStatus', item.preStatus)}</td>

                            <td  align="center">${my:get('outBalanceStatus', item.afterStatus)}</td>

                            <td  align="center">${item.description}</td>

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

	<tr>
        <td width="100%">
        <div align="right">
    <input
            type="button" name="ba" class="button_class"
            onclick="javascript:history.go(-1)"
            value="&nbsp;&nbsp;返 回&nbsp;&nbsp;">
            </div>
        </td>
        <td width="0%"></td>
    </tr>

</table>
</form>
</body>
</html>

