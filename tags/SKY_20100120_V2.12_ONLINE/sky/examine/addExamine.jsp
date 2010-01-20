<%@ page contentType="text/html;charset=GBK" language="java"
	errorPage="../common/error.jsp"%>
<%@include file="../common/common.jsp"%>
<html>
<head>
<p:link title="增加考核" guid="true"/>
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
    
    if ($$('type') == 1 || true)
    {
	    if ($$('attType') == '1' && $$('parentId') == '')
	    {
	        alert('父考核不能为空');
	        return false;
	    }
	    
	    if ($$('attType') == '2' && $$('parentId') == '')
	    {
	        alert('父考核不能为空');
	        return false;
	    }
    }
    
	submit('确定增加考核?');
}

function rptQueryExamine()
{
    if ($$('attType') == '')
    {
        alert('请选择考核类型');
        return false;
    }
    
    if ($$('attType') == '0')
    {
        alert('考分公司经理考核不需要父级考核');
        return false;
    }
    
    if ($$('locationId') == '')
    {
        alert('请选择考核分公司');
        return false;
    }
    
    if ($$('year') == '')
    {
        alert('请选择考核年度');
        return false;
    }
    
    window.common.modal('../examine/examine.do?method=rptQueryExamine&locationId='
        + $$('locationId') + '&year=' + $$('year') + '&attType=' + $$('attType') + '&type=' + $$('type'));
}

function getExamine(obj)
{
    $O('parentId').value = obj.value;
    $O('parentName').value = obj.pname;
}

function queryStaffer()
{
    if ($$('attType') == '')
    {
        alert('请选择考核类型');
        return false;
    }
    
    if ($$('locationId') == '')
    {
        alert('请选择考核分公司');
        return false;
    }
    
    var id = $$('locationId');
    
    lbuffalo.remoteCall("commonManager.queryStafferByLocationIdAndFiter",[id, parseInt($$('type')), parseInt($$('attType'))], function(reply) {
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
    
    //处理为个人的时候
    if ($$('attType') == '2')
    {
	    for (var j = 0; j < ops.length; j++)
	    {
	        if (ops[j].selected)
	        {
	           $O('stafferId').value = ops[j].value + ',' + $O('stafferId').value ;
	           
	           $O('stafferName').value = ops[j].text + ' ' + $O('stafferName').value ;
	        }
	    }
    }
    else
    {
        var sid = $$('staffer');
	    $O('stafferId').value = sid;
	    $O('stafferName').value = getOptionText('staffer');
    }
    
    $.unblockUI();
}

function cchange()
{
    $O('stafferId').value = '';
    $O('stafferName').value = '';
}

function cchange1()
{
    $O('parentId').value = '';
    $O('parentName').value = '';
    
    if ($$('attType') == 2)
    {
        $O('staffer').multiple = true;
    }
    else
    {
        $O('staffer').multiple = false;
    }
    
    cchange();
}

function cchange2()
{
    cchange1();
}

function cchange0()
{
    cchange1();
    
    //终端
    /*
    if ($$('type') == 0)
    {
        setSelect($O('attType'), 2)
        
        $d('attType');
        
        $d('parentName');
        $d('qout');
    }
    else
    {
        setSelectIndex($O('attType'), 0);
        $d('attType', false);
        $d('parentName', false);
        $d('qout', false);
    }
    */
}

function load()
{
    cchange0();
}

</script>

</head>
<body class="body_class" onload="load()">
<form name="addApply" action="../examine/examine.do"><input
	type="hidden" name="method" value="addExamine"><input
    type="hidden" name="parentId" value=""> 
    <input
    type="hidden" name="stafferId" value=""><input
    type="hidden" name="forward" value=""><p:navigation
	height="22">
	<td width="550" class="navigation"><span style="cursor: pointer;"
		onclick="javascript:history.go(-1)">考核管理</span> &gt;&gt; 增加考核</td>
	<td width="85"></td>
</p:navigation> <br>

<p:body width="98%">

	<p:title>
		<td class="caption"><strong>考核基本信息：</strong></td>
	</p:title>

	<p:line flag="0" />

	<p:subBody width="100%">
		<p:class value="com.china.center.oa.examine.bean.ExamineBean" />

		<p:table cells="2">
			<p:pro field="name" cell="2" innerString="size=60" />
			
			<p:pro field="type" innerString="onchange=cchange0()">
                <p:option type="examineType"/>
            </p:pro>
			
			<p:pro field="attType" innerString="onchange=cchange1()">
                <option value="">--</option>
                <p:option type="attType"/>
            </p:pro>
            
            <p:pro field="locationId" innerString="quick=true onchange=cchange()">
                <option value="">--</option>
                <c:forEach items="${locationList}" var="item">
                    <option value="${item.id}">${item.name}</option>
                </c:forEach>
            </p:pro>

			<p:pro field="year" innerString="quick=true onchange=cchange2()">
				<option value="">--</option>
				<c:forEach begin="2008" end="2100" var="item">
					<option value="${item}">${item}</option>
				</c:forEach>
			</p:pro>
			
			
			<p:pro field="parentId" cell="2" innerString="size=60 readonly=true">&nbsp;&nbsp;<input
                            type="button" value="&nbsp;...&nbsp;" id="qout" class="button_class" onclick="rptQueryExamine()"></p:pro>
                            
            <p:pro field="stafferId" cell="2" innerString="size=60 readonly=true">&nbsp;&nbsp;<input
                            type="button" value="&nbsp;...&nbsp;" id="qout1" class="button_class" onclick="queryStaffer()"></p:pro>

			<p:pro field="description" cell="0" innerString="rows=4 cols=60" />

		</p:table>
	</p:subBody>

	<p:line flag="1" />

	<p:button leftWidth="100%" rightWidth="0%">
		<div align="right"><input type="button" class="button_class"
			id="ok_b" style="cursor: pointer" value="&nbsp;&nbsp;保 存&nbsp;&nbsp;"
			onclick="addBean(0)">&nbsp;&nbsp; <input type="button"
			class="button_class" id="ok_b1" style="cursor: pointer"
			value="&nbsp;&nbsp;保存并制定考核项&nbsp;&nbsp;" onclick="addBean(1)"></div>
	</p:button>

	<p:message />
</p:body></form>
<div id="dataDiv" style="display:none">
<p align='left'><label><font color=""><b>请选择职员(个人考核可多选)</b></font></label></p>
<p><label>&nbsp;</label></p>
<select name="staffer" id="staffer" quick="true" style="width: 85%"></select>
<p><label>&nbsp;</label></p>
<p>
<input type='button' value='&nbsp;&nbsp;确 定&nbsp;&nbsp;' id='div_b_ok1' class='button_class' onclick='$process()'/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
<input type='button' id='div_b_cancle' value='&nbsp;&nbsp;取 消&nbsp;&nbsp;' class='button_class' onclick='$close()'/>
</p>
<p><label>&nbsp;</label></p>
</div>
</body>
</html>

