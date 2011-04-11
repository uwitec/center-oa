<%@ page contentType="text/html;charset=UTF-8" language="java"
    errorPage="../common/error.jsp"%>
<%@include file="../common/common.jsp"%>
<html>
<head>
<p:link title="盘点列表" />
<script language="JavaScript" src="../js/common.js"></script>
<script language="JavaScript" src="../js/JCheck.js"></script>
<script language="JavaScript" src="../js/tableSort.js"></script>
<script language="javascript">
function process()
{
	if (getRadioValue('consigns') == '')
	{
		alert('请选择');
		return;
	}
	
	document.location.href = '../product/reports.do?method=listStorageLog&productId=' + getRadioValue('consigns');
}

function exports()
{
	document.location.href = '../product/reports.do?method=export';
}

function exportsAll()
{
	document.location.href = '../product/reports.do?method=exportAll';
}


function load()
{
	loadForm();
}

function cc()
{
	if (compareDays($$('beginDate'), $$('endDate')) > 40)
	{
		alert('跨度不能大于40天');
		return false;
	}
	
	return true;
}
function stat()
{
	submit(null, null, cc);
}
</script>

</head>
<body class="body_class" onload="load()">
<form name="formEntry" action="../product/reports.do"><input
	type="hidden" name="method" value="statReports"> <p:navigation
	height="22">
	<td width="550" class="navigation">盘点管理 &gt;&gt; 盘点列表</td>
	<td width="85"></td>
</p:navigation> <br>

<p:body width="90%">

	<p:subBody width="95%">
		<table width="100%" align="center" cellspacing='1' class="table0">
			<tr class="content1">
				<td width="15%" align="center">开始时间</td>
				<td align="left">
				<p:plugin name="beginDate" size="20" value="${beginDate}"  oncheck="notNone"/>
				</td>
				<td width="15%" align="center">结束时间</td>
				<td align="left">
				<p:plugin name="endDate" size="20" value="${endDate}"  oncheck="notNone"/>
				</td>
			</tr>

			<tr class="content2">
				<td width="15%" align="center">仓区:</td>
				<td align="left" colspan="1"><select name="depotpartId"
					class="select_class" values="${depotpartId}" style="width: 100%">
					<option value="">--</option>
					<c:forEach items="${depotpartList}" var="item">
					<option value="${item.id}">${item.name}</option>
					</c:forEach>
				</select></td>
				<td width="15%" align="center">产品</td>
				<td align="left" colspan="1"><input type="text" name="productName" value="${productName}"></td>
			</tr>

			<tr class="content1">
				<td colspan="4" align="right"><input type="button" onclick="stat()"
					class="button_class" value="&nbsp;&nbsp;查 询&nbsp;&nbsp;">&nbsp;&nbsp;<input
					type="reset" class="button_class"
					value="&nbsp;&nbsp;重 置&nbsp;&nbsp;"></td>
			</tr>
		</table>

	</p:subBody>

	<p:title>
		<td class="caption"><strong>盘点列表：</strong></td>
	</p:title>

	<p:line flag="0" />

	<p:subBody width="95%">
		<table width="100%" align="center" cellspacing='1' class="table0">
			<tr align=center class="content0">
				<td align="center" class="td_class">选择</td>
				<td align="center" class="td_class" onclick="tableSort(this)"><strong>仓区</strong></td>
				<td align="center" class="td_class" onclick="tableSort(this)"><strong>储位</strong></td>
				<td align="center" class="td_class" onclick="tableSort(this)"><strong>产品</strong></td>
				<td align="center" class="td_class" onclick="tableSort(this, true)"><strong>原始数量</strong></td>
				<td align="center" class="td_class" onclick="tableSort(this, true)"><strong>异动数量</strong></td>
				<td align="center" class="td_class" onclick="tableSort(this, true)"><strong>当前数量</strong></td>
			</tr>

			<c:forEach items="${statList}" var="item"
				varStatus="vs">
				<tr class="${vs.index % 2 == 0 ? 'content1' : 'content2'}">
					<td align="center"><input type="radio" name="consigns"
						value="${item.productId};${item.depotpartId}"
						${vs.index== 0 ? "checked" : ""}/></td>
					<td align="center" onclick="hrefAndSelect(this)">${item.depotpartName}</td>
					<td align="center" onclick="hrefAndSelect(this)">${item.storageName}</td>
					<td align="center" onclick="hrefAndSelect(this)">${item.productName}</td>
					
					<td align="center" onclick="hrefAndSelect(this)">${item.preAmount}</td>
					<td align="center" onclick="hrefAndSelect(this)">${item.changeAmount}</td>
					<td align="center" onclick="hrefAndSelect(this)">${item.currentAmount}</td>
				</tr>
			</c:forEach>
		</table>

	</p:subBody>

	<p:line flag="1" />

	<p:button leftWidth="98%" rightWidth="2%">
		<div align="right"><input type="button" class="button_class"
			value="&nbsp;&nbsp;导出异动&nbsp;&nbsp;" onclick="exports()">&nbsp;&nbsp;
			<input type="button" class="button_class"
			value="&nbsp;&nbsp;导出盘点&nbsp;&nbsp;" onclick="exportsAll()">&nbsp;&nbsp;
			<input type="button" class="button_class"
			value="&nbsp;&nbsp;查看明细&nbsp;&nbsp;" onclick="process()">&nbsp;&nbsp;
		</div>
	</p:button>

	<p:message />

</p:body></form>
</body>
</html>

