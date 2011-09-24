function addTr()
{
    for (var i = 0; i < 1; i++)
    {
        addTrInner("tables", "trCopy");
    }
}

function addPayTr()
{
    for (var i = 0; i < 1; i++)
    {
        addTrInner("tables_pay", "trCopy_pay");
    }
}

function addShareTr()
{
    for (var i = 0; i < 1; i++)
    {
        addTrInner("tables_share", "trCopy_share");
    }
}

function compareNumber(a, b)
{
	var aa = a * 1000;
    var bb = b * 1000;
    
    if (Math.abs(aa - bb) < 10)
    {
    	return 0;
    }
    
    if (aa > bb)
    {
        return 1;
    }

    return -1;
}

function removeTr(obj)
{
    obj.parentNode.parentNode.removeNode(true);
}

function getEle(eles, name)
{
    for (var i = 0; i < eles.length; i++)
    {
        if (eles[i].name == name)
        {
            return eles[i];
        }
    }
    
    return null;
}

var cityObj;

function selectCity(obj)
{
	cityObj = obj;
    window.common.modal('../admin/pop.do?method=rptQueryCity&load=1&selectMode=1');
}

var budgetObj;

function selectBudget(obj)
{
	budgetObj = obj;
    window.common.modal('../budget/budget.do?method=rptQueryRunDepartmentBudget&load=1&selectMode=1');
}

function calDateInner(obj, name)
{
	var tr = getTrObject(obj);
	
	var el = getInputInTr(tr, name);
	
	return calDate(el)
}

function checks()
{
	if ($$('payType') == 0)
	{
	    var borrow = sumborrowTotal() + parseFloat($$('borrowTotal'));
	        
	    var stotal = sumTotal();
	    
	    if (compareNumber(borrow, stotal) > 0)
	    {
	        alert('公司付款给员工下报销金额必须等于借款金额加公司的付款总金额');
	        
	        return false;
	    }
	}
	
	if ($$('payType') == 1)
    {
        var borrow = parseFloat($$('borrowTotal'));
            
        var stotal = sumTotal();
        
        if (compareNumber(borrow, stotal) > 0)
        {
            alert('收支平衡下报销金额必须等于借款金额');
            
            return false;
        }
    }
	
	if ($$('payType') == 2)
    {
        var borrow = parseFloat($$('borrowTotal'));
        
        var lastMoney = parseFloat($$('lastMoney'));
            
        var stotal = sumTotal() + lastMoney;
        
        if (compareNumber(borrow, stotal) > 0)
        {
            alert('员工付款给公司下报销金额加还款金额必须等于借款金额');
            
            return false;
        }
    }

    return true;
}

function sumRatio()
{
    var total = 0;
    
    $("input[name='s_ratio']").each(
        function()
        {
            if (this.value != '')
            {
                total += parseInt(this.value);
            }
        }
    );   
    
    return total;
}

var gMore = 0;

function showMoreAtt()
{
    var obj = getObj('tr_att_more');
    
    if (gMore % 2 == 0)
    {
        $v('tr_att_more', true);
    }
    else
    {
        $v('tr_att_more', false);
    }
    
    gMore++;
}

function borrowChange()
{
    if ($$('borrow') == 0)
    {
        $v('pay_main_tr', false);
        
        //remove tr
        var list = formEntry.elements;
        
        if (list)
        {
        	for (var i = 0; i < list.length; i++)
            {
            	if (list[i].name== 'pay_del_bu')
            	list[i].onclick.apply(list[i]);
            }
        }
    }
    
    if ($$('borrow') == 1)
    {
        $v('pay_main_tr', true);
    }
}

function payTypeChange()
{
    if ($$('payType') == 0 || $$('payType') == 2)
    {
        $v('pay_main_tr', false);
        
        //remove tr
        var list = formEntry.elements;
        
        if (list)
        {
            for (var i = 0; i < list.length; i++)
            {
                if (list[i].name== 'pay_del_bu')
                list[i].onclick.apply(list[i]);
            }
        }
    }
    
    if ($$('payType') == 1)
    {
        $v('pay_main_tr', true);
    }
    
    if ($$('payType') == 2)
    {
        $v('lastMoney', true);
    }
    else
    {
    	$v('lastMoney', false);
    }
}


function getCitys(oos)
{
    var obj = oos[0];
    
    cityObj.value = obj.pname;
}

function sumborrowTotal()
{
    var total = 0;
    
    $("input[name='p_moneys']").each(
        function()
        {
            if (this.value != '')
            {
                total += parseFloat(this.value);
            }
        }
    );
    
    return total;
}

function sumTotal()
{
    var total = 0;
    
    $('#traTable input').each(
        function()
        {
            if (this.value != '')
            {
                total += parseFloat(this.value);
            }
        }
    );
    
    $("input[name='i_moneys']").each(
        function()
        {
            if (this.value != '')
            {
                total += parseFloat(this.value);
            }
        }
    );
    
    $("input[name='allTotal']").val(formatNum(total + '', 2));
    
    return total;
}

function getRunDepartmentBudgets(oos)
{
    var obj = oos[0];
    
    budgetObj.value = obj.pname;
    
    var tr = getTrObject(budgetObj);
    
    setInputValueInTr(tr, 's_budgetId', obj.value);
    
    setInputValueInTr(tr, 's_departmentName', obj.pdname);
    setInputValueInTr(tr, 's_departmentId', obj.pdid);
    
    setInputValueInTr(tr, 's_approverName', obj.psignername);
    setInputValueInTr(tr, 's_approverId', obj.psignerid);
}

function selectNext(type, value)
{
    if (type == 'group')
    {
    	window.common.modal('../group/group.do?method=rptQueryGroupMember&selectMode=1&load=1&pid=' + value);
    }	
}

function getStaffers(oo)
{
    if (oo.length > 0)
    {
        var item = oo[0];
        $("input[name='processer']").val(item.pname);
        $("input[name='processId']").val(item.value);
    }
}
