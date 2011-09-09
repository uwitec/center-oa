<%@ page contentType="text/html;charset=UTF-8" language="java"
	errorPage="../common/error.jsp"%>
<%@include file="../common/common.jsp"%>
<html>
<head>
<p:link title="差旅费申请" guid="true"/>
<script language="JavaScript" src="../js/common.js"></script>
<script language="JavaScript" src="../js/public.js"></script>
<script language="JavaScript" src="../js/key.js"></script>
<script language="JavaScript" src="../js/JCheck.js"></script>
<script language="JavaScript" src="../tcp_js/travelApply.js"></script>
<script language="javascript">
function load()
{
    showFlowLogTr();
}

function processBean(opr)
{
    $("input[name='oprType']").val(opr);
    
    var msg = '';
    
    if ("0" == opr)
    {
        msg = '确定通过差旅费申请?';
    }
    
    if ("1" == opr)
    {
        msg = '确定驳回到差旅费申请到初始?';
    }
    
    if ("2" == opr)
    {
        msg = '确定驳回到差旅费申请到上一步?';
    }
    
    if ($O('processer'))
    {
	    if ("0" != opr)
	    {
	        $O('processer').oncheck = '';
	    }
	    else
	    {
	        $O('processer').oncheck = 'notNone';
	    }
    }
    
    submit(msg, null, null);
}

var showTr = false;

function showFlowLogTr()
{
    $v('flowLogTr', showTr);
    
    showTr = !showTr;
}

</script>
</head>

<body class="body_class" onload="load()">
<form name="formEntry" action="../tcp/apply.do"  method="post">
<input type="hidden" name="method" value="processTravelApplyBean"> 
<input type="hidden" name="oprType" value="0"> 
<input type="hidden" name="processId" value=""> 
<input type="hidden" name="id" value="${bean.id}"> 

<p:navigation height="22">
	<td width="550" class="navigation"><span style="cursor: pointer;"
        onclick="javascript:history.go(-1)">待我处理</span> &gt;&gt; 差旅费申请处理</td>
    <td width="85"></td>
</p:navigation> <br>

<p:body width="100%">

	<p:title>
		<td class="caption">
		 <strong>出差申请及借款</strong>
		</td>
	</p:title>

	<p:line flag="0" />

	<p:subBody width="98%">
	
	    <p:class value="com.china.center.oa.tcp.bean.TravelApplyBean" opr="2"/>
	    
		<p:table cells="2">

            <p:pro field="stafferId" value="${g_stafferBean.name}"/>
            <p:pro field="departmentId" value="${g_stafferBean.principalshipName}"/>
            
            <p:pro field="name"/>
            
            <p:pro field="status">
                <p:option type="tcpStatus"></p:option>
            </p:pro>
            
            <p:pro field="beginDate"/>
            <p:pro field="endDate"/>
            
            <p:pro field="srcCity" innerString="onclick='selectCity(this)' style='cursor: pointer;'"/>
            <p:pro field="destCity" innerString="onclick='selectCity(this)' style='cursor: pointer;'"/>
            
            <p:pro field="borrow" cell="0" innerString="onchange='borrowChange()'">
                <p:option type="travelApplyBorrow"></p:option>
            </p:pro>
            
            <p:pro field="showTotal"/>
            <p:pro field="showBorrowTotal"/>

            <p:pro field="description" cell="0" innerString="rows=4 cols=55" />
            
            <p:cell title="附件" width="8" end="true">
            <c:forEach items="${bean.attachmentList}" var="item">
            <a href="../tcp/apply.do?method=downAttachmentFile&id=${item.id}" title="点击下载附件">${item.name}</a>
            <br>
            <br>
            </c:forEach>
            </p:cell>
            
            <p:cell title="处理人" width="8" end="true">
            ${bean.processer}
            </p:cell>

        </p:table>
	</p:subBody>
	
	<p:title>
        <td class="caption">
         <strong>差旅费明细</strong>
        </td>
    </p:title>

    <p:line flag="0" />

    <p:subBody width="98%">
    
        <p:table cells="2" id="traTable">

            <p:pro field="showAirplaneCharges"/>
            <p:pro field="showTrainCharges"/>
            
            <p:pro field="showBusCharges"/>
            <p:pro field="showHotelCharges"/>
            
            <p:pro field="showEntertainCharges"/>
            <p:pro field="showAllowanceCharges"/>
            
            <p:pro field="showOther1Charges"/>
            <p:pro field="showOther2Charges"/>

        </p:table>
    </p:subBody>
	
    <p:title>
        <td class="caption">
         <strong>其他申请费用明细(包含差旅费)</strong>
        </td>
    </p:title>

    <p:line flag="0" />
	
	<tr>
        <td colspan='2' align='center'>
        <table width="98%" border="0" cellpadding="0" cellspacing="0"
            class="border">
            <tr>
                <td>
                <table width="100%" border="0" cellspacing='1' id="tables">
                    <tr align="center" class="content0">
                        <td width="15%" align="center">开始日期</td>
                        <td width="15%" align="center">结束日期</td>
                        <td width="15%" align="center">预算项</td>
                        <td width="10%" align="center">申请金额</td>
                        <td width="40%" align="center">备注</td>
                    </tr>
                    <c:forEach items="${bean.itemVOList}" var="item">
                    <tr align="center" class="content1">
                        <td width="15%" align="center">${item.beginDate}</td>
                        <td width="15%" align="center">${item.endDate}</td>
                        <td width="15%" align="center">${item.feeItemName}</td>
                        <td width="10%" align="center">${my:formatNum(item.moneys / 100.0)}</td>
                        <td width="40%" align="center"><c:out value="${item.description}"/></td>
                    </tr>
                    </c:forEach>
                </table>
                </td>
            </tr>
        </table>
        </td>
    </tr>

	<p:title>
        <td class="caption">
         <strong>收款明细</strong>
        </td>
    </p:title>

    <p:line flag="0" />
    
    <tr id="pay_main_tr">
        <td colspan='2' align='center'>
        <table width="98%" border="0" cellpadding="0" cellspacing="0"
            class="border">
            <tr>
                <td>
                <table width="100%" border="0" cellspacing='1' id="tables_pay">
                    <tr align="center" class="content0">
                        <td width="10%" align="center">收款方式</td>
                        <td width="15%" align="center">开户银行</td>
                        <td width="15%" align="center">户名</td>
                        <td width="20%" align="center">收款帐号</td>
                        <td width="10%" align="center">收款金额</td>
                        <td width="25%" align="center">备注</td>
                    </tr>
                    <c:forEach items="${bean.payList}" var="item">
                    <tr align="center" class="content1">
                        <td align="center">${my:get('travelApplyReceiveType', item.receiveType)}</td>
                        <td align="center">${item.bankName}</td>
                        <td align="center">${item.userName}</td>
                        <td align="center">${item.bankNo}</td>
                        <td align="center">${my:formatNum(item.moneys / 100.0)}</td>
                        <td align="center"><c:out value="${item.description}"/></td>
                    </tr>
                    </c:forEach>
                </table>
                </td>
            </tr>
        </table>

        </td>
    </tr>
    
    <p:title>
        <td class="caption">
         <strong>费用分担</strong>
        </td>
    </p:title>

    <p:line flag="0" />
    
    <tr>
        <td colspan='2' align='center'>
        <table width="98%" border="0" cellpadding="0" cellspacing="0"
            class="border">
            <tr>
                <td>
                <table width="100%" border="0" cellspacing='1' id="tables_share">
                    <tr align="center" class="content0">
                        <td width="35%" align="center">月度预算</td>
                        <td width="35%" align="center">部门</td>
                        <td width="15%" align="center">权签人</td>
                        <td width="10%" align="center">分担比例(%)</td>
                    </tr>
                    <c:forEach items="${bean.shareVOList}" var="item">
                    <tr align="center" class="content1">
                        <td align="center">${item.budgetName}</td>
                        <td align="center">${item.departmentName}</td>
                        <td align="center">${item.approverName}</td>
                        <td align="center">${item.ratio}</td>
                    </tr>
                    </c:forEach>
                </table>
                </td>
            </tr>
        </table>

        </td>
    </tr>
    
    <p:title>
        <td class="caption">
         <strong><span style="cursor: pointer;" onclick="showFlowLogTr()">流程日志(点击查看)</span></strong>
        </td>
    </p:title>

    <p:line flag="0" />
    
    <tr id="flowLogTr">
        <td colspan='2' align='center'>
        <table width="98%" border="0" cellpadding="0" cellspacing="0"
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

                            <td  align="center">${item.oprModeName}</td>

                            <td  align="center">${item.preStatusName}</td>

                            <td  align="center">${item.afterStatusName}</td>

                            <td  align="center">${item.description}</td>

                            <td  align="center">${item.logTime}</td>

                        </tr>
                    </c:forEach>
                </table>
                </td>
            </tr>
        </table>

        </td>
    </tr>
    
    <p:title>
        <td class="caption">
         <strong>审核</strong>
        </td>
    </p:title>

    <p:line flag="0" />
    
    <tr>
        <td colspan='2' align='center'>
        <table width="98%" border="0" cellpadding="0" cellspacing="0"
            class="border">
            <tr>
                <td>
                <table width="100%" border="0" cellspacing='1' id="tables_pay">
                    <tr align="center" class="content0">
                        <td width="15%" align="center">审批意见</td>
                        <td align="left">
                        <textarea rows="3" cols="55" oncheck="notNone;maxLength(200);" name="reason"></textarea>
                        <font color="red">*</font>
                        </td>
                    </tr>
                    <c:if test="${requestScope.pluginType == 'group'}">
                    <tr align="center" class="content1">
                        <td width="15%" align="center">提交到</td>
                        <td align="left">
                        <input type="text" name="processer" readonly="readonly" oncheck="notNone" head="下环处理人"/>&nbsp;
                        <font color=red>*</font>
                        <input type="button" value="&nbsp;...&nbsp;" name="qout" id="qout"
                            class="button_class" onclick="selectNext('${pluginType}', '${pluginValue}')">&nbsp;&nbsp;
                        </td>
                    </tr>
                    </c:if>
                </table>
                </td>
            </tr>
        </table>

        </td>
    </tr>
    
    <p:line flag="1" />
    
	<p:button leftWidth="98%" rightWidth="0%">
        <div align="right">
        <input type="button" class="button_class" id="sub_b1"
            value="&nbsp;&nbsp;通 过&nbsp;&nbsp;" onclick="processBean(0)">
          &nbsp;&nbsp;
          <c:if test="${token.reject == 1}">
          <input type="button" class="button_class" id="sub_b2"
            value="&nbsp;&nbsp;驳回到初始&nbsp;&nbsp;" onclick="processBean(1)">
          </c:if>
          <c:if test="${token.rejectToPre == 1}">
          <input type="button" class="button_class" id="sub_b2"
            value="&nbsp;&nbsp;驳回到上一步&nbsp;&nbsp;" onclick="processBean(2)">
          </c:if>
        </div>
    </p:button>
	
	<p:message2/>
</p:body>
</form>
</body>
</html>

