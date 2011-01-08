<%@ page contentType="text/html;charset=UTF-8" language="java"
	errorPage="../common/error.jsp"%>
<%@include file="../common/common.jsp"%>
<html>
<head>
<p:link title="增加发票" />
<script language="JavaScript" src="../js/JCheck.js"></script>
<script language="JavaScript" src="../js/common.js"></script>
<script language="JavaScript" src="../js/public.js"></script>
<script language="JavaScript" src="../js/json.js"></script>
<script language="javascript">

var showJSON = JSON.parse('${showJSON}');

function addBean()
{
	submit('确定增加发票?', null, check);
}

function check()
{
    var showArr = document.getElementsByName('showId');
    var amountArr = document.getElementsByName('amount');
    var priceArr = document.getElementsByName('price');
    
    var index = -1;
    
    for (var i = 0; i < showArr.length; i++)
    {
        if (showArr[i].value != '' && index != -1 && index != (i + 1))
        {
            alert('请安装顺序选择品名');
            
            return false;
        }
        
        if (showArr[i].value == '' && i == 0)
        {
            alert('请安装顺序选择品名');
            
            return false;
        }
        
        if (showArr[i].value == '')
        {
            index = i;
        }
        else
        {
            if (!isNumbers(amountArr[i].value))
	        {
	            alert('请填写数量');
	            
	            $f(amountArr[i]);
	            
	            return false;
	        }
	        
	        if (!isFloat(priceArr[i].value))
	        {
	            alert('请填写价格');
	            
	            $f(priceArr[i]);
	            
	            return false;
	        }
        }
    }
    
    return true;
}

function loadShow()
{
    var json = showJSON;
    
    var pid = $$('dutyId');
    
    var showArr = document.getElementsByName('showId');
    
    for (var i = 0; i < showArr.length; i++)
    {
        var each = showArr[i];
        
        removeAllItem(each);
        
        setOption(each, "", "--");
        
        for (var j = 0; j < json.length; j++)
        {
            var item = json[j];
            
            if (item.dutyId == pid)
            {
                setOption(each, item.id, item.name);
            }
        }
    }
}

function opens()
{
    if ($O('customerId').value == '')
    {
        alert('请选择客户');
        return false;
    }
    window.common.modal('../sail/out.do?method=rptQueryOut&mode=1&selectMode=1&load=1&invoiceId=' + $$('invoiceId') + '&dutyId=' + $$('dutyId') + '&customerId=' + $$('customerId'));
}

function getOut(oos)
{
    var outId = $$('outId');
    
    for (var i = 0 ; i < oos.length; i++)
    {
        var oo = oos[i];
        
        if (outId.indexOf(oo.value) == -1)
        {
            outId += oo.value + ";";
        }
    }
    
    $O('outId').value = outId;
}

function clears()
{
    $O('outId').value = '';
}

function selectCus()
{
    window.common.modal('../customer/customer.do?method=rptQueryAllCustomer&load=1');
}

function getCustomer(obj)
{
    $O('customerId').value = obj.value;
    $O('cname').value = obj.pname;
}
</script>

</head>
<body class="body_class" onload="loadShow()">
<form name="formEntry" action="../finance/invoiceins.do" method="post">
<input type="hidden" name="method" value="addInvoiceins"> 
<input type="hidden" name="customerId" value=""> 
<p:navigation
	height="22">
	<td width="550" class="navigation"><span style="cursor: pointer;"
		onclick="javascript:history.go(-1)">发票管理</span> &gt;&gt; 开发票</td>
	<td width="85"></td>
</p:navigation> <br>

<p:body width="98%">

	<p:title>
		<td class="caption"><strong>发票基本信息：</strong></td>
	</p:title>

	<p:line flag="0" />

	<p:subBody width="100%">

		<p:class value="com.china.center.oa.finance.bean.InvoiceinsBean" />

		<p:table cells="1">

			<p:pro field="invoiceDate"/>
			
			<p:pro field="unit" innerString="size=60" />

			<p:pro field="invoiceId" innerString="style='WIDTH: 340px;'">
			    <c:forEach items="${invoiceList}" var="item">
			    <option value="${item.id}">${item.fullName}</option>
			    </c:forEach>
			</p:pro>

			<p:pro field="dutyId" innerString="onchange=loadShow()">
				<p:option type="dutyList" />
			</p:pro>
			
			<p:cell title="开票客户" end="true">
                <input type="text" size="60" readonly="readonly" name="cname" oncheck="notNone;"> 
                <font color="red">*</font>
                <input type="button" value="&nbsp;选 择&nbsp;" name="qout1" id="qout1"
                    class="button_class" onclick="selectCus()">
            </p:cell>
			
			<p:cell title="关联单据" end="true">
			    <input type="text" size="60" readonly="readonly" name="outId"> 
                <input type="button" value="&nbsp;选 择&nbsp;" name="qout" id="qout"
                    class="button_class" onclick="opens()">&nbsp;
                <input type="button" value="&nbsp;清 空&nbsp;" name="qout" id="qout"
                    class="button_class" onclick="clears()">&nbsp;&nbsp;
            </p:cell>

			<p:pro field="description" cell="0" innerString="rows=3 cols=55" />

		</p:table>

	</p:subBody>

	<p:tr />


	<p:subBody width="100%">

		<p:table cells="1">

			<tr align="center" class="content0">
				<td width="40%" align="center">品名</td>
				<td width="30%" align="center">数量</td>
				<td width="30%" align="center">单价</td>
			</tr>
			
			<c:forEach begin="1" end="15">
			<tr align="center" class="content0">
                <td align="center"><select name="showId" style="WIDTH: 100%;" quick=true ></td>
                <td align="center"><input type="text" name="amount" style="width: 100%" oncheck="isNumber"></td>
                <td align="center"><input type="text" name="price" style="width: 100%" oncheck="isFloat2"></td>
            </tr>
            </c:forEach>

		</p:table>

	</p:subBody>
	
	<p:line flag="1" />

	<p:button leftWidth="100%" rightWidth="0%">
		<div align="right"><input type="button" class="button_class"
			id="ok_b" style="cursor: pointer" value="&nbsp;&nbsp;确 定&nbsp;&nbsp;"
			onclick="addBean()"></div>
	</p:button>

	<p:message />
</p:body></form>
</body>
</html>

