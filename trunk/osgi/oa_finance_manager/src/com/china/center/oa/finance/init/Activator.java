package com.china.center.oa.finance.init;


import java.util.LinkedList;
import java.util.List;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import com.china.center.common.taglib.DefinedCommon;
import com.china.center.oa.finance.constant.FinanceConstant;


public class Activator implements BundleActivator
{
    private List<Class> parserClass = new LinkedList<Class>()
    {
        {
            add(FinanceConstant.class);
        }
    };

    public void start(BundleContext context)
        throws Exception
    {
        for (Class each : parserClass)
        {
            DefinedCommon.addDefinedClass(each);
        }
    }

    public void stop(BundleContext context)
        throws Exception
    {
        for (Class each : parserClass)
        {
            DefinedCommon.reomoveConstant(each);
        }
    }

}
