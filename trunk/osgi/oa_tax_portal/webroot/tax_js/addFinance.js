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
	}
	
	for (var i = 0; i < taxList.length; i++)
	{
		if ((taxList[i].code + taxList[i].name) == option.value)
		{
			// in
			if (taxList[i].forward == 0)
			{
				outEle.value = 'NA';
				
				outEle.oncheck = '';
				
				outEle.readOnly = true;
				
				inEle.readOnly = false;
				
				inEle.value = '0.0';
				
				inEle.oncheck = 'notNone;isFloat';
			}
			
			if (taxList[i].forward == 1)
			{
				inEle.value = 'NA';
				
				inEle.oncheck = '';
				
				inEle.readOnly = true;
				
				outEle.readOnly = false;
				
				outEle.value = '0.0';
				
				outEle.oncheck = 'notNone;isFloat';
			}

			if (taxList[i].department == 1)
			{
				$v2(depEle, true);
			}
			else
			{
				$v2(depEle, false);
			}
			
			if (taxList[i].staffer == 1)
			{
				$v2(staEle, true);
			}
			else
			{
				$v2(staEle, false);
			}
			
			if (taxList[i].unit == 1)
			{
				$v2(unitEle, true);
			}
			else
			{
				$v2(unitEle, false);
			}
			
			break;
		}
	}
}

var current;

function selectStaffer(obj)
{
	current = obj;
	window.common.modal('../admin/pop.do?method=rptQueryStaffer&load=1&selectMode=1');
}

function getStaffers(oos)
{
	var obj = oos[0];
	
	var hid = getNextInput(current.nextSibling);
	
    hid.value = obj.value;
    
    current.value = obj.pname;
}

function getCustomer(obj)
{
	var hid = getNextInput(current.nextSibling);
    hid.value = obj.value;
    current.value = obj.pname;
}


function selectUnit(obj)
{
	current = obj;
	window.common.modal('../customer/customer.do?method=rptQueryAllCustomer&load=1');
}

function getNextInput(el)
{
    if (el.tagName && el.tagName.toLowerCase() == 'input')
    {
        return el;
    }
    else
    {
        return getNextInput(el.nextSibling);
    }
}

