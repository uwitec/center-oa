<%@ page contentType="text/html;charset=GBK" language="java"
	errorPage="../common/error.jsp"%>
<%@include file="../common/common.jsp"%>
<html>
<head>
<p:link title="增加产品考核" guid="true"/>
<script language="JavaScript" src="../js/common.js"></script>
<script language="JavaScript" src="../js/cnchina.js"></script>
<script language="JavaScript" src="../js/public.js"></script>
<script src="../js/jquery.blockUI.js"></script>
<script language="JavaScript" src="../js/JCheck.js"></script>
<script src="../js/json.js"></script>
<script language="javascript">

var lbuffalo = window.top.topFrame.gbuffalo;

function addBean(flag)
{
    $O('forward').value = flag;
    
	submit('确定增加考核?');
}

function rptQueryProduct()
{
    window.common.modal('../admin/pop.do?method=rptQueryProdcut&load=1');
}

function getProduct(obj)
{
    $O('productId').value = obj.value;
    $O('productName').value = obj.pname;
}

function queryStaffer()
{
    if ($$('locationId') == '')
    {
        alert('请选择考核分公司');
        return false;
    }
    
    var id = $$('locationId');
    
    lbuffalo.remoteCall("commonManager.queryStafferByLocationIdAndFiter",[id, -1, -1], function(reply) {
                var result = reply.getResult();
                
                var sList = result;
                
                removeAllItem($O('staffer'));
                
                $O('staffer').size = 10;
                
                for (var i = 0; i < sList.length; i++)
                {
                    setOption($O('staffer'), sList[i].id,  sList[i].name);
                }
                
                $.blockUI({ message: $('#dataDiv'),css: { width: '40%'}});
        });
}

function $process()
{
    var ops = $O('staffer').options;
    
    $O('stafferId').value = '';
    $O('stafferName').value = '';
    
    for (var j = 0; j < ops.length; j++)
    {
        if (ops[j].selected)
        {
           $O('stafferId').value = ops[j].value + ',' + $O('stafferId').value ;
           
           $O('stafferName').value = ops[j].text + ' ' + $O('stafferName').value ;
        }
    }
    
    $.unblockUI();
}

function cchange()
{
    $O('stafferId').value = '';
    $O('stafferName').value = '';
}

function load()
{
    cchange();
}

</script>

</head>
<body class="body_class" onload="load()">
<form name="addApply" action="../examine/product.do"><input
	type="hidden" name="method" value="addProductExamine"><input
    type="hidden" name="productId" value=""> 
    <input
    type="hidden" name="stafferId" value=""><input
    type="hidden" name="forward" value=""><p:navigation
	height="22">
	<td width="550" class="navigation"><span style="cursor: pointer;"
		onclick="javascript:history.go(-1)">考核管理</span> &gt;&gt; 增加产品考核</td>
	<td width="85"></td>
</p:navigation> <br>

<p:body width="98%">

	<p:title>
		<td class="caption"><strong>产品考核基本信息：</strong></td>
	</p:title>

	<p:line flag="0" />

	<p:subBody width="100%">
		<p:class value="com.china.center.oa.examine.bean.ProductExamineBean" />

		<p:table cells="2">
			<p:pro field="name" cell="2" innerString="size=60" />
			
            <p:pro field="locationId" innerString="quick=true onchange=cchange()">
                <option value="">--</option>
                <c:forEach items="${locationList}" var="item">
                    <option value="${item.id}">${item.name}</option>
                </c:forEach>
            </p:pro>
			
			<p:pro field="productId" cell="1" innerString="readonly=true oncheck='notNone;'">&nbsp;&nbsp;<input
                            type="button" value="&nbsp;...&nbsp;" id="qout" class="button_class" onclick="rptQueryProduct()"></p:pro>
                            
            <p:cells title="考核职员" celspan="2">
            <input name="stafferName" size=60 readonly=true oncheck='notNone;' head="考核职员"/><font color='#FF0000'>*</font> &nbsp;&nbsp;<input
                            type="button" value="&nbsp;...&nbsp;" id="qout" class="button_class" onclick="queryStaffer()">
            </p:cells>   
            
            <p:pro field="beginTime"></p:pro>        
                 
            <p:pro field="endTime"></p:pro>             

			<p:pro field="description" cell="0" innerString="rows=4 cols=60" />

		</p:table>
	</p:subBody>

	<p:line flag="1" />

	<p:button leftWidth="100%" rightWidth="0%">
		<div align="right"><input type="button" class="button_class"
			id="ok_b" style="cursor: pointer" value="&nbsp;&nbsp;保 存&nbsp;&nbsp;"
			onclick="addBean(0)">&nbsp;&nbsp; <input type="button"
			class="button_class" id="ok_b1" style="cursor: pointer"
			value="&nbsp;&nbsp;保存并制定产品考核&nbsp;&nbsp;" onclick="addBean(1)"></div>
	</p:button>

	<p:message />
</p:body></form>
<div id="dataDiv" style="display:none">
<p align='left'><label><font color=""><b>请选择职员(可多选)</b></font></label></p>
<p><label>&nbsp;</label></p>
<select name="staffer" id="staffer" quick="true" style="width: 85%" multiple="multiple"></select>
<p><label>&nbsp;</label></p>
<p>
<input type='button' value='&nbsp;&nbsp;确 定&nbsp;&nbsp;' id='div_b_ok1' class='button_class' onclick='$process()'/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
<input type='button' id='div_b_cancle' value='&nbsp;&nbsp;取 消&nbsp;&nbsp;' class='button_class' onclick='$close()'/>
</p>
<p><label>&nbsp;</label></p>
</div>
</body>
</html>

