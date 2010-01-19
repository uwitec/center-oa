<%@ page contentType="text/html;charset=GBK" language="java"
	errorPage="./error.jsp"%>
<%@include file="./common.jsp"%>
<html>
<head>
<p:link title="√‹¬Î…Í«Î" />
<script language="JavaScript" src="../js/prototype.js"></script>
<script language="JavaScript" src="../js/buffalo.js"></script>
<script language="JavaScript" src="../js/template.js"></script>
<script language="JavaScript" src="../js/JCheck.js"></script>
<script src="../js/public.js"></script>
<script language="javascript">

var END_POINT="${pageContext.request.contextPath}/bfapp";

var buffalo = new Buffalo(END_POINT);

function applyPasswords()
{
	if (formCheck())
	buffalo.remoteCall("ehcacheManager.applySecPassWord",[parseInt($("applys").value)], function(reply) {	
		        var result = reply.getResult(); 
		        
		        $("resut").value = result;
		        return;
		});
	
}

function KeyPress()
{
	var event = common.getEvent();
    if(event.keyCode == 13)
    {
        event.keyCode = "";
        applyPasswords();
    }
}

function load()
{
	$('applys').focus();
}

</script>

</head>
<body class="body_class" onload="load()">
<form><p:navigation height="22">
	<td width="550" class="navigation">√‹¬Îπ‹¿Ì &gt;&gt; …Í«Î∂˛¥Œ√‹¬Î</td>
	<td width="85"></td>
</p:navigation> <br>

<p:body width="80%">

	<p:title>
		<td class="caption"><strong>…Í«Î√‹¬Î£∫(√‹¬Î”––ß∆⁄:5∑÷÷”)</strong></td>
	</p:title>

	<p:line flag="0" />

	<p:subBody width="75%">
		<p:table cells="1">
			<p:cell title="…Í«Î ˝ƒø" width="35">
				<input type="text" id="applys" maxLength="20" value="10"
					oncheck="isInt;range(1, 20)" onkeydown="KeyPress()" />
				<font color="#FF0000">*</font>
				<input type="text" disabled="disabled"
					style="visibility: hidden" />
			</p:cell>

			<p:cell title="√‹¬Î">
				<textarea id="resut" rows="20" cols="50" readonly="readonly"></textarea>
			</p:cell>

		</p:table>
	</p:subBody>

	<p:line flag="1" />

	<p:button leftWidth="87%" rightWidth="13%">
		<div align="right"><input type="button" class="button_class"
			style="cursor: pointer" value="&nbsp;&nbsp;…Í «Î&nbsp;&nbsp;"
			onclick="applyPasswords()"></div>
	</p:button>

	<tr>
		<td colspan='2' align="center"><FONT color="blue">${MESSAGE_INFO}</FONT><FONT
			color="red">${errorInfo}</FONT></td>
	</tr>
</p:body></form>
</body>
</html>

