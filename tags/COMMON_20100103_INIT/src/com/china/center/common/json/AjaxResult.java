/**
 * File Name: AjaxResult.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2008-11-12<br>
 * Grant: open source to everybody
 */
package com.china.center.common.json;

import java.io.Serializable;
import java.util.Formatter;

/**
 * AjaxResult
 * 
 * @author zhuzhu
 * @version 2008-11-12
 * @see AjaxResult
 * @since 1.0
 */
public class AjaxResult implements Serializable
{
    private int ret = 0;
    
    private Object msg = null;

    /**
     * default constructor
     */
    public AjaxResult()
    {}

    /**
     * @return the ret
     */
    public int getRet()
    {
        return ret;
    }

    /**
     * @param ret the ret to set
     */
    public void setRet(int ret)
    {
        this.ret = ret;
    }
    
    public void setError()
    {
        this.ret = 1;
    }
    
    public void setError(Object msg)
    {
        this.ret = 1;
        
        this.msg = msg;
    }
    
    public void setError(String msg, Object... par)
    {
        this.ret = 1;
        
        Formatter formatter = new Formatter();
        
        this.msg = formatter.format(msg, par).toString();
    }
    
    public void setSuccess()
    {
        this.ret = 0;
    }
    
    public void setSuccess(Object msg)
    {
        this.ret = 0;
        
        this.msg = msg;
    }
    
    public void setSuccess(String msg, Object... par)
    {
        this.ret = 0;
        
        Formatter formatter = new Formatter();
        
        this.msg = formatter.format(msg, par).toString();
    }

    /**
     * @return the msg
     */
    public Object getMsg()
    {
        return msg;
    }

    /**
     * @param msg the msg to set
     */
    public void setMsg(Object msg)
    {
        this.msg = msg;
    }
}
