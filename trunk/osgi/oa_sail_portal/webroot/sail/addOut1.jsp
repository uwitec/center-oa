<%@ page contentType="text/html;charset=UTF-8" language="java"
	errorPage="../common/error.jsp"%>
<%@include file="../common/common.jsp"%>

<html>
<head>
<p:link title="填写入库单" cal="false" />
<script language="JavaScript" src="../js/prototype.js"></script>
<script language="JavaScript" src="../js/common.js"></script>
<script language="JavaScript" src="../js/compatible.js"></script>
<script language="JavaScript" src="../js/public.js"></script>
<script language="JavaScript" src="../js/JCheck.js"></script>
<script language="JavaScript" src="../js/addOut.js"></script>
<script language="JavaScript" src="../js/buffalo.js"></script>
<script language="javascript">
var oo;
var END_POINT="${pageContext.request.contextPath}/bfapp";

var buffalo = new Buffalo(END_POINT);
buffalo.onError = new Function();

var ids = '';
var amous = '';
var tsts;
var messk = '';
var locationId = '${currentLocationId}';
var currentLocationId = '${currentLocationId}';
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
	
	var locationIdI;
	
	if ($$('outType') == '8')
	{
		if (isNone($$('sdestinationId')))
		{
			alert('请选择产品区域,您需要指定是从哪个区域调出');
			
			$f('sdestinationId');
			
			return;
		}
		
		locationIdI = $$('sdestinationId');
	}
	else
	{
		locationIdI = locationId;
	}

	window.common.modal('./product.do?method=rptInQueryProduct&firstLoad=1&locationInner='+ locationIdI);
}

function selectCustomer()
{
	window.common.modal("./out.do?method=queryProvider&flagg=${GFlag}");
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

function titleChange()
{
	if ($$('outType') == '1' || $$('outType') == '4' || $$('outType') == '8')
	{
		$('outd').innerHTML = '调出部门：';
	}
	else
	{
		$('outd').innerHTML = '供应商：';
	}
	
	//hides1(true);
	if ($$('outType') == '8')
	{
		hides1(false);
		
		$v('b_save', false);
	}
	else
	{
		hides1(true);
		
		$v('b_save', true);
	}

	if ($$('outType') == '1')
	{
		hides(false);

		//$hide('tranNo', false);
	}
	else
	{
		hides(true);

		//$hide('tranNo', true);
	}

	//调入的处理
	if ($$('outType') == '4')
	{
		showTr(true);
	}
	else
	{
		showTr(false);

		if ($('refOutFullId'))
		$('refOutFullId').value = '';
	}
}

function load()
{
	titleChange();

	hides(true);
	hides1(true);
}

function hides(boo)
{
	$d('dirs', boo);
	$v('dirs', !boo);

	$d('dirs1', boo);
	$v('dirs1', !boo);

	$d('destinationId', boo);
	$v('destinationId', !boo);
}

function hides1(boo)
{
	$d('sdirs', boo);
	$v('sdirs', !boo);

	$d('sdirs1', boo);
	$v('sdirs1', !boo);

	$d('sdestinationId', boo);
	$v('sdestinationId', !boo);
}

function showTr(boo)
{
	$v('in_out', boo);
	$v('refOutFullId', boo);
}

function getOutId(id)
{
	$('refOutFullId').value = id;
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
		alert('请选择销售日期');
		return false;
	}

	if ($$('outType') == '')
	{
		alert('请选择库单类型');
		return false;
	}

	if ($('customerId').value == '')
	{
		alert('请选择客户');
		return false;
	}

	if ($$('department') == '')
	{
		alert('请选择销售部门');
		return false;
	}


	var proNames = document.getElementsByName('productName');
	var units = document.getElementsByName('unit');
	var amounts = document.getElementsByName('amount');
	var prices = document.getElementsByName('price');
	var values = document.getElementsByName('value');


	//isNumbers
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
	for (var i = 1; i < desList.length; i++)
	{
		if (trim(desList[i].value) != '')
		if (!isFloat(desList[i].value))
		{
			alert('格式错误,成本只能是浮点数!');
			desList[i].focus();
			return false;
		}
	}
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

//获得产品区域
function getProductLocation()
{
	if ($$('outType') == '8')
	{
		return $$('sdestinationId');
	}
	else
	{
		return locationId;
	}
}

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


	 if (($$('outType') == 1 && $$('type') == 1))
	 {
	 	 if (!window.confirm('您当前所操作的是调出，调出时也是正数增加库存，负数减少库存，您确认填写的调出符合实际情形?'))
	 	 {
	 	 	return;
	 	 }
	 }
	 
	 if ($$('outType') == 8)
	 {
	 	 if (!window.confirm('您当前所操作的是调入，调入时也是正数增加库存，负数减少库存，但是库存减少的是调出区域的库存，您确认填写的调出符合实际情形?'))
	 	 {
	 	 	return;
	 	 }
	 }

	if ($('saves').value != '')
    {

	     if (window.confirm("确定保存库单?" + messk))
	     {
	     	$('id').value = '';
	     	disableAllButton();
	     	outForm.submit();
	     }

	     return;
    }

    ccv = getProductLocation();

    if (isNone(ccv))
    {
    	alert('产品区域为空，请核实');
    	return false;
    }

     //判断method
    if ($$('method') != 'addOut')
    {
    	alert('提示：提交没有方法，请重新登录操作');
    	return false;
    }

	buffalo.remoteCall("productDAO.check",[ids, amous, ${ff ? "1" : "0"}, ccv], function(reply) {
		        var result = reply.getResult();
		        if (result != 'true')
		        {
		        	alert(result);
		        	return;
		        }

	        	if (window.confirm("确定提交库单?" + messk))
		        {
		        	$('id').value = '';
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

function managerChange()
{
	if ($$('outType') == 0)
	{
		$('customerName').value = '';
		$('arriveDate').value = '';
		$('customerId').value = '';
		$('customerName').disabled  = false;
		$('arriveDate').disabled  = false;
		$('reday').value = '';
		$('reday').readOnly = false;
	}
	else
	{
		$('customerName').value = '个人领样';
		$('arriveDate').value = '个人领样';
		$('customerId').value = '0';
		$('customerName').disabled  = true;
		$('arriveDate').disabled  = true;
		$('reday').value = '30';
		$('reday').readOnly = true;
	}
}

function selectOut()
{
	//window.common.modal('../admin/out.do?method=queryOut3&load=1&flag=4');
}

</script>
</head>
<body class="body_class" onload="load()">
<form name="outForm" method=post action="./out.do?"><input
	type=hidden name="method" value="addOut" /><input type=hidden
	name="nameList" /> <input type=hidden name="idsList" /> <input
	type=hidden name="unitList" /> <input type=hidden name="amontList" />
<input type=hidden name="priceList" /> <input type=hidden
	name="totalList" /> <input type=hidden name="totalss" /> <input
	type=hidden name="customerId" /> <input type=hidden name="type"
	value='1' /> <input type=hidden name="saves" value="" />
<input type=hidden name="desList" value="" />
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
				<td width="550" class="navigation">库单管理 &gt;&gt; 填写入库单</td>
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
								<td class="caption"><strong>填写入库单信息:<font color=blue>入库单不管是盘亏还是盘盈还是调入调出都是正数增加库存,负数减少库存</font></strong>
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
						<td width="15%" align="right">入库日期：</td>

						<td width="35%"><input type="text" name="outTime"
							value="${current}" maxlength="20" size="20"
							readonly="readonly"><font color="#FF0000">*</font></td>

							<td width="15%" align="right">入库类型：</td>
							<td width="35%">
							<select name="outType" class="select_class" onchange="titleChange()">
								<option value=''>--</option>
								<option value="0">采购入库</option>
								<option value="1">调出</option>
								<option value="8">调入</option>
								<option value="2">盘亏出库</option>
								<option value="3">盘盈入库</option>
								<option value="5">退换货入库</option>
								<option value="6">报废出库</option>
								<option value="7">采购退货</option>
							</select><font color="#FF0000">*</font></td>
					</tr>

					<tr class="content1">
						<td align="right" id="outd">供应商：</td>
						<td><input type="text" name="customerName" maxlength="14" value=""
							onclick="selectCustomer()" style="cursor: pointer;"
							readonly="readonly"><font color="#FF0000">*</font></td>
						<td align="right">销售部门：</td>
						<td><select name="department" class="select_class">
							<option value=''>--</option>

							<c:forEach items='${departementList}' var="item">
								<option value="${item}">${item}</option>
							</c:forEach>
						</select><font color="#FF0000">*</font></td>
					</tr>

					<tr class="content2">
						<td align="right">联系人：</td>
						<td><input type="text" name="connector" maxlength="14"
							readonly="readonly"></td>
						<td align="right">联系电话：</td>
						<td><input type="text" name="phone" maxlength="20" readonly></td>
					</tr>

					<tr class="content1">
						<td align="right">经手人：</td>
						<td><input type="text" name="stafferName" maxlength="14"
							value="${user.stafferName}" readonly="readonly"></td>
						<td align="right">单据号码：</td>
						<td><input type="text" name="id" maxlength="20"
							value="系统自动生成" readonly></td>
					</tr>

					<tr class="content2">
						<td align="right">运输单号：</td>
						<td><input type="text" name="tranNo" maxlength="40"
							value="" oncheck="notNone"><font color="#FF0000">*</font></td>
						<td align="right"><div id="sdirs1">产品区域：</div></td>
						<td colspan="1"><div id="sdirs"><select id="sdestinationId" name="sdestinationId" class="select_class" oncheck="notNone;noEquals('${currentLocationId}')"
						message="产品区域不能为空或者选择当前区域">
								<option value=''>--</option>
								<c:forEach items="${locationList}" var="item">
								<option value='${item.id}'>${item.locationName}</option>
								</c:forEach>
						</select><font color="#FF0000">*</font></div></td>
					</tr>

					<c:if test='${user.locationID == "0"}'>
					<tr class="content2">
						<td align="right">选择仓区：</td>
						<td colspan="1"><select name="depotpartId" class="select_class">
								<option value=''>--</option>
								<c:forEach items="${depotpartList}" var="item">
								<option value='${item.id}'>${item.name}</option>
								</c:forEach>
							</select><font color="#FF0000">*</font></td>
							<td align="right"><div id="dirs1">目的区域：</div></td>
						<td colspan="1"><div id="dirs"><select name="destinationId" class="select_class" oncheck="notNone;noEquals('${currentLocationId}')"
						message="不能为空或者选择当前区域">
								<option value=''>--</option>
								<c:forEach items="${locationList}" var="item">
								<option value='${item.id}'>${item.locationName}</option>
								</c:forEach>
							</select><font color="#FF0000">*</font></div></td>
					</tr>
					</c:if>

					<tr class="content2" id="in_out">
						<td align="right">调出库单：</td>
						<td colspan="1"><input type="text" name="refOutFullId" maxlength="40" oncheck="notNone;" readonly="readonly"
							value="">&nbsp;&nbsp;<input
							type="button" value="&nbsp;...&nbsp;" name="qout" class="button_class" onclick="selectOut()">
							<font color="#FF0000">*</font></td>

						<td align="right"></td>
						<td></td>
					</tr>

					<tr class="content1">
						<td align="right">入库单备注：</td>
						<td colspan="3"><textarea rows="3" cols="55"
							name="description"></textarea></td>
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
						<td width="15%" align="center">单价</td>
						<td width="15%" align="left">金额<span id="total"></span></td>
						<td width="25%" align="center">成本</td>
						<td width="15%" align="center"><input type="button"
							value="增加" class="button_class" onclick="addTr()"></td>
					</tr>

					<tr class="content1" id="trCopy" style="display: none;">
						<td width="20%"><input type="text" name="productName"
							onclick="opens(this)" productId="" readonly="readonly"
							style="width: 100%; cursor: pointer;"></td>

						<td width="5%"><select name="unit" style="WIDTH: 50px;">
							<option value="套">套</option>
							<option value="枚">枚</option>
							<option value="个">个</option>
							<option value="本">本</option>
						</select></td>

						<td width="10%" align="center"><input type="text"
							style="width: 100%" maxlength="6" onkeyup="cc(this)"
							name="amount"></td>

						<td width="15%" align="center"><input type="text"
							style="width: 100%" maxlength="8" onkeyup="cc(this)"
							onblur="blu(this)" name="price"></td>

						<td width="20%" align="center"><input type="text"
							value="0.00" readonly="readonly" style="width: 100%" name="value"></td>

						<td width="35%" align="center"><input type="text"
							style="width: 100%" name="desciprt"></td>

						<td width="15%" align="center"></td>
					</tr>

					<tr class="content2">
						<td width="20%"><input type="text" name="productName"
							onclick="opens(this)" productId="" readonly="readonly"
							style="width: 100%; cursor: pointer;"></td>

						<td width="5%"><select name="unit" style="WIDTH: 50px;">
							<option value="套">套</option>
							<option value="枚">枚</option>
							<option value="个">个</option>
							<option value="本">本</option>
						</select></td>

						<td width="10%" align="center"><input type="text" style="width: 100%"
							maxlength="6" onkeyup="cc(this)" name="amount"></td>

						<td width="15%" align="center"><input type="text" style="width: 100%"
							maxlength="8" onkeyup="cc(this)" onblur="blu(this)" name="price"></td>

						<td width="20%" align="center"><input type="text"
							value="0.00" readonly="readonly" style="width: 100%" name="value"></td>

						<td width="25%" align="center"><input type="text"
							style="width: 100%" name="desciprt"></td>

						<td width="15%" align="center"></td>
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
		<td width="92%">
		<div align="right"><input type="button" class="button_class" id="b_save"
			value="&nbsp;&nbsp;保 存&nbsp;&nbsp;" onClick="save()" />&nbsp;&nbsp;<input
			type="button" class="button_class"
			value="&nbsp;&nbsp;提 交&nbsp;&nbsp;" onClick="sub()" /></div>
		</td>
		<td width="8%"></td>
	</tr>

</table>
</form>
</body>
</html>

