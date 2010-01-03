/**
 * 
 */
package com.china.center.tools;

;

/**
 * 〈一句话功能简述〉
 * 
 * @author zhuzhu
 * @version 2007-7-30
 * @see ReflectException
 * @since
 */

public class ReflectException extends Exception
{
    /**
     * Constructs a <code>NoSuchMethodException</code> without a detail message.
     */
    public ReflectException()
    {
        super();
    }

    /**
     * Constructs a <code>NoSuchMethodException</code> with a detail message.
     * 
     * @param s
     *            the detail message.
     */
    public ReflectException(String s)
    {
        super(s);
    }

    public ReflectException(Throwable cause)
    {
        super(cause);
    }
}
