<%@ page contentType="text/html;charset=UTF-8" language="java"
    errorPage="../common/error.jsp"%>
<%@include file="../common/common.jsp"%>
<html>
<head>
<p:link title="查询委托代销" />
<link href="../js/plugin/dialog/css/dialog.css" type="text/css" rel="stylesheet"/>
<script src="../js/title_div.js"></script>
<script src="../js/public.js"></script>
<script src="../js/JCheck.js"></script>
<script src="../js/common.js"></script>
<script src="../js/tableSort.js"></script>
<script src="../js/jquery/jquery.js"></script>
<script src="../js/plugin/dialog/jquery.dialog.js"></script>
<script src="../js/plugin/highlight/jquery.highlight.js"></script>
<script language="javascript">
function comp()
{
	var now = '${now}';

	var str1 = $O('outTime').value;

	var str2 = $O('outTime1').value;

	//必须要有开始和结束时间一个
	if (str1 == '' && str2 == '')
	{
		alert('必须要有开始和结束时间一个');
		return false;
	}

	if (str1 != '' && str2 == '')
	{
		if (!coo(str1, now))
		{
			alert('查询日期跨度不能大于3个月(90天)!');
			return false;
		}

		$O('outTime1').value = now;
	}

	if (str1 == '' && str2 != '')
	{
		if (!coo(now, str2))
		{
			alert('查询日期跨度不能大于3个月(90天)!');
			return false;
		}

		$O('outTime').value = now;
	}

	if (str1 != '' && str2 != '')
	{
		if (!coo(str1, str2))
		{
			alert('查询日期跨度不能大于3个月(90天)!');
			return false;
		}
	}

	return true;
}

function coo(str1, str2)
{
	var s1 = str1.split('-');
	var s2 = str2.split('-');

	var year1 = parseInt(s1[0], 10);

	var year2 = parseInt(s2[0], 10);

	var month1 = parseInt(s1[1], 10);

	var month2 = parseInt(s2[1], 10);

	var day1 = parseInt(s1[2], 10);

	var day2 = parseInt(s2[2], 10);

	return Math.abs((year2 - year1) * 365 + (month2 - month1) * 30 + (day2 - day1)) <= 90;
}


function query()
{
	if (comp())
	{
	    getObj('method').value = 'queryOutBalance';
	    
		adminForm.submit();
	}
}


function res()
{
	$O("outId").value = '';
	
	setSelectIndex($O('status'), 0);
	
	setSelectIndex($O('type'), 0);
}

function load()
{
	loadForm();
	
	highlights($("#mainTable").get(0), ['驳回', '退货单'], 'red');
	
	highlights($("#mainTable").get(0), ['结算中心通过'], 'blue');
}

function reject()
{
    if (getRadio('fullId').statuss == 0)
    {
        $.messager.prompt('驳回', '请输入驳回原因', '', function(r){
                if (r)
                {
                    $Dbuttons(true);
                    getObj('method').value = 'rejectOutBalance';
                    getObj('id').value = getRadioValue("fullId");
        
                    var sss = r;
        
                    getObj('reason').value = r;
        
                    if (!(sss == null || sss == ''))
                    {
                        adminForm.submit();
                    }
                    else
                    {
                        $Dbuttons(false);
                    }
                }
               
            });
    }
    else
    {
        alert('不可以操作!');
    }
}

function pass()
{
    if (getRadio('fullId').statuss == 0 && window.confirm("确定通过结算清单?"))
    {
        getObj('method').value = 'passOutBalance';
        
        getObj('id').value = getRadioValue("fullId");
        
        adminForm.submit();
    }
    else
    {
        alert('不能操作');
    }
}

function del()
{
    if (getRadio('fullId').statuss == 2 && window.confirm("确定删除结算清单?"))
    {
        getObj('method').value = 'deleteOutBalance';
        
        getObj('id').value = getRadioValue("fullId");
        
        adminForm.submit();
    }
    else
    {
        alert('不能操作');
    }
}

</script>

</head>
<body class="body_class" onload="load()">
<form action="../sail/out.do" name="adminForm"><input type="hidden"
	value="queryOutBalance" name="method"> <input type="hidden" value="1"
	name="firstLoad">
	<input type="hidden" value="${queryType}"
	name="queryType">
	<input type="hidden" value="" name="id">
	<input type="hidden" value="" name="reason">

<p:navigation
    height="22">
    <td width="550" class="navigation">销售管理 &gt;&gt; 我的结算清单</td>
                <td width="85"></td>
</p:navigation> <br>

<table width="98%" border="0" cellpadding="0" cellspacing="0"
	align="center">
	<tr>
		<td align='center' colspan='2'>
		<table width="100%" border="0" cellpadding="0" cellspacing="0"
			class="border">
			<tr>
				<td>
				<table width="100%" border="0" cellspacing='1'>
					<tr class="content1">
						<td width="15%" align="center">开始时间</td>
						<td align="center" width="35%"><p:plugin name="outTime" size="20" value="${outTime}"/></td>
						<td width="15%" align="center">结束时间</td>
						<td align="center"><p:plugin name="outTime1" size="20" value="${outTime1}"/>
						</td>
					</tr>

					<tr class="content2">
						<td width="15%" align="center">状态</td>
						<td align="center">
						<select name="status" class="select_class" values="${status}">
							<option value="">--</option>
							<p:option type="outBalanceStatus"/>
						</select>
						</td>
						
						<td width="15%" align="center">销售单号</td>
                        <td align="center"><input type="text" name="outId" value="${outId}"></td>
					</tr>
					
					<tr class="content1">
                        <td width="15%" align="center">类型</td>
                        <td align="center">
                        <select name="type" class="select_class" values="${type}">
                            <option value="">--</option>
                            <p:option type="outBalanceType"/>
                        </select>
                        </td>
                        
                        <td width="15%" align="center"></td>
                        <td align="center"></td>
                    </tr>

					<tr class="content2">

						<td colspan="4" align="right"><input type="button" id="query_b"
							onclick="query()" class="button_class"
							value="&nbsp;&nbsp;查 询&nbsp;&nbsp;">&nbsp;&nbsp;
							<input type="button" onclick="res()" class="button_class" value="&nbsp;&nbsp;重 置&nbsp;&nbsp;"></td>
					</tr>
				</table>
				</td>
			</tr>
		</table>
		</td>
	</tr>
	
	<p:title>
        <td class="caption"><strong>浏览结算清单</strong></td>
    </p:title>

    <p:line flag="0" />

	<tr>
		<td align='center' colspan='2'>
		<table width="100%" border="0" cellpadding="0" cellspacing="0"
			class="border">
			<tr>
				<td>
				<table width="100%" border="0" cellspacing='1' id="mainTable">
					<tr align="center" class="content0">
						<td align="center" width="5%" align="center">选择</td>
						<td align="center" onclick="tableSort(this)" class="td_class">销售单号</td>
						<td align="center" onclick="tableSort(this)" class="td_class">状态</td>
						<td align="center" onclick="tableSort(this)" class="td_class">客户</td>
						<td align="center" onclick="tableSort(this)" class="td_class">金额</td>
						<td align="center" onclick="tableSort(this)" class="td_class">类型</td>
						<td align="center" onclick="tableSort(this)" class="td_class">提交人</td>
						<td align="center" onclick="tableSort(this)" class="td_class">时间</td>
					</tr>

					<c:forEach items="${resultList}" var="item" varStatus="vs">
						<tr class='${vs.index % 2 == 0 ? "content1" : "content2"}'
						>
							<td align="center"><input type="radio" name="fullId" 
								statuss='${item.status}' 
								value="${item.id}" ${vs.index== 0 ? "checked" : ""}/></td>
							<td align="center" onclick="hrefAndSelect(this)">
							<a href="../sail/out.do?method=findOutBalance&id=${item.id}">
							${item.outId}
							</a>
							</td>
							<td align="center" onclick="hrefAndSelect(this)">${my:get('outBalanceStatus', item.status)}</td>
							<td align="center" onclick="hrefAndSelect(this)">${item.customerName}</td>
							<td align="center" onclick="hrefAndSelect(this)">${my:formatNum(item.total)}</td>
							<td align="center" onclick="hrefAndSelect(this)">${my:get('outBalanceType', item.type)}</td>
							<td align="center" onclick="hrefAndSelect(this)">${item.stafferName}</td>
							<td align="center" onclick="hrefAndSelect(this)">${item.logTime}</td>
						</tr>
					</c:forEach>
				</table>

				<p:formTurning form="adminForm" method="queryOutBalance"></p:formTurning>
				</td>
			</tr>
		</table>
		</td>
	</tr>

	<tr>
		<td height="10" colspan='2'></td>
	</tr>


	<tr>
		<td background="../images/dot_line.gif" colspan='2'></td>
	</tr>

	<tr>
		<td height="10" colspan='2'></td>
	</tr>

	<c:if test="${my:length(resultList) > 0}">
	<tr>
		<td width="100%">
		<div align="right">
		<c:if test="${queryType == '1'}">
		<input type="button" class="button_class"
			value="&nbsp;&nbsp;删 除&nbsp;&nbsp;" onClick="del()">&nbsp;&nbsp;
		</c:if>
		
		<c:if test="${queryType == '2'}">
		<input
			type="button" class="button_class"
			value="&nbsp;&nbsp;通 过&nbsp;&nbsp;" onclick="pass()" />&nbsp;&nbsp; 
			<input
			type="button" class="button_class"
			value="&nbsp;&nbsp;驳 回&nbsp;&nbsp;" onclick="reject()" />&nbsp;&nbsp;
		</c:if>
		</div>
		
		</td>
		<td width="0%"></td>
	</tr>
	
	</c:if>

	<p:message2/>
</table>

</form>
</body>
</html>
