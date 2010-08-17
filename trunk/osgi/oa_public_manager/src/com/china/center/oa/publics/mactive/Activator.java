package com.china.center.oa.publics.mactive;


import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import com.china.center.common.taglib.DefinedCommon;
import com.china.center.common.taglib.MapBean;
import com.china.center.oa.publics.constant.DutyComstant;
import com.china.center.oa.publics.constant.PublicConstant;
import com.china.center.oa.publics.constant.StafferConstant;


/**
 * Activator
 * 
 * @author ZHUZHU
 * @version 2010-6-27
 * @see Activator
 * @since 1.0
 */
public class Activator implements BundleActivator
{
    private List<Class> parserClass = new LinkedList<Class>()
    {
        {
            add(PublicConstant.class);
            add(StafferConstant.class);
            add(DutyComstant.class);
        }
    };

    public void start(BundleContext context)
        throws Exception
    {
        for (Class each : parserClass)
        {
            DefinedCommon.addDefinedClass(each);
        }

        List<MapBean> userStatus = new ArrayList<MapBean>();

        userStatus.add(new MapBean(PublicConstant.LOGIN_STATUS_COMMON, "正常"));

        userStatus.add(new MapBean(PublicConstant.LOGIN_STATUS_LOCK, "锁定"));

        DefinedCommon.addDefined("userStatus", userStatus);
    }

    public void stop(BundleContext context)
        throws Exception
    {
        for (Class each : parserClass)
        {
            DefinedCommon.reomoveConstant(each);
        }

        DefinedCommon.removeDefined("userStatus");
    }

}
