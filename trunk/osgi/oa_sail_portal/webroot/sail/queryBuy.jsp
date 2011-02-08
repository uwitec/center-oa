<%@ page contentType="text/html;charset=UTF-8" language="java"
    errorPage="../common/error.jsp"%>
<%@include file="../common/common.jsp"%>
<html>
<head>
<p:link title="查询入库单" />
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
	    getObj('method').value = 'queryBuy';
	    
		adminForm.submit();
	}
}

function res()
{
	$O('customerName').value = '';
	$O("customerId").value = '';
	$O("id").value = '';
	setSelectIndex($O('status'), 0);
	setSelectIndex($O('inway'), 0);
	setSelectIndex($O('location'), 0);
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

function processInvoke()
{
    if (getRadioValue("fullId") != '')
    document.location.href = '../sail/out.do?method=findOut&fow=5&outId=' + getRadioValue("fullId");
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

var nextStatusMap = {"1" : 9, "2" : 10, "3" : 3, "4" : 3};

var oldStatusMap = {"1" : 8, "2" : 9, "3" : 10, "4" : 1};

var queryType = "${queryType}";

// 通过入库单
function check()
{
    if (getRadio('fullId').statuss == oldStatusMap[queryType])
    {
        var hi = '';
        
        if (nextStatusMap[queryType] == 3)
        {
            hi = '通过此库单库存会发生变动,';
        }
        
        if (window.confirm(hi + "确定审核通过入库单?"))
        {
            $Dbuttons(true);
            
            getObj('method').value = 'modifyOutStatus';

            getObj('statuss').value = nextStatusMap[queryType];
            
            getObj('oldStatus').value = getRadio('fullId').statuss;

            getObj('outId').value = getRadioValue("fullId");

            getObj('radioIndex').value = $Index('fullId');

            getObj('reason').value = '同意';

            adminForm.submit();
        }
        else
        {
            $Dbuttons(false);  
        }
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


</script>

</head>
<body class="body_class" onkeypress="tooltip.bingEsc(event)" onload="load()">
<form action="../sail/out.do" name="adminForm"><input type="hidden"
	value="queryBuy" name="method"> <input type="hidden" value="1"
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

<c:set var="fg" value='入库'/>

<p:navigation
    height="22">
    <td width="550" class="navigation">库单管理 &gt;&gt; 查询入库单${queryType}</td>
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
						<td width="15%" align="center">入库单状态</td>
						<td align="center">
						<select name="status" class="select_class" values="${status}">
							<option value="">--</option>
							<p:option type="outStatus"/>
						</select>

						</td>
						<td width="15%" align="center">供应商：</td>
						<td align="center"><input type="text" name="customerName" maxlength="14" value="${customerName}"></td>
					</tr>

					<tr class="content1">
						<td width="15%" align="center">入库类型</td>
						<td align="center">
						<select name="outType"
							class="select_class" values=${outType}>
							<option value="">--</option>
							<p:option type="outType_in"></p:option>
						</select>

						</td>
						<td width="15%" align="center">入库单号</td>
						<td align="center"><input type="text" name="id" value="${id}"></td>
					</tr>

					<tr class="content2">
						<td width="15%" align="center">在途状态</td>
						<td align="center" colspan="1">
						<select name="inway" values="${inway}"
							class="select_class">
							<option value="">--</option>
							<p:option type="inway"></p:option>
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
						<td align="center" onclick="tableSort(this)" class="td_class">供应商</td>
						<td align="center" onclick="tableSort(this)" class="td_class">状态</td>
						<td align="center" onclick="tableSort(this)" class="td_class">${fg}类型</td>
						<td align="center" onclick="tableSort(this)" class="td_class">${fg}时间</td>
						<td align="center" onclick="tableSort(this)" class="td_class">金额</td>
						<td align="center" onclick="tableSort(this)" class="td_class">${fg}人</td>
						<td align="center" onclick="tableSort(this)" class="td_class">目的仓库</td>
						<td align="center" onclick="tableSort(this)" class="td_class">源仓库</td>
					</tr>

					<c:forEach items="${listOut1}" var="item" varStatus="vs">
						<tr class='${vs.index % 2 == 0 ? "content1" : "content2"}'
						>
							<td align="center"><input type="radio" name="fullId" 
							   temptype="${item.tempType}"
							   inway="${item.inway}"
							   hasmap="${hasMap[item.fullId]}"
							   index="${radioIndex}"
							   con="${item.consign}"
							   pay="${item.reserve3}"
							   paytype="${item.pay}"
							   statuss='${item.status}' 
							   value="${item.fullId}"/></td>
							<td align="center"
							onMouseOver="showDiv('${item.fullId}')" onmousemove="tooltip.move()" onmouseout="tooltip.hide()"><a onclick="hrefAndSelect(this)" href="../sail/out.do?method=findOut&fow=99&outId=${item.fullId}">
							${item.fullId}</a></td>
							<td align="center" onclick="hrefAndSelect(this)">${item.customerName}</td>
							<td align="center" onclick="hrefAndSelect(this)">${my:get('outStatus', item.status)}</td>
							<td align="center" onclick="hrefAndSelect(this)">${my:get('outType_in', item.outType)}</td>
							<td align="center" onclick="hrefAndSelect(this)">${item.outTime}</td>
							<td align="center" onclick="hrefAndSelect(this)">${my:formatNum(item.total)}</td>
							<td align="center" onclick="hrefAndSelect(this)">${item.stafferName}</td>
							<td align="center" onclick="hrefAndSelect(this)">${item.destinationName}</td>
							<td align="center" onclick="hrefAndSelect(this)">${item.depotName}</td>
						</tr>
					</c:forEach>
				</table>

				<p:formTurning form="adminForm" method="queryBuy"></p:formTurning>
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
		<c:if test="${queryType != '4'}">
        <input name="bu1"
                type="button" class="button_class" value="&nbsp;审核通过&nbsp;"
                onclick="check()" />&nbsp;&nbsp;<input type="button" name="bu2"
                class="button_class" value="&nbsp;&nbsp;驳 回&nbsp;&nbsp;"
                onclick="reject()" />&nbsp;&nbsp;
		<input
			type="button" class="button_class"
			value="&nbsp;导出查询结果&nbsp;" onclick="exports()" />&nbsp;&nbsp;"
		</c:if>	
		
		<c:if test="${queryType == '4'}">
        <input type="button" class="button_class"
                value="&nbsp;&nbsp;处理调拨&nbsp;&nbsp;" onClick="processInvoke()"/>&nbsp;&nbsp;
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