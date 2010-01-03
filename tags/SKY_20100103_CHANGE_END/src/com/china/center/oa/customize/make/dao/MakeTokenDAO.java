/**
 * File Name: MakeTokenDAO.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: ZHUACHEN<br>
 * CreateTime: 2009-10-11<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.customize.make.dao;


import java.util.List;

import net.sourceforge.sannotations.annotation.Bean;

import com.china.center.jdbc.inter.impl.BaseDAO2;
import com.china.center.oa.customize.make.bean.MakeTokenBean;


/**
 * MakeTokenDAO
 * 
 * @author ZHUZHU
 * @version 2009-10-11
 * @see MakeTokenDAO
 * @since 1.0
 */
@Bean(name = "makeTokenDAO")
public class MakeTokenDAO extends BaseDAO2<MakeTokenBean, MakeTokenBean>
{
    /**
     * queryHistoryToken
     * 
     * @param status
     * @return
     */
    public List<MakeTokenBean> queryHistoryToken(int status)
    {
        return this.queryEntityBeansByCondition("where id < ? order by id", status);
    }

    public List<MakeTokenBean> querySelfHistoryToken(int status)
    {
        return this.queryEntityBeansByCondition("where id < ? and id in (1, 2, 13, 14)", status);
    }
}
