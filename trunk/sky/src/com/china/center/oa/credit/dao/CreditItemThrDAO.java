/**
 * File Name: CreditItemThrDAO.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: ZHUACHEN<br>
 * CreateTime: 2009-10-27<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.credit.dao;


import net.sourceforge.sannotations.annotation.Bean;

import com.china.center.jdbc.inter.impl.BaseDAO2;
import com.china.center.oa.credit.bean.CreditItemThrBean;
import com.china.center.oa.credit.vo.CreditItemThrVO;


/**
 * CreditItemThrDAO
 * 
 * @author ZHUZHU
 * @version 2009-10-27
 * @see CreditItemThrDAO
 * @since 1.0
 */
@Bean(name = "creditItemThrDAO")
public class CreditItemThrDAO extends BaseDAO2<CreditItemThrBean, CreditItemThrVO>
{
    /**
     * countByPidAndIndexPos
     * 
     * @param pid
     * @param indexPos
     * @return
     */
    public int countByPidAndIndexPos(String pid, int indexPos)
    {
        return this.countByCondition("where pid = ? and indexPos = ?", pid, indexPos);
    }
}
