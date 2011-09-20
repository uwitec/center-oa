<%@ page contentType="text/html;charset=UTF-8" language="java"
    errorPage="../common/error.jsp"%>
<%@include file="../common/common.jsp"%>

<html>
<head>
<p:link title="发货单列表" />
<script src="../js/common.js"></script>
<script src="../js/tableSort.js"></script>
<script language="javascript">
function process()
{
	if (getRadioValue('consigns') == '')
	{
		alert('请选择发货单');
		return;
	}
	
	$l('../sail/transport.do?method=findConsign&fullId=' + getRadioValue('consigns') + '&gid=' + getRadio('consigns').pgid);
}

function load()
{
	loadForm();
}

function pagePrint()
{
	if (getRadioValue('consigns') == '')
	{
		alert('请选择发货单');
		return;
	}
	
	window.open('../sail/transport.do?method=findConsign&forward=1&fullId=' + getRadioValue("consigns") + '&gid=' + getRadio('consigns').pgid + '&CENTER_COMMON_CENTER_COMMON=' + new Date().getTime());
}

function exports()
{
    if (window.confirm("确定导出当前的全部查询的发货单?"))
    document.location.href = '../sail/transport.do?method=exportConsign';
}
</script>

</head>
<body class="body_class" onload="load()">
<form name="formEntry" action="../sail/transport.do"><input
	type="hidden" name="method" value="queryConsign"> <p:navigation
	height="22">
	<td width="550" class="navigation">发货单管理 &gt;&gt; 发货单列表</td>
	<td width="85"></td>
</p:navigation> <br>

<p:body width="100%">

	<p:subBody width="98%">
		<table width="100%" align="center" cellspacing='1' class="table0">
			<tr class="content1">
				<td width="15%" align="center">库管审批时间从</td>
				<td align="left">
				<p:plugin name="beginDate" size="20" value="${g_queryConsign_pmap.beginDate}"/>
				</td>
				<td width="15%" align="center">到</td>
				<td align="left">
				<p:plugin name="endDate" size="20" value="${g_queryConsign_pmap.endDate}"/>
				</td>
			</tr>
			
			<tr class="content2">
				<td width="15%" align="center">(到货)开始时间</td>
				<td align="left">
				<p:plugin name="abeginDate" size="20" value="${g_queryConsign_pmap.abeginDate}"/>
				</td>
				<td width="15%" align="center">(到货)结束时间</td>
				<td align="left">
				<p:plugin name="aendDate" size="20" value="${g_queryConsign_pmap.aendDate}"/>
				</td>
			</tr>

			<tr class="content1">
				<td width="15%" align="center">货单状态:</td>
				<td align="left"><select name="currentStatus"
					class="select_class" values="${g_queryConsign_pmap.currentStatus}">
					<option value="">--</option>
					<option value="1">初始</option>
					<option value="2">通过</option>
				</select></td>
				<td width="15%" align="center">回复类型:</td>
				<td align="left"><select name="reprotType" class="select_class"
					values="${g_queryConsign_pmap.reprotType}">
					<option value="">--</option>
					<option value="0">无回复</option>
					<option value="1">正常收货</option>
					<option value="2">异常收货</option>
				</select></td>
			</tr>
			
			<tr class="content2">
				<td width="15%" align="center">单号：</td>
				<td align="left">
				<input name="fullId" size="20" value="${g_queryConsign_pmap.fullId}"  />
				</td>
				<td width="15%" align="center"></td>
				<td align="left">
				</td>
			</tr>

			<tr class="content1">
				<td colspan="4" align="right"><input type="submit"
					class="button_class" value="&nbsp;&nbsp;查 询&nbsp;&nbsp;">&nbsp;&nbsp;<input
					type="reset" class="button_class"
					value="&nbsp;&nbsp;重 置&nbsp;&nbsp;"></td>
			</tr>
		</table>

	</p:subBody>

	<p:title>
		<td class="caption"><strong>发货单列表：(最多返回500条)</strong></td>
	</p:title>

	<p:line flag="0" />

	<p:subBody width="98%">
		<table width="100%" align="center" cellspacing='1' class="table0">
			<tr align=center class="content0">
				<td align="center" class="td_class">选择</td>
				<td align="center" class="td_class" onclick="tableSort(this)"><strong>标识</strong></td>
				<td align="center" class="td_class" onclick="tableSort(this)"><strong>单据时间</strong></td>
				<td align="center" class="td_class" onclick="tableSort(this)"><strong>单据</strong></td>
				<td align="center" class="td_class" onclick="tableSort(this)"><strong>货单状态</strong></td>
				<td align="center" class="td_class" onclick="tableSort(this)"><strong>回复类型</strong></td>
				<td align="center" class="td_class" onclick="tableSort(this)"><strong>到货时间</strong></td>

			</tr>
			
			<c:forEach items="${consignList}" var="item"
                varStatus="vs">
                <tr class="${vs.index % 2 == 0 ? 'content1' : 'content2'}">
                    <td align="center"><input type="radio" name="consigns"
                        statuss="${item.currentStatus}" 
                        value="${item.fullId}"
                        pgid="${item.gid}"
                        ${vs.index== 0 ? "checked" : ""}/></td>
                    <td align="center" onclick="hrefAndSelect(this)">
                    <a
                        href="../sail/transport.do?method=findConsign&fullId=${item.fullId}&gid=${item.gid}"
                        >
                    ${item.gid}</a></td>
                    <td align="center" onclick="hrefAndSelect(this)">${item.outTime}</td>
                    <td align="center" onclick="hrefAndSelect(this)"><a href="../sail/out.do?method=findOut&radioIndex=0&fow=99&outId=${item.fullId}">${item.fullId}</a></td>
                    <td align="center" onclick="hrefAndSelect(this)">${my:get('consignStatus', item.currentStatus)}</td>
                    
                    <td align="center" onclick="hrefAndSelect(this)">${my:get('consignReprotType', item.reprotType)}</td>
                    <td align="center" onclick="hrefAndSelect(this)">${item.arriveDate}</td>
                </tr>
            </c:forEach>
            
		</table>

	</p:subBody>

	<p:line flag="1" />

	<p:button leftWidth="98%" rightWidth="2%">
		
		<div align="right">
		    <input
            type="button" class="button_class"
            value="&nbsp;导出查询结果&nbsp;" onclick="exports()" />&nbsp;&nbsp;
			<input type="button" 
			class="button_class" onclick="pagePrint()"
			value="&nbsp;&nbsp;定制打印&nbsp;&nbsp;">&nbsp;&nbsp;
			<input type="button" class="button_class"
			value="&nbsp;&nbsp;处理发货单&nbsp;&nbsp;" onclick="process()">&nbsp;&nbsp;
		</div>
	</p:button>

	<p:message2 />

</p:body></form>
</body>
</html>

