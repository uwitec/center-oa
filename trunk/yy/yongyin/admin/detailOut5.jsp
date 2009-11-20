<%@ page contentType="text/html;charset=GBK" language="java"%>
<%@ taglib prefix="my" uri="/tags/elFunction"%>
<%@include file="./common.jsp"%>
<html>
<head>
<p:link title="发货单" />
<script language="JavaScript" src="../js/prototype.js"></script>
<script language="JavaScript" src="../js/common.js"></script>
<script language="JavaScript" src="../js/JCheck.js"></script>
<script language="JavaScript" src="../js/key.js"></script>
<script language="javascript">
function sub()
{
	if (formCheck())
	{
		if (window.confirm('确定审核通过发货单?'))
		{
			outForm.submit();
		}
	}
}

function sub1()
{
	if (formCheck())
	{
		if (window.confirm('确次定发货单已经收货?'))
		{
			$('method').value = 'reportConsign';
			outForm.submit();
		}
	}
}


function load()
{
	loadForm();
	change();
	loadForm();
}

function pris()
{
	$('pr').style.display = 'none';
	$('na').style.display = 'none';
	window.print();
	
	$('pr').style.display = 'block';
	$('na').style.display = 'inline';	
}

var jmap = new Object();
<c:forEach items="${transportList}" var="item">
	jmap['${item.id}'] = "${map[item.id]}";
</c:forEach>

function change()
{
	if ($('transport') == null)
	{
		return;
	}
	
	removeAllItem($('transport'));
	
	setOption($('transport'), '', '--');
	var items = jmap[$F('transport1')];
	
	if (isNoneInCommon(items))
	{
		return;
	}
	
	var ss = items.split('#');
	for (var i = 0; i < ss.length; i++)
	{
		if (!isNoneInCommon(ss[i]))
		{
			var kk = ss[i].split('~');
			setOption($('transport'), kk[1], kk[0]);
		}
	}
}
</script>
</head>
<body class="body_class" onload="load()">
<form name="outForm" method=post action="../admin/transport.do"><input
	type=hidden name="method" value="passConsign" /> <input type=hidden
	name="fullId" value="${out.fullId}" /> <input type="hidden" value="3"
	name="statuss">
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
				<td width="550" class="navigation">发货单库单</td>
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

<table width="80%" border="0" cellpadding="0" cellspacing="0"
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
								<td class="caption"><strong>发货单单信息:</strong></td>
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
		<table width="85%" border="0" cellpadding="0" cellspacing="0"
			class="border">
			<tr>
				<td>
				<table width="100%" border="0" cellspacing='1'>
					<tr class="content2">
						<td width="15%">发货单日期：</td>
						<td width="35%">${out.outTime}</td>
						<td width="15%">销售类型：</td>
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
						<td>调出部门：</td>
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
							<c:if test="${out.pay == 0}">
								<td></td>
								<td></td>
							</c:if>
							<c:if test="${out.pay == 1}">
								<td>是否回款：</td>
								<td>是</td>
							</c:if>
						</tr>
					</c:if>

					<tr class="content1">
						<td>产品区域：</td>
						<td colspan="1">${out.locationName}</td>
						<td>到货日期：</td>
						<td>${out.arriveDate}</td>
					</tr>
					
					<tr class="content1">
						<td>仓区：</td>
						<td colspan="1">${out.depotpartName}</td>
						<td></td>
						<td></td>
					</tr>

					<tr class="content2">
						<td>销售单描述：</td>
						<td colspan="3"><textarea rows="3" cols="55"
							readonly="readonly">${out.description}</textarea></td>
					</tr>

					<c:if test="${consignBean.currentStatus < 2}">
						<tr class="content1">
							<td>运输方式：</td>
							<td colspan="3"><select name="transport1" oncheck="notNone"
								values="${tss1.id}" onchange="change()">
								<option value="">--</option>
								<c:forEach items="${transportList}" var="item">
									<option value="${item.id}">${item.name}</option>
								</c:forEach>
							</select>&nbsp;&nbsp; <select name="transport" oncheck="notNone"
								values="${consignBean.transport}">
								<option value="">--</option>
							</select></td>
						</tr>
						
						<tr class="content2">
                            <td>发货单号：</td>
                            <td colspan="3"><input type="text" name="transportNo" id="transportNo" class="input_class"></td>
                        </tr>

						<tr class="content1">
							<td>发货单备注：</td>
							<td colspan="3"><textarea rows="3" cols="55" name="applys" head="发货单备注"
								oncheck="notNone">${consignBean.applys}</textarea></td>
						</tr>
					</c:if>

					<c:if test="${consignBean.currentStatus >= 2}">
						<tr class="content1">
							<td>运输方式：</td>
							<td colspan="3">${tss1.name} &gt;&gt; ${tss.name}</td>
						</tr>
						
						<tr class="content2">
                            <td>发货单号：</td>
                            <td colspan="3">${consignBean.transportNo}</td>
                        </tr>

						<tr class="content2">
							<td>发货单备注：</td>
							<td colspan="3"><textarea rows="3" cols="55" name="applys"
								readonly="readonly" oncheck="notNone">${consignBean.applys}</textarea></td>
						</tr>
					</c:if>

					<c:if test="${consignBean.reprotType == 0 && consignBean.currentStatus >= 2}">
						<tr class="content1">
							<td>收货类型：</td>
							<td colspan="3"><select name="reprotType" oncheck="notNone"
								values="${consignBean.reprotType}">
								<option value="">--</option>
								<option value="1">正常收货</option>
								<option value="2">异常收货</option>
							</select></td>
						</tr>
					</c:if>
					
					<c:if test="${consignBean.reprotType != 0}">
						<tr class="content1">
							<td>收货类型：</td>
							<td colspan="3">${consignBean.reprotType == 1 ? "正常收货" : "异常收货"}</td>
						</tr>
					</c:if>


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
		<table width="85%" border="0" cellpadding="0" cellspacing="0"
			class="border">
			<tr>
				<td>
				<table width="100%" border="0" cellspacing='1' id="tables">
					<tr align="center" class="content0">
						<td width="10%" align="center">储位</td>
						<td width="20%" align="center">品名</td>
						<td width="5%" align="center">单位</td>
						<td width="10%" align="center">数量</td>
						<td width="15%" align="center">单价</td>
						<td width="15%" align="left">金额(总计:<span id="total">${my:formatNum(out.total)}</span>)</td>
						<td width="20%" align="center">成本</td>
					</tr>

					<c:forEach items="${baseList}" var="item" varStatus="vs">
						<tr class='${vs.index % 2 == 0 ? "content1" : "content2"}'>
						
							<td width="20%" align="center">${item.storageId}</td>

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

	<tr>
		<td background="../images/dot_line.gif" colspan='2'></td>
	</tr>

	<tr>
		<td height="10" colspan='2'></td>
	</tr>

	<tr>
		<td height="20" colspan='2'></td>
	</tr>

	<tr>
		<td width="92%">
		<div align="right" id="pr"><c:if test="${init}">
			<input type="button" class="button_class" onclick="sub()"
				value="&nbsp;&nbsp;审核通过&nbsp;&nbsp;">&nbsp;&nbsp;
			</c:if><c:if test="${consignBean.reprotType == 0 && consignBean.currentStatus >= 2}">
			<input type="button" class="button_class" onclick="sub1()"
				value="&nbsp;&nbsp;确定收货&nbsp;&nbsp;">&nbsp;&nbsp;
			</c:if> <input type="button" class="button_class"
			onclick="javascript:history.go(-1)"
			value="&nbsp;&nbsp;返 回&nbsp;&nbsp;"></div>
		</td>
		<td width="8%"></td>
	</tr>

</table>
</form>
</body>
</html>

