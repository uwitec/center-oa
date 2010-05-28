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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.china.center.spring.ex.annotation.ExceptionalManager;

import com.china.center.common.MYException;


/**
 * MyExceptionalManager
 * 
 * @author ZHUZHU
 * @version 2008-8-2
 * @see
 * @since
 */
public class MyExceptionalManager implements ExceptionalManager
{
    private final Log _logger = LogFactory.getLog(getClass());

    private String unifyException = "";

    private Class exceptionalClass = null;

    private Constructor con = null;

    /**
     * default constructor
     */
    public MyExceptionalManager()
    {}

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
            MYException oo = (MYException)con.newInstance(ex);

            oo.setErrorContent("系统内部错误,请重新操作");

            return oo;
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

                con = exceptionalClass.getDeclaredConstructor(Throwable.class);
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
}
