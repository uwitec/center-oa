function doSearch()
{
	$modalQuery('../admin/query.do?method=popCommonQuery2&key=queryProductExamine');
}

function addBean(opr, grid)
{
   $l('../examine/product.do?method=preForAddProductExamine');
}

function delBean()
{
    if (getRadio('checkb') && getRadioValue('checkb'))
    {
        if (getRadio('checkb').sstatus != 0 && getRadio('checkb').sstatus != 2)
        {
            alert('只有初始的考评可以删除');
            
            return false;
        }
        
        if (window.confirm('确定删除产品考评--' + getRadio('checkb').lname))
        {
            $ajax('../examine/product.do?method=delProductExamine&id=' + getRadioValue('checkb'), callBackFun);
        }
    }
}

function submitBean()
{
    if (getRadio('checkb') && getRadioValue('checkb'))
    {
        if (getRadio('checkb').sstatus != 0 && getRadio('checkb').sstatus != 2)
        {
            alert('只有初始的考评可以提交');
            
            return false;
        }
        
        if (window.confirm('确定提交考评--' + getRadio('checkb').lname))
        $ajax('../examine/product.do?method=submitProductExamine&id=' + getRadioValue('checkb'), callBackFun);
    }
}

function updateBean()
{
	if (getRadio('checkb') && getRadioValue('checkb'))
    {
        if (getRadio('checkb').sstatus != 0 && getRadio('checkb').sstatus != 2)
        {
            alert('只有初始的考评可以修改');
            
            return false;
        }
        
        $l('../examine/product.do?method=queryProductExamineItem&pid=' + getRadioValue('checkb'));
    }
}

function queryCurrent()
{
	if (getRadio('checkb') && getRadioValue('checkb'))
    {
        $l('../examine/product.do?method=queryProductExamineItem&look=1&pid=' + getRadioValue('checkb'));
    }
}

function callBackFun(data)
{
    reloadTip(data.msg, data.ret == 0);

    if (data.ret == 0)
    commonQuery();
}

function logBean()
{
    if (getRadio('checkb') && getRadioValue('checkb'))
    {
        $ajax('../examine/examine.do?method=queryExaminLog&id=' + getRadioValue('checkb'), callBackFunLog);
    }
}

function callBackFunLog(data)
{
    $O('logD').innerHTML = '';
    
    var logs = data.msg;
    
    var htm = '';
    for(var i = 0; i < logs.length; i++)
    {
        var item = logs[i];
        
        var llog = item.logTime + ' / ' + item.operation + ' / ' +  item.log + '<br>';
        
        htm += llog;
    }
    
    $O('logD').innerHTML = htm;
    
    $.blockUI({ message: $('#logDiv'),css:{width: '40%'}});
}