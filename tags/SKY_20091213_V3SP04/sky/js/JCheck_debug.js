/* depend on public_resources.js*/
/*
 * 自动提交
 *  message 提示信息
 *  form(缺醒为第一个form)
 *  fun 回调函数
 */
function submit(message, form, fun)
{
    if (arguments[1] == undefined)
    {
        form = document.forms[0];
    }
    else
    {
        form = arguments[1];
    }

    //自动校验
    if (formCheck(form, fun))
    {
        submitC(form, message);
    }
}

function JCheck()
{
}

function isNone(obj)
{
    if (isNull(obj))
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

function isNull(obj)
{
    if (obj == undefined || obj == null)
    {
        return true;
    }

    return false;
}

function trim(num)
{
    if (isNone(num))
    {
        return '';
    }

    while(true)
    {
        var cI = num.charAt(0) ;
        if(cI != " ")
        {
            break ;
        }
        else
        {
            num = num.substring(1 , num.length) ;
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
            num = num.substring(0 , num.length - 1) ;
        }
    }
    return num ;
}

/* *
 * 检测一个字符串的真实长度（汉字算2个字节）
 */
function getLengthInCheck(str)
{
    var length2 = str.length;
    for(var i = 0; i < str.length; i ++ )
    {
        if(str.charAt(i) > '~' || str.charAt(i) < ' ')
        {
            length2 ++ ;
        }
    }

    return length2;
}

function getNowDateInCheck()
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

function checkDateInCheck(dateStr)
{
    // 日期格式正则表达式，格式为"yyyy-MM-dd"
    var re = /^\d{4}-[0-1][0-9]-[0-3][0-9]$/;
    return re.test(dateStr)
}


JCheck.prototype = (
{
    // 内部value对象
    innerObj : null,

    // 是否alert信息
    show : true,

    index : 'value',

    bingFun : null,

    head : 'head',

    // getvalue
    getValue :
    function()
    {
        if (this.innerObj.type.toLowerCase().indexOf('select') != - 1)
        {
            if (this.innerObj.selectedIndex == -1)
            {
                return '';
            }

            return getAttribute(this.innerObj.options[this.innerObj.selectedIndex], this.index);
        }

        if (this.innerObj.type.toLowerCase() == 'radio' || this.innerObj.type.toLowerCase() == 'checkbox')
        {
            var obj = document.getElementsByName(this.innerObj.name);
            for (var i = 0; i < obj.length; i ++ )
            {
                if (obj[i].checked)
                {
                    return getAttribute(obj[i], this.index);
                }
            }

            return "";
        }

        if (isNull(getAttribute(this.innerObj, this.index)))
        {
            return null;
        }
        else
        {
            return getAttribute(this.innerObj, this.index);
        }
    }
    ,

    // getHead
    getHead :
    function()
    {
        var he = getAttribute(this.innerObj, this.head);

        if (isNull(he))
        {
            return '';
        }
        else
        {
            return he;
        }
    }
    ,

    innerCheck :
    function()
    {
        // 如果value没有值
        if (isNull(this.getValue()))
        {
            this.alert(Res._r['INNERCHECK_HINT']);
            return false;
        }

        return true;
    }
    ,

    alert :
    function(mes)
    {
        this.showMessage(mes);
    }
    ,

    // alert默认或者自定义的提示
    showMessage :
    function(defaultMessage, args)
    {
        if (this.show)
        if (isNull(args) || isNull(args[0]) || args[0] == true)
        {
            var mee = getAttribute(this.innerObj, 'message');
            if ( ! isNone(mee))
            {
                alert(mee);
            }
            else
            {
                if ( ! isNone(defaultMessage))
                {
                    alert(this.getHead() + ' ' + defaultMessage);
                }
            }
        }
    }
    ,

    // 是否为空
    notNone :
    function()
    {
        // 去空后
        if (trim(this.getValue()) == '')
        {
            var defaultMessage = Res._r['NOT_NULL'];

            this.showMessage(defaultMessage, arguments);

            return false;
        }
        else
        {
            return true;
        }
    }
    ,

    // 正则校验
    isReg :
    function(reg, defaultMessage)
    {
        var value = trim(this.getValue());

        var arr = [true];

        if ( ! isNull(arguments[2]))
        {
            arr[0] = arguments[2];
        }

        if ( ! reg.test(value))
        {
            this.showMessage(defaultMessage, arr);

            return false;
        }
        else
        {
            return true;
        }
    }
    ,

    // 仅仅是数字(空算正确)
    isNumber :
    function()
    {
        var reg = /^[0-9]*$/;

        var defaultMessage = Res._r['ONLY_NUMBER'];

        return this.isReg(reg, defaultMessage);
    }
    ,

    // 仅仅是数字(空算错误)
    isOnlyNumber :
    function()
    {
        var reg = /^[0-9]+$/;

        var defaultMessage = Res._r['ONLY_NUMBER'];

        return this.isReg(reg, defaultMessage);
    }
    ,

     // 仅仅是数字(空算错误)
    isInt :
    function()
    {
        var reg = /^[1-9][0-9]*$/;

        var defaultMessage = Res._r['ONLY_NATURAL_NUMBER'];

        return this.isReg(reg, defaultMessage);
    }
    ,

    // 仅仅是数字(空算错误, 可以有负号)
    isMathNumber :
    function()
    {
        var reg = /^[0-9]+$/;
        var reg1 = /^-{1}[0-9]+$/;

        var defaultMessage = Res._r['ONLY_NUMBER'];

        if (this.isReg(reg, null, false) || this.isReg(reg1, null, false))
        {
            return true;
        }
        else
        {
            this.showMessage(defaultMessage, arguments);
            return false;
        }
    }
    ,

    maxLength :
    function(parmters)
    {
        var length;
        if (typeof parmters == 'number')
        {
            length = parmters;
        }
        else
        {
            this.alert(Res._r['PARAMETER_NOT_NUMBER']);
            return false;
        }

        if (getLengthInCheck(this.getValue()) > length)
        {
            var defaultMessage = Res._r['LENGTH_ALERT_OVER'] + length;

            this.showMessage(defaultMessage);

            return false;
        }
        else
        {
            return true;
        }
    }
    ,

    isChecked :
    function()
    {
        var flag = false;
        if (this.innerObj.type.toLowerCase() == 'radio' || this.innerObj.type.toLowerCase() == 'checkbox')
        {
            var obj = document.getElementsByName(this.innerObj.name);
            for (var i = 0; i < obj.length; i ++ )
            {
                if (obj[i].checked)
                {
                    flag = true;
                }
            }
        }

        if ( ! flag)
        {
            var defaultMessage = Res._r['SELECT_NOT_NULL'];

            this.showMessage(defaultMessage, arguments);

            return false;
        }
        else
        {
            return true;
        }
    }
    ,

    isCommonChar :
    function()
    {
        var regText = /^[^`~#\$\^&\*=\!\+\\\/\|<>\?;\:'"\{\}\[\]　,]*$/;
        if ( ! isNone(arguments[0]))
        {
            if (typeof arguments[0] == 'string')
            {
                regText = arguments[0];
            }
        }

        var defaultMessage = Res._r['CAN_NOT_CONTAIN'] + ',./<>?\'; : ~ ! `#$ % ^ & * () = + \\ | {}[]"';

        return this.isReg(regText, defaultMessage);
    }
    ,

    // 验证字符串是否仅包含字母和数字
    isNumberOrLetter :
    function()
    {
        var regText = /^[A-Za-z0-9]*$/;

        var defaultMessage = Res._r['ONLY_NUMBER_OR_LETTER'];

        return this.isReg(regText, defaultMessage);
    }
    ,

    // 验证字符串是否仅包含字母
    isLetter :
    function()
    {
        var regText = /^[A-Za-z]*$/;

        var defaultMessage = Res._r['ONLY_LETTER'];

        return this.isReg(regText, defaultMessage);
    }
    ,

    // 验证字符串是浮点
    isFloat :
    function()
    {
        var regText = /^(-?\d+)(\.\d+)?$/;

        var defaultMessage = Res._r['ONLY_FLOAT'];

        return this.isReg(regText, defaultMessage);
    }
    ,
    
    // 验证字符串是正浮点
    isFloat3 :
    function()
    {
        var regText = /^(\d+)(\.\d+)?$/;

        var defaultMessage = Res._r['ONLY_FLOAT2'];

        return this.isReg(regText, defaultMessage);
    }
    ,
    
    // 验证字符串是浮点(空算正确)
    isFloat2 :
    function()
    {
        if (!this.notNone(false))
        {
            return true;
        }
        
        return this.isFloat();
    }
    ,

    // 验证E-Mail类型
    isEmail :
    function()
    {
        //kkk@[127.0.0.1]
        var regText1 = /(^\w{1,}\.?\w{1,})@(\[\d{1,3}\.\d{1,3}\.\d{1,3}.\d{1,3}\])$/;

        //kkk@center.com
        var regText = /(^\w{1,}\.?\w{1,})@(\w{2,}\.(\w{2,}\.)?[a-zA-Z]{2,3})$/;

        var defaultMessage = Res._r['ONLY_EMAIL'];

        var rs = this.isReg(regText) || this.isReg(regText1);

        if (rs)
        {
            return rs;
        }
        else
        {
            this.showMessage(defaultMessage, arguments);
            return false;
        }
    }
    ,

    // 验证字符串是浮点
    isUrl :
    function()
    {
        var regText = /^[a-zA-z]+:\/\/(\w+(-\w+)*)(\.(\w+(-\w+)*))*(\?\S*)?$/;

        var defaultMessage = Res._r['ONLY_URL'];

        return this.isReg(regText, defaultMessage);
    }
    ,

    // 最小长度
    minLength :
    function(min)
    {
        var length;
        if (typeof min == 'number')
        {
            length = min;
        }
        else
        {
            this.alert(Res._r['PARAMETER_NOT_NUMBER']);
            return false;
        }

        if (getLengthInCheck(this.getValue()) < length)
        {
            var defaultMessage = Res._r['LENGTH_ALERT_LESS'] + length;

            this.showMessage(defaultMessage);

            return false;
        }
        else
        {
            return true;
        }
    }
    ,

    // 不能小于当前时间
    cnow :
    function(cc, dir, format)
    {
        var cint = '==';
        if ( ! isNull(cc))
        {
            cint = cc;
        }


        // 暂时不支持格式化比较
        var ff = 'yyyy-MM-dd';
        var current;

        // 获取时间
        var src = trim(this.getValue());

        if (isNull(dir))
        {
            // 当前时间
            current = getNowDateInCheck();
        }
        else
        {
            // 指定比较时间
            current = dir;
        }

        if ( ! checkDateInCheck(src))
        {
            this.alert(src + Res._r['CNOW_ALERT_1'] + ff + Res._r['CNOW_ALERT_2']);
            return false;
        }

        if ( ! checkDateInCheck(current))
        {
            this.alert(current + Res._r['CNOW_ALERT_1'] + ff + Res._r['CNOW_ALERT_2']);
            return false;
        }

        var cresult;
        var selfMessage;

        // - 2小于等于
        if (cint == '<=' || cint == '12')
        {
            cresult = (src <= current);
            selfMessage = Res._r['CNOW_ALERT_OVER'] + current;
        }

        if (cint == '=='  || cint == '22')
        {
            cresult = (src == current);
            selfMessage = Res._r['CNOW_ALERT_EQUAL'] + current;
        }

        if (cint == '<' || cint == '10')
        {
            cresult = (src < current);
            selfMessage = Res._r['CNOW_ALERT_OVER_EQUAL'] + current;
        }

        if (cint == '>=' || cint == '32')
        {
            cresult = (src >= current);
            selfMessage = Res._r['CNOW_ALERT_LESS']  + current;
        }

        if (cint == '>' || cint == '30')
        {
            cresult = (src > current);
            selfMessage = Res._r['CNOW_ALERT_LESS_EQUAL'] + current;
        }


        if ( ! cresult)
        {
            var defaultMessage = selfMessage;

            this.showMessage(defaultMessage);

            return false;
        }
        else
        {
            return true;
        }
    }
    ,

    noEquals :
    function(str)
    {
        if (this.getValue() == str)
        {
            var defaultMessage = Res._r['VALUE_NOT_EQUAL'] + str;

            this.showMessage(defaultMessage);

            return false;
        }
        else
        {
            return true;
        }
    }
    ,

    equals :
    function(str)
    {
        if (this.getValue() != str)
        {
            var defaultMessage = Res._r['VALUE_EQUAL'] + str;

            this.showMessage(defaultMessage);

            return false;
        }
        else
        {
            return true;
        }
    }
    ,

    // 范围比较 >= <= 空返回true
    range :
    function(begin, end)
    {
        // 关闭提示
        if (this.notNone(false))
        {
            var defaultMessage = '';

            // 前后比较的
            if ( ! isNull(begin) && ! isNull(end))
            {
                defaultMessage = Res._r['RANGE_OVER_EQUAL'] + begin + ',' + Res._r['RANGE_LESS_EQUAL'] + end;

                var num = parseInt(this.getValue(), 10);
                if (num >= begin && num <= end)
                {
                    return true;
                }
                else
                {
                    this.showMessage(defaultMessage);
                    return false;
                }
            }

            // 前比较的
            if ( ! isNull(begin) && isNull(end))
            {
                defaultMessage = Res._r['RANGE_OVER_EQUAL'] + begin;

                var num = parseInt(this.getValue(), 10);
                if (num >= begin)
                {
                    return true;
                }
                else
                {
                    this.showMessage(defaultMessage);
                    return false;
                }
            }

            // 后比较的
            if (isNull(begin) && ! isNull(end))
            {
                defaultMessage = Res._r['RANGE_LESS_EQUAL'] + end;

                var num = parseInt(this.getValue(), 10);
                if (num <= end)
                {
                    return true;
                }
                else
                {
                    this.showMessage(defaultMessage);
                    return false;
                }
            }

            // 默认没有
            defaultMessage = Res._r['RANGE_EXCEPTION'];
            this.showMessage(defaultMessage);
            return false;

        }
        else
        {
            return true;
        }
    }
}
);

var jCheck = new JCheck();

/*
 * 按钮全部变灰
 */
function disableAllButton()
{
    var bus = document.getElementsByTagName('input');

    for (var i = 0; i < bus.length; i++)
    {
        if (bus[i].type.toLowerCase() == 'button' ||
            bus[i].type.toLowerCase() == 'submit' ||
            bus[i].type.toLowerCase() == 'reset')
        {
            bus[i].disabled = true;
        }
    }
}

/*
 * 自动提交(按钮全部变灰)
 */
function submitC(obj, message)
{
    var form;
    if (arguments[0] == undefined)
    {
        form = document.forms[0];
    }
    else
    {
        form = arguments[0];
    }

    if (isNone(arguments[1]))
    {
        disableAllButton();
        form.submit();
    }
    else
    {
        if (window.confirm(message))
        {
            disableAllButton();
            form.submit();
        }
    }
}

/**
 * 从Element里面获得Attribute(IE AND Firefox)
 */
function getAttribute(oElem, att)
{
    if (oElem == null)
    {
        return '';
    }

    if (att == 'value')
    {
        return oElem.value;
    }

    return oElem.getAttribute(att) || oElem[att];
}


/*
 * elements自动校验
 */
function eCheck()
{
    var elements = arguments[0];

    if (elements == null)
    {
        return true;
    }

    for (var i = 0; i < elements.length; i++)
    {
        var oo = elements[i];

        var attr = null;

        //filter disabled element
        if (oo.disabled)
        {
            continue;
        }

        //不是hidden，确认隐藏域要忽略
        if (oo.type != 'hidden' && oo.style.display == 'none')
        {
            continue;
        }

        attr = getAttribute(oo, 'oncheck');

        if (attr == null)
        {
            continue;
        }

        //notNull;maxLength(10)
        var methods = attr.split(';');
        var fun = null;
        var checkResult = false;
        for(var k= 0; k < methods.length; k++)
        {
            if (trim(methods[k]) == "")
            {
                continue;
            }

            var par = getFun(methods[k]);

            var rs;
            //自定义的函数
            if (par[0])
            {
                fun = this[par[2]];

                if (isNull(fun))
                {
                    alert(Res._r['ECHECK_MISS_FUNCTION'] + par[2]);
                    return false;
                }

                try
                {
                    //直接调用函数
                    rs = fun.apply(this, [oo]);
                }
                catch(e)
                {
                    alert(Res._r['ECHECK_INVOKE_ERROR'] + par[2]);
                    return false;
                }
            }
            else
            {
                //自动校验函数
                fun = jCheck[par[2]];

                if (isNull(fun))
                {
                    alert(Res._r['ECHECK_MISS_FUNCTION'] + par[2]);
                    return false;
                }

                jCheck.innerObj = oo;

                var paa = getAttribute(oo, 'parameter');
                if (!isNone(paa))
                {
                    jCheck.index = paa;
                }
                else
                {
                    jCheck.index = 'value';
                }

                var sh = getAttribute(oo, 'show');
                if (!isNone(sh))
                {
                    jCheck.show = getBoolean(sh);
                }
                else
                {
                    jCheck.show = true;
                }

                //校验check的value是否存在
                jCheck.innerCheck();

                try
                {
                    //window.exectScript只能在IE里面运行
                    eval('checkResult = jCheck.' + par[1]);
                }
                catch(e)
                {
                    alert(Res._r['ECHECK_INVOKE_ERROR'] + par[1]);
                    return false;
                }


                rs = checkResult;
            }

            if (!rs)
            {
                try
                {
                    //Element获得焦点
                    if (isFocusElement(oo))
                    {
                        oo.focus();
                        oo.select();
                    }
                }
                catch (e)
                {}

                return false;
            }
        }
    }

    return true;
}

function isFocusElement(oo)
{
    if (oo.type.toLowerCase() == 'text' || oo.type.toLowerCase() == 'textarea' || oo.type.toLowerCase() == 'select-one' )
    {
        return true;
    }

    if (oo.type.toLowerCase() == 'checkbox' || oo.type.toLowerCase() == 'radio')
    {
        return true;
    }

    return false;
}

/*
 * form表单自动校验
 */
function formCheck(form, functions)
{
    var checkForm = arguments[0];

    if (isNull(checkForm))
    {
        checkForm = document.forms[0];
    }

    var elements = checkForm.elements;

    if (!eCheck(elements))
    {
        return false;
    }

    if (!isNull(arguments[1]))
    {
        var rre = arguments[1].call(this);

        if (!rre)
        {
            return false;
        }
    }

    return true;
}

function getBoolean(str)
{
    if (str == 'false')
    {
        return false;
    }
    else
    {
        return true;
    }
}

/**
 * get function
 */
function getFun(str)
{
    var arr = new Array();

    if (str.indexOf(':') == -1)
    {
        // jCheck function
        arr[0] = false;
        arr[1] = str;
    }
    else
    {
        //self function
        arr[0] = true;
        arr[1] = str.substring(1);
    }

    //get the function name
    if (arr[1].indexOf('(') != -1)
    {
        arr[2] = arr[1].substring(0, arr[1].indexOf('('));
    }
    else
    {
        arr[2] = arr[1];
        arr[1] = arr[1] + '()';
    }

    return arr;
}