/**
 * depends on: 
 */
function getObj()
{
  var elements = new Array();

  for (var i = 0; i < arguments.length; i++) {
    var element = arguments[i];
    var arg = arguments[i];
    if (typeof element == 'string')
    {
      element = document.getElementById(element);
    }

    var element1;
    if (element == null)
    {
        element1 = document.getElementsByName(arg);
        if (element1.length == 1)
        {
            element = element1[0];
        }
    }

    if (arguments.length == 1)
      return element;

    elements.push(element);
  }

  return elements;
}

function $O()
{
  var elements = new Array();

  for (var i = 0; i < arguments.length; i++) {
    var element = arguments[i];
    var arg = arguments[i];
    if (typeof element == 'string')
    {
      element = document.getElementById(element);
    }

    if (element == null)
    {
        var element1 = document.getElementsByName(arg);
        if (element1.length == 1)
        {
            element = element1[0];
        }
    }

    if (arguments.length == 1)
      return element;

    elements.push(element);
  }

  return elements;
}

function trim(num)
{
    while(true)
    {
        var cI = num.charAt(0) ;
        if(cI != " ")
        {
            break ;
        }
        else
        {
            num = num.substring(1 ,num.length) ;
        }
    }
    while(true)
    {
        var cJ = num.charAt(num.length - 1) ;
        if(cJ != " ")
        {
            break ;
        }
        else
        {
            num = num.substring(0 ,num.length - 1) ;
        }
    }
    return num ;
}


function getRadioValue(name)
{
    var obj = document.getElementsByName(name);
    for (var i = 0; i < obj.length; i++)
    {
        if (obj[i].checked)
        {
            return obj[i].value;
        }
    }

    return '';
}

function getRadio(name)
{
    var obj = document.getElementsByName(name);
    for (var i = 0; i < obj.length; i++)
    {
        if (obj[i].checked)
        {
            return obj[i];
        }
    }

    return null;
}

function resetRadio(name)
{
    var obj = document.getElementsByName(name);
    for (var i = 0; i < obj.length; i++)
    {
        obj[i].checked = false;
    }

    return null;
}


/**
 * 设置select的选择
 */
function setSelect(selectObj, value)
{
    var va = value;

    if (!isNoneInCommon(va))
    {
        var os = selectObj.options;

        for (var j = 0; j < os.length; j++)
        {
            if (os[j].value == va)
            {
                os[j].selected = true;
                return true;
            }
        }
    }

    return false;
}

/**
 * 设置select的选择
 */
function setSelectIndex(selectObj, index)
{
    index = index + '';

    if (!isNoneInCommon(index))
    {
        index = parseInt(index);

        var os = selectObj.options;

        if (os.length > index)
        {
            selectObj.selectedIndex = index;
        }
    }
}

/**
 * 在firfox里面增加属性
 */
function $$E(attArray)
{
    if(window.HTMLElement)
    for (var i = 0; i < attArray.length; i++)
    {
        var att = attArray[i];
        eval("HTMLElement.prototype.__defineGetter__('" + att + "',function(){return this.getAttribute('" + att + "');});");
        eval("HTMLElement.prototype.__defineSetter__('" + att + "',function(sText){this.setAttribute('" + att + "', sText);return sText;});");
    }
}

function concat(arr1, arr2)
{
    for (var i = 0; i < arr2.length; i++)
    {
        arr1.push(arr2[i]);
    }
}

function setAllReadOnly()
{
	var elements = [];
    
    tem = document.getElementsByTagName("input");
    
    concat(elements, tem);
    
    tem = document.getElementsByTagName("select");
    
    concat(elements, tem);
    
    tem = document.getElementsByTagName("textarea");
    
    concat(elements, tem);
    
    for (var i = 0; i < elements.length; i++)
    {
    	var ele = elements[i];
    	
    	ele.readOnly = true;
    	
    	ele.readonly = 'true';
    }
    
    loadForm();
}

/**
 * 加载form的初始化
 */
function loadForm()
{
    var elements = [];
    
    var tem = document.getElementsByTagName("button");
    
    concat(elements, tem);
    
    tem = document.getElementsByTagName("input");
    
    concat(elements, tem);
    
    tem = document.getElementsByTagName("select");
    
    concat(elements, tem);
    
    tem = document.getElementsByTagName("textarea");
    
    concat(elements, tem);

    var ele = null;
    var rIndex = 0;
    var bb = true;
    for (var i = 0; i < elements.length; i++)
    {
        ele = elements[i];
        
        if(window.HTMLElement)
        {
            var atts = ele.attributes;
            
            for (var k = 0; k < atts.length; k++)
            {
                //Firefox only support lowerCase letter
                var aname = atts[k].name;
                
                if (ele[aname] == undefined)
                //if (aname.indexOf('ext') == 0)
                {
                    $$E([aname])
                }
            }
        }
        
        if (ele.type.toLowerCase() == 'select-one')
        {
            var va = ele.getAttribute('values');

            setSelect(ele, va);

            var index = ele.getAttribute('index');

            setSelectIndex(ele, index);

            var quick = ele.getAttribute('quick');

            if (quick == "true")
            {
                ele.style.imeMode="disabled";
                
                quickSelect(ele);
            }

            setSelectIndex(ele, index);

            //process readonly
            va = ele.getAttribute('readonly') || ele.getAttribute('readOnly') || ele.readonly;
            
            if (va == 'true' || va == true)
            {
                var ii = 0;
                for (ii = ele.options.length - 1; ii >= 0; ii--)
                {
                    if (!ele.options[ii].selected)
                    ele.remove(ii);
                }
            }
        }

        if (ele.type.toLowerCase() == 'text')
        {
            var va = ele.getAttribute('ime');

            if (va == "false")
            {
                ele.style.imeMode="disabled";
            }
        }

        if (ele.type.toLowerCase() == 'radio' && bb)
        {
            var va = ele.getAttribute('values');

            var index = ele.getAttribute('index');

            if(index == '')
            {
                ele.checked = true;
                bb = false;
            }

            index = index + '';

            if (!isNoneInCommon(va))
            {
                if (ele.value == va)
                {
                    ele.checked = true;
                }
            }

            if (!isNoneInCommon(index))
            {
                var index = parseInt(index);

                if (index == rIndex)
                {
                    ele.checked = true;
                }
            }

            rIndex++;
        }
    }
}

/**
 * set the index of radio or select
 */
function $Set(name, index)
{
    var obj = document.getElementsByName(name);

    for (var i = 0; i < obj.length; i++)
    {
        if (index == i)
        {
            obj[i].checked = true;
        }
    }
}


/**
 * $F
 */
function $$(name)
{
    var obj = document.getElementsByName(name);

    if (obj == null || obj.length == 0)
    {
        var aar = [];
        
        aar[0] = document.getElementById(name);
        
        obj = aar;
        
        if (aar[0] == null)
        {
            return null;
        }
    }

    if (obj[0].type == 'select-one' || obj[0].type == 'select')
    {
        return getOption(obj[0]).value;
    }

    if (obj[0].type == 'radio' || obj[0].type == 'checkbox')
    {
        for (var i = 0; i < obj.length; i++)
        {
            if (obj[i].checked)
            {
                return obj[i].value;
            }
        }

        return null;
    }

    return obj[0].value;
}

function $f(obj)
{
     if (typeof obj == 'string')
     {
        obj = $O(obj);
     }

     obj.focus();
     //obj.select();
}

/**
 * 从Element里面获得Attribute(IE AND Firefox)
 */
function $a(oElem, att)
{
    if (oElem == null)
    {
        return '';
    }

    if (typeof oElem == 'string')
    {
        oElem = $O(oElem);
    }

    if (att == 'value')
    {
        return oElem.value;
    }

    return oElem.getAttribute(att);
}

function getCheckBox(name)
{
    var arr = new Array();
    var k =0;
    var obj = document.getElementsByName(name);
    for (var i = 0; i < obj.length; i++)
    {
        if (obj[i].checked)
        {
            arr[k++] = obj[i];
        }
    }

    return arr;
}

function isNoneInCommon(obj)
{
    if (isNullInCommon(obj))
    {
        return true;
    }

    if (typeof obj == 'string')
    {
        if (obj == '')
        {
            return true;
        }
    }

    return false;
}

function isNullInCommon(obj)
{
    if (obj == undefined || obj == null)
    {
        return true;
    }

    return false;
}

function getOption(obj)
{
	if (obj.options.length > 0)
    return obj.options[obj.selectedIndex];
    else
    return "";
}

function getOptionText(obj)
{
    if (typeof obj == 'string')
    {
        obj = $O(obj);
    }

    return obj.options[obj.selectedIndex].text;
}


function setOption(obj, value, text)
{
    var oOption = document.createElement("OPTION");
    oOption.text = text;
    oOption.value= value;
    obj.options.add(oOption);
}

function $Index(name)
{
    var list = document.getElementsByName(name);

    for (var i = 0; i < list.length; i++)
    {
        if (list[i].checked == true)
        {
            return i;
        }
    }
    
    return -1;
}

function $N(name)
{
    return document.getElementsByName(name);
}

function $d(name, f)
{
    var obj = getObj(name);

    if (obj != null)
    {
        if (f != null)
        {
            obj.disabled = f;
        }
        else
        {
            obj.disabled = true;
        }
    }
}

function $v(name, f)
{
    var obj = getObj(name);

    if (obj != null)
    {
        if (f)
        obj.style.display = 'inline';
        else
        obj.style.display = 'none';
    }
}

function $hide(name, f)
{
    var obj = getObj(name);

    if (obj != null)
    {
        if (f != null)
        {
            obj.disabled = f;
        }
        else
        {
            obj.disabled = true;
        }
    }

    if (obj != null)
    {
        if (!f)
        obj.style.display = 'inline';
        else
        obj.style.display = 'none';
    }
}



/**
 * 验证字符串是否仅数字
 */
function isNumbers(str)
{
    var reg = /^[0-9]+$/;
    var reg1 = /^-{1}[0-9]+$/;
    return reg.test(str) || reg1.test(str);
}

/**
 * 验证字符串是否仅数字
 */
function isLetter(str)
{
   var reg = /^[A-Za-z]*$/;
    return reg.test(str);
}

function isFloat(num)
{
    var reg = /^[0-9]*(.)?[0-9]+$/;

    return reg.test(num);
}

function isFloatValue(oo)
{
    var reg = /^[0-9]*(.)?[0-9]+$/;

    if (reg.test(oo.value))
    {
        return true;
    }
    else
    {
        alert('填写正确的金额');
    }
}


function getLength0(length)
{
    var s = '';
    for (var i = 0; i < length; i++)
    {
        s += '0';
    }

    return s;
}


function isNull(obj)
{
    if (obj == undefined || obj == null)
    {
        return true;
    }

    return false;
}

/**
 * 删除所有的select元素
 */
function removeAllItem(fromSelect)
{
    for (i=fromSelect.options.length-1; i>=0; i--)
    {
        fromSelect.remove(i);
    }

    fromSelect.blur;
}

function $Dbuttons(f)
{
    var bus = document.getElementsByTagName('input');
    
    for (var i = 0; i < bus.length; i++)
    {
        if (bus[i].type.toLowerCase() == 'button' ||
            bus[i].type.toLowerCase() == 'submit' ||
            bus[i].type.toLowerCase() == 'reset')
        {
            dinner(bus[i], f);
        }
    }
}

function dinner(obj, disable){

  if (window.event)
  {
      obj.disabled = disable;
      return;
  }

  if(disable){
    obj.setAttribute('color_bak', obj.style.color);
    obj.style.color="gray";
  }
  else{
    obj.style.color=obj.getAttribute("color_bak");
  }

  obj.disabled = disable;
}

/**
 * 比较时间
 */
function compareDays(date1, date2)
{
    var s1 = date1.split('-');
    var s2 = date2.split('-');

    var year1 = parseInt(s1[0], 10);

    var year2 = parseInt(s2[0], 10);

    var month1 = parseInt(s1[1], 10);

    var month2 = parseInt(s2[1], 10);

    var day1 = parseInt(s1[2], 10);

    var day2 = parseInt(s2[2], 10);

    return Math.abs((year2 - year1) * 365 + (month2 - month1) * 30 + (day2 - day1));
}

/**
 * 快速绑定select建立简拼索引
 */
function quickSelect(element)
{
    if (typeof element == 'string')
    {
        element = document.getElementById(element);
    }

    //防止重复绑定
    if (!element.hasspell)
    {
        window.common.addEvent(element, 'keydown', spellList);
        
        element.hasspell = true;
    }
}

function $l(href)
{
    $Dbuttons(true);
    
    document.location.href = href;
}

function addEventCommon(el, evname, func)
{
    if (el.attachEvent)
    {
        // IE
        el.attachEvent("on" + evname, func);
    }
    else if (el.addEventListener)
    {
        // Gecko / W3C
        el.addEventListener(evname, func, true);
    }
    else
    {
        el["on" + evname] = func;
    }
}

if (window.addEventCommon)
window.addEventCommon(window, 'load', loadForm);

