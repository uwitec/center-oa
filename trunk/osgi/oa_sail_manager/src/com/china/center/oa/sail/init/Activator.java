package com.china.center.oa.sail.init;


import java.util.LinkedList;
import java.util.List;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import com.china.center.common.taglib.DefinedCommon;
import com.china.center.oa.sail.constanst.OutConstant;
import com.china.center.oa.sail.constanst.SailConstant;


public class Activator implements BundleActivator
{
    private List<Class> parserClass = new LinkedList<Class>()
    {
        {
            add(OutConstant.class);
            add(SailConstant.class);
        }
    };

    /*
     * (non-Javadoc)
     * 
     * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
     */
    public void start(BundleContext context)
        throws Exception
    {
        for (Class each : parserClass)
        {
            DefinedCommon.addDefinedClass(each);
        }

        DefinedCommon.addDefined("reprotType", new String[] {"<font color=blue>无回复</font>", "正常收货",
            "<font color=red>异常收货</font>"});

        DefinedCommon.addDefined("consignStatus", new String[] {"", "未通过",
            "<font color=blue><b>通过</b></font>"});
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
     */
    public void stop(BundleContext context)
        throws Exception
    {
        for (Class each : parserClass)
        {
            DefinedCommon.reomoveConstant(each);
        }

        DefinedCommon.removeDefined("reprotType");
    }

}
