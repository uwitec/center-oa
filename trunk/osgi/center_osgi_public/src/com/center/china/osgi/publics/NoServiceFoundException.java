/**
 * File Name: NoServiceFoundException.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2011-7-1<br>
 * Grant: open source to everybody
 */
package com.center.china.osgi.publics;

/**
 * NoServiceFoundException
 * 
 * @author ZHUZHU
 * @version 2011-7-1
 * @see NoServiceFoundException
 * @since 1.0
 */
public class NoServiceFoundException extends RuntimeException
{
    /**
     * default constructor
     */
    public NoServiceFoundException(String message, Throwable cause)
    {
        super(message, cause);
    }

    /**
     * default constructor
     */
    public NoServiceFoundException(String message)
    {
        super(message);
    }

    /**
     * default constructor
     */
    public NoServiceFoundException(Throwable cause)
    {
        super(cause);
    }

}
