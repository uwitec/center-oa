/**
 *
 */
package com.china.center.oa.util;


import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;


/**
 * @author Administrator
 */
public class MySessionListener implements HttpSessionListener
{
    public static Set<String> sessionSet = new HashSet<String>();

    public void sessionCreated(HttpSessionEvent event)
    {}

    public void sessionDestroyed(HttpSessionEvent event)
    {
        sessionSet.remove(event.getSession().getId());
    }
    
    public static int count()
    {
        return sessionSet.size();
    }
}
