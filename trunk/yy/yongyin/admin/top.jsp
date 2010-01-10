<%@ page contentType="text/html;charset=GBK" language="java"
    errorPage="./error.jsp"%>
<%@include file="./common.jsp"%>
<html>
<head>
<p:link title="TOP" />
<script language="JavaScript" src="../js/public.js"></script>
<script language="JavaScript" src="../js/common.js"></script>
<script language="JavaScript" src="../js/oa/enc.js"></script>
<STYLE type="text/css">
body
{
    font-size: 12px;
}
</STYLE>
<script language="javascript">

function sho()
{
    var targerWindow = window.top.main;
    
    var doc = targerWindow.document;
    
    var baseT = doc.getElementsByTagName("base");
    
    if (baseT && baseT.length > 0 && baseT[0].href)
    {
        var links = baseT[0].href;
        
        var jsp = links.substring(links.lastIndexOf('/') + 1);
        
        window.common.clipboard(jsp); 
    }
}

function load()
{
    <c:if test="${hasEncLock}">
    setTimeout("checkLock()", 30000);
    </c:if>
}

var faileCount = 0;

function checkLock()
{
    var str = checkLockExist();
    
    if (str == '' || str != '${gkey}')
    {
        alert('检查加密锁失败[' + (faileCount + 1) + '次],请插入加密锁否则系统自动退出!');
        
        faileCount++;
        
        if (faileCount >= 3)
        {
            window.top.location = '../admin/logout.do';
            
            return;
        }
    }
    else
    {
        faileCount = 0;
    }
    
    
    setTimeout("checkLock()", 30000);
}
</script>
</head>
<body bgcolor="#FFFFFF" leftmargin="0" topmargin="0" marginwidth="0" onload="load()"
    marginheight="0">

<table width="100%" border="0" cellspacing="0" cellpadding="0" background="../images/login/vistablue.jpg" style="height: 63px">
  <tr ondblclick="sho()" background="../images/login/vistablue.jpg">
    <td ondblclick="sho()"></td>
    <td ondblclick="sho()"><font color="#FFFFFF" size="2"><b>&nbsp;${SN}/SKY软件【V3.23.20100102】</b></font></td>
    
    
    <td   ondblclick="sho()" align="right">
    <font color="#FFFFFF">登录者：${user.stafferName}</font>&nbsp;&nbsp;
    <c:if test="${user.role != 'NETASK'}">
    <a href="../admin/logout.do" target="_parent" title="退出登录"><img src="../images/login/logout.gif" width="20px" height="20px" border="0"/></a>
    </c:if>
    
    <c:if test="${user.role == 'NETASK'}">
    <a href="../admin/logoutAsk.do" target="_parent" title="退出登录"><img src="../images/login/logout.gif" width="20px" height="20px" border="0"/></a>
    </c:if>
    </td>
    <td ondblclick="sho()" width="2%"></td>
    
  </tr>
</table>
</body>
</html>
