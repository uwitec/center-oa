<%@ page contentType="text/html;charset=GBK" language="java"
	errorPage="./error.jsp"%>
<%@include file="./common.jsp"%>
<html>
<head>
<p:link title="库单" />
<script language="JavaScript" src="../js/common.js"></script>
<script language="JavaScript" src="../js/key.js"></script>
<script language="javascript">

function invokeOther()
{
	if ($$('location') == '')
	{
		alert('请选择转调的区域(非物流中心)');
		return false;
	}
	
	if ($$('location') == '0')
	{
		alert('请选择转调的区域(非物流中心)');
		return false;
	}
	
	if ($$('location') == $$('productLocationId'))
	{
		alert('转调区域不能是产品调出区域');
		return false;
	}
	
	if (window.confirm('确定把此调出库单转调至--' + getOptionText('location')))
	{
		document.location.href = '../admin/out.do?method=processInvoke&outId=${out.fullId}&flag=2&changeLocationId=' + $$('location');
	}
}

function recives()
{
	if ($('depotpartId'))
	{
		if ($$('depotpartId') == '')
		{
			alert('请选择自动接口调入的仓区');
			return false;
		}
		
		if (window.confirm('确定全部接受此调出的库单?'))
		document.location.href = '../admin/out.do?method=processInvoke&outId=${out.fullId}&flag=1&depotpartId=' + $$('depotpartId');
	}
	else
	{
		if (window.confirm('确定全部接受此调出的库单?'))
		document.location.href = '../admin/out.do?method=processInvoke&outId=${out.fullId}&flag=1';
	}
}

function rejects()
{
	if (window.confirm('确定驳回此调出的库单?'))
	{
		document.location.href = '../admin/out.do?method=processInvoke&outId=${out.fullId}&flag=3';
	}
}

function load()
{
}
</script>
</head>
<body class="body_class" onload="load()">
<input type="hidden" name="productLocationId" value="${out.location}">
<table width="100%" border="0" cellpadding="0" cellspacing="0" id="na">
	<tr>
		<td height="22" valign="bottom">
		<table width="100%" height="22" border="0" cellpadding="0"
			cellspacing="0">
			<tr valign="middle">
				<td width="8"></td>
				<td width="30">
				<div align="center"><img src="../images/dot_a.gif" width="9"
					height="9"></div>
				</td>
				<td width="550" class="navigation">库单管理 &gt;&gt; 处理调出入库单</td>
				<td width="85"></td>
			</tr>
		</table>
		</td>
	</tr>

	<tr>
		<td height="6" valign="top">
		<table width="100%" border="0" cellpadding="0" cellspacing="0">
			<!--DWLayoutTable-->
			<tr>
				<td width="8" height="6"
					background="../images/index_sp_welcome_center_10.gif"><img
					src="../images/index_sp_welcome_center_07.gif" width="8" height="6"></td>
				<td width="190"
					background="../images/index_sp_welcome_center_08.gif"></td>
				<td width="486"
					background="../images/index_sp_welcome_center_10.gif"></td>
				<td align="right"
					background="../images/index_sp_welcome_center_10.gif">
				<div align="right"><img
					src="../images/index_sp_welcome_center_12.gif" width="23"
					height="6"></div>
				</td>
			</tr>
		</table>
		</td>
	</tr>
</table>

<table width="90%" border="0" cellpadding="0" cellspacing="0"
	align="center">
	<tr>
		<td valign="top" colspan='2'>
		<table width="100%" border="0" cellpadding="0" cellspacing="0">
			<!--DWLayoutTable-->
			<tr>
				<td width="784" height="6"></td>
			</tr>
			<tr>
				<td align="center" valign="top">
				<div align="left">
				<table width="90%" border="0" cellspacing="2">
					<tr>
						<td>
						<table width="100%" border="0" cellpadding="0" cellspacing="10">
							<tr>
								<td width="35">&nbsp;</td>
								<td width="6"><img src="../images/dot_r.gif" width="6"
									height="6"></td>
								<td class="caption"><strong>入库单信息:</strong></td>
							</tr>
						</table>
						</td>
					</tr>


				</table>
				</div>
				</td>
			</tr>
		</table>
		</td>
	</tr>


	<tr>
		<td background="../images/dot_line.gif" colspan='2'></td>
	</tr>

	<tr>
		<td height="10" colspan='2'></td>
	</tr>

	<tr>
		<td colspan='2' align='center'>
		<table width="90%" border="0" cellpadding="0" cellspacing="0"
			class="border">
			<tr>
				<td>
				<table width="100%" border="0" cellspacing='1'>
					<tr class="content2">
						<td width="15%">${ff}日期：</td>
						<td width="35%">${out.outTime}</td>
						<td width="15%">${ff}类型：</td>
						<c:if test="${out.type == 0}">
							<td width="35%">${out.outType == 0 ? "销售出库" : "个人领样"}</td>
						</c:if>

						<c:if test="${out.type == 1}">
							<td width="35%">${my:get('outType', out.outType)}</td>
						</c:if>

					</tr>
					<tr class="content1">

						<c:if test="${out.outType == 1 && out.type == 1}">
							<td>调出部门：</td>
						</c:if>

						<c:if test="${out.outType != 1 || out.type != 1}">
							<td>供应商(客户)：</td>
						</c:if>
						<td>${out.customerName}</td>
						<td>调${ff}部门：</td>
						<td>${out.department}</td>
					</tr>
					<tr class="content2">
						<td>联系人：</td>
						<td>${out.connector}</td>
						<td>联系电话：</td>
						<td>${out.phone}</td>
					</tr>
					<tr class="content1">
						<td>经手人：</td>
						<td>${out.stafferName}</td>
						<td>单据号码：</td>
						<td>${out.fullId}</td>
					</tr>
					
					<tr class="content1">
						<td>相关库单：</td>
						<td><a href="../admin/out.do?method=findOut&outId=${out.refOutFullId}">${out.refOutFullId}</a></td>
						<td>运输单号：</td>
						<td>${out.tranNo}</td>
					</tr>

					<tr class="content2">
						<td>状态：</td>
						<td colspan="1">${my:status(out.status)}</td>
						<td>回款天数：</td>
						<td>${out.reday}</td>
					</tr>

					<c:if test="${out.type == 0}">
						<tr class="content1">
							<td>回款日期：</td>
							<td colspan="1">${out.redate}</td>
							<td>是否回款：</td>
							<td>${out.pay == 0 ? "否" : "是"}</td>
						</tr>
					</c:if>

					<tr class="content2">
						<td>产品区域：</td>
						<td colspan="1">${out.locationName}</td>
						<td>到货日期：</td>
						<td>${out.arriveDate}</td>
					</tr>
					
					<tr class="content1">
						<td>调出目的区域：</td>
						<td colspan="1">${out.destinationName}</td>
						<td>在途方式：</td>
						<%
								String[] sss1 = new String[]{"" , "在途", "在途结束"};
								request.setAttribute("sss1", sss1);
						%>
						<td>${my:getValue(out.inway, sss1)}</td>
					</tr>

					<tr class="content2">
						<td>${ff}单描述：</td>
						<td colspan="3">${out.description}</td>
					</tr>

					<tr class="content1">
						<td>${ff}单核对：</td>
						<td colspan="3">${out.checks}</td>
					</tr>
				</table>
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

	<tr>
		<td colspan='2' align='center'>
		<table width="90%" border="0" cellpadding="0" cellspacing="0"
			class="border">
			<tr>
				<td>
				<table width="100%" border="0" cellspacing='1' id="tables">
					<tr align="center" class="content0">
						<td width="20%" align="center">品名</td>
						<td width="5%" align="center">单位</td>
						<td width="10%" align="center">数量</td>
						<td width="15%" align="center">单价</td>
						<td width="20%" align="left">金额(总计:<span id="total">${my:formatNum(out.total)}</span>)</td>
						<td width="25%" align="center">成本</td>
					</tr>

					<c:forEach items="${baseList}" var="item" varStatus="vs">
						<tr class='${vs.index % 2 == 0 ? "content1" : "content2"}'>
							<td width="20%" align="center">${item.productName}</td>

							<td width="5%" align="center">${item.unit}</td>

							<td width="10%" align="center">${item.amount}</td>

							<td width="15%" align="center">${my:formatNum(item.price)}</td>

							<td width="15%" align="center">${my:formatNum(item.value)}</td>

							<td width="25%" align="center">${item.description}</td>

						</tr>
					</c:forEach>
				</table>
				</td>
			</tr>
		</table>

		</td>
	</tr>

	<tr>
		<td height="10" colspan='2'></td>
	</tr>

	
	<c:if test="${isSystem}">
	<tr id="locations">
		<td height="10" colspan='2' align="center">仓区
			<select name="depotpartId" class="select_class">
				<option value="">--</option>
			<c:forEach items='${depotpartList}' var="item">
				<option value="${item.id}">${item.name}</option>
			</c:forEach>
		</select>
		&nbsp;&nbsp;转调区域
			<select name="location" class="select_class" values="${currentLocationId}">
				<option value="">--</option>
			<c:forEach items='${locationList}' var="item">
				<option value="${item.id}">${item.locationName}</option>
			</c:forEach>
		</select>
		</td>
	</tr>
	
	<tr>
		<td height="10" colspan='2'></td>
	</tr>
	</c:if>
	
	<tr>
		<td background="../images/dot_line.gif" colspan='2'></td>
	</tr>

	<tr>
		<td height="10" colspan='2'></td>
	</tr>

	<tr>
		<td width="92%">
		<div align="right"><input type="button" 
			class="button_class" onclick="recives()"
			value="&nbsp;&nbsp;全部接受&nbsp;&nbsp;">&nbsp;&nbsp;
			<c:if test="${isSystem}">
			<input type="button" 
			class="button_class" onclick="invokeOther()"
			value="&nbsp;转调其他区域&nbsp;">&nbsp;&nbsp;
			</c:if>
			<input type="button" 
			class="button_class" onclick="rejects()"
			value="&nbsp;&nbsp;全部驳回&nbsp;&nbsp;">&nbsp;&nbsp;<input
			type="button" name="ba" class="button_class"
			onclick="javascript:history.go(-1)"
			value="&nbsp;&nbsp;返 回&nbsp;&nbsp;"></div>
		</td>
		<td width="8%"></td>
	</tr>

</table>
</body>
</html>

