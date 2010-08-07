/**
 * File Name: PublicTriggerImpl.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2010-6-27<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.publics.trigger.impl;


import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.china.center.oa.publics.trigger.CommonJob;
import com.china.center.oa.publics.trigger.PublicTrigger;


/**
 * PublicTriggerImpl
 * 
 * @author ZHUZHU
 * @version 2010-6-27
 * @see PublicTriggerImpl
 * @since 1.0
 */
public class PublicTriggerImpl implements PublicTrigger
{
    /**
     * JOBMAP由于OSGI在bundle重新加载的时候对静态的域是不重新加载的(所以全局的使用static保存)
     */
    private static Map<String, CommonJob> DAYJOBMAP = new HashMap();

    private static Map<String, CommonJob> HOURJOBMAP = new HashMap();

    private final Log triggerLog = LogFactory.getLog("trigger");

    /**
     * 每天凌晨1点执行<br>
     * 事务由目的对象保证
     */
    public void everyDayCarryWithOutTransactional()
    {
        Collection<CommonJob> values = DAYJOBMAP.values();

        for (CommonJob commonJob : values)
        {
            triggerLog.info("begin excute " + commonJob.getJobName());

            try
            {
                commonJob.excuteJob();
            }
            catch (Throwable e)
            {
                triggerLog.error(e, e);
            }

            triggerLog.info("end excute " + commonJob.getJobName());
        }
    }

    /**
     * 每小时执行一次 <br>
     * 事务由目的对象保证
     */
    public void everyHourCarryWithOutTransactional()
    {
        Collection<CommonJob> values = HOURJOBMAP.values();

        for (CommonJob commonJob : values)
        {
            triggerLog.info("begin excute " + commonJob.getJobName());

            try
            {
                commonJob.excuteJob();
            }
            catch (Throwable e)
            {
                triggerLog.error(e, e);
            }

            triggerLog.info("end excute " + commonJob.getJobName());
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.china.center.oa.publics.trigger.PublicTrigger#putDayCommonJob(com.china.center.oa.publics.trigger.CommonJob)
     */
    public void putDayCommonJob(CommonJob job)
    {
        DAYJOBMAP.remove(job.getJobName());

        DAYJOBMAP.put(job.getJobName(), job);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.china.center.oa.publics.trigger.PublicTrigger#putHourCommonJob(com.china.center.oa.publics.trigger.CommonJob)
     */
    public void putHourCommonJob(CommonJob job)
    {
        HOURJOBMAP.remove(job.getJobName());

        HOURJOBMAP.put(job.getJobName(), job);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.china.center.oa.publics.trigger.PublicTrigger#removeDayCommonJob(java.lang.String)
     */
    public void removeDayCommonJob(String name)
    {
        DAYJOBMAP.remove(name);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.china.center.oa.publics.trigger.PublicTrigger#removeHourCommonJob(java.lang.String)
     */
    public void removeHourCommonJob(String name)
    {
        HOURJOBMAP.remove(name);
    }
}
