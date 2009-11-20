<%@ page contentType="text/html;charset=GBK" language="java"
	errorPage="../common/error.jsp"%>
<%@include file="../common/common.jsp"%>
<html>
<head>
<p:link title="增加定制产品" />
<script language="JavaScript" src="../js/common.js"></script>
<script language="JavaScript" src="../js/JCheck.js"></script>
<script language="JavaScript" src="../js/public.js"></script>
<script language="javascript">
function addBean()
{
	submit('确定申请定制产品?');
}

function selectCus()
{
    window.common.modal('../admin/pop.do?method=rptQueryAllCustomer&load=1');
}

function getProduct(obj)
{
    $O('cid').value = obj.value;
    $O('cname').value = obj.pname;
}

function getCustomer(obj)
{
    $O('cid').value = obj.value;
    $O('cname').value = obj.pname;
}

function selectNext()
{
    window.common.modal('../admin/pop.do?method=rptQueryGroupMember&selectMode=1&pid=90000000000000000001');
}

function getStaffers(oo)
{
    if (oo.length > 0)
    {
        var item = oo[0];
        $O("handerName").value = item.pname;
        $O("handerId").value = item.value;
    }
}

var showTD = 0;
var showTD2 = 0;

function showOrHidden()
{
    if (showTD == 0)
    {
        $v('endTime_TR');
        $v('flowTypeName_TR');
        $v('endTime2_TR');
        $v('flowTypeName2_TR');
        
        showTD = 1;
    }
    else
    {
        $v('endTime_TR', true);
        $v('flowTypeName_TR', true);
        $v('endTime2_TR', true);
        $v('flowTypeName2_TR', true);
        
        showTD = 0;
    }
}

function showOrHidden2()
{
    if (showTD2 == 0)
    {
        $v('appraisalType_TR');
        $v('gujiaType_TR');
        $v('appType_TR');
        
        showTD2 = 1;
    }
    else
    {
        $v('appraisalType_TR', true);
        $v('gujiaType_TR', true);
        $v('appType_TR', true);
        
        showTD2 = 0;
    }
}

function load()
{
    loadForm();
    
    var td = $O('CF');
    
    td.style.cursor = 'pointer';
    
    td.title = '点击配置';
    
    td.onclick = showOrHidden;
    
    td = $O('GJ');
    
    td.style.cursor = 'pointer';
    
    td.title = '点击配置';
    
    td.onclick = showOrHidden2;
    
    showOrHidden();
    showOrHidden2();
}
</script>

</head>
<body class="body_class" onload="load()">
<form name="formEntry" action="../make/make.do" method="post"><input
	type="hidden" name="method" value="addMake">
<input type="hidden" name="cid" value=""> 
<input type="hidden" name="handerId" value=""> 
<p:navigation
	height="22">
	<td width="550" class="navigation">增加定制产品</td>
	<td width="85"></td>
</p:navigation> <br>

<p:body width="100%">

	<p:title>
		<td class="caption"><strong>定制产品基本信息：(只可以上传office文字附件，任何含有图片的文件不能传输，需转为QQ传递)</strong></td>
	</p:title>

	<p:line flag="0" />

	<p:subBody width="98%">
		<p:class value="com.china.center.oa.customize.make.bean.Make01Bean" />

		<p:table cells="1">
		    
		    <p:cell title="标题">
		      <input name="title" id="title" size="60" oncheck="notNone;" head="标题"><font color="red">*</font>
		    </p:cell>  
		    
		    <p:cell title="类型">
              <select class="select_class" name="type">
                  <p:option type="makeType"></p:option> 
              </select>
              <font color="red">*</font>
            </p:cell> 

			<p:pro field="cname">
			    <input type="button" value="&nbsp;...&nbsp;" name="qout" id="qout"
                    class="button_class" onclick="selectCus()">&nbsp;&nbsp; 
			</p:pro>

			<p:pro field="description" cell="0" innerString="rows=3 cols=55" />
			
			<p:cells celspan="1" title="工厂出货期<font color=red>*</font>" id="CF">
			<span style="cursor: pointer;" onclick="showOrHidden()" title="点击配置">开始配置--></span>
            </p:cells> 
			
			<p:pro field="endTime"/>
			<p:pro field="flowTypeName"/>
			
			<p:pro field="endTime2"/>
			<p:pro field="flowTypeName2"/>
			
			<p:pro field="cdes" innerString="rows=5 cols=55 oncheck='minLength(100);maxLength(1000)'" outString="内容不能少于50字" />
			
			<p:pro field="amount"/>
			
			<p:pro field="charact"/>
			
			<p:pro field="sampleType">
			    <p:option type="sampleType"></p:option>
			</p:pro>
			
			<p:pro field="billType">
                <p:option type="mbillType"></p:option>
            </p:pro>
            
            <p:pro field="customerType">
                <p:option type="mcustomerType"></p:option>
            </p:pro>
            
			<p:pro field="certificate" innerString="size=60"/>
			
			<p:pro field="request">
			    <p:option type="requestType"></p:option> 
			</p:pro>
			
			<p:cells celspan="1" title="估价配置<font color=red>*</font>" id="GJ">
            <span style="cursor: pointer;" onclick="showOrHidden2()" title="点击配置">开始配置--></span>
            </p:cells> 
			
			<p:pro field="appraisalType">
                <p:option type="appraisalType"></p:option>
            </p:pro>
            
            <p:pro field="gujiaType" innerString="style='width:240px'">
                <p:option type="gujiaType"></p:option>
            </p:pro>
            
            <p:pro field="appType">
                <option value="">--</option>
                <p:option type="mappType"></p:option>
            </p:pro>
            
            <p:cell title="提交到">
              <input name="handerName" id="handerName" oncheck="notNone;" readonly="readonly">&nbsp;&nbsp; 
              <input type="button" value="&nbsp;...&nbsp;" name="qout1" id="qout1"
                    class="button_class" onclick="selectNext()">&nbsp;&nbsp; 
              <font color="red">*</font>
            </p:cell> 

		</p:table>
	</p:subBody>

	<p:line flag="1" />

	<p:button leftWidth="100%" rightWidth="0%">
		<div align="right"><input type="button" class="button_class" id="ok_b"
		 value="&nbsp;&nbsp;确 定&nbsp;&nbsp;"
			onclick="addBean()"></div>
	</p:button>
</p:body></form>
</body>
</html>

