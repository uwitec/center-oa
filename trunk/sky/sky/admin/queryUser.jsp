<%@page contentType="text/html; charset=GBK"%>
<%@include file="../common/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<p:link title="用户管理" link="true" guid="true" cal="true" dialog="true" />
<script src="../js/common.js"></script>
<script src="../js/public.js"></script>
<script src="../js/pop.js"></script>
<script src="../js/json.js"></script>
<script src="../js/plugin/highlight/jquery.highlight.js"></script>
<script type="text/javascript">

var allDef = window.top.topFrame.allDef;
var guidMap;
var thisObj;

function load()
{
     preload();
     
	 guidMap = {
		 title: '用户列表',
		 url: '../admin/user.do?method=queryUser',
		 colModel : [
		     {display: '选择', name : 'check', content : '<input type=radio name=checkb value={id} lname={name} role={roleId}>', width : 40, sortable : false, align: 'center'},
		     {display: '用户', name : 'name', width : '15%', sortable : false, align: 'left'},
		     {display: '职员', name : 'stafferName', width : '15%', sortable : false, align: 'left'},
		     {display: '分公司', name : 'locationName', width : '15%', sortable : false, align: 'left'},
		     {display: '状态', name : 'status', width : '10%', sortable : false, align: 'left', cc: 'userStatus'},
		     {display: '最近登录', name : 'loginTime', width : 'auto', sortable : true, align: 'left'}
		     ],
		 extAtt: {
		     name : {begin : '<a href=../admin/role.do?method=findRole&update=1&id={roleId}>', end : '</a>'}
		 },
		 buttons : [
		     {id: 'add', bclass: 'add', onpress : addBean, auth: '010401'},
		     {id: 'update1', caption: '修改权限', bclass: 'update', onpress : updateRole, auth: '010401'},
		     {id: 'del', bclass: 'delete', onpress : delBean, auth: '010401'},
		     {id: 'unlock', caption: '解锁', bclass: 'update', onpress : unlock, auth: '010401'},
		     {id: 'init', caption: '初始化密码', bclass: 'update', onpress : initPassword, auth: '010401'},
		     {id: 'search', bclass: 'search', onpress : doSearch}
		     ],
		 usepager: true,
		 useRp: true,
		 queryMode: 1,
		 cache: 0,
		 auth: window.top.topFrame.gAuth,
		 showTableToggleBtn: true,
		 height: DEFAULT_HEIGHT,
		 def: allDef,
		 callBack: $callBack //for firefox load ext att
	 };
	 
	 $("#mainTable").flexigrid(guidMap, thisObj);
}

function $callBack()
{
    loadForm();
    
    $.highlight($("#mainTable").get(0), '锁定', 'red');
}
 
function doSearch()
{
    $modalQuery('../admin/query.do?method=popCommonQuery2&key=queryUser');
}

function addBean(opr, grid)
{
    $l('../admin/user.do?method=preForAddUser');
}

function delBean()
{
    if (getRadio('checkb') && getRadioValue('checkb'))
    {
        if (window.confirm('确定删除--' + getRadio('checkb').lname))
        {
            $ajax('../admin/user.do?method=delUser&id=' + getRadioValue('checkb'), callBackFun);
        }
    }
}

function initPassword()
{
    if (getRadio('checkb') && getRadioValue('checkb'))
    {
        if (window.confirm('确定初始化密码--' + getRadio('checkb').lname))
        {
            $ajax('../admin/user.do?method=initPassword&id=' + getRadioValue('checkb'), callBackFun);
        }
    }
}

function unlock()
{
    if (getRadio('checkb') && getRadioValue('checkb'))
    {
        if (window.confirm('确定解锁--' + getRadio('checkb').lname))
        {
            $ajax('../admin/user.do?method=unlock&id=' + getRadioValue('checkb'), callBackFun);
        }
    }
}

function callBackFun(data)
{
    reloadTip(data.msg, data.ret == 0);
    
    if (data.ret == 0)
    commonQuery();
}

function updateBean(opr, grid)
{
    if (getRadio('checkb') && getRadioValue('checkb'))
    {
       $l('../admin/user.do?method=findUser&id=' + getRadioValue('checkb'));
    }
}

function updateRole(opr, grid)
{
    if (getRadio('checkb') && getRadioValue('checkb'))
    {
       $l('../admin/role.do?method=findRole&update=1&id=' + getRadio('checkb').role);
    }
}

function commonQuery(par)
{
    gobal_guid.p.queryCondition = par;
    
    gobal_guid.grid.populate(true);
}
</script>
</head>
<body onload="load()" class="body_class">
<form>
<p:cache/>
</form>
<p:message></p:message>
<table id="mainTable" style="display: none"></table>
<p:query/>
</body>