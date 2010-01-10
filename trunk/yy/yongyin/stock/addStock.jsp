<%@ page contentType="text/html;charset=GBK" language="java"
	errorPage="./error.jsp"%>
<%@include file="./common.jsp"%>
<html>
<head>
<p:link title="处理采购" />
<script language="JavaScript" src="../js/common.js"></script>
<script language="JavaScript" src="../js/JCheck.js"></script>
<script language="JavaScript" src="../js/public.js"></script>
<script language="JavaScript" src="../js/cnchina.js"></script>
<script language="javascript">

var cindex = -1;
function addBean(opr)
{
	$('oprMode').value = opr;

	submit('确定申请产品采购?', null, lverify);
}

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
			if ($('productName_' + i).value == '' || $('productId_' + i).value == '' )
			{
				alert('产品不能为空');
				return false;
			}

			if ($$('amount_' + i)  == null)
			{
				alert('请选择产品是否满足数量要求');
				return false;
			}

			if (imap[$('productId_' + i).value] == $('productId_' + i).value)
			{
				alert('选择的产品不能重复');
				return false;
			}

			imap[$('productId_' + i).value] = $('productId_' + i).value;
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
			$d('amount_' + index, false);
		}
		else
		{
			$('price_' + index).value = '';
			$('productName_' + index).value = '';
			$('productId_' + index).value = '';
			$d('qout_' + index);
			$d('price_' + index);
			$d('amount_' + index);
		}
	}
}

function selectProduct(index)
{
	cindex = index;
	window.common.modal("../admin/product.do?method=rptInQueryProduct3&firstLoad=1");
}

function getProduct(oo)
{
	if (cindex != -1)
	{
		$("productName_" + cindex).value = oo.productname;
		$("productId_" + cindex).value = oo.value;
	}
}

</script>

</head>
<body class="body_class" onload="load()">
<form name="addApply" action="../stock/stock.do" method="post"><input
	type="hidden" name="method" value="addStock">
	<input type="hidden" name="productId_0" value="">
	<input type="hidden" name="productId_1" value="">
	<input type="hidden" name="productId_2" value="">
	<input type="hidden" name="productId_3" value="">
	<input type="hidden" name="productId_4" value="">
	<input type="hidden" name="oprMode" value="">
	<input
	type="hidden" name="id" value="${bean.id}"> <p:navigation
	height="22">
	<td width="550" class="navigation"><span style="cursor: hand"
		onclick="javascript:history.go(-1)">采购管理</span> &gt;&gt; 处理采购</td>
	<td width="85"></td>
</p:navigation> <br>

<p:body width="100%">

	<p:title>
		<td class="caption"><strong>采购信息：</strong></td>
	</p:title>

	<p:line flag="0" />

	<p:subBody width="100%">
		<p:class value="com.china.centet.yongyin.bean.StockBean" />

		<p:table cells="1">
			<p:pro field="needTime"/>
			
			<p:pro field="type">
                <option value="0">内部询价</option>
                <option value="1">外网询价</option>
            </p:pro>

			<p:pro field="flow" innerString="quick='true'" outString="支持简拼选择">
			<option value="">--</option>
			<c:forEach items="${departementList}" var="item">
			<option value="${item}">${item}</option>
			</c:forEach>
			</p:pro>

			<p:pro field="description"  innerString="cols=80 rows=3" />


			<p:cells id="selects" celspan="2" title="采购处理">
				<table id="mselect">
					<tr>
						<td>
							<input type="checkbox" name="check_init" id="check_init_0" value="0" onclick="init()">产品一：<input type="button"
								value="&nbsp;选 择&nbsp;" name="qout_0" class="button_class"
								onclick="selectProduct(0)">&nbsp;
							产品:<input
							type="text" name="productName_0" value="" size="20" readonly="readonly">&nbsp;
							参考价格:<input
							type="text" name="price_0" id="price_0" value="" size="6" oncheck="notNone;isFloat;">&nbsp;
							数量:<input
							type="text" name="amount_0" id="amount_0" value="" size="6" oncheck="notNone;isNumber;">

							</td>
					</tr>

					<tr>
						<td><input type="checkbox" name="check_init" id="check_init_1" value="1" onclick="init()">产品二：<input type="button"
								value="&nbsp;选 择&nbsp;" name="qout_1" class="button_class"
								onclick="selectProduct(1)">&nbsp;
							产品:<input
							type="text" name="productName_1" value="" size="20" readonly="readonly">&nbsp;
							参考价格:<input
							type="text" name="price_1" id="price_1" value="" size="6" oncheck="notNone;isFloat;">&nbsp;
							数量:<input
							type="text" name="amount_1" id="amount_1" value="" size="6" oncheck="notNone;isNumber;">

						</td>
					</tr>

					<tr>
						<td><input type="checkbox" name="check_init" value="2" id="check_init_2" onclick="init()">产品三：<input type="button"
								value="&nbsp;选 择&nbsp;" name="qout_2" class="button_class"
								onclick="selectProduct(2)">&nbsp;
							产品:<input
							type="text" name="productName_2" value="" size="20" readonly="readonly">&nbsp;
							参考价格:<input
							type="text" name="price_2" id="price_2" value="" size="6" oncheck="notNone;isFloat;">&nbsp;
							数量:<input
							type="text" name="amount_2" id="amount_2" value="" size="6" oncheck="notNone;isNumber;">
							</td>
					</tr>

					<tr>
						<td><input type="checkbox" name="check_init" value="3" id="check_init_3" onclick="init()">产品四：<input type="button"
								value="&nbsp;选 择&nbsp;" name="qout_3" class="button_class"
								onclick="selectProduct(3)">&nbsp;
							产品:<input
							type="text" name="productName_3" value="" size="20" readonly="readonly">&nbsp;
							参考价格:<input
							type="text" name="price_3" id="price_3" value="" size="6" oncheck="notNone;isFloat;">&nbsp;
							数量:<input
							type="text" name="amount_3" id="amount_3" value="" size="6" oncheck="notNone;isNumber;">
							</td>
					</tr>

					<tr>
						<td><input type="checkbox" name="check_init" value="4" id="check_init_4" onclick="init()">产品五：<input type="button"
								value="&nbsp;选 择&nbsp;" name="qout_4" class="button_class"
								onclick="selectProduct(4)">&nbsp;
							产品:<input
							type="text" name="productName_4" value="" size="20" readonly="readonly">&nbsp;
							参考价格:<input
							type="text" name="price_4" id="price_4" value="" size="6" oncheck="notNone;isFloat;">&nbsp;
							数量:<input
							type="text" name="amount_4" id="amount_4" value="" size="6" oncheck="notNone;isNumber;">
							</td>
					</tr>
				</table>
			</p:cells>

		</p:table>
	</p:subBody>

	<p:line flag="1" />

	<p:button leftWidth="100%" rightWidth="0%">
		<div align="right"><input type="button" class="button_class"
			name="b_saves" style="cursor: pointer"
			value="&nbsp;&nbsp;保 存&nbsp;&nbsp;" onclick="addBean(0)">&nbsp;&nbsp;
		<input type="button" class="button_class"
			name="b_submit" style="cursor: pointer"
			value="&nbsp;&nbsp;提 交&nbsp;&nbsp;" onclick="addBean(1)">&nbsp;&nbsp;
		<input type="button" class="button_class" name="b_back"
			onclick="javascript:history.go(-1)"
			value="&nbsp;&nbsp;返 回&nbsp;&nbsp;"></div>
	</p:button>

	<tr>
		<td colspan='2' align="center"><FONT color="blue">${MESSAGE_INFO}</FONT><FONT
			color="red">${errorInfo}</FONT></td>
	</tr>
</p:body></form>
</body>
</html>

