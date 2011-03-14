<%@ page contentType="text/html;charset=UTF-8" language="java"
    errorPage="../common/error.jsp"%>
<%@include file="../common/common.jsp"%>
<html>
<head>
<p:link title="查询销售单" />
<link href="../js/plugin/dialog/css/dialog.css" type="text/css" rel="stylesheet"/>
<script src="../js/title_div.js"></script>
<script src="../js/public.js"></script>
<script src="../js/JCheck.js"></script>
<script src="../js/common.js"></script>
<script src="../js/tableSort.js"></script>
<script src="../js/jquery/jquery.js"></script>
<script src="../js/plugin/dialog/jquery.dialog.js"></script>
<script src="../js/plugin/highlight/jquery.highlight.js"></script>
<script language="javascript">
function detail()
{
	document.location.href = '../sail/out.do?method=findOut&outId=' + getRadioValue("fullId");
}

function pagePrint()
{
	window.open('../sail/out.do?method=findOut&fow=4&outId=' + getRadioValue("fullId"));
}

function exports()
{
	if (window.confirm("确定导出当前的全部查询的库单?"))
	document.location.href = '../sail/out.do?method=export';
}

function comp()
{
	var now = '${now}';

	var str1 = $O('outTime').value;

	var str2 = $O('outTime1').value;

	//必须要有开始和结束时间一个
	if (str1 == '' && str2 == '')
	{
		alert('必须要有开始和结束时间一个');
		return false;
	}

	if (str1 != '' && str2 == '')
	{
		if (!coo(str1, now))
		{
			alert('查询日期跨度不能大于3个月(90天)!');
			return false;
		}

		$O('outTime1').value = now;
	}

	if (str1 == '' && str2 != '')
	{
		if (!coo(now, str2))
		{
			alert('查询日期跨度不能大于3个月(90天)!');
			return false;
		}

		$O('outTime').value = now;
	}

	if (str1 != '' && str2 != '')
	{
		if (!coo(str1, str2))
		{
			alert('查询日期跨度不能大于3个月(90天)!');
			return false;
		}
	}

	return true;
}

function coo(str1, str2)
{
	var s1 = str1.split('-');
	var s2 = str2.split('-');

	var year1 = parseInt(s1[0], 10);

	var year2 = parseInt(s2[0], 10);

	var month1 = parseInt(s1[1], 10);

	var month2 = parseInt(s2[1], 10);

	var day1 = parseInt(s1[2], 10);

	var day2 = parseInt(s2[2], 10);

	return Math.abs((year2 - year1) * 365 + (month2 - month1) * 30 + (day2 - day1)) <= 90;
}

function query()
{
	if (comp())
	{
	    getObj('method').value = 'queryOut';
	    
		adminForm.submit();
	}
}

function res()
{
	$O('customerName').value = '';
	$O("customerId").value = '';
	$O("id").value = '';
	$O("stafferName").value = '';
	setSelectIndex($O('status'), 0);
	setSelectIndex($O('pay'), 0);
	setSelectIndex($O('department'), 0);
}

var jmap = new Object();
<c:forEach items="${listOut1}" var="item">
	jmap['${item.fullId}'] = "${divMap[item.fullId]}";
</c:forEach>

function showDiv(id)
{
	tooltip.showTable(jmap[id]);
}

function load()
{
	loadForm();
	tooltip.init();
	
	highlights($("#mainTable").get(0), ['驳回'], 'red');
}

function payOut()
{
    if (getRadio('fullId').statuss == 1 && getRadio('fullId').pay == 1)
    {
        if (window.confirm("确定此销售单已经收到货款?"))
        {
	        getObj('method').value = 'payOut';
	        
	        getObj('outId').value = getRadioValue("fullId");
	        
	        adminForm.submit();
        }
    }
    else
    {
        alert('不能操作');
    }
}

function payOut2()
{
    if (getRadio('fullId').statuss == 3 && getRadio('fullId').paytype != 1)
    {
        if (window.confirm("确定此销售单已经全部回款?"))
        {
	        getObj('method').value = 'payOut2';
	        
	        getObj('outId').value = getRadioValue("fullId");
	        
	        adminForm.submit();
        }
    }
    else
    {
        alert('不能操作');
    }
}

function fourcePayOut()
{
    if (getRadio('fullId').statuss == 3 && getRadio('fullId').paytype != 1)
    {
        if (window.confirm("确定此销售单已经全部回款(仅仅适用于2011-04-01之前的销售单)?"))
        {
            getObj('method').value = 'fourcePayOut';
            
            getObj('outId').value = getRadioValue("fullId");
            
            adminForm.submit();
        }
    }
    else
    {
        alert('不能操作');
    }
}

function payOut3()
{
    if (getRadio('fullId').statuss == 3 && getRadio('fullId').paytype != 1 && getRadio('fullId').outtype != 1)
    {
        $.messager.prompt('销售单坏账', '请输入销售单坏账金额', getRadio('fullId').baddebts, function(value, isOk){
                if (isOk)
                {
                    if (isFloat(value))
                    {
                        getObj('method').value = 'payOut3';
            
			            getObj('baddebts').value = value;
			            
			            getObj('outId').value = getRadioValue("fullId");
			            
			            adminForm.submit();
                    }
                    else
                    {
                        alert('只能输入金额');
                        $Dbuttons(false);
                    }
                }
                else
                {
                    $Dbuttons(false);
                }
            });
    }
    else
    {
        alert('不能操作');
    }
}

function payOut4()
{
    if (getRadio('fullId').statuss == 4 && getRadio('fullId').paytype == 1 && parseFloat(getRadio('fullId').baddebts) > 0.0 && getRadio('fullId').outtype != 1)
    {
        if (window.confirm("确定取消坏账?"))
        {
            getObj('method').value = 'payOut4';
            
            getObj('outId').value = getRadioValue("fullId");
            
            adminForm.submit();
        }
    }
    else
    {
        alert('不能操作');
    }
}

function centerCheck()
{
    if (getRadio('fullId').statuss == 3 && getRadio('fullId').paytype == 1)
    {
        $.messager.prompt('总部核对', '请核对说明', '', function(r){
                if (r)
                {
                    $Dbuttons(true);
                    getObj('method').value = 'checks';
                    getObj('outId').value = getRadioValue("fullId");
                    getObj('radioIndex').value = $Index('fullId');
        
                    var sss = r;
        
                    getObj('reason').value = r;
        
                    if (!(sss == null || sss == ''))
                    {
                        adminForm.submit();
                    }
                    else
                    {
                        $Dbuttons(false);
                    }
                }
               
            });
    }
    else
    {
        alert('不能操作');
    }
}

var nextStatusMap = {"1" : 99, "2" : 6, "3" : 7, "4" : 3, "6" : 4, "7" : 6};

var oldStatusMap = {"1" : 8, "2" : 1, "3" : 6, "4" : 7, "6" : 3, "7" : 9};

var queryType = "${queryType}";

// 通过销售单
function check()
{
    if (getRadio('fullId').statuss == oldStatusMap[queryType])
    {
        var hi = '';
        
        if (getRadio('fullId').hasmap == "true")
        {
            hi = "注意:当前业务员下有超期的销售单  ";
        }
        
         $.messager.prompt('通过', '请输入意见', '同意', function(r){
                if (r)
                {
                    $Dbuttons(true);
                    
                    getObj('method').value = 'modifyOutStatus';

		            getObj('statuss').value = nextStatusMap[queryType];
		            
		            getObj('oldStatus').value = getRadio('fullId').statuss;
		
		            getObj('outId').value = getRadioValue("fullId");
		
		            getObj('radioIndex').value = $Index('fullId');
        
                    var sss = r;
        
                    getObj('reason').value = r;
        
                    if (!(sss == null || sss == ''))
                    {
                        adminForm.submit();
                    }
                    else
                    {
                        $Dbuttons(false);
                    }
                }
               
            });
    }
    else
    {
        alert('不可以操作!');
    }
}

function reject()
{
    if (getRadio('fullId').statuss == oldStatusMap[queryType])
    {
        $.messager.prompt('驳回', '请输入驳回原因', '', function(r){
                if (r)
                {
                    $Dbuttons(true);
                    getObj('method').value = 'modifyOutStatus';
                    getObj('statuss').value = '2';
                    getObj('oldStatus').value = getRadio('fullId').statuss;
                    getObj('outId').value = getRadioValue("fullId");
        
                    getObj('radioIndex').value = $Index('fullId');
        
                    var sss = r;
        
                    getObj('reason').value = r;
        
                    if (!(sss == null || sss == ''))
                    {
                        adminForm.submit();
                    }
                    else
                    {
                        $Dbuttons(false);
                    }
                }
               
            });
    }
    else
    {
        alert('不可以操作!');
    }
}

function outBack()
{
    //个人领样
    if (getRadio('fullId').statuss == 3 && getRadio('fullId').outtype == 1)
    {
        $l('../sail/out.do?method=findOut&fow=91&outId=' + getRadioValue("fullId"));
    }
    else
    {
        alert('不能操作');
    }
}

function outBack2()
{
    //销售退货
    if ((getRadio('fullId').statuss == 3 || getRadio('fullId').statuss == 4))
    {
        $l('../sail/out.do?method=findOut&fow=92&outId=' + getRadioValue("fullId"));
    }
    else
    {
        alert('不能操作');
    }
}

function applyBackPay()
{
    //申请退款
    if (getRadio('fullId').statuss == 3 || getRadio('fullId').statuss == 4)
    {
        $l('../sail/out.do?method=findOut&fow=93&outId=' + getRadioValue("fullId"));
    }
    else
    {
        alert('不能操作');
    }
}

function swatchToSail()
{
	//个人领样
    if (getRadio('fullId').statuss == 3 && getRadio('fullId').outtype == 1)
    {
    	if (window.confirm("确定领样转销售?"))
        $l('../sail/out.do?method=swatchToSail&outId=' + getRadioValue("fullId"));
    }
    else
    {
        alert('不能操作');
    }
}

</script>

</head>
<body class="body_class" onkeypress="tooltip.bingEsc(event)" onload="load()">
<form action="../sail/out.do" name="adminForm"><input type="hidden"
	value="queryOut" name="method"> <input type="hidden" value="1"
	name="firstLoad">
	<input type="hidden" value="${customerId}"
	name="customerId">
	<input type="hidden" value="${queryType}"
	name="queryType">
	<input type="hidden" value=""
	name="outId">
<input type="hidden" value="" name="oldStatus">
<input type="hidden" value="" name="statuss">
<input type="hidden" value="" name="radioIndex">
<input type="hidden" value="" name="reason">
<input type="hidden" value="" name="baddebts">

<c:set var="fg" value='销售'/>

<p:navigation
    height="22">
    <td width="550" class="navigation">库单管理 &gt;&gt; 查询销售单${queryType}</td>
                <td width="85"></td>
</p:navigation> <br>

<table width="98%" border="0" cellpadding="0" cellspacing="0"
	align="center">
	<tr>
		<td align='center' colspan='2'>
		<table width="100%" border="0" cellpadding="0" cellspacing="0"
			class="border">
			<tr>
				<td>
				<table width="100%" border="0" cellspacing='1'>
					<tr class="content1">
						<td width="15%" align="center">开始时间</td>
						<td align="center" width="35%"><p:plugin name="outTime" size="20" value="${outTime}"/></td>
						<td width="15%" align="center">结束时间</td>
						<td align="center"><p:plugin name="outTime1" size="20" value="${outTime1}"/>
						</td>
					</tr>

					<tr class="content2">
						<td width="15%" align="center">销售单状态</td>
						<td align="center">
						<select name="status" class="select_class" values="${status}">
							<option value="">--</option>
							<p:option type="outStatus"/>
						</select>

						</td>
						<td width="15%" align="center">客户：</td>
						<td align="center"><input type="text" name="customerName" maxlength="14" value="${customerName}"></td>
					</tr>

					<tr class="content1">
						<td width="15%" align="center">销售类型</td>
						<td align="center">
						<select name="outType"
							class="select_class" values=${outType}>
							<option value="">--</option>
							<p:option type="outType_out"></p:option>
						</select>

						</td>
						<td width="15%" align="center">销售单号</td>
						<td align="center"><input type="text" name="id" value="${id}"></td>
					</tr>

					<tr class="content2">
						<td width="15%" align="center">是否回款</td>
						<td align="center" colspan="1"><select name="pay" values="${pay}"
							class="select_class">
							<option value="">--</option>
							<option value="1">是</option>
							<option value="0">否</option>
						</select></td>
						
						<td width="15%" align="center">仓库</td>
                        <td align="center">
                        <select name="location"
                            class="select_class" values=${location}>
                            <option value="">--</option>
                            <c:forEach items="${depotList}" var="item">
                             <option value="${item.id}">${item.name}</option>
                            </c:forEach>
                        </select>
                        </td>
					</tr>
					
					<tr class="content1">
						<td width="15%" align="center">销售部门</td>
						<td align="center">
						<select name="department"
							class="select_class" values=${department}>
							<option value="">--</option>
							<c:forEach items='${departementList}' var="item">
								<option value="${item.name}">${item.name}</option>
							</c:forEach>
						</select>
						</td>
						<td width="15%" align="center">销售人员</td>
						<td align="center"><input type="text" name="stafferName" value="${stafferName}"></td>
					</tr>

					<tr class="content2">

						<td colspan="4" align="right"><input type="button" id="query_b"
							onclick="query()" class="button_class"
							value="&nbsp;&nbsp;查 询&nbsp;&nbsp;">&nbsp;&nbsp;
							<input type="button" onclick="res()" class="button_class" value="&nbsp;&nbsp;重 置&nbsp;&nbsp;"></td>
					</tr>
				</table>
				</td>
			</tr>
		</table>
		</td>
	</tr>

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
								<td width="35">&nbsp;</td>
								<td width="6"><img src="../images/dot_r.gif" width="6"
									height="6"></td>
								<td class="caption"><strong>浏览${fg}单:</strong>
								<c:if test="${queryType == '1'}">
								<font color=blue>[当前您剩余的信用:${credit}]</font>
								</c:if>
								</td>
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
		<td align='center' colspan='2'>
		<table width="100%" border="0" cellpadding="0" cellspacing="0"
			class="border">
			<tr>
				<td>
				<table width="100%" border="0" cellspacing='1' id="mainTable">
					<tr align="center" class="content0">
						<td align="center" width="5%" align="center">选择</td>
						<td align="center" onclick="tableSort(this)" class="td_class">单据编号</td>
						<td align="center" onclick="tableSort(this)" class="td_class">客户</td>
						<td align="center" onclick="tableSort(this)" class="td_class">状态</td>
						<td align="center" onclick="tableSort(this)" class="td_class">${fg}类型</td>
						<td align="center" onclick="tableSort(this)" class="td_class">${fg}时间</td>
						<td align="center" onclick="tableSort(this)" class="td_class">结算通过</td>
						<td align="center" onclick="tableSort(this)" class="td_class">回款日期</td>
						<td align="center" onclick="tableSort(this)" class="td_class">超期(天)</td>
						<td align="center" onclick="tableSort(this)" class="td_class">金额</td>
						<td align="center" onclick="tableSort(this)" class="td_class">付款</td>
						<td align="center" onclick="tableSort(this)" class="td_class">${fg}人</td>
						<td align="center" onclick="tableSort(this)" class="td_class">仓库</td>
						<td align="center" onclick="tableSort(this)" class="td_class">发货单</td>
					</tr>

					<c:forEach items="${listOut1}" var="item" varStatus="vs">
						<tr class='${vs.index % 2 == 0 ? "content1" : "content2"}'
						>
							<td align="center"><input type="radio" name="fullId" 
							   temptype="${item.tempType}"
							   hasmap="${hasMap[item.fullId]}"
							   index="${radioIndex}"
							   con="${item.consign}"
							   pay="${item.reserve3}"
							   paytype="${item.pay}"
							   outtype="${item.outType}"
							   statuss='${item.status}' 
							   baddebts='${my:formatNum(item.total - item.hadPay)}' 
							   value="${item.fullId}"/></td>
							<td align="center"
							onMouseOver="showDiv('${item.fullId}')" onmousemove="tooltip.move()" onmouseout="tooltip.hide()"><a onclick="hrefAndSelect(this)" href="../sail/out.do?method=findOut&fow=99&outId=${item.fullId}">
							${item.fullId}</a></td>
							<td align="center" onclick="hrefAndSelect(this)">${item.customerName}</td>
							<td align="center" onclick="hrefAndSelect(this)">${my:get('outStatus', item.status)}</td>
							<td align="center" onclick="hrefAndSelect(this)">${my:get('outType_out', item.outType)}</td>
							<td align="center" onclick="hrefAndSelect(this)">${item.outTime}</td>
							<td align="center" onclick="hrefAndSelect(this)">${item.managerTime}</td>
							<c:if test="${item.pay == 0}">
							<td align="center" onclick="hrefAndSelect(this)"><font color=red>${item.redate}</font></td>
							</c:if>
							<c:if test="${item.pay == 1}">
							<td align="center" onclick="hrefAndSelect(this)"><font color=blue>${item.redate}</font></td>
							</c:if>
							<td align="center" onclick="hrefAndSelect(this)">${overDayMap[item.fullId]}</td>
							<td align="center" onclick="hrefAndSelect(this)">${my:formatNum(item.total)}</td>
							<td align="center" onclick="hrefAndSelect(this)">${my:formatNum(item.hadPay)}</td>
							<td align="center" onclick="hrefAndSelect(this)">${item.stafferName}</td>
							<td align="center" onclick="hrefAndSelect(this)">${item.depotName}</td>
							<c:if test="${queryType == '3'}">
							<td align="center" onclick="hrefAndSelect(this)"><a
							href="../sail/transport.do?method=findConsign&fullId=${item.fullId}"
							>${my:get("consignStatus", item.consign)}</a></td>
							</c:if>
							<c:if test="${queryType != '3'}">
                            <td align="center" onclick="hrefAndSelect(this)">
                            <a
                            href="../sail/transport.do?method=findConsign&forward=2&fullId=${item.fullId}"
                            >
                            ${my:get("consignStatus", item.consign)}
                            </a>
                            </td>
                            </c:if>
						</tr>
					</c:forEach>
				</table>

				<p:formTurning form="adminForm" method="queryOut"></p:formTurning>
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

	<c:if test="${my:length(listOut1) > 0}">
	<tr>
		<td width="100%">
		<div align="right">
		
		<c:if test="${queryType != '5' && queryType != '6' && queryType != '8' && queryType != '9' && queryType != '10'}">
		
			<c:if test="${queryType == '2'}">
				<input type="button" class="button_class" style="display: none;"
		                value="&nbsp;&nbsp;确认回款&nbsp;&nbsp;" onClick="payOut()">&nbsp;&nbsp;
	        </c:if>
	        
	        <input name="bu1"
	                type="button" class="button_class" value="&nbsp;审核通过&nbsp;"
	                onclick="check()" />&nbsp;&nbsp;<input type="button" name="bu2"
	                class="button_class" value="&nbsp;&nbsp;驳 回&nbsp;&nbsp;"
	                onclick="reject()" />&nbsp;&nbsp;
			<input
				type="button" class="button_class"
				value="&nbsp;导出查询结果&nbsp;" onclick="exports()" />&nbsp;&nbsp;
		</c:if>	
		
		
		<c:if test="${queryType == '10'}">
             <input type="button" class="button_class" value="&nbsp;导出查询结果&nbsp;" onclick="exports()" />&nbsp;&nbsp;
        </c:if>
		
		<c:if test="${queryType == '5'}">
		<input type="button" class="button_class"
                value="&nbsp;&nbsp;确认回款&nbsp;&nbsp;" onClick="payOut2()"/>&nbsp;&nbsp;
        <input type="button" class="button_class"
                value="&nbsp;&nbsp;强制回款&nbsp;&nbsp;" onClick="fourcePayOut()"/>&nbsp;&nbsp;
        <!--  
        <input type="button" class="button_class"
                value="&nbsp;&nbsp;确认坏账&nbsp;&nbsp;" onClick="payOut3()"/>&nbsp;&nbsp;-->
        <input type="button" class="button_class"
                value="&nbsp;&nbsp;坏账取消&nbsp;&nbsp;" onClick="payOut4()"/>&nbsp;&nbsp;
		</c:if>
		
		<c:if test="${queryType == '6'}">
        <input type="button" class="button_class"
                value="&nbsp;&nbsp;总部核对&nbsp;&nbsp;" onClick="centerCheck()"/>&nbsp;&nbsp;
        </c:if>
        
        <c:if test="${queryType == '9'}">
        <input type="button" class="button_class"
                value="&nbsp;&nbsp;领样退库&nbsp;&nbsp;" onClick="outBack()"/>&nbsp;&nbsp;
        <input type="button" class="button_class"
                value="&nbsp;&nbsp;领样转销售&nbsp;&nbsp;" onClick="swatchToSail()"/>&nbsp;&nbsp;
        </c:if>
        
        <c:if test="${queryType == '8'}">
        <input type="button" class="button_class"
                value="&nbsp;&nbsp;销售退单&nbsp;&nbsp;" onClick="outBack2()"/>&nbsp;&nbsp;
        <input type="button" class="button_class"
                value="&nbsp;&nbsp;申请退款&nbsp;&nbsp;" onClick="applyBackPay()"/>&nbsp;&nbsp;
        </c:if>
        
		</div>
		</td>
		<td width="0%"></td>
	</tr>
	
	</c:if>

	<tr height="10">
		<td height="10" colspan='2'></td>
	</tr>

	<p:message2/>
</table>

</form>
</body>
</html>
