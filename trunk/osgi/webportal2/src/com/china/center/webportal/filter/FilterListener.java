/**
 * File Name: FilterListener.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2010-7-3<br>
 * Grant: open source to everybody
 */
package com.china.center.webportal.filter;


import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import com.center.china.osgi.publics.ParentListener;


/**
 * FilterListener
 * 
 * @author ZHUZHU
 * @version 2010-7-3
 * @see FilterListener
 * @since 1.0
 */
public interface FilterListener extends ParentListener
{
    /**
     * onDoFilterAfterCheckUser
     * 
     * @param req
     * @param resp
     * @param chain
     * @return boolean(true就是直接在onDoFilterAfterCheckUser里面return)(false就是继续向下下一个onDoFilterAfterCheckUser)
     */
    boolean onDoFilterAfterCheckUser(ServletRequest req, ServletResponse resp, FilterChain chain)
        throws ServletException, IOException;
}
