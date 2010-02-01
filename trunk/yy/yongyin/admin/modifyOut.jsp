<%@ page contentType="text/html;charset=GBK" language="java"
	errorPage="./error.jsp"%>
<%@include file="./common.jsp"%>

<html>
<head>
<p:link title="修改出库单" />
<c:set var="ff" value='${out.type == 0 ? "出" : "入"}'/>
<c:set var="ff1" value='${out.type == 0}'/>
<script language="JavaScript" src="../js/prototype.js"></script>
<script language="JavaScript" src="../js/common.js"></script>
<script language="JavaScript" src="../js/addOut.js"></script>
<script language="JavaScript" src="../js/buffalo.js"></script>
<script language="JavaScript" src="../js/template.js"></script>
<script language="JavaScript" src="../js/cal.js"></script>
<script language="JavaScript" src="../js/public.js"></script>
<script language="JavaScript" src="../js/JCheck.js"></script>
<script language="javascript">
var oo;
var END_POINT="${pageContext.request.contextPath}/bfapp";

var buffalo = new Buffalo(END_POINT);
buffalo.onError = new Function();

var ids = '';
var amous = '';
var tsts= '${out.total}';
var messk = '';
var locationId = '${currentLocationId}';
function change()
{
	var obj = getOption($("customerId"));
	$("connector").value = obj.connector;
	$("phone").value = obj.phone;
	$("customerName").value = obj.cname;
}

function opens(obj)
{
	oo = obj;    
	if ($('location'))
	{
		locationIdI = $F('location');
	}
	else
	{
		locationIdI = locationId;
	}
        
    window.common.modal('./product.do?method=rptInQueryProduct&firstLoad=1&locationInner='+ locationIdI);
}

function selectCustomer()
{
	window.common.modal("./out.do?method=queryCustomer");
}

function getCustmeor(id, name, conn, phone)
{
	$("connector").value = conn;
	$("phone").value = phone;
	$("customerName").value = name;
	$("customerId").value = id;
}

function total()
{
	var obj = document.getElementsByName("value");
	
	var total = 0;
	for (var i = 1; i < obj.length; i++)
	{
		if (obj[i].value != '')
		{
			total = add(total, parseFloat(obj[i].value));
		}
	}
	
	var ss =  document.getElementById("total");
	tsts = formatNum(total, 2);
	ss.innerHTML = '(总计:' + tsts + ')';
}

function check()
{
	if (!formCheck())
	{
		return false;
	}
	
	ids = '';
	amous = '';
	$('priceList').value = '';
	$('totalList').value = '';
	$('nameList').value = '';
	$('unitList').value = '';
	
	if (trim($('outTime').value) == '')
	{
		alert('请选择出库日期');
		return false;
	}
	
	if ($('customerName').value == '')
	{
		alert('请选择客户');
		return false;
	}
	
	<c:if test='${ff1}'>
	if ($$('reday') == '' || !isNumbers($$('reday')))
	{
		alert('请填入1到180之内的数字');
		$('reday').focus();
		return false;
	}
	
	if (parseInt($$('reday'), 10) > 180 || parseInt($$('reday'), 10) < 1)
	{
		alert('请填入1到180之内的数字');
		$('reday').focus();
		return false;
	}
	</c:if>
	
	var proNames = document.getElementsByName('productName');
	var units = document.getElementsByName('unit');
	var amounts = document.getElementsByName('amount');
	var prices = document.getElementsByName('price');
	var values = document.getElementsByName('value');
	
	//isNumber
	for (var i = 1; i < proNames.length; i++)
	{
		if (proNames[i].value == '')
		{
			alert('数据不完整,请选择产品名称!');
			return false;
		}
		
		ids = ids + proNames[i].productId + '~';
		
		$('nameList').value = $('nameList').value +  proNames[i].value + '~';
		
		$('idsList').value = ids;
	}
	
	for (var i = 1; i < amounts.length; i++)
	{
		if (trim(amounts[i].value) == '')
		{
			alert('数据不完整,请填写产品数量!');
			amounts[i].focus();
			return false;
		}
		
		if (!isNumbers(amounts[i].value))
		{
			alert('数据错误,产品数量 只能是整数!');
			amounts[i].focus();
			return false;
		}
		
		amous = amous + amounts[i].value + '~';
		
		$('amontList').value = amous;
	}
	
	for (var i = 1; i < prices.length; i++)
	{
		if (trim(prices[i].value) == '')
		{
			alert('数据不完整,请填写产品价格!');
			prices[i].focus();
			return false;
		}
		
		if (!isFloat(prices[i].value))
		{
			alert('数据错误,产品数量只能是浮点数!');
			prices[i].focus();
			return false;
		}
		
		$('priceList').value = $('priceList').value + prices[i].value + '~';
	}
	
	var desList = document.getElementsByName('desciprt');
	for (var i = 1; i < values.length; i++)
	{
		$('totalList').value = $('totalList').value + values[i].value + '~';
		$('desList').value = $('desList').value + desList[i].value + '~';
	}
	
	for (var i = 1; i < units.length; i++)
	{
		$('unitList').value = $('unitList').value + units[i].value + '~';
	}
	
	$('totalss').value = tsts;
	
	return true;
}

var productLocation = '${out.location}';
function checkTotal()
{
	messk = '';
	var gh = $('nameList').value.split('~');
	var ghk = $('amontList').value.split('~');
	
	messk += '\r\n';
	for(var i = 0 ; i < gh.length - 1; i++)
	{
		messk += '\r\n' + '产品【' + gh[i] + '】   数量:' + ghk[i];
	}
	
   	 if ($$('outType') == 1 && $$('type') == 1)
	 {
	 	 if (!window.confirm('您当前所操作的是调出，调出时也是正数增加库存，负数减少库存，您确认填写的调出符合实际情形?'))
	 	 {
	 	 	return;
	 	 }
	 }
		 
	if ($('saves').value != '')
    {
		 
	     if (window.confirm("确定保存${ff}库单?" + messk))
	     {
	     	$d('outType', false);
	     	disableAllButton();
	     	outForm.submit();
	     }
	     
	     return;
    }
    
	buffalo.remoteCall("productDAO.check",[ids, amous, ${out.type}, productLocation], function(reply) {	
		        var result = reply.getResult();  
		        if (result != 'true')
		        {
		        	alert(result);
		        	return;
		        }
		        
		        if (window.confirm("确定提交${ff}库单?" + messk))
		        {
		        	$d('outType', false);
		        	disableAllButton();
		        	outForm.submit();
		        }
		});
}

function save()
{
	$('saves').value = 'saves';
	if (check())
	{
		checkTotal();
	}
}

function sub()
{
	$('saves').value = '';
	if (check())
	{
		checkTotal();
	}
}

function titleChange()
{
	if ($$('outType') == '1' || $$('outType') == '4')
	{
		$('outd').innerText = '调出部门：';
	}
	else
	{
		$('outd').innerText = '供应商：';
	}
	
	<c:if test="${out.type == 1}">
	if ($$('outType') == '1')
	{
		hides(false);
	}
	else
	{
		hides(true);
	}
	
	//调入的处理
	if ($$('outType') == '4')
	{
		showTr(true);
	}
	else
	{
		showTr(false);
	}
	</c:if>
	
	<c:if test="${out.type == 0}">
	managerChange();
	</c:if>
}

function selectOut()
{
	window.common.modal('../admin/out.do?method=queryOut3&load=1&flag=4');
}

function getOutId(id)
{
	$('refOutFullId').value = id;
}

function hides(boo)
{
	$d('dirs', boo);
	$v('dirs', !boo);
	
	$d('dirs1', boo);
	$v('dirs1', !boo);
}

function showTr(boo)
{
	$v('in_out', boo);
	$v('refOutFullId', boo);
}


function managerChange()
{
	if ($$('outType') == 0)
	{
		
	}
	else
	{
		$('customerName').value = '个人领样';
		$('customerId').value = '0';
		$('customerName').disabled  = true;
		$('reday').value = '7';
		$('reday').readOnly = true;
	}
}

function load()
{
	titleChange();
	
	loadForm();
	
	if ($$('outType') == '1')
	{
		hides(false);
	}
	else
	{
		hides(true);
	}
	
	titleChange();
	
	$$E(['productId']);
}

function locationChange()
{
	productLocation = $$('location');
}
</script>
</head>
<body class="body_class" onload="load()">
<form name="outForm" method=post action="./out.do?"><input
	type=hidden name="method" value="addOut" /> <input type=hidden
	name="nameList" /> <input type=hidden name="idsList" /> <input
	type=hidden name="unitList" /> <input type=hidden name="amontList" />
<input type=hidden name="priceList" /> <input type=hidden
	name="totalList" /> <input type=hidden name="totalss" value="${out.total}" /> <input
	type=hidden name="customerId" value="${out.customerId}" /> <input type=hidden name="saves"
	value="" />
	<input type=hidden name="id" value="${out.id}" />
	<input type=hidden name="desList" value="" />
	<input type=hidden name="type"
	value='${out.type}' />
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
				<td width="550" class="navigation">库单管理 &gt;&gt; <span
					style="cursor:hand" onclick="javascript:history.go(-1)">查询${ff}库单</span>
				&gt;&gt; 修改${ff}库单</td>
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

<table width="95%" border="0" cellpadding="0" cellspacing="0"
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
								<td width="35">&nbsp;</td>
								<td width="6"><img src="../images/dot_r.gif" width="6"
									height="6"></td>
								<td class="caption"><strong>修改${ff}库单信息:您的信用额度还剩下:${credit}</strong>
								<c:if test="${out.type == 0}">
								<font color="blue">产品区域：</font>
								<select name="location" class="select_class" values="${out.location}" onchange="locationChange()" readonly="true">
									<c:forEach items='${locationList}' var="item">
										<option value="${item.id}">${item.locationName}</option>
									</c:forEach>
								</select>
								</c:if>
								</td>
							</tr>
						</table>
						</td>
					</tr>


				</table>
				</div>
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
		<table width="95%" border="0" cellpadding="0" cellspacing="0"
			class="border">
			<tr>
				<td>
				<table width="100%" border="0" cellspacing='1'>
					<tr class="content2">
						<td width="15%" align="right">${ff}库日期：</td>
						<td width="35%"><input type="text" name="outTime"
							value="${out.outTime}" onKeypress="calDate(this)"
							class="time_input" onclick="calDate(this)" title="点击选择开通时间"
							maxlength="20" size="20" readonly="readonly"><font
							color="#FF0000">*</font></td>
						<td width="15%" align="right">${ff}库类型：</td>
						<td width="35%">
						
						<c:if test="${out.type == 0}">
						<select name="outType" class="select_class">
							<c:if test="${out.outType == 0}">
							<option value="0">销售出库</option>
							</c:if>
							<c:if test="${out.outType == 1}">
							<option value="1">个人领样</option>
							</c:if>
						</select>
						</c:if>
						
						<c:if test="${out.type == 1}">
							<select name="outType" class="select_class"  onchange="titleChange()" values="${out.outType}" readonly=true>
								<option value="0">采购入库</option>
								<option value="1">调出</option>
								<option value="4">调入</option>
								<option value="2">盘亏出库</option>
								<option value="3">盘盈入库</option> 
							</select>
						</c:if>
						
						<font color="#FF0000">*</font></td>

					</tr>
					<tr class="content1">
						<td align="right" id="outd">${out.type == 1 ? "供应商" : "客户"}：</td>
						<td><input type="text" name="customerName" maxlength="14" value="${out.customerName}"
							readonly="readonly"><font color="#FF0000">*</font></td>
						<td align="right">调${ff}部门：</td>
						<td><select name="department" class="select_class">
							<c:forEach items="${departementList}" var="item">
								<option value="${item}" ${out.department==item?"selected" : ""}>${item}</option>
							</c:forEach>
						</select><font color="#FF0000">*</font></td>
					</tr>
					<tr class="content2">
						<td align="right">联系人：</td>
						<td><input type="text" name="connector" maxlength="14"
							value="${out.connector}" readonly="readonly"></td>
						<td align="right">联系电话：</td>
						<td><input type="text" name="phone" maxlength="20" readonly
							value="${out.phone}"></td>
					</tr>
					<tr class="content1">
						<td align="right">经手人：</td>
						<td><input type="text" name="stafferName" maxlength="14"
							value="${out.stafferName}" readonly="readonly"></td>
						<td align="right">单据号码：</td>
						<td><input type="text" name="fullId" maxlength="20" readonly
							value="${out.fullId}"></td>
					</tr>
					
					<c:if test='${ff1}'>
					<tr class="content2"> 
						<td align="right">回款天数：</td>  
						<td colspan="1"><input type="text" name="reday" maxlength="4"
							value="${out.reday}" title="请填入1到180之内的数字"><font color="#FF0000">*</font></td>
							
							<td align="right">到货日期：</td> 
						<td><input type="text" name="arriveDate"
							onKeypress="calDate(this)" class="time_input" oncheck="notNone;cnow('>')"  value="${out.arriveDate}"
							onclick="calDate(this)"  maxlength="20" size="20"
							readonly="readonly"><font color="#FF0000">*</font></td>
					</tr>
					</c:if>
					
					<c:if test='${!ff1 && user.locationID == "0"}'>
					<tr class="content2"> 
						<td align="right">选择仓区：</td>  
						<td colspan="1"><select name="depotpartId" class="select_class" values="${out.depotpartId}"> 
								<option value=''>--</option>
								<c:forEach items="${depotpartList}" var="item">
								<option value='${item.id}'>${item.name}</option>
								</c:forEach>
							</select><font color="#FF0000">*</font></td>
							<td align="right"><div id="dirs1">目的区域：</div></td>  
						<td colspan="1"><div id="dirs"><select name="destinationId" class="select_class" 
						oncheck="notNone;noEquals('${currentLocationId}')"
						message="不能为空或者选择当前区域"
						values="${out.destinationId}"> 
								<option value=''>--</option>
								<c:forEach items="${locationList}" var="item">
								<option value='${item.id}'>${item.locationName}</option>
								</c:forEach>
							</select><font color="#FF0000">*</font></div></td>
					</tr>
					</c:if>
					
					<c:if test='${!ff1}'>
					<tr class="content2" id="in_out"> 
						<td align="right">调出库单：</td>  
						<td colspan="1"><input type="text" name="refOutFullId" maxlength="40" oncheck="notNone;" readonly="readonly"
							value="${out.refOutFullId}">&nbsp;&nbsp;<input
							type="button" value="&nbsp;...&nbsp;" name="qout" class="button_class" onclick="selectOut()">
							<font color="#FF0000">*</font></td>
						
						<td align="right"></td> 
						<td></td>
					</tr>
					</c:if>
					
					<tr class="content2">
                        <td align="right">付款方式：</td>
                        <td colspan="3">
                        <select name="reserve3" class="select_class" oncheck="notNone;" head="付款方式" values="${out.reserve3}" readonly="true"
                        style="width: 240px">
                            <option value='2'>客户信用和业务员信用额度担保</option>
                            <option value='1'>款到发货(黑名单客户)</option>
                        </select>
                        <font color="#FF0000">*</font></td>
                    </tr>
					
					
					<tr class="content1">
						<td align="right">备注：</td>
						<td colspan="3"><textarea name="description" rows="3"
							cols="55">${out.description}</textarea></td>
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
		<td colspan='2' align='center'>
		<table width="95%" border="0" cellpadding="0" cellspacing="0"
			class="border">
			<tr>
				<td>
				<table width="100%" border="0" cellspacing='1' id="tables">
					<tr align="center" class="content0">
						<td width="20%" align="center">品名</td>
						<td width="5%" align="center">单位</td>
						<td width="10%" align="center">数量</td>
						<td width="10%" align="center">单价</td>
						<td width="20%" align="left">金额<span id="total">(总计:${my:formatNum(out.total)})</span></td>
						<td width="25%" align="center">成本</td>
						<td width="15%" align="center"><input type="button"
							value="增加" class="button_class" onclick="addTr()"></td>
					</tr>

					<tr class="content1" id="trCopy" style="display: none;">
						<td width="20%"><input type="text" name="productName"
							onclick="opens(this)" productId="" readonly="readonly" style="width: 100%; cursor: hand">
							</td>

						<td width="5%"><select name="unit" style="WIDTH: 50px;">
							<option value="套">套</option>
							<option value="枚">枚</option>
							<option value="个">个</option>
							<option value="本">本</option>
						</select></td>

						<td width="10%" align="center"><input type="text" style="width: 100%" maxlength="6"
							onkeyup="cc(this)" name="amount"></td>

						<td width="10%" align="center"><input type="text" style="width: 100%"
							onkeyup="cc(this)" onblur="blu(this)" name="price"></td>

						<td width="20%" align="center"><input type="text" value="0.00"
							readonly="readonly" style="width: 100%" name="value"></td>
							
						<td width="25%" align="center"><input type="text"
							 style="width: 100%" name="desciprt"></td>

						<td width="15%" align="center"></td>
					</tr>

					<c:forEach items="${baseList}" var="item" varStatus="vs">
						<tr class='${vs.index % 2 == 0 ? "content2" : "content1"}'>
							<td width="20%"><input type="text" name="productName"
								value="${item.productName}" onclick="opens(this)"
								productId="${item.productId}" readonly="readonly" style="width: 100%"
								style="cursor: hand"></td>

							<td width="10%"><select name="unit" style="WIDTH: 50px;">
								<option value="套" ${item.unit=="套" ? "selected" : ""}>套</option>
								<option value="枚" ${item.unit=="枚" ? "selected" : ""}>枚</option>
								<option value="个" ${item.unit=="个" ? "selected" : ""}>个</option>
								<option value="本" ${item.unit=="本" ? "selected" : ""}>本</option>

							</select></td>

							<td width="10%" align="center"><input type="text" style="width: 100%"
								value="${item.amount}" onkeyup="cc(this)" name="amount"></td>

							<td width="10%" align="center"><input type="text" style="width: 100%"
								value="${my:formatNum(item.price)}" onkeyup="cc(this)" onblur="blu(this)"
								name="price"></td>

							<td width="20%" align="center"><input type="text"
								value="${my:formatNum(item.value)}" readonly="readonly" style="width: 100%"
								name="value"></td>
							
							<td width="25%" align="center"><input type="text" value="${item.description}"
							 style="width: 100%" name="desciprt"></td>
							 
							<c:if test="${vs.first}">
							<td width="15%" align="center"></td>
							</c:if>
							
							<c:if test="${!vs.first}">
							<td width="15%" align="center"><input type=button value="&nbsp;删 除&nbsp;" class=button_class onclick="removeTr(this)"></td>
							</c:if>
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
		<td background="../images/dot_line.gif" colspan='2'></td>
	</tr>

	<tr>
		<td height="10" colspan='2'></td>
	</tr>

	<tr>
		<td width="97%">
		<div align="right"><input type="button" class="button_class"
			value="&nbsp;&nbsp;保 存&nbsp;&nbsp;" onClick="save()" />&nbsp;&nbsp;<input
			type="button" class="button_class"
			value="&nbsp;&nbsp;提 交&nbsp;&nbsp;" onClick="sub()" />&nbsp;&nbsp;<input type="button" class="button_class"
			onclick="javascript:history.go(-1)"
			value="&nbsp;&nbsp;返 回&nbsp;&nbsp;">
			</div>
		</td>
		<td width="3%"></td>
	</tr>

</table>
</form>
</body>
</html>

