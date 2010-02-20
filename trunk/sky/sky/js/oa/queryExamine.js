function doSearch()
{
	$modalQuery('../admin/query.do?method=popCommonQuery2&key=queryExamine');
}

function queryCustomerDistribute()
{
    $l('../customer/customer.do?method=queryCustomerDistribute');
}

function queryAllStafferCustomerDistribute()
{
    $l('../customer/customer.do?method=queryAllStafferCustomerDistribute');
}

function configYear(opr, grid)
{
    if (getRadio('checkb') && getRadioValue('checkb'))
    {
        var readonly = 0;
        
        if (getRadio('checkb').sstatus != 0 && getRadio('checkb').sstatus != 2)
        {
            readonly = 1;
            
            alert('只有初始和驳回的考评可以配置');
            
            return false;
        }
        
        if (getRadio('checkb').abs == 1)
        {
        	alert('抽象的考评不能单独配置');
            
            return false;
        }
        
        $l('../examine/examine.do?method=configExamineItem&id=' + getRadioValue('checkb') + '&readonly=' + readonly);
    }
}


function changeItem(opr, grid)
{
    if (getRadio('checkb') && getRadioValue('checkb'))
    {
        if (getRadio('checkb').sstatus != 3)
        {
            alert('只有运行考核的可以变更');
            
            return false;
        }
        
        if (getRadio('checkb').atttype == 2)
        {
        	$l('../examine/updateIndex.jsp?&id=' + getRadioValue('checkb'));
        }
        else
        {
            $l('../examine/updateProfitItem.jsp?&id=' + getRadioValue('checkb'));
        }
    }
}

function configAllSubItem(opr, grid)
{
    if (getRadio('checkb') && getRadioValue('checkb'))
    {
        var readonly = 0;
        
        if (getRadio('checkb').atttype != 1 && getRadio('checkb').abs == 0)
        {
            alert('只有选择部门考核');
            
            return false;
        }
        
        $l('../examine/examine.do?method=configAllSubExamineItem&id=' + getRadioValue('checkb') + '&readonly=0');
    }
}

function queryCurrent(opr, grid)
{
	if (getRadio('checkb') && getRadioValue('checkb'))
    {
        $l('../examine/examine.do?method=configExamineItem&look=1&id=' + getRadioValue('checkb'));
    }
}

function queryAllSubCurrent(opr, grid)
{
	if (getRadio('checkb') && getRadioValue('checkb'))
    {
        var readonly = 0;
        
        if (getRadio('checkb').atttype != 1 && getRadio('checkb').abs == 0)
        {
            alert('只有选择部门考核');
            
            return false;
        }
        
        $l('../examine/examine.do?method=configAllSubExamineItem&look=1&id=' + getRadioValue('checkb'));
    }
}

/**
 * 0 or 2 can del examine
 */
function delBean()
{
    if (getRadio('checkb') && getRadioValue('checkb'))
    {
        if (getRadio('checkb').sstatus != 0 && getRadio('checkb').sstatus != 2)
        {
            alert('只有初始和驳回的考评可以删除');
            
            return false;
        }
        
        if (window.confirm('确定删除考评--' + getRadio('checkb').lname))
        $ajax('../examine/examine.do?method=delExamine&id=' + getRadioValue('checkb'), callBackFun);
    }
}

function delBean2()
{
    if (getRadio('checkb') && getRadioValue('checkb'))
    {
        if (getRadio('checkb').sstatus != 3)
        {
            alert('只有运行考核的可以废弃');
            
            return false;
        }
        
        if (window.confirm('确定废弃考评--' + getRadio('checkb').lname))
        $ajax('../examine/examine.do?method=delExamine2&id=' + getRadioValue('checkb'), callBackFun);
    }
}

function submitBean()
{
    if (getRadio('checkb') && getRadioValue('checkb'))
    {
        if (getRadio('checkb').sstatus != 0 && getRadio('checkb').sstatus != 2)
        {
            alert('只有初始和驳回的考评可以提交');
            
            return false;
        }
        
        if (window.confirm('确定提交考评--' + getRadio('checkb').lname))
        $ajax('../examine/examine.do?method=submitExamine&id=' + getRadioValue('checkb'), callBackFun);
    }
}

function passBean()
{
    if (getRadio('checkb') && getRadioValue('checkb'))
    {
        if (getRadio('checkb').sstatus != 1)
        {
            alert('只有待业务员确认的考评可以通过');
            
            return false;
        }
        
        if (window.confirm('确定通过考评--' + getRadio('checkb').lname))
        $ajax('../examine/examine.do?method=passExamine&id=' + getRadioValue('checkb'), callBackFun);
    }
}

function logBean()
{
    if (getRadio('checkb') && getRadioValue('checkb'))
    {
        $ajax('../examine/examine.do?method=queryExaminLog&id=' + getRadioValue('checkb'), callBackFunLog);
    }
}

function rejectBean()
{
    if (getRadio('checkb') && getRadioValue('checkb'))
    {
        if (getRadio('checkb').sstatus != 1)
        {
            alert('只有待业务员确认的考评可以退回');
            
            return false;
        }
        
        $.blockUI({ message: $('#dataDiv'),css:{width: '40%'}});
    }
}

function $process()
{
    if (eCheck([$O('reason')]))
    {
        //字符转义
        var sid = $encode($$('reason'));
        
        if (window.confirm('确定退回选中的考评?'))
        {
            $ajax('../examine/examine.do?method=rejectExamine&id=' + getRadioValue('checkb') + '&reason=' + sid,
                 callBackFun);
            
            $.unblockUI();
        }
    }
}

function callBackFun(data)
{
    reloadTip(data.msg, data.ret == 0);

    if (data.ret == 0)
    commonQuery();
}

function callBackFunLog(data)
{
    $O('logD').innerHTML = '';
    
    var logs = data.msg;
    
    var htm = '';
    for(var i = 0; i < logs.length; i++)
    {
        var item = logs[i];
        
        var llog = item.logTime + ' / ' + item.stafferName + ' / ' + item.operation + ' / ' +  item.log + '<br>';
        
        htm += llog;
    }
    
    $O('logD').innerHTML = htm;
    
    $.blockUI({ message: $('#logDiv'),css:{width: '50%', left : '25%', top : '15%'}});
}

function addBean(opr, grid)
{
   $l('../examine/examine.do?method=preForAddExamine');
}

/**
 * 0 or 2 can update examine
 */
function updateBean(opr, grid)
{
    if (getRadio('checkb') && getRadioValue('checkb'))
    {
        if (getRadio('checkb').sstatus != 0 && getRadio('checkb').sstatus != 2)
        {
            alert('只有初始和驳回的考评可以修改');
            return false;
        }
        
        $l('../examine/examine.do?method=findExamine&update=1&id=' + getRadioValue('checkb'));
    }
}

function commonQuery(par)
{
    gobal_guid.p.queryCondition = par;
    
    gobal_guid.grid.populate(true);
}