<%@ page contentType="text/html;charset=GBK" language="java"
	errorPage="../common/error.jsp"%>
<%@include file="../common/common.jsp"%>
<html>
<head>
<p:link title="工作日志" />
<script language="JavaScript" src="../js/common.js"></script>
<script language="JavaScript" src="../js/public.js"></script>
<script language="JavaScript" src="../js/JCheck.js"></script>
<script language="JavaScript" src="../js/compatible.js"></script>
<script language="JavaScript" src="../js/oa/worklog.js"></script>
<script language="javascript">
function load()
{
}

function addBean(opr)
{
    $O('opr').value = opr;
    if (opr == 0)
    {
        submit('确定保存工作日志?', null, checks);
    }
    else
    {
       submit('工作日志提交后不能修改,确定提交工作日志?', null, checks);
    }
}

function checks()
{
    return true;
}

var current;
function selectCustmer(obj)
{
    current = obj;
    window.common.modal('../admin/pop.do?method=rptQueryCustomer&load=1');
}

function getCustomer(oo)
{
    current.value = oo.pname;
    
    var hobj = getNextInput(current.nextSibling);
    
    hobj.value = oo.value;
}

function getNextInput(el)
{
    if (el.tagName && el.tagName.toLowerCase() == 'input')
    {
        return el;
    }
    else
    {
        return getNextInput(el.nextSibling);
    }
}
</script>

</head>
<body class="body_class" onload="load()">
<form name="formEntry" action="../worklog/worklog.do" method="post"><input
    type="hidden" name="method" value="addOrUpdateWorkLog"> 
    <input
    type="hidden" name="opr" value="0"><input
    type="hidden" name="id" value="${bean.id}"><input
    type="hidden" name="update" value="1">
 <p:navigation
	height="22">
	<td width="550" class="navigation"><span style="cursor: pointer;"
		onclick="javaScript:window.history.go(-1);">工作日志管理</span> &gt;&gt; 更新工作日志</td>
	<td width="85"></td>
</p:navigation> <br>

<div style="width: 2000px">
<p:body width="100%">

	<p:title>
		<td class="caption"><strong>工作日志基本信息：</strong></td>
	</p:title>

	<p:line flag="0" />

	<p:subBody width="98%">
		<p:table cells="1">
			<p:cell title="工作日期"><p:plugin name="workDate" value="${bean.workDate}"/></p:cell>
			
		</p:table>
	</p:subBody>
	
	<p:tr></p:tr>
	
	<tr>
        <td colspan='2' align='center'>
        <table width="98%" border="0" cellpadding="0" cellspacing="0"
            class="border">
            <tr>
                <td>
                <table width="100%" border="0" cellspacing='1' id="tables">
                    <tr align="center" class="content0">
                        <td width="10%" align="center">工作内容</td>
                        <td width="10%" align="center">工作对象</td>
                        <td width="10%" align="center">开始时间</td>
                        <td width="10%" align="center">结束时间</td>
                        <td width="10%" align="center">达成结果</td>
                        <td width="20%" align="center">跟进工作</td>
                        <td width="10%" align="center">跟进时间</td>
                        <td width="30%" align="center">备注纪要</td>
                        <td width="5%" align="left"><input type="button" accesskey="A"
                            value="增加" class="button_class" onclick="addTr()"></td>
                    </tr>
                    
                    <c:forEach items="${bean.visits}" var="item">
                    <tr class="content1">
				         <td width="10%" align="center">
				         <select name="workType" class="select_class" style="width: 100%;" values="${item.workType}">
				         <p:option type="110"/>
				         </select>
				         </td>
				         <td width="10%" align="center"><input type="text" style="width: 100%;cursor: pointer;"  onclick="selectCustmer(this)"
				         readonly="readonly" value="${item.targerName}" oncheck="notNone" name="targerName" >
				         <input type="hidden" name="targerId" value="${item.targerId}">
				         </td>
				         <td width="10%" align="center"><input type="text" readonly="readonly"  style="cursor: pointer; width: 100%"
				                    name="beginTime" value="${item.beginTime}" oncheck="notNone"></td>
				         <td width="10%" align="center"><input type="text" readonly="readonly"  style="cursor: pointer; width: 100%"
				                    name="endTime" value="${item.endTime}"></td>
				         <td width="10%" align="center">
				         <select name="result" class="select_class" style="width: 100%;" values="${item.result}">
				         <p:option type="111"/>
				         </select>
				         </td>
				         <td width="20%" align="center">
				         <textarea name="nextWork" rows="2"  style="width: 100%;">${item.nextWork}</textarea>
				         </td>
				         <td width="10%" align="center">
				         <input type="text" readonly="readonly" style="cursor: pointer; width: 100%"
				                    name="nextDate" value="${item.nextDate}"/>
				         </td>
				         <td width="30%" align="center">
				         <textarea name="description" rows="2"  style="width: 100%;"/>${item.description}</textarea>
				         </td>
				         <td width="5%" align="center"><input type=button
                            value="&nbsp;删 除&nbsp;" class=button_class onclick="removeTr(this)"></td>
				    </tr>
				    </c:forEach>
                </table>
                </td>
            </tr>
        </table>

        </td>
    </tr>

	<p:line flag="1" />

	<p:button leftWidth="100%" rightWidth="0%">
        <div align="right"><input type="button" class="button_class" id="sub_b"
            style="cursor: pointer" value="&nbsp;&nbsp;保 存&nbsp;&nbsp;"
            onclick="addBean(0)">&nbsp;&nbsp;<input type="button" class="button_class" id="sub_b"
            style="cursor: pointer" value="&nbsp;&nbsp;提 交&nbsp;&nbsp;"
            onclick="addBean(1)"></div>
    </p:button>
</p:body>
</div>
</form>
<table>
    <tr class="content1" id="trCopy" style="display: none;">
         <td width="10%" align="center">
         <select name="workType" class="select_class" style="width: 100%;">
         <p:option type="110"/>
         </select>
         </td>
         <td width="10%" align="center"><input type="text" style="width: 100%;cursor: pointer;" readonly="readonly" value="" oncheck="notNone" name="targerName" onclick="selectCustmer(this)">
         <input type="hidden" name="targerId" value="">
         </td>
         <td width="10%" align="center"><input type="text" readonly="readonly" onclick="calDateTime(this)" style="cursor: pointer; width: 100%"
                    name="beginTime" value="" oncheck="notNone"></td>
         <td width="10%" align="center"><input type="text" readonly="readonly" onclick="calDateTime(this)" style="cursor: pointer; width: 100%"
                    name="endTime" value="" oncheck="notNone"></td>
         <td width="10%" align="center">
         <select name="result" class="select_class" style="width: 100%;">
         <p:option type="111"/>
         </select>
         </td>
         <td width="20%" align="center">
         <textarea name="nextWork" rows="2"  style="width: 100%;" oncheck="maxLength(200)"></textarea>
         </td>
         <td width="10%" align="center">
         <input type="text" readonly="readonly" onclick="calDate(this)" style="cursor: pointer; width: 100%"
                    name="nextDate" value=""/>
         </td>
         <td width="30%" align="center">
         <textarea name="description" rows="2"  style="width: 100%;" oncheck="maxLength(300)"/></textarea>
         </td>
        <td width="5%" align="center"><input type=button
            value="&nbsp;删 除&nbsp;" class=button_class onclick="removeTr(this)"></td>
    </tr>
</table>
</body>
</html>

