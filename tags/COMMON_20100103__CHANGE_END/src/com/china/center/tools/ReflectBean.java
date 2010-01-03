/**
 * 
 */
package com.china.center.tools;

/**
 * bean Ù–‘∂¡»°
 * 
 * @author zhuzhu
 * @version 2007-7-30
 * @see ReflectBean
 * @since
 */

public class ReflectBean
{
    private Object object = null;

    private Object current = null;

    public ReflectBean(Object tar)
    {
        this.object = tar;
        this.current = tar;
    }

    public ReflectBean()
    {}

    /**
     * get the current value form target object <br>
     * support[Bean Map]
     * 
     * @param name
     * @param args
     * @return ReflectBean
     * @throws ReflectException
     */
    public ReflectBean get(String name, Object[] args)
        throws ReflectException
    {
        if (current == null)
        {
            throw new ReflectException("the invoke object is null");
        }

        this.current = InnerReflect.get(this.current, name, args);

        return this;
    }

    /**
     * get the current value form target object <br>
     * support[Bean Map]
     * 
     * @param name
     * @param args
     * @return ReflectBean
     * @throws ReflectException
     */
    public ReflectBean get(String name)
        throws ReflectException
    {
        return get(name, null);
    }

    /**
     * get the lastest value<br>
     * 
     * @return Object
     */
    public Object complite()
    {
        Object result = this.current;
        this.current = this.object;
        return result;
    }

    /**
     * get the current value form target object(user index) <br>
     * support[Collection List Set Array]
     * 
     * @param index
     * @return ReflectBean
     * @throws ReflectException
     */
    public ReflectBean get(int index)
        throws ReflectException
    {
        if (current == null)
        {
            throw new ReflectException("the invoke object is null");
        }

        this.current = InnerReflect.get(this.current, index);

        return this;
    }

    public Object getObject()
    {
        return object;
    }

    public void setObject(Object object)
    {
        this.object = object;
        this.current = object;
    }

    public Object getCurrent()
    {
        return this.current;
    }

}
