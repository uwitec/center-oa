<%@ page contentType="text/html;charset=UTF-8" language="java"
	errorPage="../common/error.jsp"%>
<%@include file="../common/common.jsp"%>
<html>
<head>
<p:link title="处理询价" />
<script language="JavaScript" src="../js/common.js"></script>
<script language="JavaScript" src="../js/JCheck.js"></script>
<script language="JavaScript" src="../js/public.js"></script>
<script language="javascript">

var cindex = -1;
function addBean()
{
	submit('确定处理询价?', null, lverify);
}

function lverify()
{
	var checkArr = document.getElementsByName('check_init');

	var isSelect = false;

	var imap = {};

	var count = 0;
	for (var i = 0; i < checkArr.length; i++)
	{
		var obj = checkArr[i];

		var index = obj.value;

		if (obj.checked)
		{
			count++;
			isSelect = true;

			if ($O('customerName_' + i).value == '' || $O('customerId_' + i).value == '' )
			{
				alert('供应商不能为空');
				return false;
			}

			if ($$('hasAmount_' + i)  == null)
			{
				alert('请选择供应商是否满足数量要求');
				return false;
			}

			if (imap[$O('customerId_' + i).value] == $O('customerId_' + i).value)
			{
				alert('选择的供应商不能重复');
				return false;
			}

			imap[$O('customerId_' + i).value] = $O('customerId_' + i).value;
		}
	}

	if(count < 3)
	{
		alert('选择询价供应商必须大于2家');
		return false;
	}

	if (!isSelect)
	{
		alert('请选择询价供应商');
		return false;
	}

	return true;
}
function load()
{
	loadForm();

	init();
}

function init()
{
	var checkArr = document.getElementsByName('check_init');

	for (var i = 0; i < checkArr.length; i++)
	{
		var obj = checkArr[i];

		var index = obj.value;

		if (obj.checked)
		{
			$d('qout_' + index, false);
			$d('price_' + index, false);
			$d('hasAmount_' + index, false);
		}
		else
		{
			$O('price_' + index).value = '';
			$O('customerName_' + index).value = '';
			$O('customerId_' + index).value = '';
			$d('qout_' + index);
			$d('price_' + index);
			$d('hasAmount_' + index);
		}
	}
}

function selectCustomer(index)
{
	cindex = index;
	
	window.common.modal("../provider/provider.do?method=rptQueryProvider&load=1&productTypeId=${product.type}&productId=${product.id}");
}

function getProvider(id, name)
{
	if (cindex != -1)
	{
		$O("customerName_" + cindex).value = name;
		$O("customerId_" + cindex).value = id;
	}
}


</script>

</head>
<body class="body_class" onload="load()">
<form name="addApply" action="../stock/stock.do" method="post"><input
	type="hidden" name="method" value="stockItemAskPrice">
	<input type="hidden" name="customerId_0" value="">
	<input type="hidden" name="customerId_1" value="">
	<input type="hidden" name="customerId_2" value="">
	<input type="hidden" name="customerId_3" value="">
	<input type="hidden" name="customerId_4" value="">
	
	<input type="hidden" name="stockId" value="${id}">
	<input
	type="hidden" name="id" value="${bean.id}"> <p:navigation
	height="22">
	<td width="550" class="navigation"><span style="cursor: hand"
		onclick="javascript:history.go(-1)">询价管理</span> &gt;&gt; 处理采购询价</td>
	<td width="85"></td>
</p:navigation> <br>

<p:body width="98%">

	<p:title>
		<td class="caption"><strong>询价信息：【询价供应商必须大于2家】</strong></td>
	</p:title>

	<p:line flag="0" />

	<p:subBody width="100%">
		<p:table cells="2">
			<p:cell title="采购单号">
			${bean.stockId}
			</p:cell>

			<p:cell title="产品名称">
			${bean.productName}
			</p:cell>

			<p:cell title="产品编码">
			${bean.productCode}
			</p:cell>

			<p:cell title="采购数量">
			${bean.amount}
			</p:cell>

			<p:cells celspan="2" title="参考价格">
			${bean.prePrice}
			</p:cells>


			<p:cells id="selects" celspan="2" title="询价处理">
				<table id="mselect">
					<tr>
						<td>
							<input type="checkbox" name="check_init" value="0" onclick="init()" id="check_init_0">供应商一：<input type="button"
								value="&nbsp;选 择&nbsp;" name="qout_0" class="button_class"
								onclick="selectCustomer(0)">&nbsp;
							供应商:<input
							type="text" name="customerName_0" value="" size="20" readonly="readonly">&nbsp;
							价格:<input
							type="text" name="price_0" value="" size="6" oncheck="isFloat;">&nbsp;
							数量是否满足:<input type="radio" name="hasAmount_0" value="0" id="hasAmount_0_0">满足
							&nbsp;&nbsp;<input type="radio" name="hasAmount_0" value="1" id="hasAmount_0_1">不满足

							</td>
					</tr>

					<tr>
						<td><input type="checkbox" name="check_init" value="1" onclick="init()" id="check_init_1">供应商二：<input type="button"
								value="&nbsp;选 择&nbsp;" name="qout_1" class="button_class"
								onclick="selectCustomer(1)">&nbsp;
							供应商:<input
							type="text" name="customerName_1" value="" size="20" readonly="readonly">&nbsp;
							价格:<input
							type="text" name="price_1" value="" size="6" oncheck="isFloat;">&nbsp;
							数量是否满足:<input type="radio" name="hasAmount_1" value="0" id="hasAmount_1_0">满足
							&nbsp;&nbsp;<input type="radio" name="hasAmount_1" value="1" id="hasAmount_1_1">不满足

						</td>
					</tr>

					<tr>
						<td><input type="checkbox" name="check_init" value="2" onclick="init()" id="check_init_2">供应商三：<input type="button"
								value="&nbsp;选 择&nbsp;" name="qout_2" class="button_class"
								onclick="selectCustomer(2)">&nbsp;
							供应商:<input
							type="text" name="customerName_2" value="" size="20" readonly="readonly">&nbsp;
							价格:<input
							type="text" name="price_2" value="" size="6" oncheck="isFloat;">&nbsp;
							数量是否满足:<input type="radio" name="hasAmount_2" value="0" id="hasAmount_2_0">满足
							&nbsp;&nbsp;<input type="radio" name="hasAmount_2" value="1" id="hasAmount_2_1">不满足
							</td>
					</tr>

					<tr>
						<td><input type="checkbox" name="check_init" value="3" onclick="init()" id="check_init_03">供应商四：<input type="button"
								value="&nbsp;选 择&nbsp;" name="qout_3" class="button_class"
								onclick="selectCustomer(3)">&nbsp;
							供应商:<input
							type="text" name="customerName_3" value="" size="20" readonly="readonly">&nbsp;
							价格:<input
							type="text" name="price_3" value="" size="6" oncheck="isFloat;">&nbsp;
							数量是否满足:<input type="radio" name="hasAmount_3" value="0" id="hasAmount_3_0">满足
							&nbsp;&nbsp;<input type="radio" name="hasAmount_3" value="1" id="hasAmount_3_1">不满足
							</td>
					</tr>

					<tr>
						<td><input type="checkbox" name="check_init" value="4" onclick="init()" id="check_init_4">供应商五：<input type="button"
								value="&nbsp;选 择&nbsp;" name="qout_4" class="button_class"
								onclick="selectCustomer(4)">&nbsp;
							供应商:<input
							type="text" name="customerName_4" value="" size="20" readonly="readonly">&nbsp;
							价格:<input
							type="text" name="price_4" value="" size="6" oncheck="isFloat;">&nbsp;
							数量是否满足:<input type="radio" name="hasAmount_4" value="0" id="hasAmount_4_0">满足
							&nbsp;&nbsp;<input type="radio" name="hasAmount_4" value="1" id="hasAmount_4_1">不满足
							</td>
					</tr>
				</table>
			</p:cells>

		</p:table>
	</p:subBody>

	<p:line flag="1" />

	<p:button leftWidth="100%" rightWidth="0%">
		<div align="right"><input type="button" class="button_class"
			name="adds" style="cursor: pointer"
			value="&nbsp;&nbsp;确认询价&nbsp;&nbsp;" onclick="addBean()">&nbsp;&nbsp;
		<input type="button" class="button_class"
			onclick="javascript:history.go(-1)"
			value="&nbsp;&nbsp;返 回&nbsp;&nbsp;"></div>
	</p:button>

	<p:message2/>
	
</p:body></form>
</body>
</html>

