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

function parseIntInner(str)
{
   if (str == null || str == "")
   {
      return 0;
   }

   return parseInt(str);
}

var g_center_handle;

var g_pop_h_old = 80;

var g_pop_h = g_pop_h_old;

function tip_start()
{
   var obj = document.getElementById("g_center_tip");

   if ( ! obj)
   {
      return;
   }

   var content1 = document.getElementById("g_tip_content1");

   var content2 = document.getElementById("g_tip_content2");

   var flagObj = document.getElementById("g_tip_flag");

   if (content1.innerHTML == "" && content2.innerHTML == "")
   {
      return;
   }

   if (flagObj.value == "1")
   {
      return;
   }
   else
   {
      flagObj.value = "1";
   }

   if (parseIntInner(obj.style.height) == 0)
   {
      obj.style.display = "block";
      g_center_handle = setInterval("changeH('up')", 2);
   }
   else
   {
      g_center_handle = setInterval("changeH('down')", 2)
   }
}

function tip_hiden()
{
   var obj = document.getElementById("g_center_tip");
   if ( ! obj)
   {
      return;
   }
   var flagObj = document.getElementById("g_tip_flag");
   flagObj.value = "1";
   if (parseIntInner(obj.style.height) != 0)
   {
      g_center_handle = setInterval("changeH('down')", 2)
   }
}


function changeH(str)
{
   var obj = document.getElementById("g_center_tip");
   if(str == "up")
   {
      if (parseIntInner(obj.style.height) > g_pop_h)
      clearInterval(g_center_handle);
      else
      obj.style.height = (parseIntInner(obj.style.height) + 8).toString() + "px";
   }
   if(str == "down")
   {
      if (parseIntInner(obj.style.height) < 8)
      {
         clearInterval(g_center_handle);
         obj.style.display = "none";
      }
      else
      obj.style.height = (parseIntInner(obj.style.height) - 8).toString() + "px";
   }
}

var timer1;
var timer2;
function loadTip()
{
   document.getElementById('g_center_tip').style.height = '0px';

   var content1 = document.getElementById("g_tip_content1");

   var content2 = document.getElementById("g_tip_content2");

   if (content1.innerHTML == "" && content2.innerHTML == "")
   {
      return;
   }
   else
   {
   	   if (timer1) 
   	   clearTimeout(timer1); 
   	   if (timer2) 
   	   clearTimeout(timer2); 
	   
	   timer1 = setTimeout("tip_start()", 300);
	
	   timer2 = setTimeout("tip_hiden()", 8000);
   }
}

function $error(msg)
{
	if (msg == undefined && Res._Public)
	reloadTip(Res._Public['REFUSE'], false)
	else
	reloadTip(msg, false)
}

function $sucess(msg)
{
    reloadTip(msg, true)
}

function reloadTip(msg, success)
{
	if (msg.length > 45)
	{
		var addH = ((msg.length - 45) / 15) + 1;
		
		g_pop_h = g_pop_h_old + addH * 12;
	}
	else
	{
		g_pop_h = g_pop_h_old;
	}
	
	if (success)
	{
		document.getElementById('g_tip_content1').innerHTML = msg;
		document.getElementById('g_tip_content2').innerHTML = '';
	}
	else
	{
		document.getElementById('g_tip_content1').innerHTML = '';
		document.getElementById('g_tip_content2').innerHTML = msg;
	}
	
	var flagObj = document.getElementById("g_tip_flag");
	
	flagObj.value = "0";
	
	loadTip();
}

if (window.addEventCommon)
window.addEventCommon(window, 'load', loadTip);
