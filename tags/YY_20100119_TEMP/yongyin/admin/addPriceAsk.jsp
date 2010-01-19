<%@ page contentType="text/html;charset=GBK" language="java"
	errorPage="./error.jsp"%>
<%@include file="./common.jsp"%>
<html>
<head>
<p:link title="增加紧急询价" />
<script language="JavaScript" src="../js/common.js"></script>
<script language="JavaScript" src="../js/public.js"></script>
<script language="JavaScript" src="../js/JCheck.js"></script>
<script language="javascript">
function addBean()
{
	submit('确定增加紧急询价?');
}

function load()
{
	//alert('load');
	init();
}

function selectProduct()
{
	//单选
	window.common.modal("../admin/product.do?method=rptInQueryProduct3&firstLoad=1&type=2");
}

function getProduct(oo)
{
	var obj = oo;
	
	$('productId').value = obj.value;
	$('productName').value = obj.productname;
}

function init()
{
    var ss = $O('instancy');
    removeAllItem(ss);
    if ($$('type') == '0')
    {
        setOption(ss, '0', '一般(2小时)');
        setOption(ss, '1', '紧急(1小时)');
        setOption(ss, '2', '非常紧急(30分钟)');
    }
    
    if ($$('type') == '1')
    {
        setOption(ss, '3', '外网询价(中午11点结束)');
        setOption(ss, '4', '外网询价(下午2点结束)');
        setOption(ss, '5', '外网询价(下午6点结束)');
        setOption(ss, '6', '外网询价(晚间23点结束)');
    }
}
</script>

</head>
<body class="body_class" onload="load()">
<form name="addApply" action="../admin/price.do"><input
	type="hidden" name="method" value="addPriceAsk"> <input
	type="hidden" name="productId" value=""><p:navigation
	height="22">
	<td width="550" class="navigation"><span style="cursor: hand"
		onclick="javascript:history.go(-1)">询价管理</span> &gt;&gt; 增加紧急询价</td>
	<td width="85"></td>
</p:navigation> <br>

<p:body width="100%">

	<p:title>
		<td class="caption"><strong>紧急询价信息：</strong></td>
	</p:title>

	<p:line flag="0" />

	<p:subBody width="98%">
		<p:class value="com.china.centet.yongyin.bean.PriceAskBean" />

		<p:table cells="1">
			<p:pro field="productId" innerString="size=60">&nbsp;&nbsp;<input type="button"
					value="&nbsp;...&nbsp;" name="qout" class="button_class"
					onclick="selectProduct()">
			</p:pro>

			<p:pro field="amount" />
			
			<p:pro field="type" innerString="onchange=init()">
                <option value="0">内部询价</option>
                <option value="1">外网询价</option>
            </p:pro>

			<p:pro field="instancy" innerString="style='width: 240px'">
				<option value="0">一般(2小时)</option>
				<option value="1">紧急(1小时)</option>
				<option value="2">非常紧急(30分钟)</option>
				<option value="3">外网询价(中午11点结束)</option>
				<option value="4">外网询价(下午2点结束)</option>
				<option value="5">外网询价(下午6点结束)</option>
				<option value="6">外网询价(晚间23点结束)</option>
			</p:pro>
			
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

	<p:message/>
</p:body></form>
</body>
</html>

