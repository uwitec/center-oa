<%@ page contentType="text/html;charset=GBK" language="java"
    errorPage="./error.jsp"%>
<%@include file="./common.jsp"%>

<html>
<head>
<p:link title="增加收付款" />

<script language="JavaScript" src="../js/prototype.js"></script>
<script language="JavaScript" src="../js/common.js"></script>
<script language="JavaScript" src="../js/public.js"></script>
<script language="JavaScript" src="../js/JCheck.js"></script>
<script language="javascript">
var fla = "1";
var mess;
function selectCustomer()
{
    window.common.modal("./out.do?method=queryAllCustomer&flagg=" + fla);
}

function selectProvider()
{
    window.common.modal("./out.do?method=queryProvider");
}

function selectOut()
{
    window.common.modal('../admin/out.do?method=queryOut3&load=1&flag=3');
}

function selectBill()
{
    window.common.modal('../admin/bill.do?method=queryBillForRpt');
}

function getOutId(id)
{
    $('outId').value = id;
}

function change()
{
    <c:if test='${rivet != "1"}'>
    var type = $F('type');

    var obj = $('billType');

    removeAllItem(obj);

    //收款单
    if (type == "0")
    {
        $('inout').innerText = '收款金额';
        $('mm').innerText = '收款日期';
        mess = '确定提交收款单?';

        fla = type;

        setOption(obj, '0', '单据收款');
        setOption(obj, '1', '预收款');
        setOption(obj, '2', '区域内转账');
        setOption(obj, '3', '跨区域收账');
    }
    else
    {
        $('inout').innerText = '付款金额';
        $('mm').innerText = '付款日期';
        mess = '确定提交付款单?';

        fla = type;

        setOption(obj, '0', '普通付款');
        setOption(obj, '1', '报销');
        setOption(obj, '4', '跨区域付账');
    }

    changeSec();
    </c:if>

    dirBank();
}

function dirBank()
{
    var type = $F('type');

    var billType = $F('billType');

    if (type == '0' && billType == '2')
    {
        $('dirBankTr').style.display = 'block';
        $d('dirBanks', false);
    }
    else
    {
        $('dirBankTr').style.display = 'none';
        $d('dirBanks', true);
    }

    if ( billType == '3')
    {
        $d('qout1', false);
        $d('refBillId', false);
    }
    else
    {
        $d('qout1', true);
        $d('refBillId', true);
        $('refBillId').value = '';
    }

    //跨区域付账
    if (type == '1' && billType == '4')
    {
        $('dirBankTr1').style.display = 'block';
        $d('destBank', false);
    }
    else
    {
        $('dirBankTr1').style.display = 'none';
        $d('destBank', true);
    }
}

function changeSec()
{
    var type = $F('type');
    var billType = $F('billType');

    dirBank();

    $d('type', false);

    if (type == '1' && billType == '1')
    {
        disCustomerName(true);
        $('customerName').value = '报销';
        $('customerId').value = '';
        return;
    }
    else if ((type == '1' && (billType == '2' || billType == '3')) ||  billType == '4')
    {
        disCustomerName(true);
        $('customerName').value = '转账';
        $('customerId').value = '';
        $d('qout', true);
        return;
    }
    else
    {
        disCustomerName(false);
    }

    if (type == '0' && billType == '0')
    {
        $d('outId', false);
        $d('qout', false);
        return;
    }
    else if ((type == '0' && (billType == '2' || billType == '3')) ||  billType == '4')
    {
        disCustomerName(true);
        $d('qout', true);
        $('customerName').value = '转账';
        $('customerId').value = '';
        $d('type');
        return;
    }
    else
    {
        $d('outId', true);
        $d('qout', true);
        $('customerName').value = '';
        $('customerId').value = '';
        return;
    }
}

function disCustomerName(boo)
{
    $d('customerName', boo);
    $d('qout11', boo);
    $d('qout12', boo);
}

function getCustmeor(id, name, conn, phone)
{
    $('mtype').value = 1;
    $("customerName").value = name;
    $("customerId").value = id;
}

function getProviderF(id, name, conn, phone)
{
    $('mtype').value = 2;
    $("customerName").value = name;
    $("customerId").value = id;
}

function addBillss()
{
    if (formCheck())
    {
        var billType = $F('billType');

        if ( billType == '3')
        {
            if (!window.confirm('您当前操作是跨区域收款，确认银行和收款金额与相关的单据符合?'))
            {
                return;
            }
        }

        if (window.confirm('确定提交收付款单?'))
        addBill.submit();
    }
}

function getBill(obj)
{
    $('refBillId').value = obj.value;
    $('moneys').value = obj.money;

    setSelect($('bank'), obj.destBank);
}

</script>

</head>
<body class="body_class" onload="change()">
<form name="addBill" action="../admin/bill.do" method="post"><input
    type="hidden" name="method" value="addBill">
    <input
    type="hidden" name="customerId" value="${customerId}">
    <input
    type="hidden" name="mtype" value="0">
    <input
    type="hidden" name="pay" value="${pay}">
<table width="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" valign="bottom">
        <table width="100%" height="22" border="0" cellpadding="0"
            cellspacing="0">
            <tr valign="middle">
                <td width="8"></td>
                <td width="30">
                <div align="center"><img src="../images/dot_a.gif" width="9"
                    height="9"></div>
                </td>
                <td width="550" class="navigation">收付款管理 &gt;&gt; <span
                    style="cursor: hand" onclick="javascript:history.go(-1)">查询收付款单</span>
                &gt;&gt; 增加收付款单</td>
                <td width="85"></td>
            </tr>
        </table>
        </td>
    </tr>

    <tr>
        <td height="6" valign="top">
        <table width="100%" border="0" cellpadding="0" cellspacing="0">
            <!--DWLayoutTable-->
            <tr>
                <td width="8" height="6"
                    background="../images/index_sp_welcome_center_10.gif"><img
                    src="../images/index_sp_welcome_center_07.gif" width="8" height="6"></td>
                <td width="190"
                    background="../images/index_sp_welcome_center_08.gif"></td>
                <td width="486"
                    background="../images/index_sp_welcome_center_10.gif"></td>
                <td align="right"
                    background="../images/index_sp_welcome_center_10.gif">
                <div align="right"><img
                    src="../images/index_sp_welcome_center_12.gif" width="23"
                    height="6"></div>
                </td>
            </tr>
        </table>
        </td>
    </tr>
</table>

<br>
<table width="98%" border="0" cellpadding="0" cellspacing="0"
    align="center">
    <tr>
        <td valign="top" colspan='2'>
        <table width="100%" border="0" cellpadding="0" cellspacing="0">
            <!--DWLayoutTable-->
            <tr>
                <td width="784" height="6"></td>
            </tr>
            <tr>
                <td align="center" valign="top">
                <div align="left">
                <table width="90%" border="0" cellspacing="2">
                    <tr>
                        <td>
                        <table width="100%" border="0" cellpadding="0" cellspacing="10">
                            <tr>
                                <td width="15">&nbsp;</td>
                                <td width="6"><img src="../images/dot_r.gif" width="6"
                                    height="6"></td>
                                <td class="caption"><strong>单据信息:</strong></td>
                            </tr>
                        </table>
                        </td>
                    </tr>
                </table>
                </td>
            </tr>
        </table>
        </td>
    </tr>


    <tr>
        <td background="../images/dot_line.gif" colspan='2'></td>
    </tr>

    <tr>
        <td height="10" colspan='2'></td>
    </tr>

    <tr>
        <td colspan='2' align='center'>
        <table width="100%" border="0" cellpadding="0" cellspacing="0"
            class="border">
            <tr>
                <td>
                <table width="100%" border="0" cellspacing='1'>
                    <tr class="content2">
                        <td width="15%">帐户:</td>
                        <td width="35%" colspan="3"><select name="bank"
                            class="select_class2" oncheck="notNone">
                            <option value="">--</option>
                            <c:forEach items="${bankList}" var="item">
                                <option value="${item.id}">${item.name}</option>
                            </c:forEach>
                        </select><font color="#FF0000">*</font></td>
                    </tr>

                    <tr class="content1">
                        <td width="15%">单据类型:</td>
                        <td width="35%"><select name="type" class="select_class"
                            oncheck="notNone" onchange="change()">
                            <option value="0">收款单</option>
                            <c:if test='${rivet != "1"}'>
                                <option value="1">付款单</option>
                            </c:if>
                        </select><font color="#FF0000">*</font></td>

                        <td width="15%">类型:</td>
                        <td width="35%"><select name="billType" class="select_class"
                            oncheck="notNone" onchange="changeSec()">
                            <option value="0">单据收款</option>
                            <c:if test='${rivet != "1"}'>
                            <option value="1">预收款</option>
                            </c:if>
                        </select> <font color="#FF0000">*</font></td>
                    </tr>

                    <tr class="content2" id="dirBankTr">
                        <td width="15%">目的帐户:</td>
                        <td width="35%" colspan="3"><select name="dirBanks"
                            class="select_class2" oncheck="notNone;noEquals($F('bank'))">
                            <option value="">--</option>
                            <c:forEach items="${bankList}" var="item">
                                <option value="${item.id}">${item.name}</option>
                            </c:forEach>
                        </select><font color="#FF0000">*</font></td>
                    </tr>

                    <tr class="content2" id="dirBankTr1">
                        <td width="15%">目的帐户:</td>
                        <td width="35%" colspan="3"><select name="destBank"
                            class="select_class2" oncheck="notNone;noEquals($F('bank'))">
                            <option value="">--</option>
                            <c:forEach items="${bankList1}" var="item">
                                <option value="${item.id}">${item.name}</option>
                            </c:forEach>
                        </select><font color="#FF0000">*</font></td>
                    </tr>

                    <tr class="content2">
                        <td width="15%">库单号:</td>
                        <td width="35%"><input type="text" name="outId" value="${outId}"
                            readonly="readonly" maxlength="50">&nbsp;&nbsp;<input
                            type="button" value="&nbsp;...&nbsp;" name="qout" class="button_class" onclick="selectOut()"></td>

                        <td width="15%">跨区域单据:</td>
                        <td width="35%"><input type="text" name="refBillId" value="" oncheck="notNone;" message="请选择跨区域单据"
                            readonly="readonly" maxlength="50">&nbsp;&nbsp;<input
                            type="button" value="&nbsp;...&nbsp;" name="qout1" class="button_class" onclick="selectBill()"></td>
                    </tr>

                    <tr class="content1">
                        <td width="15%" id="inout">收款金额:</td>
                        <td width="35%"><input type="text" name="moneys"
                            oncheck="notNone;isFloat" maxlength="50"><font
                            color="#FF0000">*</font></td>

                        <td width="15%" id="cust">客户/供应商:</td>
                        <td width="35%"><input type="text" name="customerName" value="${customerName}"
                            oncheck="notNone" maxlength="50"
                             readonly="readonly">&nbsp;&nbsp;<input
                            type="button" value="&nbsp;客户&nbsp;" name="qout11" class="button_class" onclick="selectCustomer()">&nbsp;&nbsp;<input
                            type="button" value="&nbsp;供应商&nbsp;" name="qout12" class="button_class" onclick="selectProvider()"><font
                            color="#FF0000">*</font></td>
                    </tr>

                    <tr class="content2">
                        <td width="15%" id="inout">经办人:</td>
                        <td width="35%"><input type="text" name="stafferName"
                            readonly="readonly" value="${user.stafferName}" maxlength="50"><font
                            color="#FF0000">*</font></td>
                        <td width="15%" id="mm">收款日期:</td>
                        <td width="35%">
                        <input type="text" name="date" size="20" value="${current}" oncheck="notNone"/>
                        <font color="#FF0000">*</font></td>
                    </tr>

                    <tr class="content1">

                        <td width="15%" id="cust">备注:</td>
                        <td width="35%" colspan="3"><textarea rows="3" cols="55"
                            name="description">${description}</textarea></td>
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
        <td background="../images/dot_line.gif" colspan='2'></td>
    </tr>

    <tr>
        <td height="10" colspan='2'></td>
    </tr>

    <tr>
        <td width="100%">
        <div align="right"><input type="button" class="button_class"
            value="&nbsp;&nbsp;确 定&nbsp;&nbsp;" onclick="addBillss()">&nbsp;
        <input name="cancel" type="reset" class="button_class"
            value="&nbsp;&nbsp;重 置&nbsp;&nbsp;">&nbsp; <input
            type="button" class="button_class"
            onclick="javascript:history.go(-1)"
            value="&nbsp;&nbsp;返 回&nbsp;&nbsp;"></div>
        </td>
        <td width="0%"></td>
    </tr>

    <tr height="10">
        <td height="10" colspan='2'></td>
    </tr>

    <tr>
        <td colspan='2' align="center"><FONT color="blue">${MESSAGE_INFO}</FONT><FONT
            color="red">${errorInfo}</FONT></td>
        <c:remove var="MESSAGE_INFO" scope="request"/>
        <c:remove var="errorInfo" scope="request"/>
        <c:remove var="MESSAGE_INFO" scope="session"/>
        <c:remove var="errorInfo" scope="session"/>
    </tr>


</table>


</form>
</body>
</html>

