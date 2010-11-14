<%@ page contentType="text/html;charset=UTF-8" language="java"
	errorPage="../common/error.jsp"%>
<%@include file="../common/common.jsp"%>

<html>
<head>
<p:link title="填写销售单" />
<script language="JavaScript" src="../js/common.js"></script>
<script language="JavaScript" src="../js/math.js"></script>
<script language="JavaScript" src="../js/public.js"></script>
<script language="JavaScript" src="../js/JCheck.js"></script>
<script language="JavaScript" src="../sail_js/addOut.js"></script>
<script language="javascript">

//当前的焦点对象
var oo;

var ids = '';
var amous = '';
var tsts;
var messk = '';
var locationId = '${currentLocationId}';
var currentLocationId = '${currentLocationId}';

/**
 * 查询库存
 */
function opens(obj)
{
	oo = obj;
	
	window.common.modal('../depot/storage.do?method=rptQueryStorageRelationInDepot&load=1&depotId='+ $$('location'));
}

function selectCustomer()
{
    window.common.modal("../customer/customer.do?method=rptQuerySelfCustomer&stafferId=${user.stafferId}&load=1");
}

//默认黑名单
var BLACK_LEVEL = '90000000000000000000';

function getCustomer(oos)
{
	var obj = oos;
	
	$O("connector").value = obj.pconnector;
	$O("phone").value = obj.phandphone;
	$O("customerName").value = obj.pname;
	$O("customerId").value = obj.value;
	$O("customercreditlevel").value = obj.pcreditlevelid;
	
	if (obj.pcreditlevelid == BLACK_LEVEL)
	{
	    removeAllItem($O('reserve3'));
	    
	    setOption($O('reserve3'), '1', '款到发货(黑名单客户)');   
	}
	else
	{
	    removeAllItem($O('reserve3'));
        
        setOption($O('reserve3'), '2', '客户信用和业务员信用额度担保');  
        setOption($O('reserve3'), '1', '款到发货(黑名单客户)');  
        setOption($O('reserve3'), '3', '分公司经理担保');  
	}
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
	<c:if test="${ff}">
	if ($$('outType') == '1' || $$('outType') == '4')
	{
		$O('outd').innerText = '调出部门：';
	}
	else
	{
		$O('outd').innerText = '供应商：';
	}

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

		if ($O('refOutFullId'))
		$O('refOutFullId').value = '';
	}
	</c:if>
}

function load()
{
	titleChange();
	loadForm();

	hides(true);
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

function showTr(boo)
{
	$v('in_out', boo);
	$v('refOutFullId', boo);
}

function getOutId(id)
{
	$O('refOutFullId').value = id;
}

function check()
{
	if (!formCheck())
	{
		return false;
	}

	ids = '';
	amous = '';
	$O('priceList').value = '';
	$O('totalList').value = '';
	$O('nameList').value = '';
	$O('unitList').value = '';
	if (trim($O('outTime').value) == '')
	{
		alert('请选择销售日期');
		return false;
	}

	if ($$('outType') == '')
	{
		alert('请选择库单类型');
		return false;
	}

	if ($O('customerId').value == '')
	{
		alert('请选择客户');
		return false;
	}

	if ($$('department') == '')
	{
		alert('请选择销售部门');
		return false;
	}

	if (!eCheck([$O('reday')]))
	{
		alert('请填入1到180之内的数字');
		$O('reday').focus();
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
		if (proNames[i].value == '' || proNames[i].productId == '')
		{
			alert('数据不完整,请选择产品名称!');
			return false;
		}

		ids = ids + proNames[i].productId + '~';

		$O('nameList').value = $O('nameList').value +  proNames[i].value + '~';

		$O('idsList').value = ids;
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

		$O('amontList').value = amous;
	}

	for (var i = 1; i < prices.length; i++)
	{
		if (trim(prices[i].value) == '')
		{
			alert('数据不完整,请填写产品价格!');
			prices[i].focus();
			return false;
		}
		
		if (parseInt(trim(prices[i].value)) == 0)
        {
            if (!window.confirm('除非赠品单价不要填0,否则到总裁审批,你确定?'))
            {
                 prices[i].focus();
                 return false;
            }
        }

		if (!isFloat(prices[i].value))
		{
			alert('数据错误,产品数量只能是浮点数!');
			prices[i].focus();
			return false;
		}

		$O('priceList').value = $O('priceList').value + prices[i].value + '~';
	}

	var desList = document.getElementsByName('desciprt');
	for (var i = 1; i < desList.length; i++)
	{
		if (trim(desList[i].value) == '')
		{
		    alert('成本是必填!');
            desList[i].focus();
            return false;
		}
		
		if (!isFloat(desList[i].value))
		{
			alert('格式错误,成本只能是浮点数!');
			desList[i].focus();
			return false;
		}
	}
	for (var i = 1; i < values.length; i++)
	{
		$O('totalList').value = $O('totalList').value + values[i].value + '~';
		$O('desList').value = $O('desList').value + desList[i].value + '~';
	}

	for (var i = 1; i < units.length; i++)
	{
		$O('unitList').value = $O('unitList').value + units[i].value + '~';
	}

	$O('totalss').value = tsts;

	return true;
}

function checkTotal()
{
	messk = '';
	var gh = $O('nameList').value.split('~');
	var ghk = $O('amontList').value.split('~');

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

	if ($O('saves').value != '')
    {

	     if (window.confirm("确定保存库单?" + messk))
	     {
	     	$O('id').value = '';
	     	disableAllButton();
	     	outForm.submit();
	     }

	     return;
    }

    if ($O('location'))
    {
    	ccv = $F('location');
    }
    else
    {
    	ccv = locationId;
    }

    if (ccv == '')
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

	if (window.confirm("确定提交库单?" + messk))
    {
    	$O('id').value = '';
    	disableAllButton();
    	outForm.submit();
    }
}

function save()
{
	$O('saves').value = 'saves';
	if (check())
	{
		checkTotal();
	}
}

function sub()
{
	$O('saves').value = '';
	if (check())
	{
		checkTotal();
	}
}

function managerChange()
{
	if ($$('outType') == 0)
	{
		$O('customerName').value = '';
		$O('customerId').value = '';
		$O('customerName').disabled  = false;
		$O('reday').value = '';
		$O('reday').readOnly = false;
	}
	else
	{
		$O('customerName').value = '个人领样';
		$O('customerId').value = '0';
		$O('customerName').disabled  = true;
		$O('reday').value = '${goDays}';
		$O('reday').readOnly = true;
	}
}

function selectOut()
{
	window.common.modal('../admin/out.do?method=queryOut3&load=1&flag=4');
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
	value='0' /> <input type=hidden name="saves" value="" />
<input type=hidden name="desList" value="" />
<input type=hidden name="customercreditlevel" value="" />
<p:navigation
	height="22">
	<td width="550" class="navigation">库单管理 &gt;&gt; 填写销售单(如果需要增加开单品名,请到 公共资源-->配置管理)</td>
				<td width="85"></td>
</p:navigation> <br>

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
								<td class="caption"><strong>填写销售单信息:<font color=red>${hasOver}</font> 您的信用额度还剩下:${credit}</strong>
								<font color="blue">产品仓库：</font>
								<select name="location" class="select_class" values="${currentLocationId}" onchange="clearsAll()">
									<c:forEach items='${locationList}' var="item">
										<option value="${item.id}">${item.name}</option>
									</c:forEach>
								</select>
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
		<table width="100%" border="0" cellpadding="0" cellspacing="0"
			class="border">
			<tr>
				<td>
				<table width="100%" border="0" cellspacing='1'>
					<tr class="content2">
						<td width="15%" align="right">销售日期：</td>

						<td width="35%"><input type="text" name="outTime"
							value="${current}" maxlength="20" size="20"
							readonly="readonly"><font color="#FF0000">*</font></td>

							<td width="15%" align="right">销售类型：</td>
							<td width="35%"><select name="outType" class="select_class" onchange="managerChange()">
								<option value="0">销售出库</option>
								<option value="1">个人领样</option>
							</select><font color="#FF0000">*</font></td>
						
					</tr>

					<tr class="content1">
						<td align="right" id="outd">客户：</td>
						<td><input type="text" name="customerName" maxlength="14" value=""
							onclick="selectCustomer()" style="cursor: pointer;"
							readonly="readonly"><font color="#FF0000">*</font></td>
						<td align="right">销售部门：</td>
						<td><select name="department" class="select_class">
							<option value=''>--</option>

							<c:forEach items='${departementList}' var="item">
								<option value="${item.name}">${item.name}</option>
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
						<td align="right">回款天数：</td>
						<td colspan="1"><input type="text" name="reday" maxlength="4" oncheck="notNone;isInt;range(1, 180)"
							value="" title="请填入1到180之内的数字"><font color="#FF0000">*</font></td>

						<td align="right">到货日期：</td>
						<td><p:plugin name="arriveDate"  size="20" oncheck="notNone;cnow('30')"/><font color="#FF0000">*</font></td>
					</tr>
					
					<tr class="content2">
                        <td align="right">付款方式：</td>
                        <td colspan="3">
                        <select name="reserve3" class="select_class" oncheck="notNone;" head="付款方式" style="width: 240px">
                            <option value='2'>客户信用和业务员信用额度担保</option>
                            <option value='1'>款到发货(黑名单客户)</option>
                            <option value='3'>分公司经理担保</option>
                        </select>
                        <font color="#FF0000">*</font></td>
                    </tr>

					<tr class="content1">
						<td align="right">销售单备注：</td>
						<td colspan="3"><textarea rows="3" cols="55" oncheck="notNone;"
							name="description"></textarea>
							<font color="#FF0000">*</font>
							<b>(请填写所销售的产品,因为短信审批会发送此内容给您的主管)</b></td>
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
		<table width="100%" border="0" cellpadding="0" cellspacing="0"
			class="border">
			<tr>
				<td>
				<table width="100%" border="0" cellspacing='1' id="tables">
					<tr align="center" class="content0">
						<td width="20%" align="center">品名</td>
						<td width="5%" align="center">单位</td>
						<td width="5%" align="center">数量</td>
						<td width="10%" align="center">单价</td>
						<td width="10%" align="left">金额<span id="total"></span></td>
						<td width="10%" align="center">成本</td>
						<td width="25%" align="center">类型</td>
						<td width="15%" align="center">开单品名</td>
						<td width="15%" align="center"><input type="button"
							value="增加" class="button_class" onclick="addTr()"></td>
					</tr>

					<tr class="content1" id="trCopy" style="display: none;">
						<td><input type="text" name="productName"
							onclick="opens(this)" productId="" readonly="readonly"
							style="width: 100%; cursor: hand"></td>

						<td><select name="unit" style="WIDTH: 50px;">
							<option value="套">套</option>
							<option value="枚">枚</option>
							<option value="个">个</option>
							<option value="本">本</option>
						</select></td>

						<td align="center"><input type="text"
							style="width: 100%" maxlength="6" onkeyup="cc(this)"
							name="amount"></td>

						<td align="center"><input type="text"
							style="width: 100%" maxlength="8" onkeyup="cc(this)"
							onblur="blu(this)" name="price"></td>

						<td align="center"><input type="text"
							value="0.00" readonly="readonly" style="width: 100%" name="value"></td>

						<td align="center"><input type="text" readonly="readonly"
							style="width: 100%" name="desciprt"></td>
							
						<td align="center"><input type="text" readonly="readonly"
							style="width: 100%" name="rstafferName"></td>
							
						<td  align="center">
						<select name="outProductName" style="WIDTH: 150px;">
							<p:option type="123"></p:option>
						</select>
						</td>

						<td align="center"></td>
					</tr>

					<tr class="content2">
						<td><input type="text" name="productName" id="unProductName"
							onclick="opens(this)" productId="" readonly="readonly"
							style="width: 100%; cursor: pointer"></td>

						<td><select name="unit" style="WIDTH: 50px;">
							<option value="套">套</option>
							<option value="枚">枚</option>
							<option value="个">个</option>
							<option value="本">本</option>
						</select></td>

						<td align="center"><input type="text" style="width: 100%" id="unAmount"
							maxlength="6" onkeyup="cc(this)" name="amount"></td>

						<td align="center"><input type="text" style="width: 100%" id="unPrice"
							maxlength="8" onkeyup="cc(this)" onblur="blu(this)" name="price"></td>

						<td align="center"><input type="text"
							value="0.00" readonly="readonly" style="width: 100%" name="value"></td>

						<td align="center"><input type="text" id="unDesciprt" readonly="readonly"
							style="width: 100%" name="desciprt"></td>
							
						<td align="center"><input type="text" id="unRstafferName" readonly="readonly"
							style="width: 100%" name="rstafferName"></td>
							
						<td align="center">
						<select name="outProductName" style="WIDTH: 150px;">
							<p:option type="123"></p:option>
						</select>
						</td>

						<td align="center"><input type=button value="清空"  class="button_class" onclick="clears()"></td>
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
			value="&nbsp;&nbsp;保 存&nbsp;&nbsp;" onClick="save()" />&nbsp;&nbsp;<input
			type="button" class="button_class" id="sub_b"
			value="&nbsp;&nbsp;提 交&nbsp;&nbsp;" onClick="sub()" /></div>
		</td>
		<td width="0%"></td>
	</tr>

</table>
</form>
</body>
</html>

