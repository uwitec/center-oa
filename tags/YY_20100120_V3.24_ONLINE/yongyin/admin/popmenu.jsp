<%@ page contentType="text/html;charset=GBK" language="java"%>
<HTML>
<HEAD>
<TITLE>菜单</TITLE>
<META http-equiv=Content-Type content=text/html;charset=GBK>
<link href="../css/default.css" type=text/css rel=stylesheet>
<link href="../css/popmenu.css" type=text/css rel=stylesheet>
<SCRIPT src="../js/popmenu_data.js"></SCRIPT>
<SCRIPT src="../js/popmenu.js"></SCRIPT>
<script language="JavaScript" type="text/JavaScript">
function load()
{
	var menu1 = new PopMenu('menuDiv', menuContent)
	menu1.init();
}
</script>
</HEAD>
<BODY bgColor=#ffffff leftMargin=0 topMargin=0 onload="load();"
	marginheight="0" marginwidth="0">
<A name=top></A>
<CENTER>
<TABLE cellSpacing=0 cellPadding=0 width=780 border=0>
	<TBODY>
		<TR>
			<TD align=right>
			<TABLE cellSpacing=0 cellPadding=0 border=0>
				<TBODY>
					<TR>
						<TD><IMG height=22 src="../images/menubar-round.jpg" width=7></TD>
						<TD id=menuDiv bgColor=#002d5e width="660"></TD>
					</TR>
				</TBODY>
			</TABLE>
			</TD>
		</TR>
	</TBODY>
</TABLE>
</CENTER>
</BODY>
</HTML>
