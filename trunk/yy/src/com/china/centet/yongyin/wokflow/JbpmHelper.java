/*
 * File Name: JbpmTools.java
 * CopyRight: Copyright by www.center.china
 * Description:
 * Creater: zhuAchen
 * CreateTime: 2007-12-20
 * Grant: open source to everybody
 */
package com.china.centet.yongyin.wokflow;


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import org.jbpm.bytes.ByteArray;
import org.jbpm.graph.exe.ExecutionContext;


/**
 * <ÃèÊö>
 * 
 * @author zhuzhu
 * @version 2007-12-20
 * @see
 * @since
 */
public class JbpmHelper
{
    private ExecutionContext executionContext = null;

    /**
     * default constructor
     */
    /**
     * default constructor
     */
    public JbpmHelper(ExecutionContext exc)
    {
        this.executionContext = exc;
    }

    public void setVariable(String key, Object value)
    {
        executionContext.setVariable(key, value);
    }

    public <T> T get(String key, Class<T> claz)
    {
        Object o1 = executionContext.getContextInstance().getVariable(key);

        if (o1 == null)
        {
            return null;
        }

        if (claz.isAssignableFrom(o1.getClass()))
        {
            return (T)o1;
        }

        if ( ! (o1 instanceof ByteArray))
        {
            throw new ClassCastException(claz.getName() + " cast error:" + o1.getClass().getName());
        }

        ByteArray oo = (ByteArray)o1;

        ByteArrayInputStream bin = new ByteArrayInputStream(oo.getBytes());

        ObjectInputStream ois = null;
        T newValue = null;

        try
        {
            try
            {
                ois = new ObjectInputStream(bin);
                newValue = (T)ois.readObject();
            }
            catch (IOException e)
            {}
            catch (ClassNotFoundException e)
            {}
        }
        catch (Exception e)
        {}
        finally
        {
            System.out.println("--------------------TODO------------");
            if (bin != null)
            {
                try
                {
                    bin.close();
                }
                catch (IOException e)
                {}
            }

            if (ois != null)
            {
                try
                {
                    ois.close();
                }
                catch (IOException e)
                {}
            }
        }

        return newValue;
    }

    public String getStringVariable(String key)
    {
        Object oo = executionContext.getContextInstance().getVariable(key);

        if (oo == null)
        {
            return null;
        }

        return (String)oo;
    }

    public int getIntVariable(String key)
    {
        Object oo = executionContext.getContextInstance().getVariable(key);

        if (oo == null)
        {
            return 0;
        }

        return (Integer)oo;
    }

    public boolean getBooleantVariable(String key)
    {
        Object oo = executionContext.getContextInstance().getVariable(key);

        if (oo == null)
        {
            return false;
        }

        return (Boolean)oo;
    }

    /**
     * @return the executionContext
     */
    public ExecutionContext getExecutionContext()
    {
        return executionContext;
    }

    /**
     * @param executionContext
     *            the executionContext to set
     */
    public void setExecutionContext(ExecutionContext executionContext)
    {
        this.executionContext = executionContext;
    }
}
