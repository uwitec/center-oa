/**
 * File Name: MyExceptionalManager.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2008-8-2<br>
 * Grant: open source to everybody
 */
package com.china.center.common.ex;


import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.china.center.spring.iaop.annotation.IntegrationAOPManager;


/**
 * MyExceptionalManager
 * 
 * @author ZHUZHU
 * @version 2008-8-2
 * @see
 * @since
 */
public class MyIntegrationAOPManager implements IntegrationAOPManager
{
    private final Log _logger = LogFactory.getLog(getClass());

    private String unifyException = "";

    private Class exceptionalClass = null;

    private Constructor con = null;

    /**
     * 全局静态锁
     */
    private static Map<String, Object> LOCKMAP = new HashMap<String, Object>();

    /**
     * default constructor
     */
    public MyIntegrationAOPManager()
    {
    }

    public Object getLockObject(String key)
    {
        if (key == null || "".equals(key))
        {
            return null;
        }

        Object object = LOCKMAP.get(key);

        if (object != null)
        {
            return object;
        }

        object = new Object();

        LOCKMAP.put(key, object);

        return object;
    }

    public Throwable processSensitiveException(int switchFlag, Throwable ex, Method method,
                                               Class claz)
    {
        initExceptional();

        _logger.error(ex, ex);

        if (ex.getClass() == exceptionalClass)
        {
            return ex;
        }

        try
        {
            return (Throwable)con.newInstance(ex, "系统内部错误,请重新操作");
        }
        catch (InstantiationException e)
        {
            _logger.fatal(e, e);
        }
        catch (IllegalAccessException e)
        {
            _logger.fatal(e, e);
        }
        catch (SecurityException e)
        {
            _logger.fatal(e, e);
        }
        catch (IllegalArgumentException e)
        {
            _logger.fatal(e, e);
        }
        catch (InvocationTargetException e)
        {
            _logger.fatal(e, e);
        }

        return ex;
    }

    public void setUnifyException(String unifyException)
    {
        this.unifyException = unifyException;

        initExceptional();
    }

    public void initExceptional()
    {
        if (exceptionalClass == null)
        {
            try
            {
                exceptionalClass = Class.forName(unifyException);

                con = exceptionalClass.getDeclaredConstructor(Throwable.class, String.class);
            }
            catch (ClassNotFoundException e)
            {
                _logger.fatal(e, e);
            }
            catch (SecurityException e)
            {
                _logger.fatal(e, e);
            }
            catch (NoSuchMethodException e)
            {
                _logger.fatal(e, e);
            }
        }
    }

    /**
     * @return the unifyException
     */
    public String getUnifyException()
    {
        return unifyException;
    }

    public void enterMethod(int arg0, MethodInvocation arg1, Method arg2, Class arg3)
    {

    }

    public void leaveMethod(int arg0, MethodInvocation arg1, Object arg2, Method arg3, Class arg4)
    {

    }
}
