/**
 * File Name: DepartmentDAO.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: zhuAchen<br>
 * CreateTime: 2008-11-2<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.publics.dao;


import java.util.List;

import net.sourceforge.sannotations.annotation.Bean;

import com.china.center.jdbc.inter.impl.BaseDAO2;
import com.china.center.oa.publics.bean.LogBean;
import com.china.center.oa.publics.vo.LogVO;


/**
 * LogDAO
 * 
 * @author ZHUZHU
 * @version 2008-11-2
 * @see LogDAO
 * @since 1.0
 */
@Bean(name = "logDAO")
public class LogDAO extends BaseDAO2<LogBean, LogVO>
{
    /**
     * findLogBeanByFKAndPosid
     * 
     * @param fk
     * @param posid
     * @return
     */
    public LogBean findLogBeanByFKAndPosid(String fk, String posid)
    {
        List<LogBean> list = this.queryEntityBeansByCondition(
            "where fkId = ? and posid = ? order by logTime desc", fk, posid);

        if (list.isEmpty())
        {
            return null;
        }

        return list.get(0);
    }
}
