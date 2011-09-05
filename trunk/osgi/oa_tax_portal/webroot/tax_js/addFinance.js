function $v2(obj, f)
{
     if (f)
     {
     	if (is_ie)
         obj.style.display = 'inline';
         else
         obj.style.display = '';
     }
     else
     obj.style.display = 'none';
}

function concat2(arr1, arr2)
{
	var arr = [];
	var j = 0;
	
    for (var i = 0; i < arr1.length; i++)
    {
        arr[j++] = arr1[i];
    }
    
    for (var i = 0; i < arr2.length; i++)
    {
        arr[j++] = arr2[i];
    }
    
    return arr;
}

function getTrInnerObj(obj, name)
{
	var tr = getTrObject(obj);
	
	var inputs1 = tr.getElementsByTagName('input');
	
	var inputs2 = tr.getElementsByTagName('select');
	
	var inputs = concat2(inputs1, inputs2);
	
	for (var i = 0; i < inputs.length; i++)
	{
		if (inputs[i].name == name)
		{
			return inputs[i];
		}
	}
	
	return null;
}

function getTrInnerObj2(tr, name)
{
    var inputs1 = tr.getElementsByTagName('input');
    
    var inputs2 = tr.getElementsByTagName('select');
    
    var inputs = concat2(inputs1, inputs2);
    
    for (var i = 0; i < inputs.length; i++)
    {
        if (inputs[i].name == name)
        {
            return inputs[i];
        }
    }
    
    return null;
}


function taxChange(obj)
{
	var option = obj;
	
	var tr = getTrObject(obj);
	
	var inputs1 = tr.getElementsByTagName('input');
	
	var inputs2 = tr.getElementsByTagName('select');
	
	var inputs = concat2(inputs1, inputs2);
	
	for (var i = 0; i < inputs.length; i++)
	{
		if (inputs[i].name == 'inmoney')
		{
			inEle = inputs[i];
		}
		
		if (inputs[i].name == 'outmoney')
		{
			outEle = inputs[i];
		}
		
		if (inputs[i].name == 'departmentId')
		{
			depEle = inputs[i];
		}
		
		if (inputs[i].name == 'stafferId')
		{
			staEle = inputs[i];
		}
		
		if (inputs[i].name == 'unitId')
		{
			unitEle = inputs[i];
		}
		
		if (inputs[i].name == 'productId')
        {
            productEle = inputs[i];
        }
        
        if (inputs[i].name == 'depotId')
        {
           depotEle = inputs[i];
        }
        
        if (inputs[i].name == 'duty2Id')
        {
           duty2Ele = inputs[i];
        }
	}
	
	for (var i = 0; i < taxList.length; i++)
	{
		if ((taxList[i].code + taxList[i].name) == option.value)
		{
			// in
            outEle.oncheck = 'notNone;isFloat';
            
            outEle.readOnly = false;
            
            inEle.readOnly = false;
            
            inEle.oncheck = 'notNone;isFloat';
			
			if (taxList[i].department == 1)
			{
				$v2(depEle, true);
				
				depEle.oncheck = 'notNone';
			}
			else
			{
				$v2(depEle, false);
				
				depEle.oncheck = '';
			}
			
			if (taxList[i].staffer == 1)
			{
				$v2(staEle, true);
				
				staEle.oncheck = 'notNone';
			}
			else
			{
				$v2(staEle, false);
				
				staEle.oncheck = '';
			}
			
			if (taxList[i].unit == 1)
			{
				$v2(unitEle, true);
				
				unitEle.oncheck = 'notNone';
			}
			else
			{
				$v2(unitEle, false);
				
				unitEle.oncheck = '';
			}
			
			if (taxList[i].product == 1)
            {
                $v2(productEle, true);
                
                productEle.oncheck = 'notNone';
            }
            else
            {
                $v2(productEle, false);
                
                productEle.oncheck = '';
            }
            
            if (taxList[i].depot == 1)
            {
                $v2(depotEle, true);
                
                depotEle.oncheck = 'notNone';
            }
            else
            {
                $v2(depotEle, false);
                
                depotEle.oncheck = '';
            }
            
            if (taxList[i].duty == 1)
            {
                $v2(duty2Ele, true);
                
                duty2Ele.oncheck = 'notNone';
            }
            else
            {
                $v2(duty2Ele, false);
                
                duty2Ele.oncheck = '';
            }
			
			var hid = getTrInnerObj(obj, 'taxId2');
			
			hid.value = taxList[i].id;
			
			break;
		}
	}
}

function checkSelect(obj)
{
    var tr = getTrObject(obj);
    
    //获取taxId
    var option = getTrInnerObj(obj, 'taxId');
    
    var inputs1 = tr.getElementsByTagName('input');
    
    var inputs2 = tr.getElementsByTagName('select');
    
    var inputs = concat2(inputs1, inputs2);
    
    for (var i = 0; i < inputs.length; i++)
    {
        
        if (inputs[i].name == 'stafferId')
        {
            staEle = inputs[i];
        }
        
        if (inputs[i].name == 'unitId')
        {
            unitEle = inputs[i];
        }
        
        if (inputs[i].name == 'productId')
        {
            productEle = inputs[i];
        }
    }
    
    for (var i = 0; i < taxList.length; i++)
    {
        if ((taxList[i].code + taxList[i].name) == option.value)
        {
            if (taxList[i].staffer == 1 && staEle.value == '选择职员')
            {
            	alert('选择职员');
            	
            	$f(staEle);
            	
            	return false;
            }
            
            if (taxList[i].unit == 1 && unitEle.value == '选择单位')
            {
                alert('选择单位');
                
                $f(unitEle);
                
                return false;
            }
           
            
            if (taxList[i].product == 1 && productEle.value == '选择产品')
            {
                alert('选择产品');
                
                $f(productEle);
                
                return false;
            }
        }
    }
    
    return true;
}

var current;

function selectStaffer(obj)
{
	current = obj;
	window.common.modal('../admin/pop.do?method=rptQueryStaffer&load=1&selectMode=1');
}


function selectProduct(obj)
{
	current = obj;
    window.common.modal('../product/product.do?method=rptQueryProduct&load=1&selectMode=1&abstractType=0&status=0');
}

//选择职位
function selectPrin(obj)
{
	current = obj;
    window.common.modal('../admin/org.do?method=popOrg');
}

function getProduct(oos)
{
    var obj = oos[0];
    
    current.value = obj.pname;
    
    var hid = getNextInput(current.nextSibling);
    
    hid.value = obj.value;
    
    current.style.color = '';
}

function getStaffers(oos)
{
	var obj = oos[0];
	
	var hid = getNextInput(current.nextSibling);
	
    hid.value = obj.value;
    
    current.value = obj.pname;
    
    current.style.color = '';
    
    var org = getNextInput(hid.nextSibling);
    
    if (org)
    {
    	if (org.name == 'departmentId')
    	{
    		org.value = obj.pdname;
    		
    		var hid = getNextInput(org.nextSibling);
    
		    hid.value = obj.pdid;
		    
		    org.style.color = '';
    	}
    }
}

function getUnit(obj)
{
	var hid = getNextInput(current.nextSibling);
    hid.value = obj.value;
    current.value = obj.pname;
    current.style.color = '';
}

function setOrgFromPop(id, name, level, pname)
{
	if (pname)
    current.value = pname + '->' + '[' + level + ']' + name;
    else
    current.value = '[' + level + ']' + name;
    
    var hid = getNextInput(current.nextSibling);
    
    hid.value = id;
    
    current.style.color = '';
}


function selectUnit(obj)
{
	current = obj;
	window.common.modal('../finance/finance.do?method=rptQueryUnit&load=1');
}

function getNextInput(el)
{
	if (el == null)
	{
	   return;	
	}   
	
    if (el.tagName && el.tagName.toLowerCase() == 'input')
    {
        return el;
    }
    else
    {
        return getNextInput(el.nextSibling);
    }
}

