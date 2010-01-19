var selftops;
//self alert
function beginDrag()
{
   var tops = selftops;
	if (tops == null)
	{
		return;
	}
   var obj = tops.document.getElementById("GGGDIVGGG"); 
   if (obj == 'undefined' || obj == null)
   {
       return; 
   }
   var x= parseInt(obj.style.left);
   var y= parseInt(obj.style.top);
   var deltaX = tops.event.clientX-x;
   var deltaY = tops.event.clientY-y;
   tops.document.attachEvent("onmousemove", moveHandler);
   tops.document.attachEvent("onmouseup", upHandler,true);
   tops.event.cancelBubble = true;
   tops.event.returnValue=true;

      
    function moveHandler()
    {
       if(!tops.event)
       tops.event = window.event;
        obj.style.left = (tops.event.clientX-deltaX)+"px";
        obj.style.top = (tops.event.clientY-deltaY)+"px";
         tops.event.cancelBubble = true;
    }
    //
    function upHandler()
    {
      // if(!event)event = window.event;
       tops.document.detachEvent("onmouseup", upHandler);
       tops.document.detachEvent("onmousemove", moveHandler);      
         tops.event.cancelBubble = true;
    }
}

function myalert(strs, tops)
{
  try
  {
	if (tops != 'undefined' && tops != null)
	{
		selftops = tops;
	}
	else
	{
		selftops = window.top;
	}
	if (selftops == 'undefined' || selftops == null)
	{
		alert(strs);
		return;
	}
	
	var mydocument = selftops.document;
	if (mydocument.body == 'undefined' || mydocument.body == null)
    {
    	alert(strs);
    	return;
    }
    var obj;
    if (mydocument.getElementById("GGGDIVGGG") == 'undefined' || mydocument.getElementById("GGGDIVGGG") == null)
    {
        obj = mydocument.createElement("DIV");
        obj.id = "GGGDIVGGG";
        obj.style.position = 'absolute';
        obj.style.height = '60px';
        obj.style.width = '200px';
        obj.style.backgroundColor = '#E7E7E7';
        obj.style.zIndex = 1;
        obj.style.wordBreak = 'keep-all';
        obj.style.border = '1px none #000000';
        obj.style.overflow = 'visible';
        obj.onmousedown = beginDrag;
        obj.onkeydown = GGGPRESS;
    }
    else
    {
        obj = mydocument.getElementById("GGGDIVGGG");
    }
   
    obj.style.display = 'block';
    
    if (strs.length < 16)
    {
        var ii = 16 - strs.length / 2 + 1;
        for (var i = 0; i < ii; i++)
        {
            strs = '&nbsp;' + strs + '&nbsp;';    
        }    
    }
    
    var str = '<table style = "background-color:#0055E6; BORDER-RIGHT: #0055E6 0.5px solid; BORDER-TOP: #0055E6 0.5px solid; BORDER-LEFT:#0055E6 0.5px solid;BORDER-BOTTOM: #0055E6 0.5px solid;">'
	str=str+'<tr style = "cursor:hand" height="6px"><td><table style = "color: #FFFFFF" ><tr><td style = "font-size:10pt" height="6px">提示!</td></tr></table></td></tr>';
		
	str=str+'<tr><td><table style = "background-color:fef8ED" id = "tb"><tr style="word-break:keep-all"><td align ="center" style="word-break:keep-all"><font color="#000000" style="word-wrap:normal">'+ strs +'</font></td></tr>';
	str=str+'<tr ><td height = "6px" ></td></tr><tr ><td align = "center"><input type="button" value="&nbsp;确 定&nbsp;" onclick="GGGCLOSEGGG()" style="cursor: hand"></td></tr></table></td></tr></table>'; 
    obj.innerHTML = str;
 
   //decide the position of Layer1
   var wleft = mydocument.body.clientLeft + mydocument.body.clientWidth;
   var wtop = mydocument.body.clientTop + mydocument.body.clientHeight - 2 * parseInt(obj.style.height); 
   
   mydocument.body.appendChild(obj);
   
   obj.style.left = wleft / 2  - parseInt(obj.clientWidth) / 2 + "px";
   obj.style.top = wtop / 2 + "px";
   obj.focus();
 }
 catch(e)
 {
 	alert(strs);
 }
}


function GGGCLOSEGGG()
{
	var tops = selftops;
	if (tops == null)
	{
		tops = window.top.main;
		if (tops == null)
		{
			tops = window.top.maps.gis;
			if (tops == null)
			return;
		}
	}
    var obj = tops.document.getElementById("GGGDIVGGG");
    if (obj == 'undefined' || obj == null)
    {
    	var oo = window.top.maps;
    	if (oo.gis != 'undefined' && oo.gis != null)
    	{
    		try
    		{
    			obj = window.top.maps.gis.document.getElementById("GGGDIVGGG");
    	    }
    	    catch(e)
    	    {
    	    	return;
    	    }
    	}
    	if (obj == 'undefined' || obj == null)
        return; 
    }
    obj.style.display = 'none';
}

function GGGPRESS()
{
	var tops = selftops;
	if (tops == null)
	{
		return;
	}
    if (tops.event.keyCode == 27 || tops.event.keyCode == 32)
    {
        GGGCLOSEGGG();
    }
}