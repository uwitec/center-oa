/**
 * depends on: 
 * prototype.js
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

function $()
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
 * ÉèÖÃselectµÄÑ¡Ôñ
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
 * ÉèÖÃselectµÄÑ¡Ôñ
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
 * ÔÚfirfoxÀïÃæÔö¼ÓÊôÐÔ
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

/**
 * ¼ÓÔØformµÄ³õÊ¼»¯
 */
function loadForm()
{
	//alert('dd');
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
			va = ele.getAttribute('readonly');

			if (va == 'true')
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
	 	obj = $(obj);
	 }

	 obj.focus();
	 //obj.select();
}

/**
 * ´ÓElementÀïÃæ»ñµÃAttribute(IE AND Firefox)
 */
function $a(oElem, att)
{
	if (oElem == null)
	{
		return '';
	}

	if (typeof oElem == 'string')
	{
		oElem = $(oElem);
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
	return obj.options[obj.selectedIndex];
}

function getOptionText(obj)
{
	if (typeof obj == 'string')
	{
		obj = $(obj);
	}

	return obj.options[obj.selectedIndex].text;
}


function setOption(obj, value, text)
{
	var oOption = document.createElement("OPTION");
	oOption.text = text;
	oOption.value= value;
	obj.add(oOption);
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

function modal(url)
{
	var sFeatures="dialogHeight: " + 800 + "px;dialogWidth:" + 900 + "px;";
    window.showModalDialog(url,
        [window],
        sFeatures
        );
}

function mul(n1, n2)
{
    var m=0, s1=n1.toString(), s2=n2.toString();
    try
    {
        m += s1.split(".")[1].length;
    }
    catch(e)
    {}

    try
    {
        m += s2.split(".")[1].length
    }
    catch(e)
    {}

    return Number(s1.replace(".","")) * Number(s2.replace(".","")) / Math.pow(10, m);
}

function add(n1, n2)
{
    var m=0, n=0,s1=n1.toString(), s2=n2.toString();
    try
    {
        m = s1.split(".")[1].length
    }
    catch(e)
    {}

    try
    {
        n = s2.split(".")[1].length
    }
    catch(e)
    {}

    var oo = Math.max(m, n);

    //»ñµÃstringÀàÐÍµÄÕûÊýÖµ
    s1 = s1.replace(".","") + getLength0(oo - m);
    s2 = s2.replace(".","") + getLength0(oo - n);

    return (Number(s1) + Number(s2)) / Math.pow(10, oo);
}

/**
 * ÑéÖ¤×Ö·û´®ÊÇ·ñ½öÊý×Ö
 */
function isNumbers(str)
{
    var reg = /^[0-9]+$/;
    var reg1 = /^-{1}[0-9]+$/;
    return reg.test(str) || reg1.test(str);
}

/**
 * ÑéÖ¤×Ö·û´®ÊÇ·ñ½öÊý×Ö
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
		alert('ÌîÐ´ÕýÈ·µÄ½ð¶î');
	}
}


//¸ñÊ½»¯Êý×Ö ËÄÉáÎåÈë
function formatNum(num, length)
{
	 var reg = /[0-9]*(.)?[0-9]*$/;

	 if (!reg.test(num))
	 {
	 	reg = /[0-9]*.$/;
	 	if (!reg.test(num))
	 	{
	 		return num;
	 	}
	 }

	 num += '';
	 if (num.indexOf('.') == -1)
	 {
	 	return num + '.' + getLength0(length);
	 }

	 var hou = num.substring(num.indexOf('.') + 1);

	 if (hou.length <= length)
	 {
	 	return num + getLength0(length - hou.length);
	 }

	 //³¬¹ý Ö¸¶¨µÄËÄÉáÎåÈë
	 var ins = parseFloat(num.substring(0, num.indexOf('.') + 1) + hou.substring(0, length));

	 var last = parseInt(hou.charAt(length));
	 var add;

	 if (last >= 5)
	 {
	 	add = '0.' + getLength0(length - 1) + '1';
	 }
	 else
	 {
	 	add = '0.' + getLength0(length);
	 }

	 var result =  ins + parseFloat(add);

	 return result + '';
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

var tempDiv = document.createElement('DIV');

//afterBegin beforeEnd afterEnd befor after
function appendTable(tab, htm, pos, consult, order)
{
	if (htm.toUpperCase().indexOf('<TABLE>') == -1)
	{
		htm = '<table>' + htm + '</table>';
	}

	tempDiv.innerHTML = htm;

	//»ñµÃtrs
	var trs = tempDiv.getElementsByTagName('TR');

	var insertPos;
	if (isNoneInCommon(arguments[2]))
	{
		insertPos = 'afterEnd';
	}
	else
	{
		insertPos = pos;
	}

	var ord;
	if (isNull(order))
	{
		ord = false;
	}
	else
	{
		ord = true;
	}

	//tr.insertAdjacentElement("afterEnd",rowArray[i])

	//²åÈëµ½±í¸ñµÄ×îÇ°Ãæ
	var element;
	var top = tab.getElementsByTagName('TR')[0];
	for (var i = 0; i < trs.length;)
	{
		element = trs[i];

		if (insertPos == 'beforeBegin')
		{
			//²åÈëµÄÊ±ºòµ½ÅÅ
			if (ord)
			{
				if (isNoneInCommon(consult))
				{
					consult = tab.getElementsByTagName('TR')[0].insertAdjacentElement(insertPos, element);
				}
				else
				{
					consult = consult.insertAdjacentElement(insertPos, element);
				}
			}
			else
			{
				top.insertAdjacentElement(insertPos, element);
			}
		}

		if (insertPos == 'afterEnd')
		{
			if (isNoneInCommon(consult))
			{
				tab.getElementsByTagName('TR')[0].insertAdjacentElement(insertPos, element);
			}
			else
			{
				consult = consult.insertAdjacentElement(insertPos, element);
			}
		}

		if (insertPos == 'befor')
		{
			if (ord)
			{
				consult = consult.insertAdjacentElement('beforeBegin', element);
			}
			else
			{
				consult.insertAdjacentElement('beforeBegin', element);
			}
		}

		if (insertPos == 'after')
		{
			if (!ord)
			{
				consult = consult.insertAdjacentElement('afterEnd', element);
			}
			else
			{
				consult.insertAdjacentElement('afterEnd', element);
			}
		}
	}

	tempDiv.removeNode(false);
}

//Ñ¡Ôñ×Ô¶¯ÁªÏëradio
function hrefAndSelect(obj)
{
	//$Set(radio, obj.indexs);
	var tr = getTrObject(obj);

	if (tr != null)
	{
		var rad = tr.getElementsByTagName('input');

		for (i = 0; i < rad.length; i++)
		{
			if (rad[i].type.toLowerCase() == 'radio')
			{
				rad[i].checked = true;
			}
		}
	}
}

function getTrObject(obj)
{
	var i = 0;
	var par;
	par = obj;
	while (par.tagName.toLowerCase() != "tr")
	{
		par = par.parentNode;
		if (i++ > 15)
		{
			return null;
		}
	}

	return par;
}

//±í¸ñÅÅÐò

var cco = 0;
var telNumbers = 11;
var sortIndex;
var isAsc = 0;
/**
 * ±í¸ñÅÅÐò
 * cell TD
 * num true:num other:ASC
 */
function tableSort(cell, num)
{
    sortIndex = cell.cellIndex;
    var rowArray=[];

    var tr = cell.parentNode;
    var tbody = tr.parentNode;
    var tab = tbody.parentNode;
    var rows = tbody.getElementsByTagName("TR")
    for(i=1;i<rows.length;i++)
    {
        var row = rows[i];

        if (row.id != 'TR_LAST' && row.style.display != "none")
        rowArray[i] = row;
    }
    
    addInsertAdjacentElement(tr);

    if(isAsc==1)
    {
        if (num)
        {
            rowArray.sort(sortAscNum);
        }
        else
        {
            rowArray.sort(sortAscChinese);
        }
    }
    else
    {
        if (num)
        {
            rowArray.sort(sortDescNum);
        }
        else
        {
            rowArray.sort(sortDescChinese);
        }
    }

    try
    {
        for(i=1;i<rowArray.length;i++)
        {
            tr.insertAdjacentElement("afterEnd", rowArray[i]);
        }

    }
    catch(e)
    {
    }

    rows = tbody.getElementsByTagName("TR");
	for (i = 1; i < rows.length; i++)
	{
		if (i % 2 == 0)
		{
			rows[i].className  = 'content2';
		}
		else
		{
			rows[i].className  = 'content1';
		}
	}

    isAsc = (isAsc+1)%2;
}

function addInsertAdjacentElement(obj)
{
	if(window.HTMLElement)
	{
		if (!obj.insertAdjacentElement)
		HTMLElement.prototype.insertAdjacentElement=function(where,parsedNode){ 
	        switch(where){ 
	            case "beforeBegin": 
	                this.parentNode.insertBefore(parsedNode,this); 
	                break; 
	            case "afterBegin": 
	                this.insertBefore(parsedNode,this.firstChild); 
	                break; 
	            case "beforeEnd": 
	                this.appendChild(parsedNode); 
	                break; 
	            case "afterEnd": 
	                if(this.nextSibling) 
	                    this.parentNode.insertBefore(parsedNode,this.nextSibling); 
	                else 
	                    this.parentNode.appendChild(parsedNode); 
	                break; 
	            } 
	        }
	        
	     
	     if (!obj.innerText)
	     {
		     HTMLElement.prototype.__defineSetter__("innerText",function(sText){ 
	        var parsedText=document.createTextNode(sText); 
	        this.innerHTML=parsedText; 
	        return parsedText; 
	        }); 
		    HTMLElement.prototype.__defineGetter__("innerText",function(){ 
		        var r=this.ownerDocument.createRange(); 
		        r.selectNodeContents(this); 
		        return r.toString(); 
		        }); 
	     }
	}     
}

function sortAsc(x, y)
{

    if (x.getElementsByTagName("TD")[sortIndex].innerText > y.getElementsByTagName("TD")[sortIndex].innerText)
    return -1;
    else if (x.getElementsByTagName("TD")[sortIndex].innerText == y.getElementsByTagName("TD")[sortIndex].innerText)
    return 0;
    else
    return 1;
}

function sortDesc(x, y)
{

    if (x.getElementsByTagName("TD")[sortIndex].innerText < y.getElementsByTagName("TD")[sortIndex].innerText)
    return -1;
    else if (x.getElementsByTagName("TD")[sortIndex].innerText == y.getElementsByTagName("TD")[sortIndex].innerText)
    return 0;
    else
    return 1;
}

function sortAscNum(x, y)
{
    try
    {
        if (parseInt(x.getElementsByTagName("TD")[sortIndex].innerText) > parseInt(y.getElementsByTagName("TD")[sortIndex].innerText))
        return -1;
        else if (parseInt(x.getElementsByTagName("TD")[sortIndex].innerText) == parseInt(y.getElementsByTagName("TD")[sortIndex].innerText))
        return 0;
        else
        return 1;
    }
    catch(e)
    {
        return 1;
    }
}

function sortDescNum(x, y)
{
    try
    {
        if (parseInt(x.getElementsByTagName("TD")[sortIndex].innerText) < parseInt(y.getElementsByTagName("TD")[sortIndex].innerText))
        return -1;
        else if (parseInt(x.getElementsByTagName("TD")[sortIndex].innerText) == parseInt(y.getElementsByTagName("TD")[sortIndex].innerText))
        return 0;
        else
        return 1;
    }
    catch(e)
    {
        return 1;
    }
}

function sortAscChinese(x, y)
{
    try
    {
    	return sortChinese(x.getElementsByTagName("TD")[sortIndex].innerText, y.getElementsByTagName("TD")[sortIndex].innerText);;
    }
    catch(e)
    {
        return 1;
    }
}

function sortDescChinese(x, y)
{
    try
    {
    	return sortChinese(y.getElementsByTagName("TD")[sortIndex].innerText, x.getElementsByTagName("TD")[sortIndex].innerText);;
    }
    catch(e)
    {
        return 1;
    }
}

function sortChinese(s, s1)
{
	//s = s.escapeText();
	//s1 = s1.escapeText();
    var  strGB = ' ! \ " #$%&`()*+,-    ./:;<=>?@[\]^_\'{|}~0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz °¡°¢°£°¤°¥°¦°§°¨°©°ª°«°¬°­°®°¯°°°±°²°³°´°µ°¶°·°¸°¹°º°»°¼°½°¾°¿°À°Á°Â°Ã°Ä°Å°Æ°Ç°È°É°Ê°Ë°Ì°Í°Î°Ï°Ð°Ñ°Ò°Ó°Ô°Õ°Ö°×°Ø°Ù°Ú°Û°Ü°Ý°Þ°ß°à°á°â°ã°ä°å°æ°ç°è°é°ê°ë°ì°í°î°ï°ð°ñ°ò°ó°ô°õ°ö°÷°ø°ù°ú°û°ü°ý°þ±¡±¢±£±¤±¥±¦±§±¨±©±ª±«±¬±­±®±¯±°±±±²±³±´±µ±¶±·±¸±¹±º±»±¼±½±¾±¿±À±Á±Â±Ã±Ä±Å±Æ±Ç±È±É±Ê±Ë±Ì±Í±Î±Ï±Ð±Ñ±Ò±Ó±Ô±Õ±Ö±×±Ø±Ù±Ú±Û±Ü±Ý±Þ±ß±à±á±â±ã±ä±å±æ±ç±è±é±ê±ë±ì±í±î±ï±ð±ñ±ò±ó±ô±õ±ö±÷±ø±ù±ú±û±ü±ý±þ²¡²¢²£²¤²¥²¦²§²¨²©²ª²«²¬²­²®²¯²°²±²²²³²´²µ²¶²·²¸²¹²º²»²¼²½²¾²¿²À²Á²Â²Ã²Ä²Å²Æ²Ç²È²É²Ê²Ë²Ì²Í²Î²Ï²Ð²Ñ²Ò²Ó²Ô²Õ²Ö²×²Ø²Ù²Ú²Û²Ü²Ý²Þ²ß²à²á²â²ã²ä²å²æ²ç²è²é²ê²ë²ì²í²î²ï²ð²ñ²ò²ó²ô²õ²ö²÷²ø²ù²ú²û²ü²ý²þ³¡³¢³£³¤³¥³¦³§³¨³©³ª³«³¬³­³®³¯³°³±³²³³³´³µ³¶³·³¸³¹³º³»³¼³½³¾³¿³À³Á³Â³Ã³Ä³Å³Æ³Ç³È³É³Ê³Ë³Ì³Í³Î³Ï³Ð³Ñ³Ò³Ó³Ô³Õ³Ö³×³Ø³Ù³Ú³Û³Ü³Ý³Þ³ß³à³á³â³ã³ä³å³æ³ç³è³é³ê³ë³ì³í³î³ï³ð³ñ³ò³ó³ô³õ³ö³÷³ø³ù³ú³û³ü³ý³þ´¡´¢´£´¤´¥´¦´§´¨´©´ª´«´¬´­´®´¯´°´±´²´³´´´µ´¶´·´¸´¹´º´»´¼´½´¾´¿´À´Á´Â´Ã´Ä´Å´Æ´Ç´È´É´Ê´Ë´Ì´Í´Î´Ï´Ð´Ñ´Ò´Ó´Ô´Õ´Ö´×´Ø´Ù´Ú´Û´Ü´Ý´Þ´ß´à´á´â´ã´ä´å´æ´ç´è´é´ê´ë´ì´í´î´ï´ð´ñ´ò´ó´ô´õ´ö´÷´ø´ù´ú´û´ü´ý´þµ¡µ¢µ£µ¤µ¥µ¦µ§µ¨µ©µªµ«µ¬µ­µ®µ¯µ°µ±µ²µ³µ´µµµ¶µ·µ¸µ¹µºµ»µ¼µ½µ¾µ¿µÀµÁµÂµÃµÄµÅµÆµÇµÈµÉµÊµËµÌµÍµÎµÏµÐµÑµÒµÓµÔµÕµÖµ×µØµÙµÚµÛµÜµÝµÞµßµàµáµâµãµäµåµæµçµèµéµêµëµìµíµîµïµðµñµòµóµôµõµöµ÷µøµùµúµûµüµýµþ¶¡¶¢¶£¶¤¶¥¶¦¶§¶¨¶©¶ª¶«¶¬¶­¶®¶¯¶°¶±¶²¶³¶´¶µ¶¶¶·¶¸¶¹¶º¶»¶¼¶½¶¾¶¿¶À¶Á¶Â¶Ã¶Ä¶Å¶Æ¶Ç¶È¶É¶Ê¶Ë¶Ì¶Í¶Î¶Ï¶Ð¶Ñ¶Ò¶Ó¶Ô¶Õ¶Ö¶×¶Ø¶Ù¶Ú¶Û¶Ü¶Ý¶Þ¶ß¶à¶á¶â¶ã¶ä¶å¶æ¶ç¶è¶é¶ê¶ë¶ì¶í¶î¶ï¶ð¶ñ¶ò¶ó¶ô¶õ¶ö¶÷¶ø¶ù¶ú¶û¶ü¶ý¶þ·¡·¢·£·¤·¥·¦·§·¨·©·ª·«·¬·­·®·¯·°·±·²·³·´·µ·¶···¸·¹·º·»·¼·½·¾·¿·À·Á·Â·Ã·Ä·Å·Æ·Ç·È·É·Ê·Ë·Ì·Í·Î·Ï·Ð·Ñ·Ò·Ó·Ô·Õ·Ö·×·Ø·Ù·Ú·Û·Ü·Ý·Þ·ß·à·á·â·ã·ä·å·æ·ç·è·é·ê·ë·ì·í·î·ï·ð·ñ·ò·ó·ô·õ·ö·÷·ø·ù·ú·û·ü·ý·þ¸¡¸¢¸£¸¤¸¥¸¦¸§¸¨¸©¸ª¸«¸¬¸­¸®¸¯¸°¸±¸²¸³¸´¸µ¸¶¸·¸¸¸¹¸º¸»¸¼¸½¸¾¸¿¸À¸Á¸Â¸Ã¸Ä¸Å¸Æ¸Ç¸È¸É¸Ê¸Ë¸Ì¸Í¸Î¸Ï¸Ð¸Ñ¸Ò¸Ó¸Ô¸Õ¸Ö¸×¸Ø¸Ù¸Ú¸Û¸Ü¸Ý¸Þ¸ß¸à¸á¸â¸ã¸ä¸å¸æ¸ç¸è¸é¸ê¸ë¸ì¸í¸î¸ï¸ð¸ñ¸ò¸ó¸ô¸õ¸ö¸÷¸ø¸ù¸ú¸û¸ü¸ý¸þ¹¡¹¢¹£¹¤¹¥¹¦¹§¹¨¹©¹ª¹«¹¬¹­¹®¹¯¹°¹±¹²¹³¹´¹µ¹¶¹·¹¸¹¹¹º¹»¹¼¹½¹¾¹¿¹À¹Á¹Â¹Ã¹Ä¹Å¹Æ¹Ç¹È¹É¹Ê¹Ë¹Ì¹Í¹Î¹Ï¹Ð¹Ñ¹Ò¹Ó¹Ô¹Õ¹Ö¹×¹Ø¹Ù¹Ú¹Û¹Ü¹Ý¹Þ¹ß¹à¹á¹â¹ã¹ä¹å¹æ¹ç¹è¹é¹ê¹ë¹ì¹í¹î¹ï¹ð¹ñ¹ò¹ó¹ô¹õ¹ö¹÷¹ø¹ù¹ú¹û¹ü¹ý¹þº¡º¢º£º¤º¥º¦º§º¨º©ºªº«º¬º­º®º¯º°º±º²º³º´ºµº¶º·º¸º¹ººº»º¼º½º¾º¿ºÀºÁºÂºÃºÄºÅºÆºÇºÈºÉºÊºËºÌºÍºÎºÏºÐºÑºÒºÓºÔºÕºÖº×ºØºÙºÚºÛºÜºÝºÞºßºàºáºâºãºäºåºæºçºèºéºêºëºìºíºîºïºðºñºòºóºôºõºöº÷ºøºùºúºûºüºýºþ»¡»¢»£»¤»¥»¦»§»¨»©»ª»«»¬»­»®»¯»°»±»²»³»´»µ»¶»·»¸»¹»º»»»¼»½»¾»¿»À»Á»Â»Ã»Ä»Å»Æ»Ç»È»É»Ê»Ë»Ì»Í»Î»Ï»Ð»Ñ»Ò»Ó»Ô»Õ»Ö»×»Ø»Ù»Ú»Û»Ü»Ý»Þ»ß»à»á»â»ã»ä»å»æ»ç»è»é»ê»ë»ì»í»î»ï»ð»ñ»ò»ó»ô»õ»ö»÷»ø»ù»ú»û»ü»ý»þ¼¡¼¢¼£¼¤¼¥¼¦¼§¼¨¼©¼ª¼«¼¬¼­¼®¼¯¼°¼±¼²¼³¼´¼µ¼¶¼·¼¸¼¹¼º¼»¼¼¼½¼¾¼¿¼À¼Á¼Â¼Ã¼Ä¼Å¼Æ¼Ç¼È¼É¼Ê¼Ë¼Ì¼Í¼Î¼Ï¼Ð¼Ñ¼Ò¼Ó¼Ô¼Õ¼Ö¼×¼Ø¼Ù¼Ú¼Û¼Ü¼Ý¼Þ¼ß¼à¼á¼â¼ã¼ä¼å¼æ¼ç¼è¼é¼ê¼ë¼ì¼í¼î¼ï¼ð¼ñ¼ò¼ó¼ô¼õ¼ö¼÷¼ø¼ù¼ú¼û¼ü¼ý¼þ½¡½¢½£½¤½¥½¦½§½¨½©½ª½«½¬½­½®½¯½°½±½²½³½´½µ½¶½·½¸½¹½º½»½¼½½½¾½¿½À½Á½Â½Ã½Ä½Å½Æ½Ç½È½É½Ê½Ë½Ì½Í½Î½Ï½Ð½Ñ½Ò½Ó½Ô½Õ½Ö½×½Ø½Ù½Ú½Û½Ü½Ý½Þ½ß½à½á½â½ã½ä½å½æ½ç½è½é½ê½ë½ì½í½î½ï½ð½ñ½ò½ó½ô½õ½ö½÷½ø½ù½ú½û½ü½ý½þ¾¡¾¢¾£¾¤¾¥¾¦¾§¾¨¾©¾ª¾«¾¬¾­¾®¾¯¾°¾±¾²¾³¾´¾µ¾¶¾·¾¸¾¹¾º¾»¾¼¾½¾¾¾¿¾À¾Á¾Â¾Ã¾Ä¾Å¾Æ¾Ç¾È¾É¾Ê¾Ë¾Ì¾Í¾Î¾Ï¾Ð¾Ñ¾Ò¾Ó¾Ô¾Õ¾Ö¾×¾Ø¾Ù¾Ú¾Û¾Ü¾Ý¾Þ¾ß¾à¾á¾â¾ã¾ä¾å¾æ¾ç¾è¾é¾ê¾ë¾ì¾í¾î¾ï¾ð¾ñ¾ò¾ó¾ô¾õ¾ö¾÷¾ø¾ù¾ú¾û¾ü¾ý¾þ¿¡¿¢¿£¿¤¿¥¿¦¿§¿¨¿©¿ª¿«¿¬¿­¿®¿¯¿°¿±¿²¿³¿´¿µ¿¶¿·¿¸¿¹¿º¿»¿¼¿½¿¾¿¿¿À¿Á¿Â¿Ã¿Ä¿Å¿Æ¿Ç¿È¿É¿Ê¿Ë¿Ì¿Í¿Î¿Ï¿Ð¿Ñ¿Ò¿Ó¿Ô¿Õ¿Ö¿×¿Ø¿Ù¿Ú¿Û¿Ü¿Ý¿Þ¿ß¿à¿á¿â¿ã¿ä¿å¿æ¿ç¿è¿é¿ê¿ë¿ì¿í¿î¿ï¿ð¿ñ¿ò¿ó¿ô¿õ¿ö¿÷¿ø¿ù¿ú¿û¿ü¿ý¿þÀ¡À¢À£À¤À¥À¦À§À¨À©ÀªÀ«À¬À­À®À¯À°À±À²À³À´ÀµÀ¶À·À¸À¹ÀºÀ»À¼À½À¾À¿ÀÀÀÁÀÂÀÃÀÄÀÅÀÆÀÇÀÈÀÉÀÊÀËÀÌÀÍÀÎÀÏÀÐÀÑÀÒÀÓÀÔÀÕÀÖÀ×ÀØÀÙÀÚÀÛÀÜÀÝÀÞÀßÀàÀáÀâÀãÀäÀåÀæÀçÀèÀéÀêÀëÀìÀíÀîÀïÀðÀñÀòÀóÀôÀõÀöÀ÷ÀøÀùÀúÀûÀüÀýÀþÁ¡Á¢Á£Á¤Á¥Á¦Á§Á¨Á©ÁªÁ«Á¬Á­Á®Á¯Á°Á±Á²Á³Á´ÁµÁ¶Á·Á¸Á¹ÁºÁ»Á¼Á½Á¾Á¿ÁÀÁÁÁÂÁÃÁÄÁÅÁÆÁÇÁÈÁÉÁÊÁËÁÌÁÍÁÎÁÏÁÐÁÑÁÒÁÓÁÔÁÕÁÖÁ×ÁØÁÙÁÚÁÛÁÜÁÝÁÞÁßÁàÁáÁâÁãÁäÁåÁæÁçÁèÁéÁêÁëÁìÁíÁîÁïÁðÁñÁòÁóÁôÁõÁöÁ÷ÁøÁùÁúÁûÁüÁýÁþÂ¡Â¢Â£Â¤Â¥Â¦Â§Â¨Â©ÂªÂ«Â¬Â­Â®Â¯Â°Â±Â²Â³Â´ÂµÂ¶Â·Â¸Â¹ÂºÂ»Â¼Â½Â¾Â¿ÂÀÂÁÂÂÂÃÂÄÂÅÂÆÂÇÂÈÂÉÂÊÂËÂÌÂÍÂÎÂÏÂÐÂÑÂÒÂÓÂÔÂÕÂÖÂ×ÂØÂÙÂÚÂÛÂÜÂÝÂÞÂßÂàÂáÂâÂãÂäÂåÂæÂçÂèÂéÂêÂëÂìÂíÂîÂïÂðÂñÂòÂóÂôÂõÂöÂ÷ÂøÂùÂúÂûÂüÂýÂþÃ¡Ã¢Ã£Ã¤Ã¥Ã¦Ã§Ã¨Ã©ÃªÃ«Ã¬Ã­Ã®Ã¯Ã°Ã±Ã²Ã³Ã´ÃµÃ¶Ã·Ã¸Ã¹ÃºÃ»Ã¼Ã½Ã¾Ã¿ÃÀÃÁÃÂÃÃÃÄÃÅÃÆÃÇÃÈÃÉÃÊÃËÃÌÃÍÃÎÃÏÃÐÃÑÃÒÃÓÃÔÃÕÃÖÃ×ÃØÃÙÃÚÃÛÃÜÃÝÃÞÃßÃàÃáÃâÃãÃäÃåÃæÃçÃèÃéÃêÃëÃìÃíÃîÃïÃðÃñÃòÃóÃôÃõÃöÃ÷ÃøÃùÃúÃûÃüÃýÃþÄ¡Ä¢Ä£Ä¤Ä¥Ä¦Ä§Ä¨Ä©ÄªÄ«Ä¬Ä­Ä®Ä¯Ä°Ä±Ä²Ä³Ä´ÄµÄ¶Ä·Ä¸Ä¹ÄºÄ»Ä¼Ä½Ä¾Ä¿ÄÀÄÁÄÂÄÃÄÄÄÅÄÆÄÇÄÈÄÉÄÊÄËÄÌÄÍÄÎÄÏÄÐÄÑÄÒÄÓÄÔÄÕÄÖÄ×ÄØÄÙÄÚÄÛÄÜÄÝÄÞÄßÄàÄáÄâÄãÄäÄåÄæÄçÄèÄéÄêÄëÄìÄíÄîÄïÄðÄñÄòÄóÄôÄõÄöÄ÷ÄøÄùÄúÄûÄüÄýÄþÅ¡Å¢Å£Å¤Å¥Å¦Å§Å¨Å©ÅªÅ«Å¬Å­Å®Å¯Å°Å±Å²Å³Å´ÅµÅ¶Å·Å¸Å¹ÅºÅ»Å¼Å½Å¾Å¿ÅÀÅÁÅÂÅÃÅÄÅÅÅÆÅÇÅÈÅÉÅÊÅËÅÌÅÍÅÎÅÏÅÐÅÑÅÒÅÓÅÔÅÕÅÖÅ×ÅØÅÙÅÚÅÛÅÜÅÝÅÞÅßÅàÅáÅâÅãÅäÅåÅæÅçÅèÅéÅêÅëÅìÅíÅîÅïÅðÅñÅòÅóÅôÅõÅöÅ÷ÅøÅùÅúÅûÅüÅýÅþÆ¡Æ¢Æ£Æ¤Æ¥Æ¦Æ§Æ¨Æ©ÆªÆ«Æ¬Æ­Æ®Æ¯Æ°Æ±Æ²Æ³Æ´ÆµÆ¶Æ·Æ¸Æ¹ÆºÆ»Æ¼Æ½Æ¾Æ¿ÆÀÆÁÆÂÆÃÆÄÆÅÆÆÆÇÆÈÆÉÆÊÆËÆÌÆÍÆÎÆÏÆÐÆÑÆÒÆÓÆÔÆÕÆÖÆ×ÆØÆÙÆÚÆÛÆÜÆÝÆÞÆßÆàÆáÆâÆãÆäÆåÆæÆçÆèÆéÆêÆëÆìÆíÆîÆïÆðÆñÆòÆóÆôÆõÆöÆ÷ÆøÆùÆúÆûÆüÆýÆþÇ¡Ç¢Ç£Ç¤Ç¥Ç¦Ç§Ç¨Ç©ÇªÇ«Ç¬Ç­Ç®Ç¯Ç°Ç±Ç²Ç³Ç´ÇµÇ¶Ç·Ç¸Ç¹ÇºÇ»Ç¼Ç½Ç¾Ç¿ÇÀÇÁÇÂÇÃÇÄÇÅÇÆÇÇÇÈÇÉÇÊÇËÇÌÇÍÇÎÇÏÇÐÇÑÇÒÇÓÇÔÇÕÇÖÇ×ÇØÇÙÇÚÇÛÇÜÇÝÇÞÇßÇàÇáÇâÇãÇäÇåÇæÇçÇèÇéÇêÇëÇìÇíÇîÇïÇðÇñÇòÇóÇôÇõÇöÇ÷ÇøÇùÇúÇûÇüÇýÇþÈ¡È¢È£È¤È¥È¦È§È¨È©ÈªÈ«È¬È­È®È¯È°È±È²È³È´ÈµÈ¶È·È¸È¹ÈºÈ»È¼È½È¾È¿ÈÀÈÁÈÂÈÃÈÄÈÅÈÆÈÇÈÈÈÉÈÊÈËÈÌÈÍÈÎÈÏÈÐÈÑÈÒÈÓÈÔÈÕÈÖÈ×ÈØÈÙÈÚÈÛÈÜÈÝÈÞÈßÈàÈáÈâÈãÈäÈåÈæÈçÈèÈéÈêÈëÈìÈíÈîÈïÈðÈñÈòÈóÈôÈõÈöÈ÷ÈøÈùÈúÈûÈüÈýÈþÉ¡É¢É£É¤É¥É¦É§É¨É©ÉªÉ«É¬É­É®É¯É°É±É²É³É´ÉµÉ¶É·É¸É¹ÉºÉ»É¼É½É¾É¿ÉÀÉÁÉÂÉÃÉÄÉÅÉÆÉÇÉÈÉÉÉÊÉËÉÌÉÍÉÎÉÏÉÐÉÑÉÒÉÓÉÔÉÕÉÖÉ×ÉØÉÙÉÚÉÛÉÜÉÝÉÞÉßÉàÉáÉâÉãÉäÉåÉæÉçÉèÉéÉêÉëÉìÉíÉîÉïÉðÉñÉòÉóÉôÉõÉöÉ÷ÉøÉùÉúÉûÉüÉýÉþÊ¡Ê¢Ê£Ê¤Ê¥Ê¦Ê§Ê¨Ê©ÊªÊ«Ê¬Ê­Ê®Ê¯Ê°Ê±Ê²Ê³Ê´ÊµÊ¶Ê·Ê¸Ê¹ÊºÊ»Ê¼Ê½Ê¾Ê¿ÊÀÊÁÊÂÊÃÊÄÊÅÊÆÊÇÊÈÊÉÊÊÊËÊÌÊÍÊÎÊÏÊÐÊÑÊÒÊÓÊÔÊÕÊÖÊ×ÊØÊÙÊÚÊÛÊÜÊÝÊÞÊßÊàÊáÊâÊãÊäÊåÊæÊçÊèÊéÊêÊëÊìÊíÊîÊïÊðÊñÊòÊóÊôÊõÊöÊ÷ÊøÊùÊúÊûÊüÊýÊþË¡Ë¢Ë£Ë¤Ë¥Ë¦Ë§Ë¨Ë©ËªË«Ë¬Ë­Ë®Ë¯Ë°Ë±Ë²Ë³Ë´ËµË¶Ë·Ë¸Ë¹ËºË»Ë¼Ë½Ë¾Ë¿ËÀËÁËÂËÃËÄËÅËÆËÇËÈËÉËÊËËËÌËÍËÎËÏËÐËÑËÒËÓËÔËÕËÖË×ËØËÙËÚËÛËÜËÝËÞËßËàËáËâËãËäËåËæËçËèËéËêËëËìËíËîËïËðËñËòËóËôËõËöË÷ËøËùËúËûËüËýËþÌ¡Ì¢Ì£Ì¤Ì¥Ì¦Ì§Ì¨Ì©ÌªÌ«Ì¬Ì­Ì®Ì¯Ì°Ì±Ì²Ì³Ì´ÌµÌ¶Ì·Ì¸Ì¹ÌºÌ»Ì¼Ì½Ì¾Ì¿ÌÀÌÁÌÂÌÃÌÄÌÅÌÆÌÇÌÈÌÉÌÊÌËÌÌÌÍÌÎÌÏÌÐÌÑÌÒÌÓÌÔÌÕÌÖÌ×ÌØÌÙÌÚÌÛÌÜÌÝÌÞÌßÌàÌáÌâÌãÌäÌåÌæÌçÌèÌéÌêÌëÌìÌíÌîÌïÌðÌñÌòÌóÌôÌõÌöÌ÷ÌøÌùÌúÌûÌüÌýÌþÍ¡Í¢Í£Í¤Í¥Í¦Í§Í¨Í©ÍªÍ«Í¬Í­Í®Í¯Í°Í±Í²Í³Í´ÍµÍ¶Í·Í¸Í¹ÍºÍ»Í¼Í½Í¾Í¿ÍÀÍÁÍÂÍÃÍÄÍÅÍÆÍÇÍÈÍÉÍÊÍËÍÌÍÍÍÎÍÏÍÐÍÑÍÒÍÓÍÔÍÕÍÖÍ×ÍØÍÙÍÚÍÛÍÜÍÝÍÞÍßÍàÍáÍâÍãÍäÍåÍæÍçÍèÍéÍêÍëÍìÍíÍîÍïÍðÍñÍòÍóÍôÍõÍöÍ÷ÍøÍùÍúÍûÍüÍýÍþÎ¡Î¢Î£Î¤Î¥Î¦Î§Î¨Î©ÎªÎ«Î¬Î­Î®Î¯Î°Î±Î²Î³Î´ÎµÎ¶Î·Î¸Î¹ÎºÎ»Î¼Î½Î¾Î¿ÎÀÎÁÎÂÎÃÎÄÎÅÎÆÎÇÎÈÎÉÎÊÎËÎÌÎÍÎÎÎÏÎÐÎÑÎÒÎÓÎÔÎÕÎÖÎ×ÎØÎÙÎÚÎÛÎÜÎÝÎÞÎßÎàÎáÎâÎãÎäÎåÎæÎçÎèÎéÎêÎëÎìÎíÎîÎïÎðÎñÎòÎóÎôÎõÎöÎ÷ÎøÎùÎúÎûÎüÎýÎþÏ¡Ï¢Ï£Ï¤Ï¥Ï¦Ï§Ï¨Ï©ÏªÏ«Ï¬Ï­Ï®Ï¯Ï°Ï±Ï²Ï³Ï´ÏµÏ¶Ï·Ï¸Ï¹ÏºÏ»Ï¼Ï½Ï¾Ï¿ÏÀÏÁÏÂÏÃÏÄÏÅÏÆÏÇÏÈÏÉÏÊÏËÏÌÏÍÏÎÏÏÏÐÏÑÏÒÏÓÏÔÏÕÏÖÏ×ÏØÏÙÏÚÏÛÏÜÏÝÏÞÏßÏàÏáÏâÏãÏäÏåÏæÏçÏèÏéÏêÏëÏìÏíÏîÏïÏðÏñÏòÏóÏôÏõÏöÏ÷ÏøÏùÏúÏûÏüÏýÏþÐ¡Ð¢Ð£Ð¤Ð¥Ð¦Ð§Ð¨Ð©ÐªÐ«Ð¬Ð­Ð®Ð¯Ð°Ð±Ð²Ð³Ð´ÐµÐ¶Ð·Ð¸Ð¹ÐºÐ»Ð¼Ð½Ð¾Ð¿ÐÀÐÁÐÂÐÃÐÄÐÅÐÆÐÇÐÈÐÉÐÊÐËÐÌÐÍÐÎÐÏÐÐÐÑÐÒÐÓÐÔÐÕÐÖÐ×ÐØÐÙÐÚÐÛÐÜÐÝÐÞÐßÐàÐáÐâÐãÐäÐåÐæÐçÐèÐéÐêÐëÐìÐíÐîÐïÐðÐñÐòÐóÐôÐõÐöÐ÷ÐøÐùÐúÐûÐüÐýÐþÑ¡Ñ¢Ñ£Ñ¤Ñ¥Ñ¦Ñ§Ñ¨Ñ©ÑªÑ«Ñ¬Ñ­Ñ®Ñ¯Ñ°Ñ±Ñ²Ñ³Ñ´ÑµÑ¶Ñ·Ñ¸Ñ¹ÑºÑ»Ñ¼Ñ½Ñ¾Ñ¿ÑÀÑÁÑÂÑÃÑÄÑÅÑÆÑÇÑÈÑÉÑÊÑËÑÌÑÍÑÎÑÏÑÐÑÑÑÒÑÓÑÔÑÕÑÖÑ×ÑØÑÙÑÚÑÛÑÜÑÝÑÞÑßÑàÑáÑâÑãÑäÑåÑæÑçÑèÑéÑêÑëÑìÑíÑîÑïÑðÑñÑòÑóÑôÑõÑöÑ÷ÑøÑùÑúÑûÑüÑýÑþÒ¡Ò¢Ò£Ò¤Ò¥Ò¦Ò§Ò¨Ò©ÒªÒ«Ò¬Ò­Ò®Ò¯Ò°Ò±Ò²Ò³Ò´ÒµÒ¶Ò·Ò¸Ò¹ÒºÒ»Ò¼Ò½Ò¾Ò¿ÒÀÒÁÒÂÒÃÒÄÒÅÒÆÒÇÒÈÒÉÒÊÒËÒÌÒÍÒÎÒÏÒÐÒÑÒÒÒÓÒÔÒÕÒÖÒ×ÒØÒÙÒÚÒÛÒÜÒÝÒÞÒßÒàÒáÒâÒãÒäÒåÒæÒçÒèÒéÒêÒëÒìÒíÒîÒïÒðÒñÒòÒóÒôÒõÒöÒ÷ÒøÒùÒúÒûÒüÒýÒþÓ¡Ó¢Ó£Ó¤Ó¥Ó¦Ó§Ó¨Ó©ÓªÓ«Ó¬Ó­Ó®Ó¯Ó°Ó±Ó²Ó³Ó´ÓµÓ¶Ó·Ó¸Ó¹ÓºÓ»Ó¼Ó½Ó¾Ó¿ÓÀÓÁÓÂÓÃÓÄÓÅÓÆÓÇÓÈÓÉÓÊÓËÓÌÓÍÓÎÓÏÓÐÓÑÓÒÓÓÓÔÓÕÓÖÓ×ÓØÓÙÓÚÓÛÓÜÓÝÓÞÓßÓàÓáÓâÓãÓäÓåÓæÓçÓèÓéÓêÓëÓìÓíÓîÓïÓðÓñÓòÓóÓôÓõÓöÓ÷ÓøÓùÓúÓûÓüÓýÓþÔ¡Ô¢Ô£Ô¤Ô¥Ô¦Ô§Ô¨Ô©ÔªÔ«Ô¬Ô­Ô®Ô¯Ô°Ô±Ô²Ô³Ô´ÔµÔ¶Ô·Ô¸Ô¹ÔºÔ»Ô¼Ô½Ô¾Ô¿ÔÀÔÁÔÂÔÃÔÄÔÅÔÆÔÇÔÈÔÉÔÊÔËÔÌÔÍÔÎÔÏÔÐÔÑÔÒÔÓÔÔÔÕÔÖÔ×ÔØÔÙÔÚÔÛÔÜÔÝÔÞÔßÔàÔáÔâÔãÔäÔåÔæÔçÔèÔéÔêÔëÔìÔíÔîÔïÔðÔñÔòÔóÔôÔõÔöÔ÷ÔøÔùÔúÔûÔüÔýÔþÕ¡Õ¢Õ£Õ¤Õ¥Õ¦Õ§Õ¨Õ©ÕªÕ«Õ¬Õ­Õ®Õ¯Õ°Õ±Õ²Õ³Õ´ÕµÕ¶Õ·Õ¸Õ¹ÕºÕ»Õ¼Õ½Õ¾Õ¿ÕÀÕÁÕÂÕÃÕÄÕÅÕÆÕÇÕÈÕÉÕÊÕËÕÌÕÍÕÎÕÏÕÐÕÑÕÒÕÓÕÔÕÕÕÖÕ×ÕØÕÙÕÚÕÛÕÜÕÝÕÞÕßÕàÕáÕâÕãÕäÕåÕæÕçÕèÕéÕêÕëÕìÕíÕîÕïÕðÕñÕòÕóÕôÕõÕöÕ÷ÕøÕùÕúÕûÕüÕýÕþÖ¡Ö¢Ö£Ö¤Ö¥Ö¦Ö§Ö¨Ö©ÖªÖ«Ö¬Ö­Ö®Ö¯Ö°Ö±Ö²Ö³Ö´ÖµÖ¶Ö·Ö¸Ö¹ÖºÖ»Ö¼Ö½Ö¾Ö¿ÖÀÖÁÖÂÖÃÖÄÖÅÖÆÖÇÖÈÖÉÖÊÖËÖÌÖÍÖÎÖÏÖÐÖÑÖÒÖÓÖÔÖÕÖÖÖ×ÖØÖÙÖÚÖÛÖÜÖÝÖÞÖßÖàÖáÖâÖãÖäÖåÖæÖçÖèÖéÖêÖëÖìÖíÖîÖïÖðÖñÖòÖóÖôÖõÖöÖ÷ÖøÖùÖúÖûÖüÖýÖþ×¡×¢×£×¤×¥×¦×§×¨×©×ª×«×¬×­×®×¯×°×±×²×³×´×µ×¶×·×¸×¹×º×»×¼×½×¾×¿×À×Á×Â×Ã×Ä×Å×Æ×Ç×È×É×Ê×Ë×Ì×Í×Î×Ï×Ð×Ñ×Ò×Ó×Ô×Õ×Ö×××Ø×Ù×Ú×Û×Ü×Ý×Þ×ß×à×á×â×ã×ä×å×æ×ç×è×é×ê×ë×ì×í×î×ï×ð×ñ×ò×ó×ô×õ×ö×÷×ø×ù&#59408;&#59409;&#59410;&#59411;&#59412;Ø¡Ø¢Ø£Ø¤Ø¥Ø¦Ø§Ø¨Ø©ØªØ«Ø¬Ø­Ø®Ø¯Ø°Ø±Ø²Ø³Ø´ØµØ¶Ø·Ø¸Ø¹ØºØ»Ø¼Ø½Ø¾Ø¿ØÀØÁØÂØÃØÄØÅØÆØÇØÈØÉØÊØËØÌØÍØÎØÏØÐØÑØÒØÓØÔØÕØÖØ×ØØØÙØÚØÛØÜØÝØÞØßØàØáØâØãØäØåØæØçØèØéØêØëØìØíØîØïØðØñØòØóØôØõØöØ÷ØøØùØúØûØüØýØþÙ¡Ù¢Ù£Ù¤Ù¥Ù¦Ù§Ù¨Ù©ÙªÙ«Ù¬Ù­Ù®Ù¯Ù°Ù±Ù²Ù³Ù´ÙµÙ¶Ù·Ù¸Ù¹ÙºÙ»Ù¼Ù½Ù¾Ù¿ÙÀÙÁÙÂÙÃÙÄÙÅÙÆÙÇÙÈÙÉÙÊÙËÙÌÙÍÙÎÙÏÙÐÙÑÙÒÙÓÙÔÙÕÙÖÙ×ÙØÙÙÙÚÙÛÙÜÙÝÙÞÙßÙàÙáÙâÙãÙäÙåÙæÙçÙèÙéÙêÙëÙìÙíÙîÙïÙðÙñÙòÙóÙôÙõÙöÙ÷ÙøÙùÙúÙûÙüÙýÙþÚ¡Ú¢Ú£Ú¤Ú¥Ú¦Ú§Ú¨Ú©ÚªÚ«Ú¬Ú­Ú®Ú¯Ú°Ú±Ú²Ú³Ú´ÚµÚ¶Ú·Ú¸Ú¹ÚºÚ»Ú¼Ú½Ú¾Ú¿ÚÀÚÁÚÂÚÃÚÄÚÅÚÆÚÇÚÈÚÉÚÊÚËÚÌÚÍÚÎÚÏÚÐÚÑÚÒÚÓÚÔÚÕÚÖÚ×ÚØÚÙÚÚÚÛÚÜÚÝÚÞÚßÚàÚáÚâÚãÚäÚåÚæÚçÚèÚéÚêÚëÚìÚíÚîÚïÚðÚñÚòÚóÚôÚõÚöÚ÷ÚøÚùÚúÚûÚüÚýÚþÛ¡Û¢Û£Û¤Û¥Û¦Û§Û¨Û©ÛªÛ«Û¬Û­Û®Û¯Û°Û±Û²Û³Û´ÛµÛ¶Û·Û¸Û¹ÛºÛ»Û¼Û½Û¾Û¿ÛÀÛÁÛÂÛÃÛÄÛÅÛÆÛÇÛÈÛÉÛÊÛËÛÌÛÍÛÎÛÏÛÐÛÑÛÒÛÓÛÔÛÕÛÖÛ×ÛØÛÙÛÚÛÛÛÜÛÝÛÞÛßÛàÛáÛâÛãÛäÛåÛæÛçÛèÛéÛêÛëÛìÛíÛîÛïÛðÛñÛòÛóÛôÛõÛöÛ÷ÛøÛùÛúÛûÛüÛýÛþÜ¡Ü¢Ü£Ü¤Ü¥Ü¦Ü§Ü¨Ü©ÜªÜ«Ü¬Ü­Ü®Ü¯Ü°Ü±Ü²Ü³Ü´ÜµÜ¶Ü·Ü¸Ü¹ÜºÜ»Ü¼Ü½Ü¾Ü¿ÜÀÜÁÜÂÜÃÜÄÜÅÜÆÜÇÜÈÜÉÜÊÜËÜÌÜÍÜÎÜÏÜÐÜÑÜÒÜÓÜÔÜÕÜÖÜ×ÜØÜÙÜÚÜÛÜÜÜÝÜÞÜßÜàÜáÜâÜãÜäÜåÜæÜçÜèÜéÜêÜëÜìÜíÜîÜïÜðÜñÜòÜóÜôÜõÜöÜ÷ÜøÜùÜúÜûÜüÜýÜþÝ¡Ý¢Ý£Ý¤Ý¥Ý¦Ý§Ý¨Ý©ÝªÝ«Ý¬Ý­Ý®Ý¯Ý°Ý±Ý²Ý³Ý´ÝµÝ¶Ý·Ý¸Ý¹ÝºÝ»Ý¼Ý½Ý¾Ý¿ÝÀÝÁÝÂÝÃÝÄÝÅÝÆÝÇÝÈÝÉÝÊÝËÝÌÝÍÝÎÝÏÝÐÝÑÝÒÝÓÝÔÝÕÝÖÝ×ÝØÝÙÝÚÝÛÝÜÝÝÝÞÝßÝàÝáÝâÝãÝäÝåÝæÝçÝèÝéÝêÝëÝìÝíÝîÝïÝðÝñÝòÝóÝôÝõÝöÝ÷ÝøÝùÝúÝûÝüÝýÝþÞ¡Þ¢Þ£Þ¤Þ¥Þ¦Þ§Þ¨Þ©ÞªÞ«Þ¬Þ­Þ®Þ¯Þ°Þ±Þ²Þ³Þ´ÞµÞ¶Þ·Þ¸Þ¹ÞºÞ»Þ¼Þ½Þ¾Þ¿ÞÀÞÁÞÂÞÃÞÄÞÅÞÆÞÇÞÈÞÉÞÊÞËÞÌÞÍÞÎÞÏÞÐÞÑÞÒÞÓÞÔÞÕÞÖÞ×ÞØÞÙÞÚÞÛÞÜÞÝÞÞÞßÞàÞáÞâÞãÞäÞåÞæÞçÞèÞéÞêÞëÞìÞíÞîÞïÞðÞñÞòÞóÞôÞõÞöÞ÷ÞøÞùÞúÞûÞüÞýÞþß¡ß¢ß£ß¤ß¥ß¦ß§ß¨ß©ßªß«ß¬ß­ß®ß¯ß°ß±ß²ß³ß´ßµß¶ß·ß¸ß¹ßºß»ß¼ß½ß¾ß¿ßÀßÁßÂßÃßÄßÅßÆßÇßÈßÉßÊßËßÌßÍßÎßÏßÐßÑßÒßÓßÔßÕßÖß×ßØßÙßÚßÛßÜßÝßÞßßßàßáßâßãßäßåßæßçßèßéßêßëßìßíßîßïßðßñßòßóßôßõßöß÷ßøßùßúßûßüßýßþà¡à¢à£à¤à¥à¦à§à¨à©àªà«à¬à­à®à¯à°à±à²à³à´àµà¶à·à¸à¹àºà»à¼à½à¾à¿àÀàÁàÂàÃàÄàÅàÆàÇàÈàÉàÊàËàÌàÍàÎàÏàÐàÑàÒàÓàÔàÕàÖà×àØàÙàÚàÛàÜàÝàÞàßàààáàâàãàäàåàæàçàèàéàêàëàìàíàîàïàðàñàòàóàôàõàöà÷àøàùàúàûàüàýàþá¡á¢á£á¤á¥á¦á§á¨á©áªá«á¬á­á®á¯á°á±á²á³á´áµá¶á·á¸á¹áºá»á¼á½á¾á¿áÀáÁáÂáÃáÄáÅáÆáÇáÈáÉáÊáËáÌáÍáÎáÏáÐáÑáÒáÓáÔáÕáÖá×áØáÙáÚáÛáÜáÝáÞáßáàáááâáãáäáåáæáçáèáéáêáëáìáíáîáïáðáñáòáóáôáõáöá÷áøáùáúáûáüáýáþâ¡â¢â£â¤â¥â¦â§â¨â©âªâ«â¬â­â®â¯â°â±â²â³â´âµâ¶â·â¸â¹âºâ»â¼â½â¾â¿âÀâÁâÂâÃâÄâÅâÆâÇâÈâÉâÊâËâÌâÍâÎâÏâÐâÑâÒâÓâÔâÕâÖâ×âØâÙâÚâÛâÜâÝâÞâßâàâáâââãâäâåâæâçâèâéâêâëâìâíâîâïâðâñâòâóâôâõâöâ÷âøâùâúâûâüâýâþã¡ã¢ã£ã¤ã¥ã¦ã§ã¨ã©ãªã«ã¬ã­ã®ã¯ã°ã±ã²ã³ã´ãµã¶ã·ã¸ã¹ãºã»ã¼ã½ã¾ã¿ãÀãÁãÂãÃãÄãÅãÆãÇãÈãÉãÊãËãÌãÍãÎãÏãÐãÑãÒãÓãÔãÕãÖã×ãØãÙãÚãÛãÜãÝãÞãßãàãáãâãããäãåãæãçãèãéãêãëãìãíãîãïãðãñãòãóãôãõãöã÷ãøãùãúãûãüãýãþä¡ä¢ä£ä¤ä¥ä¦ä§ä¨ä©äªä«ä¬ä­ä®ä¯ä°ä±ä²ä³ä´äµä¶ä·ä¸ä¹äºä»ä¼ä½ä¾ä¿äÀäÁäÂäÃäÄäÅäÆäÇäÈäÉäÊäËäÌäÍäÎäÏäÐäÑäÒäÓäÔäÕäÖä×äØäÙäÚäÛäÜäÝäÞäßäàäáäâäãäääåäæäçäèäéäêäëäìäíäîäïäðäñäòäóäôäõäöä÷äøäùäúäûäüäýäþå¡å¢å£å¤å¥å¦å§å¨å©åªå«å¬å­å®å¯å°å±å²å³å´åµå¶å·å¸å¹åºå»å¼å½å¾å¿åÀåÁåÂåÃåÄåÅåÆåÇåÈåÉåÊåËåÌåÍåÎåÏåÐåÑåÒåÓåÔåÕåÖå×åØåÙåÚåÛåÜåÝåÞåßåàåáåâåãåäåååæåçåèåéåêåëåìåíåîåïåðåñåòåóåôåõåöå÷åøåùåúåûåüåýåþæ¡æ¢æ£æ¤æ¥æ¦æ§æ¨æ©æªæ«æ¬æ­æ®æ¯æ°æ±æ²æ³æ´æµæ¶æ·æ¸æ¹æºæ»æ¼æ½æ¾æ¿æÀæÁæÂæÃæÄæÅæÆæÇæÈæÉæÊæËæÌæÍæÎæÏæÐæÑæÒæÓæÔæÕæÖæ×æØæÙæÚæÛæÜæÝæÞæßæàæáæâæãæäæåæææçæèæéæêæëæìæíæîæïæðæñæòæóæôæõæöæ÷æøæùæúæûæüæýæþç¡ç¢ç£ç¤ç¥ç¦ç§ç¨ç©çªç«ç¬ç­ç®ç¯ç°ç±ç²ç³ç´çµç¶ç·ç¸ç¹çºç»ç¼ç½ç¾ç¿çÀçÁçÂçÃçÄçÅçÆçÇçÈçÉçÊçËçÌçÍçÎçÏçÐçÑçÒçÓçÔçÕçÖç×çØçÙçÚçÛçÜçÝçÞçßçàçáçâçãçäçåçæçççèçéçêçëçìçíçîçïçðçñçòçóçôçõçöç÷çøçùçúçûçüçýçþè¡è¢è£è¤è¥è¦è§è¨è©èªè«è¬è­è®è¯è°è±è²è³è´èµè¶è·è¸è¹èºè»è¼è½è¾è¿èÀèÁèÂèÃèÄèÅèÆèÇèÈèÉèÊèËèÌèÍèÎèÏèÐèÑèÒèÓèÔèÕèÖè×èØèÙèÚèÛèÜèÝèÞèßèàèáèâèãèäèåèæèçèèèéèêèëèìèíèîèïèðèñèòèóèôèõèöè÷èøèùèúèûèüèýèþé¡é¢é£é¤é¥é¦é§é¨é©éªé«é¬é­é®é¯é°é±é²é³é´éµé¶é·é¸é¹éºé»é¼é½é¾é¿éÀéÁéÂéÃéÄéÅéÆéÇéÈéÉéÊéËéÌéÍéÎéÏéÐéÑéÒéÓéÔéÕéÖé×éØéÙéÚéÛéÜéÝéÞéßéàéáéâéãéäéåéæéçéèéééêéëéìéíéîéïéðéñéòéóéôéõéöé÷éøéùéúéûéüéýéþê¡ê¢ê£ê¤ê¥ê¦ê§ê¨ê©êªê«ê¬ê­ê®ê¯ê°ê±ê²ê³ê´êµê¶ê·ê¸ê¹êºê»ê¼ê½ê¾ê¿êÀêÁêÂêÃêÄêÅêÆêÇêÈêÉêÊêËêÌêÍêÎêÏêÐêÑêÒêÓêÔêÕêÖê×êØêÙêÚêÛêÜêÝêÞêßêàêáêâêãêäêåêæêçêèêéêêêëêìêíêîêïêðêñêòêóêôêõêöê÷êøêùêúêûêüêýêþë¡ë¢ë£ë¤ë¥ë¦ë§ë¨ë©ëªë«ë¬ë­ë®ë¯ë°ë±ë²ë³ë´ëµë¶ë·ë¸ë¹ëºë»ë¼ë½ë¾ë¿ëÀëÁëÂëÃëÄëÅëÆëÇëÈëÉëÊëËëÌëÍëÎëÏëÐëÑëÒëÓëÔëÕëÖë×ëØëÙëÚëÛëÜëÝëÞëßëàëáëâëãëäëåëæëçëèëéëêëëëìëíëîëïëðëñëòëóëôëõëöë÷ëøëùëúëûëüëýëþì¡ì¢ì£ì¤ì¥ì¦ì§ì¨ì©ìªì«ì¬ì­ì®ì¯ì°ì±ì²ì³ì´ìµì¶ì·ì¸ì¹ìºì»ì¼ì½ì¾ì¿ìÀìÁìÂìÃìÄìÅìÆìÇìÈìÉìÊìËìÌìÍìÎìÏìÐìÑìÒìÓìÔìÕìÖì×ìØìÙìÚìÛìÜìÝìÞìßìàìáìâìãìäìåìæìçìèìéìêìëìììíìîìïìðìñìòìóìôìõìöì÷ìøìùìúìûìüìýìþí¡í¢í£í¤í¥í¦í§í¨í©íªí«í¬í­í®í¯í°í±í²í³í´íµí¶í·í¸í¹íºí»í¼í½í¾í¿íÀíÁíÂíÃíÄíÅíÆíÇíÈíÉíÊíËíÌíÍíÎíÏíÐíÑíÒíÓíÔíÕíÖí×íØíÙíÚíÛíÜíÝíÞíßíàíáíâíãíäíåíæíçíèíéíêíëíìíííîíïíðíñíòíóíôíõíöí÷íøíùíúíûíüíýíþî¡î¢î£î¤î¥î¦î§î¨î©îªî«î¬î­î®î¯î°î±î²î³î´îµî¶î·î¸î¹îºî»î¼î½î¾î¿îÀîÁîÂîÃîÄîÅîÆîÇîÈîÉîÊîËîÌîÍîÎîÏîÐîÑîÒîÓîÔîÕîÖî×îØîÙîÚîÛîÜîÝîÞîßîàîáîâîãîäîåîæîçîèîéîêîëîìîíîîîïîðîñîòîóîôîõîöî÷îøîùîúîûîüîýîþï¡ï¢ï£ï¤ï¥ï¦ï§ï¨ï©ïªï«ï¬ï­ï®ï¯ï°ï±ï²ï³ï´ïµï¶ï·ï¸ï¹ïºï»ï¼ï½ï¾ï¿ïÀïÁïÂïÃïÄïÅïÆïÇïÈïÉïÊïËïÌïÍïÎïÏïÐïÑïÒïÓïÔïÕïÖï×ïØïÙïÚïÛïÜïÝïÞïßïàïáïâïãïäïåïæïçïèïéïêïëïìïíïîïïïðïñïòïóïôïõïöï÷ïøïùïúïûïüïýïþð¡ð¢ð£ð¤ð¥ð¦ð§ð¨ð©ðªð«ð¬ð­ð®ð¯ð°ð±ð²ð³ð´ðµð¶ð·ð¸ð¹ðºð»ð¼ð½ð¾ð¿ðÀðÁðÂðÃðÄðÅðÆðÇðÈðÉðÊðËðÌðÍðÎðÏðÐðÑðÒðÓðÔðÕðÖð×ðØðÙðÚðÛðÜðÝðÞðßðàðáðâðãðäðåðæðçðèðéðêðëðìðíðîðïðððñðòðóðôðõðöð÷ðøðùðúðûðüðýðþñ¡ñ¢ñ£ñ¤ñ¥ñ¦ñ§ñ¨ñ©ñªñ«ñ¬ñ­ñ®ñ¯ñ°ñ±ñ²ñ³ñ´ñµñ¶ñ·ñ¸ñ¹ñºñ»ñ¼ñ½ñ¾ñ¿ñÀñÁñÂñÃñÄñÅñÆñÇñÈñÉñÊñËñÌñÍñÎñÏñÐñÑñÒñÓñÔñÕñÖñ×ñØñÙñÚñÛñÜñÝñÞñßñàñáñâñãñäñåñæñçñèñéñêñëñìñíñîñïñðñññòñóñôñõñöñ÷ñøñùñúñûñüñýñþò¡ò¢ò£ò¤ò¥ò¦ò§ò¨ò©òªò«ò¬ò­ò®ò¯ò°ò±ò²ò³ò´òµò¶ò·ò¸ò¹òºò»ò¼ò½ò¾ò¿òÀòÁòÂòÃòÄòÅòÆòÇòÈòÉòÊòËòÌòÍòÎòÏòÐòÑòÒòÓòÔòÕòÖò×òØòÙòÚòÛòÜòÝòÞòßòàòáòâòãòäòåòæòçòèòéòêòëòìòíòîòïòðòñòòòóòôòõòöò÷òøòùòúòûòüòýòþó¡ó¢ó£ó¤ó¥ó¦ó§ó¨ó©óªó«ó¬ó­ó®ó¯ó°ó±ó²ó³ó´óµó¶ó·ó¸ó¹óºó»ó¼ó½ó¾ó¿óÀóÁóÂóÃóÄóÅóÆóÇóÈóÉóÊóËóÌóÍóÎóÏóÐóÑóÒóÓóÔóÕóÖó×óØóÙóÚóÛóÜóÝóÞóßóàóáóâóãóäóåóæóçóèóéóêóëóìóíóîóïóðóñóòóóóôóõóöó÷óøóùóúóûóüóýóþô¡ô¢ô£ô¤ô¥ô¦ô§ô¨ô©ôªô«ô¬ô­ô®ô¯ô°ô±ô²ô³ô´ôµô¶ô·ô¸ô¹ôºô»ô¼ô½ô¾ô¿ôÀôÁôÂôÃôÄôÅôÆôÇôÈôÉôÊôËôÌôÍôÎôÏôÐôÑôÒôÓôÔôÕôÖô×ôØôÙôÚôÛôÜôÝôÞôßôàôáôâôãôäôåôæôçôèôéôêôëôìôíôîôïôðôñôòôóôôôõôöô÷ôøôùôúôûôüôýôþõ¡õ¢õ£õ¤õ¥õ¦õ§õ¨õ©õªõ«õ¬õ­õ®õ¯õ°õ±õ²õ³õ´õµõ¶õ·õ¸õ¹õºõ»õ¼õ½õ¾õ¿õÀõÁõÂõÃõÄõÅõÆõÇõÈõÉõÊõËõÌõÍõÎõÏõÐõÑõÒõÓõÔõÕõÖõ×õØõÙõÚõÛõÜõÝõÞõßõàõáõâõãõäõåõæõçõèõéõêõëõìõíõîõïõðõñõòõóõôõõõöõ÷õøõùõúõûõüõýõþö¡ö¢ö£ö¤ö¥ö¦ö§ö¨ö©öªö«ö¬ö­ö®ö¯ö°ö±ö²ö³ö´öµö¶ö·ö¸ö¹öºö»ö¼ö½ö¾ö¿öÀöÁöÂöÃöÄöÅöÆöÇöÈöÉöÊöËöÌöÍöÎöÏöÐöÑöÒöÓöÔöÕöÖö×öØöÙöÚöÛöÜöÝöÞößöàöáöâöãöäöåöæöçöèöéöêöëöìöíöîöïöðöñöòöóöôöõööö÷öøöùöúöûöüöýöþ÷¡÷¢÷£÷¤÷¥÷¦÷§÷¨÷©÷ª÷«÷¬÷­÷®÷¯÷°÷±÷²÷³÷´÷µ÷¶÷·÷¸÷¹÷º÷»÷¼÷½÷¾÷¿÷À÷Á÷Â÷Ã÷Ä÷Å÷Æ÷Ç÷È÷É÷Ê÷Ë÷Ì÷Í÷Î÷Ï÷Ð÷Ñ÷Ò÷Ó÷Ô÷Õ÷Ö÷×÷Ø÷Ù÷Ú÷Û÷Ü÷Ý÷Þ÷ß÷à÷á÷â÷ã÷ä÷å÷æ÷ç÷è÷é÷ê÷ë÷ì÷í÷î÷ï÷ð÷ñ÷ò÷ó÷ô÷õ÷ö÷÷÷ø÷ù÷ú÷û÷ü÷ý÷þ';
    var i,p,l=strGB.length;

    var length = Math.min(s.length, s1.length);

    for (i = 0; i < length; i++)
    {
    	if (s.charAt(i) == s1.charAt(i))
    	{
    		continue;
    	}

    	if (strGB.indexOf(s.charAt(i) + '') > strGB.indexOf(s1.charAt(i) + ''))
    	{
    		return 1;
    	}
    	else
    	{
    		return -1;
    	}
    }

    return s.length - s1.length;
}

/**
 * É¾³ýËùÓÐµÄselectÔªËØ
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
 * ±È½ÏÊ±¼ä
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

function displayTime()
{
    var today = new Date();
    var date = today.getYear() + "-";
	if ( today.getMonth() < 9 ) date += "0";
	date += today.getMonth() + 1 + "-";
	if ( today.getDate() < 10 ) date += "0";
	date += today.getDate() + " ";
	if ( today.getHours() < 10 ) date += "0";
	date += today.getHours() + ":";
	if ( today.getMinutes() < 10 ) date += "0";
	date += today.getMinutes() + ":";
	if ( today.getSeconds() < 10 ) date += "0";
	date += today.getSeconds();
	document.getElementById("timer").innerHTML=date;
	setTimeout("displayTime()",990);
}

/**
 * ¿ìËÙ°ó¶¨select½¨Á¢¼òÆ´Ë÷Òý
 */
function quickSelect(element)
{
	if (typeof element == 'string')
    {
        element = document.getElementById(element);
    }

	window.common.addEvent(element, 'keydown', spellList);
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

