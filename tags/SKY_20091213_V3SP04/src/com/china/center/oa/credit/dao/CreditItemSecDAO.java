/**
 * File Name: CreditItemSecDAO.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: ZHUACHEN<br>
 * CreateTime: 2009-10-27<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.credit.dao;


import net.sourceforge.sannotations.annotation.Bean;

import com.china.center.annosql.tools.BeanTools;
import com.china.center.jdbc.inter.impl.BaseDAO2;
import com.china.center.oa.credit.bean.CreditItemSecBean;
import com.china.center.oa.credit.vo.CreditItemSecVO;


/**
 * CreditItemSecDAO
 * 
 * @author ZHUZHU
 * @version 2009-10-27
 * @see CreditItemSecDAO
 * @since 1.0
 */
@Bean(name = "creditItemSecDAO")
public class CreditItemSecDAO extends BaseDAO2<CreditItemSecBean, CreditItemSecVO>
{
    /**
     * sumPerByPid
     * 
     * @param pid
     * @return
     */
    public double sumPerByPid(String pid)
    {
        return this.jdbcOperation.queryForDouble(BeanTools.getSumHead(claz, "per")
                                                 + "where pid = ?", pid);
    }
}
