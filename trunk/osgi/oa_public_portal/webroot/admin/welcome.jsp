<%@ page contentType="text/html;charset=UTF-8" language="java"
	errorPage="../common/error.jsp"%>
<%@include file="../common/common.jsp"%>
<HTML xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<p:link title="主题" link="true" guid="false" cal="false"/>
<META http-equiv=Content-Type content="text/html; charset=UTF-8">
<LINK href="../css/tabs/jquery.tabs-ie.css" type=text/css rel=stylesheet>
<LINK href="../css/tabs/jquery.tabs.css" type=text/css rel=stylesheet>
<script src="../js/jquery/jquery.js"></script>
<SCRIPT src="../js/jquery/jquery.tabs.js"></SCRIPT>
<SCRIPT>
function load()
{
    $('#container-1').tabs();
    
    //显示container-1
    $('#container-1').css({display:"block"});
}
</SCRIPT>
</HEAD>
<BODY onload=load()>
<div id="container-1" style="display: none;">
<ul>
	<li><a href="#fragment-1"><span>我的桌面</span></a></li>
	
	<c:if test='${my:auth(user, "1411") && my:dym("com.china.center.oa.sail.portal")}'>
	<li><a href="#fragment-Consign"><span>今天到货</span></a></li>
	</c:if>
	
	<c:if test='${my:auth(user, "1401") && my:dym("com.china.center.oa.sail.portal")}'>
    <li><a href="#fragment-Out"><span>回款预警</span></a></li>
    </c:if>
	
</ul>
<c:if test='${my:dym("com.china.center.oa.flow.portal")}'>
<div id="fragment-1"><IFRAME height="100%"
	src="../pers/persional.do?method=queryPersionalDeskTop"
	id="ifr1" frameborder="0" width="100%" scrolling="auto"></IFRAME></div>
</c:if>

<c:if test="${!my:dym('com.china.center.oa.flow.portal')}">
<div id="fragment-1"><IFRAME height="100%"
    src="test.htm"
    id="ifr1" frameborder="0" width="100%" scrolling="auto"></IFRAME></div>
</c:if>

<!-- ///////////////////////////////////////////////////////////////////////// -->

<c:if test='${my:auth(user, "1411") && my:dym("com.china.center.oa.sail.portal")}'>
<div id="fragment-Consign"><IFRAME height="100%"
    src="../sail/transport.do?method=queryTodayConsign"
    id="ifr1" frameborder="0" width="100%" scrolling="auto"></IFRAME></div>
</c:if>

<c:if test='${my:auth(user, "1401") && my:dym("com.china.center.oa.sail.portal")}'>
<div id="fragment-Out"><IFRAME height="100%"
    src="../sail/out.do?method=queryWarnOut"
    id="ifr1" frameborder="0" width="100%" scrolling="auto"></IFRAME></div>
</c:if>

</div>
</BODY>
</HTML>