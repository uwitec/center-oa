<%@ page contentType="text/html;charset=GBK" language="java"
	errorPage="./error.jsp"%>
<%@include file="./common.jsp"%>

<html>

<head>
<p:link title="查询" />
<c:if test='${flag == "3"}'>
<base target="_self">
</c:if>
<script src="../js/CommonScriptMethod.js"></script>
<script src="../js/prototype.js"></script>
<script src="../js/common.js"></script>
<script src="../js/fanye.js"></script>
<script src="../js/public.js"></script>
<script src="../js/title_div.js"></script>
<script language="javascript">
function detail()
{
	var index = $Index('fullId');
	document.location.href = '../admin/out.do?method=findOut&outId=' + getRadioValue("fullId") + '&fow=2&index=' + index + "&flag=${flag}";
}

function sures()
{
	var opener = window.common.opener();
	var oo = getRadio("fullId");
	
	if (oo == null)
	{
		alert('请选择库单');
		return;
	}
	
    opener.getOutId(oo.value);
    //opener.getProduct(oo.value, oo.productName, oo.num);
    opener = null;
    window.close();
}

function res()
{
	$('customerId').value= '';
	$('customerName').value= '';
	$('id').value= '';
	$('type').selectedIndex = 0;
	$('checks').selectedIndex = 0;
	$('stafferName').selectedIndex = 0;
	$('pay').selectedIndex = 0;
	$('reCom').selectedIndex = 0;
	$('redate').value= '';
}

function comp()
{
	var now = '${now}';
	
	var str1 = $('outTime').value;
	
	var str2 = $('outTime1').value;
	
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
			alert('查询日期跨度不能大于30个月(900天)!');
			return false;
		}
		
		$('outTime1').value = now;
	}
	
	if (str1 == '' && str2 != '')
	{
		if (!coo(now, str2))
		{
			alert('查询日期跨度不能大于30个月(900天)!');
			return false;
		}
		
		$('outTime').value = now;
	}
	
	if (str1 != '' && str2 != '')
	{
		if (!coo(str1, str2))
		{
			alert('查询日期跨度不能大于30个月(900天)!');
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
	
	return Math.abs((year2 - year1) * 365 + (month2 - month1) * 30 + (day2 - day1)) <= 900;
}

function modfiy()
{
	if (getRadio('fullId').statuss == '0' || getRadio('fullId').statuss == '2')
	{
		document.location.href = '../admin/out.do?method=findOut&outId=' + getRadioValue("fullId") + "&fow=1";
	}
	else
	{
		alert('只有保存态和驳回态的出库单可以修改!');
	}
}

function del()
{
	if (getRadio('fullId').statuss == '0' || getRadio('fullId').statuss == '2')
	{
		 if (window.confirm("确定删除出库单?"))
		document.location.href = '../admin/out.do?method=delOut&outId=' + getRadioValue("fullId");
	}
	else
	{
		alert('只有保存态和驳回态的出库单可以删除!');
	}
}

function query()
{
	if (comp())
	{
		adminForm.submit();
	}
}

function selectCustomer()
{
        
    window.common.modal("./out.do?method=queryAllCustomer&flagg=${GFlag}");
}

function getCustmeor(id, name, conn, phone)
{
	$("customerName").value = name;
	$("customerId").value = id;
}

var jmap = new Object();
<c:forEach items="${listOut3}" var="item">
	jmap['${item.fullId}'] = "${divMap[item.fullId]}";
</c:forEach>

function showDiv(id, obj)
{
	obj.focus();
	tooltip.showTable(jmap[id]);
}

</script>
<title>出库单列表</title>

</head>
<body class="body_class" onkeypress="tooltip.bingEsc(event)">
<form action="./out.do" name="adminForm"><input type="hidden"
	value="queryOut3" name="method"> <input type="hidden" value="1"
	name="firstLoad">
	<input type="hidden" value="${customerId}"
	name="customerId">
	<input type="hidden" value=""
	name="outId">
	<input type="hidden" value="${flag}"
	name="flag">
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
				<td width="550" class="navigation">查询库单</td>
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
<table width="100%" border="0" cellpadding="0" cellspacing="0"
	align="center">
	<tr>
		<td align='center' colspan='2'>
		<table width="100%" border="0" cellpadding="0" cellspacing="0"
			class="border">
			<tr>
				<td>
				<c:if test='${flag != "4"}'>
				<table width="100%" border="0" cellspacing='1'>
					<tr class="content1">
						<td width="15%" align="center">开始时间</td>
						<td align="center">
						<p:plugin name="outTime" size="20" value="${out3Map.outTime}"/>
						</td>
						<td width="15%" align="center">结束时间</td>
						<td align="center">
						<p:plugin name="outTime1" size="20" value="${out3Map.outTime1}"/>
						</td>
					</tr>

					<tr class="content2">
						<td width="15%" align="center">单号</td>
						<td align="center"><input type="text" name="id" value="${out3Map.id}"></td>
						
						<td width="15%" align="center">客户：</td>
						<td align="center"><input type="text" name="customerName" maxlength="14" value="${out3Map.customerName}"
							onclick="selectCustomer()" style="cursor: hand"
							readonly="readonly"></td>
					</tr>

					<tr class="content1">
						<td width="15%" align="center">库单类型:</td>
						<td align="center" ><select name="type" values="${out3Map.type}"
							class="select_class">
							<option value="">--</option>
							<option value="0">销售单</option>
							<option value="1">入库单</option>
						</select></td>
						
						<td width="15%" align="center">是否核对:</td>
						<td align="center" ><select name="checks"  values="${out3Map.checks}"
							class="select_class">
							<option value=""  >--</option>
							<option value="0">未核对</option>
							<option value="1">已核对</option>
						</select></td>
					</tr>
					
					<tr class="content2">
						<td width="15%" align="center">出库人</td>
						<td align="center"><input name="stafferName" value="${out3Map.stafferName}" type="text">
							</td>
						<td width="15%" align="center">回款日期</td>
						<td align="center">
						<select name="reCom" style="WIDTH: 75px" values="${out3Map.reCom}">
							<option value="">--</option>
							<option value="=" }>等于</option>
							<option value="&gt;=" >大于等于</option>
							<option value="&lt;=" >小于等于</option>
						</select>
						<p:plugin name="redate" size="20" value="${out3Map.redate}"/>
						</td>
					</tr>
					
					<tr class="content1">
						<td width="15%" align="center">是否回款</td>
						<td align="center"><select name="pay"
							class="select_class">
							<option value="" ${out3Map.pay==""?"selected" : ""}>--</option>
							<option value="1" ${out3Map.pay=="1"?"selected" : ""}>是</option>
							<option value="0" ${out3Map.pay=="0"?"selected" : ""}>否</option>
						</select></td>
						
						<td colspan="2" align="right"><input type="button"
							onclick="query()" class="button_class"
							value="&nbsp;&nbsp;查 询&nbsp;&nbsp;">&nbsp;&nbsp;<input type="button"
							onclick="res()" class="button_class"
							value="&nbsp;&nbsp;重 置&nbsp;&nbsp;"></td>
					</tr>
				</table>
				</c:if>
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
								<td class="caption"><strong>浏览${fg}库单:</strong><font color=blue>[当前查询数量:${TATOL}]</font></td>
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
				<table width="100%" border="0" cellspacing='1' id=result>
					<tr align="center" class="content0">
						<td align="center" width="5%" align="center">选择</td>
						<td align="center" onclick="tableSort(this)" class="td_class">单据编号</td>
						<td align="center" onclick="tableSort(this)" class="td_class">客户</td>
						<td align="center" onclick="tableSort(this)" class="td_class">状态</td>
						<td align="center" onclick="tableSort(this)" class="td_class">库单类型</td>
						<td align="center" onclick="tableSort(this)" class="td_class">填写时间</td>
						<td align="center" onclick="tableSort(this)" class="td_class">回款日期</td>
						<td align="center" onclick="tableSort(this, true)" class="td_class">金额</td>
						<td align="center" onclick="tableSort(this)" class="td_class">填写人</td>
						<td align="center" onclick="tableSort(this)" class="td_class">付款</td>
						<td align="center" onclick="tableSort(this)" class="td_class">核对</td>
					</tr>

					<c:set var="ind" value="${out3Map.rIndex > my:length(listOut3) ? 0 : out3Map.rIndex}"/>
					<c:set var="hr" value='${flag != "3"}'/>
					<c:forEach items="${listOut3}" var="item" varStatus="vs">
						<tr class='${vs.index % 2 == 0 ? "content1" : "content2"}'>
							<td align="center"><input type="radio" name="fullId"
								statuss='${item.status}' value="${item.fullId}" ${vs.index == ind ? "checked" : ""}/></td>
							<td align="center"
							onMouseOver="showDiv('${item.fullId}', this)" onmousemove="tooltip.move()" onmouseout="tooltip.hide()"><a 
							onclick="hrefAndSelect(this)" 
							<c:if test='${hr}'>
							href="../admin/out.do?method=findOut&outId=${item.fullId}"
							</c:if>
							>${item.fullId}</a></td>
							<td align="center" onclick="hrefAndSelect(this)">${item.customerName}</td>
							<td align="center" onclick="hrefAndSelect(this)">${my:status(item.status)}</td>
							<td align="center" onclick="hrefAndSelect(this)">${item.type == 0 ? "销售单" : "入库单"}</td>
							<td align="center" onclick="hrefAndSelect(this)">${item.outTime}</td>
							<c:if test="${item.pay == 0}">
							<td align="center" onclick="hrefAndSelect(this)"><font color=red>${item.redate}</font></td>
							</c:if>
							<c:if test="${item.pay == 1}">
							<td align="center" onclick="hrefAndSelect(this)"><font color=blue>${item.redate}</font></td>
							</c:if>
							<td align="center" onclick="hrefAndSelect(this)">${my:formatNum(item.total)}</td>
							<td align="center" onclick="hrefAndSelect(this)">${item.stafferName}</td>
							<td align="center" onclick="hrefAndSelect(this)">${item.hadPay}</td>
							<td align="center" onclick="hrefAndSelect(this)">${item.checks == "" ? "未核对" : "<font color=blue><B>已核对</B></font>"}</td>
						</tr>
					</c:forEach>
				</table>
				<c:if test='${hr}'>
				<p:turning url="../admin/out.do?method=queryOut3&flag=${flag}"></p:turning>
				</c:if>
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
	<c:set var='dis' value='${flag eq "2" ? "往来" : "总部会计"}'/>
	<c:if test="${my:length(listOut3) > 0}">
	<c:if test='${flag != "3" && flag != "4"}'>
	<tr>
		<td width="100%">
		<div align="right"><input type="button" class="button_class"
			value="&nbsp;&nbsp;${dis}核对&nbsp;&nbsp;" onClick="detail()">&nbsp;&nbsp;</div>
		</td>
		<td width="0%"></td>
	</tr>
	</c:if>
	<c:if test='${flag == "3" || flag == "4"}'>
	<tr>
		<td width="100%">
		<div align="right"><input type="button" class="button_class"
			value="&nbsp;&nbsp;确 定&nbsp;&nbsp;" onClick="sures()">&nbsp;&nbsp;</div>
		</td>
		<td width="0%"></td>
	</tr>
	</c:if>
	</c:if>

	<tr height="10">
		<td height="10" colspan='2'></td>
	</tr>

	<tr>
		<td colspan='2' align="center"><FONT color="blue">${MESSAGE_INFO}</FONT><FONT
			color="red">${errorInfo}</FONT> <c:remove var="MESSAGE_INFO"
			scope="session" /><c:remove var="errorInfo" scope="session" /></td>
	</tr>
</table>

</form>
</body>
</html>
