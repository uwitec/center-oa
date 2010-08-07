/**
 *
 */
package com.china.center.webportal.listener;


import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import com.china.center.webplugin.inter.WebPathListener;


/**
 * @author Administrator
 */
public class MySessionListener implements HttpSessionListener
{
    private static Set<String> sessionSet = new HashSet<String>();

    public void sessionCreated(HttpSessionEvent event)
    {}

    public void sessionDestroyed(HttpSessionEvent event)
    {
        sessionSet.remove(event.getSession().getId());

        WebPathListener.getGMap().put("SA", sessionSet.size());
    }

    public static int count()
    {
        return sessionSet.size();
    }

    /**
     * @return the sessionSet
     */
    public static Set<String> getSessionSet()
    {
        return sessionSet;
    }
}
