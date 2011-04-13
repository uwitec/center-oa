<%@ page contentType="text/html;charset=UTF-8" language="java"%>

//当前的焦点对象
var oo;

var ids = '';
var amous = '';
var tsts;
var messk = '';
var locationId = '${currentLocationId}';
var currentLocationId = '${currentLocationId}';

var showJSON = JSON.parse('${showJSON}');

function total()
{
    var obj = document.getElementsByName("value");

    var total = 0;
    for (var i = 1; i < obj.length; i++)
    {
        if (obj[i].value != '')
        {
            total = add(total, parseFloat(obj[i].value));
        }
    }

    var ss =  document.getElementById("total");
    tsts = formatNum(total, 2);
    ss.innerHTML = '(总计:' + tsts + ')';
}

function titleChange()
{
    if ($O('outType'))
    {
        removeOption($O('outType'), 0);
        removeOption($O('outType'), 4);
        removeOption($O('outType'), 5);
    }
}

function load()
{
    titleChange();
    
    loadForm();
    
    managerChange();
}

function check()
{
    if (!formCheck())
    {
        return false;
    }
    
    if ($$('outType') == 1 && $$('destinationId') == $$('location'))
    {
        alert('源仓库和目的仓库不能相同');
        return false;
    }

    ids = '';
    amous = '';
    
    $O('priceList').value = '';
    $O('totalList').value = '';
    $O('nameList').value = '';
    $O('unitList').value = '';
    $O('otherList').value = '';
    $O('showIdList').value = '';
    $O('showNameList').value = '';
    
    if (trim($O('outTime').value) == '')
    {
        alert('请选择销售日期');
        return false;
    }

    if ($$('outType') == '')
    {
        alert('请选择库单类型');
        return false;
    }

    var proNames = document.getElementsByName('productName');
    var units = document.getElementsByName('unit');
    var amounts = document.getElementsByName('amount');
    var prices = document.getElementsByName('price');
    var values = document.getElementsByName('value');
    var outProductNames = document.getElementsByName('outProductName');

    var tmpMap = {};
    //isNumbers
    for (var i = 1; i < proNames.length; i++)
    {
        if (proNames[i].value == '' || proNames[i].productid == '')
        {
            alert('数据不完整,请选择产品名称!');
            
            return false;
        }

        ids = ids + proNames[i].productid + '~';

        $O('nameList').value = $O('nameList').value +  proNames[i].value + '~';
        
        var ikey = toUnqueStr2(proNames[i]);
        
        if (tmpMap[ikey])
        {
            alert('选择的产品重复[仓区+产品+职员+价格],请核实!');
            
            return false;
        }
        
        tmpMap[ikey] = ikey;
        
        //库存重要的标识
        $O('otherList').value = $O('otherList').value + ikey + '~';

        $O('idsList').value = ids;
    }

    for (var i = 1; i < amounts.length; i++)
    {
        if (trim(amounts[i].value) == '')
        {
            alert('数据不完整,请填写产品数量!');
            amounts[i].focus();
            return false;
        }

        if (!isNumbers(amounts[i].value))
        {
            alert('数据错误,产品数量 只能是整数!');
            amounts[i].focus();
            return false;
        }

        amous = amous + amounts[i].value + '~';

        $O('amontList').value = amous;
    }
    
    for (var i = 1; i < outProductNames.length; i++)
    {
        if (trim(outProductNames[i].value) == '')
        {
            alert('数据不完整,请选择!');
            outProductNames[i].focus();
            return false;
        }

        amous = amous + amounts[i].value + '~';

        $O('showIdList').value =  $O('showIdList').value + outProductNames[i].value + '~';
        
        $O('showNameList').value =  $O('showNameList').value + getOptionText(outProductNames[i]) + '~';
    }

    for (var i = 1; i < prices.length; i++)
    {
        if (trim(prices[i].value) == '')
        {
            alert('数据不完整,请填写产品价格!');
            prices[i].focus();
            return false;
        }
        
        if (!isFloat(prices[i].value))
        {
            alert('数据错误,产品数量只能是浮点数!');
            prices[i].focus();
            return false;
        }

        $O('priceList').value = $O('priceList').value + prices[i].value + '~';
    }

    var desList = document.getElementsByName('desciprt');
    
    for (var i = 1; i < desList.length; i++)
    {
        if (trim(desList[i].value) == '')
        {
            alert('成本是必填!');
            desList[i].focus();
            return false;
        }
        
        if (!isFloat(desList[i].value))
        {
            alert('格式错误,成本只能是浮点数!');
            desList[i].focus();
            return false;
        }
        
        if (parseFloat(trim(desList[i].value)) == 0)
        {
            alert('入库成本价格不能为0!');
            desList[i].focus();
            return false;
        }
    }
    
    for (var i = 1; i < values.length; i++)
    {
        $O('totalList').value = $O('totalList').value + values[i].value + '~';
        $O('desList').value = $O('desList').value + desList[i].value + '~';
    }

    for (var i = 1; i < units.length; i++)
    {
        $O('unitList').value = $O('unitList').value + units[i].value + '~';
    }

    $O('totalss').value = tsts;

    return true;
}

function checkTotal()
{
    messk = '';
    var gh = $O('nameList').value.split('~');
    var ghk = $O('amontList').value.split('~');

    messk += '\r\n';
    for(var i = 0 ; i < gh.length - 1; i++)
    {
        messk += '\r\n' + '产品【' + gh[i] + '】   数量:' + ghk[i];
    }


    if ($O('saves').value == 'save')
    {
         if (window.confirm("入库单所有类型都是正数增加库存，负数减少库存，您确认填写的调出符合实际情形,确定保存入库单?" + messk))
         {
            disableAllButton();
            outForm.submit();
         }

         return;
    }

    ccv = $$('location');

    if (ccv == '')
    {
        alert('产品仓库为空，请核实');
        return false;
    }

    //判断method
    if ($$('method') != 'addOut')
    {
        alert('提示：提交没有方法，请重新登录操作');
        return false;
    }

    if (window.confirm("入库单所有类型都是正数增加库存，负数减少库存，您确认填写的调出符合实际情形,确定提交库单?" + messk))
    {
        disableAllButton();
        outForm.submit();
    }
}

function save()
{
    $O('saves').value = 'save';
    if (check())
    {
        checkTotal();
    }
}

function sub()
{
    $O('saves').value = 'submit';
    if (check())
    {
        checkTotal();
    }
}

var modifyPage = ('${bean.fullId}' != '');

var g_url_query = 0;

function managerChange()
{
    g_url_query = 0;
    
    //调拨
    if ($$('outType') == 2 || $$('outType') == 3 || $$('outType') == 6 || $$('outType') == 99)
    {
        showTr('dir_tr', false);
    }
    
    
    if ($$('outType') == 1)
    {
        showTr('dir_tr', true);
    }
    
    //报废
    if ($$('outType') == 2)
    {
        showTr('duty_tr', true);
        showTr('invoice_tr', false);
    }
    
    if ($$('outType') == 1 || $$('outType') == 3 || $$('outType') == 99)
    {
         showTr('duty_tr', false);
         showTr('invoice_tr', false);
    }
    
    if ($$('outType') == 6)
    {
        showTr('pro_tr', true);
        showTr('duty_tr', true);
    }
    else
    {
        showTr('pro_tr', false);
    }
    
    if ($$('outType') == 99)
    {
        var nameList = document.getElementsByName("price");
        
        for (var i = 0; i < nameList.length; i++)
        {
            nameList[i].readOnly = false;
        }
        
        //desciprt
        nameList = document.getElementsByName("desciprt");
        
        for (var i = 0; i < nameList.length; i++)
        {
            nameList[i].readOnly = false;
        }
        
        g_url_query = 1;
    }
    else
    {
        var nameList = document.getElementsByName("price");
        
        for (var i = 0; i < nameList.length; i++)
        {
            nameList[i].readOnly = true;
            
            if (!modifyPage)
            {
                nameList[i].value = '0.0';
                cc(nameList[i]);
            }
        }
        
        nameList = document.getElementsByName("desciprt");
        
        for (var i = 0; i < nameList.length; i++)
        {
            nameList[i].readOnly = true;
            
            if (!modifyPage)
            {
                nameList[i].value = '0.0';
            }
        }
    }
}

function showTr(id, show)
{
    $v(id, show);
    $d(id, !show);
}

function selectCustomer()
{
    window.common.modal('../finance/finance.do?method=rptQueryUnit&load=1');
}

function getUnit(oo)
{
    $O('customerId').value = oo.value;
    $O('customerName').value = oo.pname;
}



