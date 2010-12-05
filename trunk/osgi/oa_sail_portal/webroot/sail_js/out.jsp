<%@ page contentType="text/html;charset=UTF-8" language="java"%>

//当前的焦点对象
var oo;

var ids = '';
var amous = '';
var tsts;
var messk = '';
var locationId = '${currentLocationId}';
var currentLocationId = '${currentLocationId}';

function selectCustomer()
{
    window.common.modal("../customer/customer.do?method=rptQuerySelfCustomer&stafferId=${user.stafferId}&load=1");
}

//默认黑名单
var BLACK_LEVEL = '90000000000000000000';

function getCustomer(oos)
{
    var obj = oos;
    
    if ($$('outType') == 3 && obj.pcreditlevelid == BLACK_LEVEL)
    {
        alert('委托代销的时候不能选择黑名单用户');
        
        return;
    }
    
    if ($$('outType') == 4 && obj.pcreditlevelid == BLACK_LEVEL)
    {
        alert('赠送的时候不能选择黑名单用户');
        
        return;
    }
    
    $O("connector").value = obj.pconnector;
    $O("phone").value = obj.phandphone;
    $O("customerName").value = obj.pname;
    $O("customerId").value = obj.value;
    $O("customercreditlevel").value = obj.pcreditlevelid;
    
    if (obj.pcreditlevelid == BLACK_LEVEL || $$('outType') == 2)
    {
        removeAllItem($O('reserve3'));
        
        setOption($O('reserve3'), '1', '款到发货(黑名单客户)');   
    }
    else if ($$('outType') == 4)
    {
        resetReserve3_ZS(); 
    }
    else
    {
        resetReserve3();
    }
}

function resetReserve3()
{
    removeAllItem($O('reserve3'));
        
    setOption($O('reserve3'), '2', '客户信用和业务员信用额度担保');  
    setOption($O('reserve3'), '1', '款到发货(黑名单客户/零售)');  
    setOption($O('reserve3'), '3', '分公司经理担保');  
}

function resetReserve3_ZS()
{
    removeAllItem($O('reserve3'));
        
    setOption($O('reserve3'), '2', '客户信用和业务员信用额度担保');  
}

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
    
}

function load()
{
    titleChange();
    
    loadForm();
}

function check()
{
    if (!formCheck())
    {
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

    if ($O('customerId').value == '')
    {
        alert('请选择客户');
        return false;
    }

    if ($$('department') == '')
    {
        alert('请选择销售部门');
        return false;
    }

    if (!eCheck([$O('reday')]))
    {
        alert('请填入1到180之内的数字');
        $O('reday').focus();
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
        
        if (parseInt(trim(prices[i].value)) == 0)
        {
            if (!window.confirm('除非赠品单价不要填0,否则到总裁审批,你确定?'))
            {
                 prices[i].focus();
                 return false;
            }
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
         if (window.confirm("确定保存库单?" + messk))
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

    if (window.confirm("确定提交库单?" + messk))
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

function managerChange()
{
    //普通销售/委托代销
    if ($$('outType') == 0 && $$('outType') == 3)
    {
        $O('customerName').value = '';
        $O('customerId').value = '';
        $O('customerName').disabled  = false;
        $O('reday').value = '';
        $O('reday').readOnly = false;
        
        if (obj.pcreditlevelid == BLACK_LEVEL)
        {
            removeAllItem($O('reserve3'));
            
            setOption($O('reserve3'), '1', '款到发货(黑名单客户)');   
        }
        else
        {
            resetReserve3();
        }
    }
    
     //赠送
    if ($$('outType') == 4)
    {
        $O('customerName').value = '';
        $O('customerId').value = '';
        $O('customerName').disabled  = false;
        $O('reday').value = '1';
        $O('reday').readOnly = true;
        
        resetReserve3_ZS();
    }
    
    //个人领样
    if ($$('outType') == 1)
    {
        $O('customerName').value = '个人领样';
        $O('customerId').value = '99';
        $O('customerName').disabled  = true;
        $O('reday').value = '${goDays}';
        $O('reday').readOnly = true;
        
        resetReserve3();
    }
    
    //零售 是给公共客户的
    if ($$('outType') == 2)
    {
        $O('customerName').value = '公共客户';
        $O('customerId').value = '99';
        $O('customerName').disabled  = true;
        $O('reday').value = '';
        $O('reday').readOnly = false;
        
        removeAllItem($O('reserve3'));
        
        setOption($O('reserve3'), '1', '款到发货(黑名单客户/零售)');   
    }
}