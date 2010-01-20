<%@ page contentType="text/html;charset=GBK" language="java"%>
<%@ taglib prefix="my" uri="/tags/elFunction"%>
<%@include file="./common.jsp"%>
<html>

<head>
<p:link title="销售单列表" />
<script src="../js/CommonScriptMethod.js"></script>
<script src="../js/prototype.js"></script>
<script src="../js/common.js"></script>
<script src="../js/title_div.js"></script>
<script src="../js/public.js"></script>
<script language="JavaScript" src="../js/cal.js"></script>
<script language="javascript">
var jmap = new Object();
<c:forEach items="${flagOut}" var="item">
	jmap['${item.fullId}'] = "${divMap[item.fullId]}";
</c:forEach>

function showDiv(id)
{
	tooltip.showTable(jmap[id]);
}

</script>

<script src="../js/CommonScriptMethod.js"></script>
</head>
<c:set var="show" value='${user.role == "COMMON" ? "回款" : "到货"}'/>
<body class="body_class" onkeypress="tooltip.bingEsc(event)">
<form action="./out.do" name="adminForm"><input type="hidden"
	value="queryOut3" name="method"> <input type="hidden" value="1"
	name="firstLoad">
	<input type="hidden" value="${customerId}"
	name="customerId">
	<input type="hidden" value=""
	name="outId">
<table width="100%" border="0" cellpadding="0" cellspacing="0">
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
				<td width="550" class="navigation">销售单</td>
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

<br>
<table width="85%" border="0" cellpadding="0" cellspacing="0"
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
								<td class="caption"><strong>需要${show}的销售单:</strong><font color=blue>[当前数量:${my:length(flagOut)}]</font></td>
							</tr>
						</table>
						</td>
					</tr>
				</table>
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
		<td align='center' colspan='2'>
		<table width="90%" border="0" cellpadding="0" cellspacing="0"
			class="border">
			<tr>
				<td>
				<table width="100%" border="0" cellspacing='1'>
					<tr align="center" class="content0">
						<td align="center" width="5%" align="center">选择</td>
						<td align="center" onclick="tableSort(this)" class="td_class">单据编号</td>
						<td align="center" onclick="tableSort(this)" class="td_class">客户</td>
						<td align="center" onclick="tableSort(this)" class="td_class">库单类型</td>
						<td align="center" onclick="tableSort(this)" class="td_class">填写时间</td>
						<td align="center" onclick="tableSort(this)" class="td_class">回款日期</td>
						<td align="center" onclick="tableSort(this)" class="td_class">金额</td>
						<td align="center" onclick="tableSort(this)" class="td_class">填写人</td>
						<td align="center" onclick="tableSort(this)" class="td_class">到货日期</td>
						<td align="center" onclick="tableSort(this)" class="td_class">是否超期</td>
					</tr>

					<c:forEach items="${flagOut}" var="item" varStatus="vs">
						<tr class='${vs.index % 2 == 0 ? "content1" : "content2"}'>
							<td align="center"><input type="radio" name="fullId"
								statuss='${item.status}' value="${item.fullId}" ${vs.index== 0 ? "checked" : ""}/></td>
							<td align="center"
							onMouseOver="showDiv('${item.fullId}')" onmousemove="tooltip.move()" onmouseout="tooltip.hide()"><a href="../admin/out.do?method=findOut&outId=${item.fullId}">
							${item.fullId}</a></td>
							<td align="center">${item.customerName}</td>
							<td align="center">${item.type == 0 ? "销售单" : "入库单"}</td>
							<td align="center">${item.outTime}</td>
							<c:if test="${item.pay == 0}">
							<td align="center"><font color=red>${item.redate}</font></td>
							</c:if>
							<c:if test="${item.pay == 1}">
							<td align="center"><font color=blue>${item.redate}</font></td>
							</c:if>
							<c:if test="${item.pay == 2}">
							<td align="center"><font color=red>${item.redate}</font></td>
							</c:if>
							<td align="center">${item.total}</td>
							<td align="center">${item.stafferName}</td>
							<td align="center"><font color=blue>${item.arriveDate}</font></td>
							<td align="center">${item.pay == 2 ? "<font color=red>超期</font>" : "未超期"}</td>
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
</table>

</form>
</body>
</html>
