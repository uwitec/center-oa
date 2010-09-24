var qTipTag = "title_div";
// Which tag do you want to qTip - ize ? Keep it lowercase ! //
var qTipX = - 10;
// This is qTip's X offset//
var qTipY = 25;
// This is qTip's Y offset//

// There's No need to edit anything below tooltip line//
tooltip =
{
	stop: false,
    name : "qTip",
    offsetX : qTipX,
    offsetY : qTipY,
    tip : null
}
tooltip.init = function ()
{
    var tipNameSpaceURI = "http://www.w3.org/1999/xhtml";
    if( ! tipContainerID)
    {
        var tipContainerID = "qTip";
    }
    var tipContainer = document.getElementById(tipContainerID);
    if( ! tipContainer)
    {
        tipContainer = document.createElementNS ? document.createElementNS(tipNameSpaceURI, "div") : document.createElement("div");
        tipContainer.setAttribute("id", tipContainerID);
        document.getElementsByTagName("body").item(0).appendChild(tipContainer);
    }

    if ( ! document.getElementById) return;
    tooltip.tip = document.getElementById (tooltip.name);
    evt = tooltip.getEvent();
    if (tooltip.tip) document.onmousemove = function (evt)
    {
        tooltip.move (evt)
    }
    ;
    var a, sTitle;
    var anchors = document.getElementsByTagName (qTipTag);
    for (var i = 0; i < anchors.length; i ++ )
    {
        a = anchors[i];
        sTitle = a.getAttribute("title");
        if(sTitle)
        {
            a.setAttribute("tiptitle", sTitle);
            a.removeAttribute("title");
            a.onmouseover = function()
            {
                tooltip.show(this.getAttribute('tiptitle'))
            }
            ;
            a.onmouseout = function()
            {
                tooltip.hide()
            }
            ;
        }
    }
}
tooltip.move = function (evt)
{
	if (!evt)
	{
		evt = tooltip.getEvent();
	}

	if (tooltip.stop)
	{
		return;
	}

    var x = 0, y = 0, divWidth = 0;
    if (document && document.all)
    {
        // IE
        x = (document.documentElement && document.documentElement.scrollLeft) ? document.documentElement.scrollLeft : document.body.scrollLeft;
        y = (document.documentElement && document.documentElement.scrollTop) ? document.documentElement.scrollTop : document.body.scrollTop;
        x += tooltip.getEvent().clientX;
        y += tooltip.getEvent().clientY;

    }
    else
    {
        // Good Browsers
        x = evt.pageX;
        y = evt.pageY;
    }

    // get the div client width

    divWidth = tooltip.tip.clientWidth;

    var lleft = 0;

    if ( document.body.clientLeft)
    {
    	lleft = document.body.clientLeft;
    }

    var maxWidth = document.body.clientWidth + lleft;

    var current = x + tooltip.offsetX + divWidth;

    //if the left is smaller than 0, the div will stop move X
    if ((x + tooltip.offsetX) > 0)
    {
        if (current < maxWidth)
        {
            tooltip.tip.style.left = (x + tooltip.offsetX) + "px";
        }
        else
        {
            tooltip.tip.style.left = (maxWidth - divWidth + tooltip.offsetX) + "px";
        }
    }

    tooltip.tip.style.top = (y + tooltip.offsetY) + "px";

    tooltip.hiddenSelect(true);
}

tooltip.move_fllow = function (evt)
{
    var x = 0, y = 0, divWidth = 0;
    if (document && document.all)
    {
        // IE
        x = (document.documentElement && document.documentElement.scrollLeft) ? document.documentElement.scrollLeft : document.body.scrollLeft;
        y = (document.documentElement && document.documentElement.scrollTop) ? document.documentElement.scrollTop : document.body.scrollTop;
        x += tooltip.getEvent().clientX;
        y += tooltip.getEvent().clientY;

    }
    else
    {
        // Good Browsers
        x = evt.pageX;
        y = evt.pageY;
    }

    // get the div client width

    divWidth = tooltip.tip.clientWidth;

     var lleft = 0;

    if ( document.body.clientLeft)
    {
    	lleft = document.body.clientLeft;
    }

    var maxWidth = document.body.clientWidth + lleft;

    var current = x + tooltip.offsetX + divWidth;

    //if the left is smaller than 0, the div will stop move X
    if ((x + tooltip.offsetX) > 0)
    {
        if (current < maxWidth)
        {
            tooltip.tip.style.left = (x + tooltip.offsetX) + "px";
        }
        else
        {
            tooltip.tip.style.left = (maxWidth - divWidth + tooltip.offsetX) + "px";
        }
    }

	if (y - document.body.clientTop - 10 > 0)
    tooltip.tip.style.top = (y - 10) + "px";

    tooltip.hiddenSelect(true);
}

tooltip.is_ie = ( /msie/i.test(navigator.userAgent) &&
!/opera/i.test(navigator.userAgent) );

tooltip.hiddenSelect = function (hidden)
{
	var el = document.getElementById('title_div_center');

	// 只有IE6以下的版本才有这个bug
	if (!el || !tooltip.is_ie)
	{
		return;
	}

	function getVisib(obj)
    {
    	is_khtml = /Konqueror|Safari|KHTML/i.test(navigator.userAgent);
        var value = obj.style.visibility;
        if (!value)
        {
            if (document.defaultView && typeof (document.defaultView.getComputedStyle) == "function")
            {
                // Gecko, W3C
                if (!is_khtml)
                value = document.defaultView.
                getComputedStyle(obj, "").getPropertyValue("visibility");
                else
                value = '';
            }
            else if (obj.currentStyle)
            {
                // IE
                value = obj.currentStyle.visibility;
            }
            else
            value = '';
        }
        return value;
    }
    ;

	p = window.common.getAbsolutePos(el);

	var EX1 = p.x;
    var EX2 = p.w + EX1;
    var EY1 = p.y;
    var EY2 = p.h + EY1;

    var ar = document.getElementsByTagName("select");

    for (var i = ar.length; i > 0;)
    {
        cc = ar[--i];

        p = window.common.getAbsolutePos(cc);
        var CX1 = p.x;
        var CX2 = p.w + CX1;
        var CY1 = p.y;
        var CY2 = p.h + CY1;

        if (!hidden || (CX1 > EX2) || (CX2 < EX1) || (CY1 > EY2) || (CY2 < EY1))
        {
            if (!cc.__msh_save_visibility)
            {
                cc.__msh_save_visibility = getVisib(cc);
            }
            cc.style.visibility = cc.__msh_save_visibility;
        }
        else
        {
            if (!cc.__msh_save_visibility)
            {
                cc.__msh_save_visibility = getVisib(cc);
            }

            cc.style.visibility = "hidden";
        }
    }
}

tooltip.showTable = function (text, width)
{
	if (tooltip.stop) return;
    if ( ! tooltip.tip) return;

    var hea = '';
    if (arguments[1] == undefined)
    {
        hea = "<table width = " + 700 + "px";
    }
    else
    {
        hea = "<table width = " + width + "px";
    }

    var tit = "<div id=title_div_center>" +hea + " BORDER=0 CELLSPACING=0 class='border'><tr><td heigh=5 bgcolor=#FFB6C1 style='cursor:move' onmousedown='moveDIV()'>&nbsp;</td></tr></table></div>";

    tooltip.tip.innerHTML = '<div id=title_div_center>'+hea + " BORDER=0 CELLSPACING=0 class='border'><tr><td>" + text + "</td></tr></table></div>";

    tooltip.tip.style.display = "block";

    tooltip.hiddenSelect(true);
}


function moveDIV()
{
	if(event.button == 30)
	{
		document.attachEvent('onmousemove', tooltip.move_fllow);
		document.attachEvent('onmouseup', diaDIV);
	}
}

function diaDIV()
{
	document.detachEvent('onmousemove', tooltip.move_fllow);
	document.detachEvent('onmouseup', diaDIV);
}

tooltip.show = function (text, width)
{
	if (tooltip.stop) return;
    if ( ! tooltip.tip) return;

    var cwidth;
    if (arguments[1] == undefined)
    {
        cwidth = 500;
    }
    else
    {
        cwidth = width;
    }

    tooltip.tip.innerHTML = "<table width = " + cwidth + "px BORDER=0 CELLSPACING=0 class='border'><tr><td><table width='100%' border='0'><tr><td>" + text + "</td></tr></table></td></tr></table>";

    tooltip.tip.style.display = "block";

    tooltip.hiddenSelect(true);
}

/**
 * 绑定ESC
 */
tooltip.bingEsc = function (event)
{
	if ( ! tooltip.tip) return;
	if (event.keyCode == 27)
	{
		if (tooltip.tip.style.display == "block")
		{
			tooltip.stop = !(tooltip.stop);
			if (!tooltip.stop)
			{
				tooltip.hide();
			}
		}
	}
}

tooltip.getEvent = function ()
{
	// IE
	if (window.event)
   	{
   		return window.event;
   	}

    var func = tooltip.getEvent;

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

tooltip.hide = function ()
{
	if (tooltip.stop) return;

    if ( ! tooltip.tip) return;

    tooltip.hiddenSelect(false);

    tooltip.tip.innerHTML = "";
    tooltip.tip.style.display = "none";
}

if (window.addEventCommon)
{
	window.addEventCommon(window, 'load', tooltip.init);
}
else
{
	window.onload = function ()
	{
	    tooltip.init ();
	
	    if (this.selfLoad)
	    {
	    	selfLoad();
	    }
	}
}
