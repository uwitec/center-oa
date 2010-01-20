 //表格排序

    var cco = 0;
 	var telNumbers = 11;
    var sortIndex;
    var isAsc = 0;
    function tableSort(cell, num){
    	sortIndex = cell.cellIndex;
    	var rowArray=[];

    	var tr = cell.parentNode;
    	var tbody = tr.parentNode;
    	var tab = tbody.parentNode;
    	var rows = tbody.getElementsByTagName("TR")
    	for(i=1;i<rows.length;i++){
    		var row = rows[i];
    		rowArray[i] = row;
    	}

    	if(isAsc==1)
    	{
    		if (num)
    		{
    			rowArray.sort(sortAscNum);
    		}
    		else
    		{
    			rowArray.sort(sortAsc);
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
    			rowArray.sort(sortDesc);
    		}
    	}

    	try{

    		for(i=1;i<rowArray.length;i++){
    			tr.insertAdjacentElement("afterEnd",rowArray[i]);
    		}

    	} catch(e){
    	}
    	isAsc = (isAsc+1)%2;
    }

    function sortAsc(x,y){

 		if (x.getElementsByTagName("TD")[sortIndex].innerText > y.getElementsByTagName("TD")[sortIndex].innerText)
  			return -1;
 		else if (x.getElementsByTagName("TD")[sortIndex].innerText == y.getElementsByTagName("TD")[sortIndex].innerText)
   			return 0;
 		else
   			return 1;
    }

    function sortDesc(x,y){

 		if (x.getElementsByTagName("TD")[sortIndex].innerText < y.getElementsByTagName("TD")[sortIndex].innerText)
  			return -1;
 		else if (x.getElementsByTagName("TD")[sortIndex].innerText == y.getElementsByTagName("TD")[sortIndex].innerText)
   			return 0;
 		else
   			return 1;
    }

    function sortAscNum(x,y)
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

    function sortDescNum(x,y)
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

    function tableSort1(cell){}


//修改密码时对密码进行检验
function CheckPwd(obj,num1,num2)
{

	var str = trim(obj.value);
  	var strLength = str.length;

	if(strLength>num2)  //先检验密码最长不能超过num2位
	{
		alert("密码不能超过"+num2+"个字符！(不包含空格)");//弹出提示对话框
		obj.value="";//清除当前值
		obj.focus();//将输入鼠标置于输入框中
		return false;
	}
	else if(strLength<num1)  //先检验密码最长不能少于num1位
	{
		alert("密码不能少于"+num1+"个字符！(不包含空格)");//弹出提示对话框
		obj.value="";//清除当前值
		obj.focus();//将输入鼠标置于输入框中
		return false;
	}
	else if(/^[\da-z]+$/i.test(str)) { //数字或者字母

		if(/^[\d]+$/i.test(str)) { //全部是数字
			alert("密码必须为字符和数字的组合！");
			return false;
		}
		else if(/^[\a-z]+$/i.test(str)) { //全部是字母
			alert("密码必须为字符和数字的组合！");
			return false;
		}
		else {
			return true;
		}
	}
    else{
	    alert("密码含有非法字符！");
	    return false;
	}
}

/**
 * 检查一个数字是否是在规定范围之内
 * @param num 待检查的数字
   maxRange 最大值
   minRange 最小值
   * @return 有效数字返回true
          无效数字返回false
          */
function checkNumber(num,minVar,maxVar)
{
    num = trim(num);
    if(num == '')
    {
        return true;
    }
    if(isNaN(num))
    {
        return false;
    }
    if(maxVar == 'p' && minVar == 'p')
    {
        return true;
    }

    if (minVar != 'p')
    {
        if (num < minVar)
        {
            return false;
        }
    }
    if (maxVar !='p')
    {
        if (num > maxVar)
        {
            return false;
        }
    }
    return true;
}


/**
 * 检查工号输入是否有效
 * @param workerNo 待检查的工号
 * @return 有效工号返回true
          无效工号返回false
          */
function checkWorkerNo(workerNo)
{
    if(workerNo == '')
    {
        return true;
    }

    workerNo = trim(workerNo);
    if(workerNo.charAt(0) == 0)
    {
        return false;
    }

    if(workerNo == '')
    {
        return true;
    }

    if(isNaN(workerNo))
    {
        return false;
    }
    if(workerNo>99999 || workerNo <1)
    {
        return false;
    }
    return true;
}

/**
 * 姓名输入的有效性检查函数
 * @param name 待检查的姓名
 * @return 有效姓名返回ture
 *         无效姓名返回false
 */
function checkNameInput(name)
{
    if(name == '')
    {
        return true;
    }
    name = trim(name);

    for(var i = 0;i < name.length;i++)
    {
        if(name.charAt(i) == '\'' || name.charAt(i) == '&'
           || name.charAt(i) == '|' || name.charAt(i) == '?'
           || name.charAt(i) == '*'  || name.charAt(i) == ' '
           || name.charAt(i) == '('  || name.charAt(i) == ')'
           || !isNaN(name.charAt(i))    )
        //￥尚不能确定哪些字符会在sql语句中造成不良影响
    {
            return false;
        }
    }
    return true;
}

/**
 * 校验字符串中是否含有非法字符
 * @param name 待检查的字符串
 * @return 无非法字符返回ture
 *               有非法字符返回false
 */
function isValidStr(name)
{
    if(name == '')
    {
        return true;
    }
    name = trim(name);

    for(var i = 0;i < name.length;i++)
    {
        if(name.charAt(i) == '\'' || name.charAt(i) == '&'
           || name.charAt(i) == '\\' || name.charAt(i) == '?'
           || name.charAt(i) == '*'  || name.charAt(i) == '/'
           || name.charAt(i) == '"'  || name.charAt(i) == '~'  )
        //￥尚不能确定哪些字符会在sql语句中造成不良影响
    {
            return false;
        }
    }
    return true;
}

//检查输入的电话号码有效性
function checkCallNo(num)
{
    num = trim(num);
    if(num == '')
    {
        return true;
    }
    for(var i = 0;i < num.length; i++)
    {

        if(isNaN(num.charAt(i)) && num.charAt(i) != '-')
        {
            return false ;
        }
    }
    return true;
}

/**
 * 提交表单函数
 * @param form 将要提交的表单
 */
function submitForm(f)
{
    var errorMeg = ""

    /** VerifyInput()函数在使用SubmitForm（）函数的页面要有定义*/
    errorMeg = VerifyInput();
    if(errorMeg == "")
    {
        f.submit();
    }
    else
    {
        alert(errorMeg);
    }
    return ;
}

/**
 * 检查两个输入框的值是否相同,如果不同则弹出错误信息,并设置对应的输入焦点为第一个参数代表的对象
 * @param num 待检查的输入框对象
 * @return 相同返回true
          不同返回false
          */
function checkEqualsWithAlert(inputObj1,inputObj2)
{
    if(inputObj1.value != inputObj2.value)
    {
        alert(inputObj1.desc+"的输入和"+inputObj2.desc+"的输入不同");
        inputObj1.focus();
        return false;
    }

    return true;
}

/**
 * 检查一个值是否是工号,并弹出对应的错误信息，设置对应的输入焦点
 * @param num 待检查的输入框对象
 * @return 有效工号返回true
          无效工号返回false
 */
function CheckWorkNoWithAlert(workNoObj)
{
    workNo = trim(workNoObj.value);

    if(workNo == "")
    {
        alert("工号输入不能为空");
        workNoObj.focus();
        return false;
    }

    if(workNo.charAt(0) == '0')
    {
        alert("工号不能以0开头");
        workNoObj.focus();
        return false;
    }

    if(isNaN(workNo))
    {
        alert("工号必须为1－9999的数字");
        workNoObj.focus();
        return false;
    }

    if(workNo>9999 || workNo <1)
    {
        alert("工号必须为1－9999的数字");
        workNoObj.focus();
        return false;
    }

    return true;
}

function compareTimeT(beginTime,endTime)
{
    if(beginTime == "" || endTime == "")
    {
        return true ;
    }
    var bDate,bHour,bMin,eDate,eHour,eMin ;
    var array0 = beginTime.split(" ")[0] ;
    var array1 = beginTime.split(" ")[1] ;
    var array2 = endTime.split(" ")[0] ;
    var array3 = endTime.split(" ")[1] ;


    bDate = array0 ;
    bHour = array1.split(":")[0] ;
    bMin = array1.split(":")[1] ;

    eDate = array2 ;
    eHour = array3.split(":")[0] ;
    eMin = array3.split(":")[1] ;

    var bDateArray = bDate.split("-");
    var eDateArray = eDate.split("-");
    if(eDate == '' || bDate == '')
    {
        return true ;
    }

    if(bHour == '')
    {
        bHour = 0;
    }

    if(bMin == '')
    {
        bMin = 0;
    }

    if(eHour == '')
    {
        eHour = 0;
    }

    if(eMin == '')
    {
        eMin = 0;
    }


    //bDateArray[0] = parseInt(bDateArray[0]) ;
    //bDateArray[1] = parseInt(bDateArray[1]) ;
    //bDateArray[2] = parseInt(bDateArray[2]) ;
    //eDateArray[0] = parseInt(eDateArray[0]) ;
    //eDateArray[1] = parseInt(eDateArray[1]) ;
    //eDateArray[2] = parseInt(eDateArray[2]) ;

//alert(bDateArray[0]+"***"+bDateArray[1]+"***"+bDateArray[2]+"***"+eDateArray[0]+"***"+eDateArray[1]+"***"+eDateArray[2]+"***");
    if(bDateArray[0] < eDateArray[0])
    {
        return true ;
    }
    if(bDateArray[0] > eDateArray[0])
    {
        return false ;
    }

    if(bDateArray[1] < eDateArray[1])
    {
        return true ;
    }
    if(bDateArray[1] > eDateArray[1])
    {
        return false ;

    }
    if(bDateArray[2] < eDateArray[2])
    {
        return true ;
    }
    if(bDateArray[2] > eDateArray[2])
    {
        return false ;
    }

    if(bHour < eHour)
    {
        return true ;
    }
    if(bHour > eHour)
    {
        return false ;
    }

    if(bMin < eMin)
    {
        return true ;
    }
    if(bMin > eMin)
    {
        return false ;
    }
    return true;
}

/**
 * 比较开始时间是否小于结束时间
 * @param bDate:开始日期
 *        bHour:开始小时
 *        bMin:开始分钟
 *        eDate:结束日期
 *        eHour:结束小时
 *        eMin:结束
 * @return ture:开始时间小于等于结束时间
 *         false:开始时间大于结束时间
 */

function CompareTime(bDate,bHour,bMin,eDate,eHour,eMin)
{
    var bDateArray = bDate.split("-");
    var eDateArray = eDate.split("-");
    if(eDate == '' || bDate == '')
    {
        return true ;
    }

    if(bHour == '')
    {
        bHour = 0;
    }

    if(bMin == '')
    {
        bMin = 0;
    }

    if(eHour == '')
    {
        eHour = 0;
    }

    if(eMin == '')
    {
        eMin = 0;
    }


    //bDateArray[0] = parseInt(bDateArray[0]) ;
    //bDateArray[1] = parseInt(bDateArray[1]) ;
    //bDateArray[2] = parseInt(bDateArray[2]) ;
    //eDateArray[0] = parseInt(eDateArray[0]) ;
    //eDateArray[1] = parseInt(eDateArray[1]) ;
    //eDateArray[2] = parseInt(eDateArray[2]) ;

    if(bDateArray[0] < eDateArray[0])
    {
        return true ;
    }
    if(bDateArray[0] > eDateArray[0])
    {
        ;
        return false ;
    }

    if(bDateArray[1] < eDateArray[1])
    {
        return true ;
    }
    if(bDateArray[1] > eDateArray[1])
    {
        return false ;

    }
    if(bDateArray[2] < eDateArray[2])
    {
        return true ;
    }
    if(bDateArray[2] > eDateArray[2])
    {
        return false ;
    }

    if(bHour < eHour)
    {
        return true ;
    }
    if(bHour > eHour)
    {
        return false ;
    }

    if(bMin < eMin)
    {
        return true ;
    }
    if(bMin > eMin)
    {
        return false ;
    }
    return true;
}

/**
 * 将时间数据封装为一个字符串
 * @param：date 日期
 *         hour  小时
 *         min  分钟
 * @return:  处理后的字符串
 */
function timeToString(date,hour,min)
{
    if(hour == '')
    {
        hour = "00";
        min = "00";
    }
    if(min == '')
    {
        min = "00";
    }
    var str = "";
    str += date + " ";
    str += hour + ":";
    str += min + ":";
    str += "00";
    return str;

}

/**
 * 去掉变量首尾的空格
 * @param num 待处理变量
 * @return 处理后变量
 */

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

/**
 * 打开定制页面窗口
 * @author 宋世栋
 */
function openDZWindow(wurl,wname)
{
    //var wx=790;
    //var wy=540;
    var x = window.screen.availWidth;
    var y = window.screen.availHeight;
    //var wtop =(x-wx)/2-4;
    //var wleft =(y-wy)/2-14;
    //alert(wtop);
    //alert(wleft);
    //window.open(wurl,wname,'left='+wleft+',top='+wtop+',toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=yes,width='+wx+',height='+wy+';,resizable=true');
    window.open(wurl,wname,'left='+0+',top='+0+',toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=yes,width='+x+',height='+(y-20)+';,resizable=true');
}

function openMonitorWindow(wurl,wname)
{
    var wx=620;
    var wy=180;
    var x = window.screen.availWidth;
    var y = window.screen.availHeight;
    var wtop =(x-wx)/2;
    var wleft =(y-wy)/2-54;
    //alert(wtop);
    //alert(wleft);
    window.open(wurl,wname,'left='+wleft+',top='+wtop+',toolbar=no,location=no,status=no,menubar=no,scrollbars=auto,resizable=no,width='+wx+',height='+wy+';,resizable=true');
}
function openPreview(wurl,wname)
{
    var wx=580;
    var wy=270;
    var x = window.screen.availWidth;
    var y = window.screen.availHeight;
    var wtop =(x-wx)/2-4;
    var wleft =(y-wy)/2-14;
    //alert(wtop);
    //alert(wleft);
    window.open(wurl,wname,'left='+wleft+',top='+wtop+',toolbar=no,location=no,status=no,menubar=no,scrollbars=no,resizable=no,width='+wx+',height='+wy+';,resizable=false');
}

function isPhoneNum(str)
{
    //检查电话号码是否满足0755(3位或者4位)-12345678(7位或者8位)格式
    //var re1 = /^\s*(\d*[-－]?\d*)?\d*\s*$/;
    //检查电话号码是否手机号13*
    //var re2 = /^\s*0?13\d{9}\s*$/
    //最新的159开头的手机号码
    //var re3 = /^\s*0?159\d{8}\s*$/

    // 检查没有区号的固定电话
    var re1 = /^\d{7,8}$/;
    // 检查有区号的固定电话
    var re2= /^0\d{2,3}-?\d{7,8}$/;
    // 检查手机号码是否手机号13*
    var re3=/^0?13\d{9}$/;
    // 检查最新的159开头的手机号码
    var re4=/^0?159\d{8}$/

    return re1.test(str)||re2.test(str)||re3.test(str)||re4.test(str);
}

function isEmail(str)
{
    var re1=/^\s*\w+@\w+(\.\w+)+\s*$/;
    return re1.test(str);
}

function isEmail2(str)
{
    var re1=/^\w[A-Za-z0-9_\.]*@\w+(\.\w+)+\s*$/;
    return re1.test(str);
}

//add by Yuhongliang begin
function checkTime(timeStr)
{
    //时间格式正则表达式，格式为"yyyy-MM-dd hh:mm:ss"
    var re = /^\d{4}-[0-1][0-9]-[0-3][0-9]\s[0-2][0-9]:[0-5][0-9]:[0-5][0-9]$/;
    return re.test(timeStr)
}

function checkDate(dateStr)
{
    //日期格式正则表达式，格式为"yyyy-MM-dd"
    var re = /^\d{4}-[0-1][0-9]-[0-3][0-9]$/;
    return re.test(dateStr)
}

/**
 * 验证字符串是否仅包含字母和数字 2005-09-17
 */
function isNumOrLetter(str)
{
    var reg = /^[A-Za-z0-9]*$/;
    return reg.test(str);
}

/**
 * 验证百分比数字格式是否正确。不能大于等于100%，
 * 小数点后最长5位数字。
 */
function isPercent(str)
{
    var reg = /^[1-9]\d?([.]\d{1,5})?[%]$/;
    return reg.test(str);
}
//add end

/////////////////////////////////e///////////////////////
// 取得当前日期,格式yyyy-mm-dd
////////////////////////////////////////////////////////
function GetCurrentDate()
{
    var Year=0;
        var Month=0;
        var Day=0;
        var CurrentDate = new Date();

        return ChangeDateToString(CurrentDate);
}

////////////////////////////////////////////////////////
// 取得当前日期,若干天前后的日期,格式yyyy-mm-dd
////////////////////////////////////////////////////////
function GetDate(day)
{
    var ms=0;
        var CurrentDate = new Date();
    ms= CurrentDate.getTime();
    ms= ms + day*24*60*60*1000;
        var NewDate = new Date(ms);
    var sNewDate;
        sNewDate= ChangeDateToString(NewDate);
        return sNewDate;
}

////////////////////////////////////////////////////////
// 将日期类型转换成字符串型格式yyyy-mm-dd
////////////////////////////////////////////////////////
function ChangeDateToString(DateIn)
{
    var Year=0;
        var Month=0;
        var Day=0;
        var CurrentDate="";

        //初始化时间
        Year      = DateIn.getYear();
        Month     = DateIn.getMonth()+1;
        Day       = DateIn.getDate();

        CurrentDate = Year + "-";
        if (Month >= 10 )
        {
            CurrentDate = CurrentDate + Month + "-";
        }
        else
        {
            CurrentDate = CurrentDate + "0" + Month + "-";
        }
        if (Day >= 10 )
        {
            CurrentDate = CurrentDate + Day ;
        }
        else
        {
            CurrentDate = CurrentDate + "0" + Day ;
        }

        return CurrentDate;
}

/**
 * 设置mFormObj表单对象所有名为boxName的chekcbox元素全部被选中或全部不被选中
 * @param mFormObj 表单对象
 * @param boxName checkbox元素的名字,字符串类型
 * @param checked true(选中)/false(不选中)
 */
function setBoxChecked(mFormObj ,boxName ,checked)
{
    if(mFormObj == null) return ;
    var count = mFormObj.elements.length ;
    for(var i=0 ;i<count ;i++)
    {
        var e = mFormObj.elements[i] ;
        if (e.type=="checkbox" && e.name == boxName)
        {
            e.checked = checked ;
        }
    }
}

/**
 *显示或隐藏部分信息
 * @param obj 在<td> 中设置的对象
 */
function dispInfo(obj)
{
  if(obj.style.display=='none')
    obj.style.display='';
  else
    obj.style.display='none';
}

/**
 *点击某个按钮先给隐藏按钮赋值再提交表单
 * @param actionID
 */
function clickBtn(actionID)
{
    document.form1.actionType.value=actionID;
    document.form1.submit();
}

/*
 *在两个列表框之间，对选择的部份进行添加
 * @param1 fromSelect 原始Selsect对象名称
 * @param2 fromSelect 目的Selsect对象名称
 */
function removeItem(fromSelect, toSelect) {
	if (fromSelect.selectedIndex != -1) {
		for (i=0; i<fromSelect.options.length; i++) {
			if (fromSelect.options(i).selected) {
				var oOption = document.createElement("OPTION");
				oOption.text = fromSelect.options[i].text;
				oOption.value= fromSelect.options[i].value;
				toSelect.add(oOption);
			}
		}
		for (i=fromSelect.options.length-1; i>=0; i--) {
			if (fromSelect.options(i).selected) {
				fromSelect.remove(i);
			}
		}
		fromSelect.blur;
		toSelect.blur;
	}
}


/*
 *在两个列表框之间，对所有项目进行添加
 * @param1 fromSelect 原始Selsect对象名称
 * @param2 fromSelect 目的Selsect对象名称
 */
function removeAllItem(fromSelect, toSelect) {
		for (i=0; i<fromSelect.options.length; i++) {

				var oOption = document.createElement("OPTION");
				oOption.text = fromSelect.options[i].text;
				oOption.value= fromSelect.options[i].value;
				toSelect.add(oOption);
		}
		for (i=fromSelect.options.length-1; i>=0; i--) {

				fromSelect.remove(i);
		}
		fromSelect.blur;
		toSelect.blur;
}

/*
 *删除列表框中所有项目
 * @param1 fromSelect 原始Selsect对象名称
 */
function removeItemAll(fromSelect) {
	for (i=fromSelect.options.length-1; i>=0; i--) {
		fromSelect.remove(i);
	}
	fromSelect.blur;
}


/*
 *判断字符串是否存在
 * @param1 allStr 以"$"为分隔符号的字符串
 * @param2 comStr 需要判断是否存在的字符串
 */
function isExist(allStr, comStr)
{
    ret = false;
	var tmpStr = allStr.split("$");
	for (j=0; j<tmpStr.length; j++)
	{
          if (tmpStr[j]==comStr)
		  {
		     ret = true;
			 break;
		  }
	}
    return ret;
}

/*
 *判断某个对象是否被选中
 * @param1 obj 对象名称
 */
function isSelected(obj)
{
	if (!obj)
	{
 		alert("对不起，没有所需的记录!");
		return false;
	}

	var selectFlag = 0;
	if(obj.length>1)
	{
		for (var i=0;i<obj.length;i++){
			if(obj[i].checked){
				selectFlag = 1;
				break;
			}
		}
	}
	else
	{
		if(obj.checked){
			selectFlag = 1;
		}
	}

	if(selectFlag == 0)
	{
		alert("对不起，请先选择所需的记录!");
		return false;
	}

	return true;
}

/*
 *判断某个对象是否被选中,并给出指定的提示信息
 * @param1 obj     对象名称
 * @param2 message 错误提示信息
 */
function isChecked(obj,message)
{
	if (!obj)
	{
 		alert(message);
		return false;
	}

	var selectFlag = 0;
	if(obj.length>1)
	{
		for (var i=0;i<obj.length;i++){
			if(obj[i].checked){
				selectFlag = 1;
				break;
			}
		}
	}
	else
	{
		if(obj.checked){
			selectFlag = 1;
		}
	}

	if(selectFlag == 0)
	{
		alert(message);
		return false;
	}

	return true;
}

/*
 *返回指定页到父框架中
 * @param2 urlStr    页面地址名称
 */
function goParent(urlStr)
{
	parent.document.window.location.href = urlStr;
}

/*
 *显示或隐藏框架左侧的功能菜单
 *
 */
function hiddenFrm()
{
    if(this.top.document.all.tags("frameset")[1]  &&  this.parent.document.all.tags("frameset")[0] && document.all("resizeBtn")){
		if (document.all("resizeBtn").value == "<<"){
			this.top.document.all.tags("frameset")[1].cols="0,*";
			this.parent.document.all.tags("frameset")[0].cols="35%,65%";
			this.resizeBtn.value = ">>";
		}
		else{
			this.top.document.all.tags("frameset")[1].cols="171,*";
			this.parent.document.all.tags("frameset")[0].cols="25%,75%";
			this.resizeBtn.value = "<<";
		}
	}
}


/*
 *设置当前步骤，使导航步骤能正确显示出来
 *
 */
function setCurrstep(currStep)
{
  var obj = top.document.all.tags("frame")[3];
  if(obj){
    obj.src ="/sysManage/guide.jsp?currStep=" + currStep;
  }
  else{
    //alert("no frame[3]");
  }
}
/************替换字符串*******************
 *输入参数：
 *         fullS  原始字符串
 *         oldS   要替换的子字符串
 *         newS   替换成新的子字符串
 *输出参数：
 *         fullS  替换后的结果字符串
 ****************************************/
function replaceString(fullS,oldS,newS)
{
  // Replaces oldS with newS in the string fullS
  for (var i=0; i<fullS.length; i++)
  {
    if (fullS.substring(i,i+oldS.length) == oldS)
    {
      fullS = fullS.substring(0,i)+newS+fullS.substring(i+oldS.length,fullS.length)
    }
  }
  return fullS;
}

/**
 * 检查一个工号(业务代表名称)列表字符串中是否有非法工号
 * @param obj 某查询控件名称
 *
 */
function checkWorkNoList(obj)
{
	var staffList = obj.value.split("$");
	for (var i=0;i<staffList.length;i++){
		if(staffList[i].length>10){
			alert("对不起，您输入的某个编号或名称过长！");
			obj.focus();
			return false;
		}
	}
	return true;
}

/**
 * 显示当前下拉列表框中被选中的项目名称
 * @param obj 某列表框控件名称
 *
 */
function getTitle(obj){

	var i = obj.selectedIndex;
	var title ="";
	if(i == -1){
		return;
	}

	if (obj.options(i).selected) {
		title = obj.options[i].text;
	}

	curTitle.innerHTML = "<font color=red>" + title + "</font>";
}

/**
 * 号段检测
 * @param workArea
 *
 */
function checkWorknoArea(worknoAreaStr)
{
  var re=/^[\w]+-[\w]/i;
  if(re.test(worknoAreaStr))
  {
   return true;
  }
  else
  {
   alert("无效的号段!\r\n正确的格式如：800-900");
   return false;
  }
}

/*
 *改变背景色
 */
var olda=null;
var oldColor=null;
function changeBK(){
  var obj=event.srcElement;
  if(!obj){
     return;
  }
  var a=obj;
  while(a.tagName!="A"){
     a=a.parentNode;
  }
  //curRowIndex = tr.rowIndex;

  if(olda&&olda!=a){
    olda.className="";
    olda.className=oldColor;
  }
  if(olda!=a){
     oldColor=a.className;
  }
  olda=a;
  a.className="table_bg";
}

/**
 *  是否为>0的数字
 */
function isNaN2(num)
{
  if(isNaN(num) || num <= 0)
  {
  	return true;
  }
  return false;
}

/**
 * 对一个包含多个EMAIL的字符串进行有效检测
 */
function checkEmailList(checkStr,splitStr){
	var checkList = checkStr.split(splitStr);
	var returnFlag = true;
	for (i=0;i<checkList.length;i++){
		if(!isEmail(checkList[i])) {
			returnFlag =false;
			break;
		}
  }
  return returnFlag;
}

/**
 * 对一个包含多个电话的字符串进行有效检测
 */
function checkPhoneList(checkStr,splitStr){
	var checkList = checkStr.split(splitStr);
	var returnFlag = true;
	for (i=0;i<checkList.length;i++){
		if(!isPhoneNum(checkList[i])) {
			returnFlag =false;
			break;
		}
  }
  return returnFlag;
}

/**
 *检测一个字符串的真实长度（一个汉字算2个字节）
 */
function length2(str)
{
  var length2 = str.length;
  for(var i=0;i<str.length;i++)
		if(str.charAt(i)>'~' || str.charAt(i)<' ')length2++;
  return length2;
}

String.prototype.length2 = function()
{
  return length2(this);
}

/**
 * 检测一个字符串的真实长度（一个汉字算2个字节）
 */
function getLength(str)
{
    var length2 = str.length;
    for(var i = 0; i < str.length; i++)
    {
		if(str.charAt(i)>'~' || str.charAt(i)<' ')
		{
		    length2++;
		}
    }
    return length2;
}

/************add by zhuhongqi****************/

/**
 * 纯字符比较时间（前提字符为yyyy-MM-dd）
 */
function compareDate(dateStr1, dateStr2)
{
    //如果有一个时间不存在则认为正确
    if (trim(dateStr1) == "" || trim(dateStr2) == "")
    {
        return true;
    }
    if (trim(dateStr1) < trim(dateStr2))
    {
        return true;
    }

    return false;
}



//验证输入的名字是否合法(包括中文，中文的符号不能去除)
function isUnlawfulChar(str)
{
    var reg = /^[^`~@#\$%\^&\*\(\)=\!\+\\\/\|<>\?;\:\.'"\{\}\[\]　, ]*$/;
    return reg.test(str);
}

//验证输入的名字是否合法(允许输入“()”包括中文，中文的符号不能去除)
function isUnlawfulCharForSuit(str)
{
    var reg = /^[^`~@#\$%\^&\*=\!\+\\\/\|<>\?;\:\.'"\{\}\[\]　, ]*$/;
    return reg.test(str);
}

/**
 * 验证字符串是否仅数字
 */
function isNumber(str)
{
    var reg = /^[0-9]+$/;
    var reg1 = /^-{1}[0-9]+$/;
    return reg.test(str) || reg1.test(str);
}

//校验是否为网页格式
function isWebFormat(str)
{
   if (trim(str) == "")
   {
       return true;
   }
   var reg = /^http:\/\/\.*/;
   var reg1 = /^https:\/\/\.*/;
   return (reg.test(str) || (reg1.test(str)));
}

//获得当前年月日 yyyy-MM-dd
function getCurrentStringDate()
{
	var crtDate = new Date();
    var tmpMonth = crtDate.getMonth() + 1;
    var tmpDay = crtDate.getDate();
    if  (tmpMonth < 10)
    {
        tmpMonth = '0' + tmpMonth;
    }
    if (tmpDay < 10)
    {
        tmpDay = '0' + tmpDay;
    }

    return crtDate.getFullYear() + '-' + tmpMonth + '-' + tmpDay;
}

