<%@ page contentType="text/html;charset=UTF-8" language="java"
	errorPage="../common/error.jsp"%>
<%@include file="../common/common.jsp"%>

<html>
<head>
<p:link title="入库单明细" />
<script language="JavaScript" src="../js/common.js"></script>
<script language="JavaScript" src="../js/math.js"></script>
<script language="JavaScript" src="../js/public.js"></script>
<script language="JavaScript" src="../js/cnchina.js"></script>
<script language="JavaScript" src="../js/JCheck.js"></script>
<script language="JavaScript" src="../js/key.js"></script>
<script language="JavaScript" src="../js/compatible.js"></script>
<script language="JavaScript" src="../sail_js/addOut.js"></script>
<script language="JavaScript" src="../js/jquery/jquery.js"></script>
<script language="JavaScript" src="../js/plugin/highlight/jquery.highlight.js"></script>
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
	
	window.common.modal('../depot/storage.do?method=rptQueryStorageRelationInDepot&showAbs=1&load=1&depotId='+ $$O('location') + '&name=' + encodeURIComponent(obj.value));
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
	
	if (obj.pcreditlevelid == BLACK_LEVEL || $$O('outType') == 2)
	{
	    removeAllItem($O('reserve3'));
	    
	    setOption($O('reserve3'), '1', '款到发货(黑名单客户)');   
	}
	else
	{
	    resetReserve3();
	}
}

function resetReserve3()
{
    removeAllItem($O('reserve3'));
        
    setOption($O('reserve3'), '2', '客户信用和业务员信用额度担保');  
    setOption($O('reserve3'), '1', '款到发货(黑名单客户/零售)');  
    setOption($O('reserve3'), '3', '分公司经理担保');  
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
	
}

function load()
{
	titleChange();
	
	loadForm();

	hides(true);
	
	$detail($O('viewTable'), ['pr', 'ba']);
	
	highlights($("#mainTable").get(0), ['未付款'], 'red');
	
	highlights($("#mainTable").get(0), ['已付款'], 'blue');
}

function hides(boo)
{
	
}

function showTr(boo)
{
	
}

function getOutId(id)
{
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
	$O('otherList').value = '';
	$O('showIdList').value = '';
	$O('showNameList').value = '';
	
	if (trim($O('outTime').value) == '')
	{
		alert('请选择入库日期');
		return false;
	}

	if ($$O('outType') == '')
	{
		alert('请选择库单类型');
		return false;
	}

	if ($O('customerId').value == '')
	{
		alert('请选择客户');
		return false;
	}

	if ($$O('department') == '')
	{
		alert('请选择入库部门');
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
	var outProductNames = document.getElementsByName('outProductName');

    var tmpMap = {};
	//isNumbers
	for (var i = 1; i < proNames.length; i++)
	{
		if (proNames[i].value == '' || proNames[i].productid == '')
		{
			alert('数据不完整,请选择产品名称!');
			
			return false;
		}

		ids = ids + proNames[i].productid + '~';

		$O('nameList').value = $O('nameList').value +  proNames[i].value + '~';
		
		var ikey = toUnqueStr2(proNames[i]);
		
		if (tmpMap[ikey])
		{
		    alert('选择的产品重复[仓区+产品+职员+价格],请核实!');
            
            return false;
		}
		
		tmpMap[ikey] = ikey;
		
		//库存重要的标识
		$O('otherList').value = $O('otherList').value + ikey + '~';

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
	
	for (var i = 1; i < outProductNames.length; i++)
    {
        if (trim(outProductNames[i].value) == '')
        {
            alert('数据不完整,请选择!');
            outProductNames[i].focus();
            return false;
        }

        amous = amous + amounts[i].value + '~';

        $O('showIdList').value =  $O('showIdList').value + outProductNames[i].value + '~';
        
        $O('showNameList').value =  $O('showNameList').value + getOptionText(outProductNames[i]) + '~';
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


	 if ($$O('outType') == 1 && $$O('type') == 1)
	 {
	 	 if (!window.confirm('您当前所操作的是调出，调出时也是正数增加库存，负数减少库存，您确认填写的调出符合实际情形?'))
	 	 {
	 	 	return;
	 	 }
	 }

	if ($O('saves').value == 'save')
    {
	     if (window.confirm("确定保存库单?" + messk))
	     {
	     	disableAllButton();
	     	outForm.submit();
	     }

	     return;
    }

    ccv = $$O('location');

    if (ccv == '')
    {
    	alert('产品仓库为空，请核实');
    	return false;
    }

    //判断method
    if ($$O('method') != 'addOut')
    {
    	alert('提示：提交没有方法，请重新登录操作');
    	return false;
    }

	if (window.confirm("确定提交库单?" + messk))
    {
    	disableAllButton();
    	outForm.submit();
    }
}

function save()
{
	$O('saves').value = 'save';
	if (check())
	{
		checkTotal();
	}
}

function sub()
{
	$O('saves').value = 'submit';
	if (check())
	{
		checkTotal();
	}
}

function managerChange()
{
    //普通入库
	if ($$O('outType') == 0)
	{
		$O('customerName').value = '';
		$O('customerId').value = '';
		$O('customerName').disabled  = false;
		$O('reday').value = '';
		$O('reday').readOnly = false;
		
		if (obj.pcreditlevelid == BLACK_LEVEL)
	    {
	        removeAllItem($O('reserve3'));
	        
	        setOption($O('reserve3'), '1', '款到发货(黑名单客户)');   
	    }
	    else
	    {
	        resetReserve3();
	    }
	}
	
	//个人领样
	if ($$O('outType') == 1)
	{
		$O('customerName').value = '个人领样';
		$O('customerId').value = '99';
		$O('customerName').disabled  = true;
		$O('reday').value = '${goDays}';
		$O('reday').readOnly = true;
		
		resetReserve3();
	}
	
	//零售 是给公共客户的
	if ($$O('outType') == 2)
    {
        $O('customerName').value = '公共客户';
        $O('customerId').value = '99';
        $O('customerName').disabled  = true;
        $O('reday').value = '';
        $O('reday').readOnly = false;
        
        removeAllItem($O('reserve3'));
        
        setOption($O('reserve3'), '1', '款到发货(黑名单客户/零售)');   
    }
}

function pagePrint()
{
    $O('na').style.display = 'none';
    $O('pr').style.display = 'none';
    $O('ba').style.display = 'none';
    $O('desc1').style.display = 'none';
    window.print();

    $O('pr').style.display = 'inline';
    $O('ba').style.display = 'inline';
    $O('na').style.display = 'block';
    $O('desc1').style.display = 'block';
}

</script>
</head>
<body class="body_class" onload="load()">
<form name="outForm" method=post action="../sail/out.do">
<input
	type=hidden name="method" value="addOut" />
<input type=hidden name="nameList" /> 
<input type=hidden name="idsList" /> 
<input
	type=hidden name="unitList" /> 
	<input type=hidden name="amontList" />
<input type=hidden name="priceList" /> 
<input type=hidden
	name="totalList" /> 
<input type=hidden name="totalss" /> 
<input type=hidden name="customerId" value="${bean.customerId}"/> 
<input type=hidden name="type"
	value='0' /> 
<input type=hidden name="saves" value="" />
<input type=hidden name="desList" value="" />
<input type=hidden name="otherList" value="" />
<input type=hidden name="showIdList" value="" />
<input type=hidden name="showNameList" value="" />
<input type=hidden name="customercreditlevel" value="" />
<div id="na">
<p:navigation
	height="22">
	<td width="550" class="navigation">入库单明细</td>
				<td width="85"></td>
</p:navigation> <br>
</div>

<table width="95%" border="0" cellpadding="0" cellspacing="0" id="viewTable"
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
								<td class="caption"><strong>填写入库单信息:<font color=red>${hasOver}</font> 您的信用额度还剩下:${credit}</strong>
								<font color="blue">产品仓库：</font>
								<select name="location" class="select_class"  onchange="clearsAll()" values="${bean.location}" readonly=true>
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
		<table width="100%" border="0" cellpadding="0" cellspacing="0" id="mainTable"
			class="border">
			<tr>
				<td>
				<table width="100%" border="0" cellspacing='1'>
					<tr class="content2">
						<td width="15%" align="right">入库日期：</td>

						<td width="35%"><input type="text" name="outTime"
							value="${bean.outTime}" maxlength="20" size="20"
							readonly="readonly"><font color="#FF0000">*</font></td>

							<td width="15%" align="right">入库类型：</td>
							<td width="35%"><select name="outType" class="select_class" onchange="managerChange()" values="${bean.outType}" readonly=true>
								<p:option type="outType_in"></p:option>
							</select><font color="#FF0000">*</font></td>
						
					</tr>

					<tr class="content1">
						<td align="right" id="outd">供应商：</td>
						<td><input type="text" name="customerName" maxlength="14" value="${bean.customerName}" onclick="selectCustomer()"
							 style="cursor: pointer;"
							readonly="readonly"><font color="#FF0000">*</font></td>
						<td align="right">入库部门：</td>
						<td><select name="department" class="select_class" values="${bean.department}">
							<option value=''>--</option>
							<c:forEach items='${departementList}' var="item">
								<option value="${item.name}">${item.name}</option>
							</c:forEach>
						</select><font color="#FF0000">*</font></td>
					</tr>
					
					<tr class="content2">
						<td align="right">联系人：</td>
						<td><input type="text" name="connector" maxlength="14" value="${bean.connector}"
							readonly="readonly"></td>
						<td align="right">联系电话：</td>
						<td><input type="text" name="phone" maxlength="20" readonly="readonly" value="${bean.phone}"></td>
					</tr>
					<tr class="content1">
						<td align="right">经手人：</td>
						<td><input type="text" name="stafferName" maxlength="14"
							value="${user.stafferName}" readonly="readonly"></td>
						<td align="right">单据标识：</td>
						<td><input type="text" name="fullId" maxlength="40"
							value="${bean.fullId}" readonly="readonly"></td>
					</tr>

					<tr class="content2">
						<td align="right">回款天数：</td>
						<td colspan="1"><input type="text" name="reday" maxlength="4" oncheck="notNone;isInt;range(1, 180)"
							value="${bean.reday}" title="请填入1到180之内的数字"><font color="#FF0000">*</font></td>

						<td align="right">到货日期：</td>
						<td><p:plugin name="arriveDate"  size="20" oncheck="notNone;cnow('30')" value="${bean.arriveDate}"/><font color="#FF0000">*</font></td>
					</tr>
					
					<tr class="content1">
                        <td align="right">付款方式：</td>
                        <td colspan="1">
                        <select name="reserve3" class="select_class" oncheck="notNone;" head="付款方式" style="width: 240px" values="${bean.reserve3}">
                            <option value='2'>客户信用和业务员信用额度担保</option>
                            <option value='1'>款到发货(黑名单客户/零售)</option>
                            <option value='3'>分公司经理担保</option>
                        </select>
                        <font color="#FF0000">*</font></td>
                        <td align="right">纳税实体：</td>
                        <td colspan="1">
                        <select name="dutyId" class="select_class" style="width: 240px" values="${bean.dutyId}">
                            <c:forEach items="${dutyList}" var="item">
                            <option value="${item.id}">${item.name}</option>
                            </c:forEach>
                        </select>
                        <font color="#FF0000">*</font></td>
                    </tr>
                    
                    <tr class="content2">
                        <td align="right">发票类型：</td>
                        <td colspan="1">
                        <select name="invoiceId" class="select_class" head="发票类型" style="width: 400px" values="${bean.invoiceId}">
                           <option value="">没有发票</option>
                            <c:forEach items="${invoiceList}" var="item">
                            <option value="${item.id}">${item.fullName}</option>
                            </c:forEach>
                        </select>
                        <font color="#FF0000">*</font></td>
                        <td align="right">总金额：</td>
                        <td colspan="1">
                       ${my:formatNum(bean.total)}
                       </td>
                    </tr>
                    
                     <tr class="content1">
                        <td align="right">状态：</td>
                        <td colspan="1">
                        <select name="status" class="select_class"  values="${bean.status}">
                           <p:option type="outStatus"></p:option>
                        </select>
                        </td>
                        <td align="right">申请人：</td>
                        <td colspan="1">
                       ${bean.stafferName}
                       </td>
                    </tr>
                    
                     <tr class="content2">
                        <td align="right">分公司：</td>
                        <td colspan="1">
                       ${bean.locationName}
                       </td>
                       <td align="right">付款状态：</td>
                        <td colspan="1">
                       ${my:get('outPay', bean.pay)}
                       </td>
                    </tr>
                    
                    <tr class="content1">
                        <td align="right">信用描述：</td>
                        <td colspan="3">
                        <font color="red">
                       ${bean.reserve6}
                       </font>
                       </td>
                    </tr>
                    
                     <tr class="content2">
                        <td align="right">信用担保：</td>
                        <td colspan="3">
                       客户:${my:formatNum(bean.curcredit)}/${bean.stafferName}:${my:formatNum(bean.staffcredit)}/分公司经理:${my:formatNum(bean.managercredit)}
                       </td>
                    </tr>

					<tr class="content1">
						<td align="right">入库单备注：</td>
						<td colspan="3"><textarea rows="3" cols="55" oncheck="notNone;"
							name="description"><c:out value="${bean.description}"/></textarea>
							<font color="#FF0000">*</font>
							</td>
					</tr>
					
					 <tr class="content2">
                        <td align="right">总部核对：</td>
                        <td colspan="3">
                       ${bean.checks}
                       </td>
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
						<td width="10%" align="center">入库价</td>
						<td width="10%" align="center">金额<span id="total"></span></td>
						<td width="10%" align="center">成本</td>
						<td width="25%" align="center">类型</td>
						<td width="15%" align="center">开单品名</td>
					</tr>

					<tr class="content1" id="trCopy" style="display: none;">
						<td>
						<input type="text" name="productName"
							onclick="opens(this)"
							productid="" 
							productcode="" 
							price=""
							stafferid=""
							depotpartid=""
							readonly="readonly"
							style="width: 100%; cursor: hand">
						</td>

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
						<select name="outProductName" style="WIDTH: 150px;" quick=true>
							<p:option type="123"></p:option>
						</select>
						</td>

						<td align="center"></td>
					</tr>

					<tr class="content2">
						<td align="center"><input type="text" name="productName" id="unProductName"
							onclick="opens(this)" 
							productid="${fristBase.productId}" 
                            productcode="" 
                            price="${my:formatNum(fristBase.costPrice)}"
                            stafferid="${fristBase.owner}"
                            depotpartid="${fristBase.depotpartId}"
							readonly="readonly"
							style="width: 100%; cursor: pointer"
							value="${fristBase.productName}"></td>

						<td align="center"><select name="unit" style="WIDTH: 50px;" values="${fristBase.unit}">
							<option value="套">套</option>
							<option value="枚">枚</option>
							<option value="个">个</option>
							<option value="本">本</option>
						</select></td>

						<td align="center"><input type="text" style="width: 100%" id="unAmount" value="${fristBase.amount}"
							maxlength="6" onkeyup="cc(this)" name="amount"></td>

						<td align="center"><input type="text" style="width: 100%" id="unPrice" value="${fristBase.price}"
							maxlength="8" onkeyup="cc(this)" onblur="blu(this)" name="price"></td>

						<td align="center"><input type="text" value="${fristBase.value}"
							value="0.00" readonly="readonly" style="width: 100%" name="value"></td>

						<td align="center"><input type="text" id="unDesciprt" readonly="readonly" value="${fristBase.description}"
							style="width: 100%" name="desciprt"></td>
							
						<td align="center"><input type="text" id="unRstafferName" readonly="readonly" value="${fristBase.depotpartName}-->${fristBase.ownerName}"
							style="width: 100%" name="rstafferName"></td>
							
						<td align="center">
						<select name="outProductName" style="WIDTH: 150px;" quick=true values="${fristBase.showId}">
							<p:option type="123"></p:option>
						</select>
						</td>

					</tr>
					
					<c:forEach items="${lastBaseList}" var="fristBase" varStatus="vs">
                    <tr class="content2">
                        <td align="center"><input type="text" name="productName"
                            onclick="opens(this)" 
                            productid="${fristBase.productId}" 
                            productcode="" 
                            price="${my:formatNum(fristBase.costPrice)}"
                            stafferid="${fristBase.owner}"
                            depotpartid="${fristBase.depotpartId}"
                            readonly="readonly"
                            style="width: 100%; cursor: pointer"
                            value="${fristBase.productName}"></td>

                        <td align="center"><select name="unit" style="WIDTH: 50px;" values="${fristBase.unit}">
                            <option value="套">套</option>
                            <option value="枚">枚</option>
                            <option value="个">个</option>
                            <option value="本">本</option>
                        </select></td>

                        <td align="center"><input type="text" style="width: 100%"  value="${fristBase.amount}"
                            maxlength="6" onkeyup="cc(this)" name="amount"></td>

                        <td align="center"><input type="text" style="width: 100%"  value="${fristBase.price}"
                            maxlength="8" onkeyup="cc(this)" onblur="blu(this)" name="price"></td>

                        <td align="center"><input type="text" value="${fristBase.value}"
                            value="0.00" readonly="readonly" style="width: 100%" name="value"></td>

                        <td align="center"><input type="text"  readonly="readonly" value="${fristBase.description}"
                            style="width: 100%" name="desciprt"></td>
                            
                        <td align="center"><input type="text" readonly="readonly" value="${fristBase.depotpartName}-->${fristBase.ownerName}"
                            style="width: 100%" name="rstafferName"></td>
                            
                        <td align="center">
                        <select name="outProductName" style="WIDTH: 150px;" quick=true values="${fristBase.showId}">
                            <p:option type="123"></p:option>
                        </select>
                        </td>

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
        </div>
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
        <div align="right"><input type="button" name="pr"
            class="button_class" onclick="pagePrint()"
            value="&nbsp;&nbsp;打 印&nbsp;&nbsp;">&nbsp;&nbsp;<input
            type="button" name="ba" class="button_class"
            onclick="javascript:history.go(-1)"
            value="&nbsp;&nbsp;返 回&nbsp;&nbsp;"></div>
        </td>
        <td width="0%"></td>
    </tr>

</table>
</form>
</body>
</html>

