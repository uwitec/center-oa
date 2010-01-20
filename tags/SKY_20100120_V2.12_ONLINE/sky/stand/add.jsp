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

</script>

</head>
<body class="body_class">
<form name="formEntry" action="../make/make.do"><input
	type="hidden" name="method" value="addMake">
<input type="hidden" name="cid" value=""> 
<p:navigation
	height="22">
	<td width="550" class="navigation"><span style="cursor: pointer;"
		onclick="javascript:history.go(-1)">定制产品管理</span> &gt;&gt; 增加定制产品</td>
	<td width="85"></td>
</p:navigation> <br>

<p:body width="100%">

	<p:title>
		<td class="caption"><strong>定制产品基本信息：</strong></td>
	</p:title>

	<p:line flag="0" />

	<p:subBody width="98%">
		<p:class value="com.china.center.oa.customize.make.bean.Make01Bean" />

		<p:table cells="1">
		    
		    <p:cell title="标题">
		      <input name="title" id="title" size="60" oncheck="notNone;"><font color="red">*</font>
		    </p:cell>  

			<p:pro field="cname">
			    <input type="button" value="&nbsp;...&nbsp;" name="qout" id="qout"
                    class="button_class" onclick="selectCus()">&nbsp;&nbsp; 
			</p:pro>

			<p:pro field="description" cell="0" innerString="rows=3 cols=55" />
			
			<p:pro field="endTime"/>
			
			<p:pro field="amount"/>
			
			<p:pro field="charact"/>
			
			<p:pro field="cdes" innerString="rows=3 cols=55"/>
			
			<p:pro field="sampleType">
			    <p:option type="sampleType"></p:option>
			</p:pro>
			
			<p:pro field="billType">
                <p:option type="mbillType"></p:option>
            </p:pro>
            
            <p:pro field="customerType">
                <p:option type="mcustomerType"></p:option>
            </p:pro>
            
			<p:pro field="certificate"/>
			
			<p:pro field="request" innerString="rows=3 cols=55"/>
			
			<p:pro field="appraisalType">
                <p:option type="appraisalType"></p:option>
            </p:pro>
            
            <p:pro field="designType">
                <p:option type="designType"></p:option>
            </p:pro>

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

