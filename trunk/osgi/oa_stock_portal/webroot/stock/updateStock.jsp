<%@ page contentType="text/html;charset=UTF-8" language="java"
	errorPage="../common/error.jsp"%>
<%@include file="../common/common.jsp"%>
<html>
<head>
<p:link title="处理采购" />
<script language="JavaScript" src="../js/common.js"></script>
<script language="JavaScript" src="../js/JCheck.js"></script>
<script language="JavaScript" src="../js/public.js"></script>
<script language="JavaScript" src="../js/cnchina.js"></script>
<script language="javascript">

var cindex = -1;
function addBean()
{
	submit('确定申请产品采购?', null, lverify);
}

var maxItem = ${maxItem};

function lverify()
{
	var checkArr = document.getElementsByName('check_init');

	var isSelect = false;

	var imap = {};

	for (var i = 0; i < checkArr.length; i++)
	{
		var obj = checkArr[i];

		var index = obj.value;

		if (obj.checked)
		{
			isSelect = true;
			if ($O('productName_' + i).value == '' || $O('productId_' + i).value == '' )
			{
				alert('产品不能为空');
				return false;
			}

			if ($$('amount_' + i)  == null)
			{
				alert('请选择产品是否满足数量要求');
				return false;
			}

			if (imap[$O('productId_' + i).value] == $O('productId_' + i).value)
			{
				alert('选择的产品不能重复');
				return false;
			}

			imap[$O('productId_' + i).value] = $O('productId_' + i).value;
		}
	}

	if (!isSelect)
	{
		alert('请选择采购产品');
		return false;
	}

	return true;
}
function load()
{
	loadForm();

	isel();

	init();
}

function isel()
{
	var checkArr = document.getElementsByName('check_init');

	for (var i = 0; i < checkArr.length; i++)
	{
		if (i < maxItem)
		{
			checkArr[i].checked = true;
		}
	}
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
			$d('amount_' + index, false);
		}
		else
		{
			$O('price_' + index).value = '';
			$O('productName_' + index).value = '';
			$O('productId_' + index).value = '';
			$d('qout_' + index);
			$d('price_' + index);
			$d('amount_' + index);
		}
	}
}

function selectProduct(index)
{
    cindex = index;
    
    if ($$('type') == 0)
    {
       window.common.modal(RPT_PRODUCT);
    }
    else
    {
       window.common.modal(RPT_PRODUCT);
    }
}

function getProduct(oos)
{
	var oo = oos[0];
	if (cindex != -1)
	{
		$O("productName_" + cindex).value = oo.pname;
		$O("productId_" + cindex).value = oo.value;
	}
}

function getPriceAskProvider(oo)
{
    if (cindex != -1)
    {
        $O("productName_" + cindex).value = oo.pn;
        $O("productId_" + cindex).value = oo.value;
        $O("price_" + cindex).value = oo.pp;
        $O("netaskId_" + cindex).value = oo.ppid;
    }
}

</script>

</head>
<body class="body_class" onload="load()">
<form name="addApply" action="../stock/stock.do" method="post"><input
	type="hidden" name="method" value="updateStock">
	<input type="hidden" name="productId_0" value="${bean.itemVO[0].productId}">
	<input type="hidden" name="productId_1" value="${bean.itemVO[1].productId}">
	<input type="hidden" name="productId_2" value="${bean.itemVO[2].productId}">
	<input type="hidden" name="productId_3" value="${bean.itemVO[3].productId}">
	<input type="hidden" name="productId_4" value="${bean.itemVO[4].productId}">
	
	<input type="hidden" name="netaskId_0" value="${bean.itemVO[0].priceAskProviderId}">
    <input type="hidden" name="netaskId_1" value="${bean.itemVO[1].priceAskProviderId}">
    <input type="hidden" name="netaskId_2" value="${bean.itemVO[2].priceAskProviderId}">
    <input type="hidden" name="netaskId_3" value="${bean.itemVO[3].priceAskProviderId}">
    <input type="hidden" name="netaskId_4" value="${bean.itemVO[4].priceAskProviderId}">
    
	<input
	type="hidden" name="id" value="${bean.id}"> <p:navigation
	height="22">
	<td width="550" class="navigation"><span style="cursor: hand"
		onclick="javascript:history.go(-1)">采购管理</span> &gt;&gt; 更新采购</td>
	<td width="85"></td>
</p:navigation> <br>

<p:body width="98%">

	<p:title>
		<td class="caption"><strong>采购信息：</strong></td>
	</p:title>

	<p:line flag="0" />

	<p:subBody width="100%">
		<p:class value="com.china.centet.yongyin.bean.StockBean" opr="1"/>

		<p:table cells="1">
			<p:pro field="needTime" value="${bean.needTime}"/>
			
			<p:pro field="willDate"/>
			
			<p:pro field="flow" innerString="quick='true'" outString="支持简拼选择" value="${bean.flow}">
			<option value="">--</option>
			<c:forEach items="${departementList}" var="item">
			<option value="${item.name}">${item.name}</option>
			</c:forEach>
			</p:pro>
			
			<p:pro field="type" value="${bean.type}" innerString="readonly=true">
                <option value="0">内部询价</option>
                <option value="1">外网询价</option>
            </p:pro>
            
            <p:pro field="stockType" outString="公卖是全公司的都可销售 自卖是只有自己可以销售">
				<option value="">--</option>
               <p:option type="stockSailType"></p:option>
            </p:pro>
			
            <p:pro field="invoiceType" innerString="style='width: 240px'">
                <option value="">没有发票</option>
                <c:forEach items="${invoiceList}" var="item">
				<option value="${item.id}">${item.name}(${my:formatNum(item.val)}%)</option>
				</c:forEach>
            </p:pro>

			<p:pro field="description"  innerString="cols=80 rows=3" value="${bean.description}" />


			<p:cells id="selects" celspan="2" title="采购处理">
				<table id="mselect">
					<tr>
						<td>
							<input type="checkbox" name="check_init" value="0" onclick="init()">产品一：<input type="button"
								value="&nbsp;选 择&nbsp;" name="qout_0" class="button_class"
								onclick="selectProduct(0)">&nbsp;
							产品:<input
							type="text" name="productName_0" value="${bean.itemVO[0].productName}" size="20" readonly="readonly">&nbsp;
							参考价格:<input
							type="text" name="price_0" value="${bean.itemVO[0].prePrice}" size="6" oncheck="notNone;isFloat;">&nbsp;
							数量:<input
							type="text" name="amount_0" value="${bean.itemVO[0].amount}" size="6" oncheck="notNone;isNumber;">

							</td>
					</tr>

					<tr>
						<td><input type="checkbox" name="check_init" value="1" onclick="init()">产品二：<input type="button"
								value="&nbsp;选 择&nbsp;" name="qout_1" class="button_class"
								onclick="selectProduct(1)">&nbsp;
							产品:<input
							type="text" name="productName_1" value="${bean.itemVO[1].productName}" size="20" readonly="readonly">&nbsp;
							参考价格:<input
							type="text" name="price_1" value="${bean.itemVO[1].prePrice}" size="6" oncheck="notNone;isFloat;">&nbsp;
							数量:<input
							type="text" name="amount_1" value="${bean.itemVO[1].amount}" size="6" oncheck="notNone;isNumber;">

						</td>
					</tr>

					<tr>
						<td><input type="checkbox" name="check_init" value="2" onclick="init()">产品三：<input type="button"
								value="&nbsp;选 择&nbsp;" name="qout_2" class="button_class"
								onclick="selectProduct(2)">&nbsp;
							产品:<input
							type="text" name="productName_2" value="${bean.itemVO[2].productName}" size="20" readonly="readonly">&nbsp;
							参考价格:<input
							type="text" name="price_2" value="${bean.itemVO[2].prePrice}" size="6" oncheck="notNone;isFloat;">&nbsp;
							数量:<input
							type="text" name="amount_2" value="${bean.itemVO[2].amount}" size="6" oncheck="notNone;isNumber;">
							</td>
					</tr>

					<tr>
						<td><input type="checkbox" name="check_init" value="3" onclick="init()">产品四：<input type="button"
								value="&nbsp;选 择&nbsp;" name="qout_3" class="button_class"
								onclick="selectProduct(3)">&nbsp;
							产品:<input
							type="text" name="productName_3" value="${bean.itemVO[3].productName}" size="20" readonly="readonly">&nbsp;
							参考价格:<input
							type="text" name="price_3" value="${bean.itemVO[3].prePrice}" size="6" oncheck="notNone;isFloat;">&nbsp;
							数量:<input
							type="text" name="amount_3" value="${bean.itemVO[3].amount}" size="6" oncheck="notNone;isNumber;">
							</td>
					</tr>

					<tr>
						<td><input type="checkbox" name="check_init" value="4" onclick="init()">产品五：<input type="button"
								value="&nbsp;选 择&nbsp;" name="qout_4" class="button_class"
								onclick="selectProduct(4)">&nbsp;
							产品:<input
							type="text" name="productName_4" value="${bean.itemVO[4].productName}" size="20" readonly="readonly">&nbsp;
							参考价格:<input
							type="text" name="price_4" value="${bean.itemVO[4].prePrice}" size="6" oncheck="notNone;isFloat;">&nbsp;
							数量:<input
							type="text" name="amount_4" value="${bean.itemVO[4].amount}" size="6" oncheck="notNone;isNumber;">
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
			value="&nbsp;&nbsp;确 定&nbsp;&nbsp;" onclick="addBean()">&nbsp;&nbsp;
		<input type="button" class="button_class"
			onclick="javascript:history.go(-1)"
			value="&nbsp;&nbsp;返 回&nbsp;&nbsp;"></div>
	</p:button>

	<p:message2/>
	
</p:body></form>
</body>
</html>
