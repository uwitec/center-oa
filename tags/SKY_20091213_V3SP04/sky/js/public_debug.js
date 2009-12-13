var common = window.common || {};

common.is_safari = /Safari/i.test(navigator.userAgent);

common.is_ie = ( /msie/i.test(navigator.userAgent) && !/opera/i.test(navigator.userAgent) );

common.is_chrome = ( /chrome/i.test(navigator.userAgent) && !/opera/i.test(navigator.userAgent) );

/**
 * get event in function
 * because in IE, window.event is the gobal object
 * but in firefox window.event is not exist.
 * so in firefox must use func.arguments.callee.caller.arguments[0]
 */
common.getEvent = function ()
{
	// IE
	if (window.event)
   	{
   		return window.event;
   	}

    var func = this.getEvent;

    while(func != null)
    {
    	//返回调用函数的【arguments.callee】(callee是arguments的一个属性值)
    	//callee:返回正被执行的 Function 对象,这里就是func
    	//caller:返回一个对函数的引用
    	//alert(func.arguments.callee.caller.arguments[0].constructor.toString());

        var eve = func.arguments.callee.arguments[0];

        if (eve != null && eve.constructor && eve.constructor.toString().indexOf('Event') != -1)
        {
        	return eve;
        }

        func = func.caller;
    }

    return null;
}

/**
 * 绑定enter
 */
common.enter = function (fun)
{
	var event = this.getEvent();

	if(event.keyCode == 13)
    {
        fun.call(this);
        event.keyCode = "";
        return false;
    }
}

common.qmodal = function(url)
{
	window.common.modal(url, 500, 600);
}
/**
 * showModalDialog
 * in IE window.showModalDialog can use directly
 * but in firefox window.showModalDialog is not exist.
 * so use window.open instead of window.showModalDialog in firefox.
 * other this method only support [window] in DialogWindow
 * in IE DialogWindow use window.dialogArguments[0] can get the opener window
 * in firefox DialogWindow use window.opener.myArguments[0] can get the opener window
 * or use common.opener() get the opener window
 */
common.modal = function(url, height, width)
{
	var end = "CENTER_COMMON_CENTER_COMMON=" + new Date().getTime();

	if (url.indexOf('?') == -1)
	{
		url = url + '?' + end;
	}
	else
	{
		url = url + '&' + end;
	}

	if (height)
	{
		iheight = height;
	}
	else
	{
		iheight = 800;
	}

	if (width)
	{
		iwidth = width;
	}
	else
	{
		iwidth = 900;
	}
	
	var sh = window.screen.height
	
	var sw = window.screen.width
	
	var dialogTop = Math.ceil((sh - iheight) / 2) - 50;
	
	var dialogLeft = Math.ceil((sw - iwidth) / 2)

	var sFeatures="dialogTop:"+dialogTop+"px;dialogLeft:"+dialogLeft+"px;center:yes;dialogHeight: " + iheight + "px;dialogWidth:" + iwidth + "px";

	//参数
    var strPara = "toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=950,height=600,left=150,top=100,resizable=false";

    if(window.showModalDialog != null)
	{
	    if (window.common.is_safari)
        {
            window.open(url, "myOpen", strPara, true);
        }
        else
        {
            window.showModalDialog(url, [window], sFeatures);
        }
        
	    //window.open(url, "myOpen", strPara, true);
    }
    else
    {
        //注册事件
        window.myAction = this;
        //打开窗口
        var DialogWin = window.open(url, "myOpen", strPara, true);

        //传入参数 in subwindow use  window.dialogArguments = window.opener.myArguments;
        window.myArguments = [window];

        //处理打开窗口最上显示（不完美）
        window.onclick = function (){DialogWin.focus()};

        this.getEvent().cancelBubble = true;
    }
}

/**
 * get opener in modalDialog
 * in IE DialogWindow use window.dialogArguments[0] can get the opener window
 * in firefox DialogWindow use window.opener.myArguments[0] can get the opener window
 */
common.opener =  function()
{
	if (window.showModalDialog != null)
	{
		if (window.dialogArguments)
		return window.dialogArguments[0];
		else
		return opener;
	}
	else
	{
		return window.opener.myArguments[0];
	}
}

common.getFocusElement = function(ev)
{
	return window.common.is_ie ? window.event.srcElement : ev.target;
}

common.getSelectText = function()
{
	if (document.selection)
	{
		return document.selection.createRange().text;
	}
	
	if (window.common.is_chrome)
	{
		return window.getSelection();
	}
	
	var curEle = window.common.getFocusElement(window.common.getEvent());
	
	if (curEle && curEle.selectionStart!= undefined)
	{
		return curEle.value.substr(curEle.selectionStart, curEle.selectionEnd - curEle.selectionStart);
	}
	
	return "";
}

/**
 * add event function to special object
 */
common.addEvent = function(el, evname, func)
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

/**
 * 停止key的响应事件
 */
common.stopEvent = function(ev)
{
	ev = ev || (ev = window.event);
    if (window.event)
    {
        ev.cancelBubble = true;
        ev.returnValue = false;
    }
    else
    {
        ev.preventDefault();
        ev.stopPropagation();
    }

    return false;
}

/**
 * 去除event
 */
common.removeEvent = function(el, evname, func)
{
    if (el.detachEvent)
    {
        // IE
        el.detachEvent("on" + evname, func);
    }
    else if (el.removeEventListener)
    {
        // Gecko / W3C
        el.removeEventListener(evname, func, true);
    }
    else
    {
        el["on" + evname] = null;
    }
}

/**
 * 获得控件的位置
 */
common.getAbsolutePos = function(el)
{
    var SL = 0, ST = 0;
    var is_div = /^div$/i.test(el.tagName);
    if (is_div && el.scrollLeft)
    SL = el.scrollLeft;
    if (is_div && el.scrollTop)
    ST = el.scrollTop;
    var r =
    {
        x: el.offsetLeft - SL, y: el.offsetTop - ST, w:el.offsetWidth, h:el.offsetHeight
    }
    ;
    if (el.offsetParent)
    {
        var tmp = this.getAbsolutePos(el.offsetParent);
        r.x += tmp.x;
        r.y += tmp.y;
    }

    return r;
}

common.clipboard = function(meintext)
{
    if (window.clipboardData) 
    {
        // the IE-manier
        window.clipboardData.setData("Text", meintext);
    }
    else if (window.netscape) 
    { 
        // dit is belangrijk maar staat nergens duidelijk vermeld:
        // you have to sign the code to enable this, or see notes below 
        try
        {
        	netscape.security.PrivilegeManager.enablePrivilege('UniversalXPConnect');
        }
        catch (e)
        {
        	alert("refuse！\nplease set friefox'about:config -> signed.applets.codebase_principal_support'\nset value:true");
        	return;
        }
          
        // maak een interface naar het clipboard
        var clip = Components.classes['@mozilla.org/widget/clipboard;1']
                        .createInstance(Components.interfaces.nsIClipboard);
        if (!clip) return;
        //alert(clip);
        // maak een transferable
        var trans = Components.classes['@mozilla.org/widget/transferable;1']
                        .createInstance(Components.interfaces.nsITransferable);
        if (!trans) return;
           
        // specificeer wat voor soort data we op willen halen; text in dit geval
        trans.addDataFlavor('text/unicode');
           
        // om de data uit de transferable te halen hebben we 2 nieuwe objecten 
        // nodig om het in op te slaan
        var str = new Object();
        var len = new Object();
        var str = Components.classes["@mozilla.org/supports-string;1"]
                        .createInstance(Components.interfaces.nsISupportsString);
        var copytext=meintext;
        str.data=copytext;
        trans.setTransferData("text/unicode",str,copytext.length*2);
        var clipid=Components.interfaces.nsIClipboard;
        if (!clip) return false;
        clip.setData(trans,null,clipid.kGlobalClipboard);
    }
    else
    {
        return false;
    }

    return false;
}