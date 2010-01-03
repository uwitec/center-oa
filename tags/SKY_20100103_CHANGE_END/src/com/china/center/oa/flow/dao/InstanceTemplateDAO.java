/**
 * File Name: InstanceTemplateDAO.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2009-5-3<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.flow.dao;


import java.util.List;

import net.sourceforge.sannotations.annotation.Bean;

import com.china.center.jdbc.inter.impl.BaseDAO2;
import com.china.center.oa.flow.bean.InstanceTemplateBean;


/**
 * InstanceTemplateDAO
 * 
 * @author zhuzhu
 * @version 2009-5-3
 * @see InstanceTemplateDAO
 * @since 1.0
 */
@Bean(name = "instanceTemplateDAO")
public class InstanceTemplateDAO extends BaseDAO2<InstanceTemplateBean, InstanceTemplateBean>
{
    /**
     * queryByDuration
     * 
     * @param beginTime
     * @param endTime
     * @return
     */
    public List<InstanceTemplateBean> queryByDuration(String beginTime, String endTime)
    {
        return this.queryEntityBeansByCondition("where logTime >= ? and logTime <= ?", beginTime,
            endTime);
    }
}
