/**
 * File Name: SessionTools.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2010-7-4<br>
 * Grant: open source to everybody
 */
package com.china.center.tools;


import java.lang.reflect.Field;

import org.apache.catalina.session.StandardSession;
import org.apache.catalina.session.StandardSessionFacade;

import com.center.china.osgi.annotation.NotCommon;


/**
 * SessionTools
 * 
 * @author ZHUZHU
 * @version 2010-7-4
 * @see SessionTools
 * @since 1.0
 */
public abstract class SessionTools
{
    /**
     * 保持tomcat的session访问时间(对于其他容器无效)
     * 
     * @param session
     */
    @NotCommon
    public static void keepSessionLastAccessTime(StandardSessionFacade session)
    {
        try
        {
            long oldTime = session.getLastAccessedTime();

            Field field = StandardSessionFacade.class.getDeclaredField("session");

            boolean old1 = field.isAccessible();

            field.setAccessible(true);

            StandardSession ss = (StandardSession)field.get(session);

            Field thisAT = StandardSession.class.getDeclaredField("thisAccessedTime");

            boolean old2 = thisAT.isAccessible();

            thisAT.setAccessible(true);

            thisAT.setLong(ss, oldTime);

            // 访问限制倒回
            field.setAccessible(old1);

            thisAT.setAccessible(old2);
        }
        catch (SecurityException e)
        {
            e.printStackTrace();
        }
        catch (IllegalArgumentException e)
        {
            e.printStackTrace();
        }
        catch (IllegalAccessException e)
        {
            e.printStackTrace();
        }
        catch (Throwable e)
        {
            e.printStackTrace();
        }
    }

}
