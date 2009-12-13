<%@ page contentType="text/html;charset=GBK" language="java"
	errorPage="../common/error.jsp"%>
<%@include file="../common/common.jsp"%>
<html>
<head>
<p:link title="定制产品" />
<script language="JavaScript" src="../js/common.js"></script>
<script language="JavaScript" src="../js/JCheck.js"></script>
<script language="JavaScript" src="../js/public.js"></script>
<script language="javascript">
function addBean()
{
    $O('method').value = 'passMake';
    
    if ($$('handerName') == '')
    {
        alert('请选择下一环处理人');
        
        return false;
    }
    
	submit('确定通过申请?');
}

function rejectMake()
{
    $O('method').value = 'rejectMake';
    
    submit('确定驳回申请?');
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
    var role = '${position.role}';
    
    if (role == '1')
    {
        window.common.modal('../make/make.do?method=rptQueryCreate&id=${make.id}&selectMode=1');
    }
    else
    {
        window.common.modal('../admin/pop.do?method=rptQueryGroupMember&selectMode=1&pid=' + role);
    }
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

function queryLog()
{
    window.common.modal('../admin/pop.do?method=rptQueryLog&fk=${make.id}');
}

function load()
{
    setAllReadOnly4(null, ['reason', 'qout1', 'ok_b', 'log_b', 'reject_b']);
}
</script>

</head>
<body class="body_class" onload="load()">
<form name="formEntry" action="../make/make.do" method="post"><input
	type="hidden" name="method" value="passMake">
<input type="hidden" name="cid" value=""> 
<input type="hidden" name="handerId" value=""> 
<input type="hidden" name="id" value="${make.id}"> 
<p:navigation
	height="22">
	<td width="550" class="navigation"><span style="cursor: pointer;"
		onclick="javascript:history.go(-1)">定制产品管理</span> &gt;&gt; 审批定制产品(第一环)</td>
	<td width="85"></td>
</p:navigation> <br>

<p:body width="100%">

	<p:title>
		<td class="caption"><strong>定制产品基本信息：</strong></td>
	</p:title>

	<p:line flag="0" />

	<p:subBody width="98%">
		<p:class value="com.china.center.oa.customize.make.bean.Make01Bean" opr="1"/>

		<p:table cells="1">
		    
		    <p:cell title="标题">
		     ${make.title}
		    </p:cell>  
		    
		    <p:cell title="类型">
              <select class="select_class" name="type" values="${make.type}">
                  <p:option type="makeType"></p:option> 
              </select>
            </p:cell> 

			<p:pro field="cname" innerString="size=40">
			    <input type="button" value="&nbsp;...&nbsp;" name="qout" id="qout"
                    class="button_class" onclick="selectCus()">&nbsp;&nbsp; 
			</p:pro>

			<p:pro field="description" cell="0" innerString="rows=3 cols=55" />
            
            <p:pro field="endTime"/>
            <p:pro field="flowTypeName"/>
            
            <p:pro field="endTime2"/>
            <p:pro field="flowTypeName2"/>
            
            <p:pro field="amount"/>
            
            <p:pro field="charact"/>
            
            <p:pro field="cdes" innerString="rows=5 cols=55 oncheck='minLength(100)'" outString="内容不能少于50字" />
            
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
            
            <p:cell title="意见">
              <textarea name="reason" rows=3 cols=55 oncheck="notNone;" head="意见"></textarea>  
              <font color="red">*</font>
            </p:cell> 
            
            <p:cell title="提交到">
                  <input name="handerName" id="handerName" readonly="readonly">&nbsp;&nbsp; 
                  <input type="hidden" name="handerId" value=""> 
                  <input type="button" value="&nbsp;...&nbsp;" name="qout1" id="qout1"
                        class="button_class" onclick="selectNext()">&nbsp;&nbsp; 
                  <font color="red">*</font>
                  <input type="button" value="&nbsp;审批日志&nbsp;" name="log_b" id="log_b"
                        class="button_class" onclick="queryLog()">
             </p:cell> 

		</p:table>
		
		
	</p:subBody>

	<p:line flag="1" />

	<p:button leftWidth="99%" rightWidth="1%">
		<div align="right"><input type="button" class="button_class" id="ok_b" name="ok_b"
		 value="&nbsp;&nbsp;通 过&nbsp;&nbsp;" accesskey="O"
			onclick="addBean()">&nbsp;&nbsp;
		<input type="button" class="button_class" id="reject_b" name="reject_b"
         value="&nbsp;&nbsp;驳 回&nbsp;&nbsp;" accesskey="R"
            onclick="rejectMake()">	
		</div>
	</p:button>
</p:body></form>
</body>
</html>

