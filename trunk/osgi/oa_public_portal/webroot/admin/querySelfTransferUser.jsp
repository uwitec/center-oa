<%@page contentType="text/html; charset=UTF-8"%>
<%@include file="../common/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<p:link title="交接用户列表" link="true" guid="true" cal="false" dialog="true"/>
<script src="../js/common.js"></script>
<script src="../js/public.js"></script>
<script src="../js/pop.js"></script>
<script src="../js/json.js"></script>
<script src="../js/plugin/highlight/jquery.highlight.js"></script>
<script type="text/javascript">

<%
  String sid = session.getId();

  request.setAttribute("sessionId", sid);
%>
var guidMap;
var thisObj;

var allDef = window.top.topFrame.allDef;

function load()
{
     preload();
     
     guidMap = {
         title: '交接人员用户列表',
         url: '../admin/user.do?method=querySelfTransferUser',
         colModel : [
             {display: '选择', name : 'check', content : '<input type=radio name=checkb value={id} lname={name} role={roleId} llocationId={locationId}>', width : 40, sortable : false, align: 'center'},
             {display: '用户', name : 'name', width : '15%', sortable : false, align: 'left'},
             {display: '职员', name : 'stafferName', width : '15%', sortable : false, align: 'left'},
             {display: '分公司', name : 'locationName', width : '15%', sortable : false, align: 'left'},
             {display: '最近登录', name : 'loginTime', width : 'auto', sortable : false, align: 'left'}
             ],
         extAtt: {
             //name : {begin : '<a href=../admin/role.do?method=findRole&update=1&id={roleId}>', end : '</a>'}
         },
         buttons : [
             {id: 'update', bclass: 'update',caption: '登录', onpress : loginBean}
             ],
         <p:conf queryMode="1"/>
     };
    
	 
	 $("#mainTable").flexigrid(guidMap, thisObj);
 }

function $callBack()
{
    loadForm();
}

function loginBean()
{
    if (getRadioValue('checkb') && window.confirm('确定使用此人员的帐号登录系统'))
    {
        window.top.document.location = '../admin/checkuser.do?method=login&loginType=99&sessionId=${sessionId}&srcUserId=' + getRadioValue('checkb') + '&key=${gkey}';
    }
    else
    $error();
}

</script>
</head>
<body onload="load()" class="body_class">
<form name="mainForm" action="../admin/staffer.do" method="post">
<input type="hidden" name="method" value="" />
<input type="hidden" name="stafferId" value="" />
<p:cache></p:cache>
</form>
<p:message></p:message>
<table id="mainTable" style="display: none"></table>
<p:query/>
</body>