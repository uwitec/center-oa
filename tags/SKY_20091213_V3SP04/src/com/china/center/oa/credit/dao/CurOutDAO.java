/**
 * File Name: CurOutDAO.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2009-11-27<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.credit.dao;


import java.util.List;

import net.sourceforge.sannotations.annotation.Bean;

import com.china.center.jdbc.inter.impl.BaseDAO2;
import com.china.center.oa.credit.bean.CurOutBean;


/**
 * CurOutDAO
 * 
 * @author ZHUZHU
 * @version 2009-11-27
 * @see CurOutDAO
 * @since 1.0
 */
@Bean(name = "curOutDAO")
public class CurOutDAO extends BaseDAO2<CurOutBean, CurOutBean>
{
    /**
     * findNearestByCid(只分析当前6个月的单据)
     * 
     * @param cid
     * @return
     */
    public CurOutBean findNearestByCid(String cid)
    {
        List<CurOutBean> list = this.queryEntityBeansByCondition("where cid = ? order by id desc",
            cid);

        if (list.size() == 0)
        {
            return null;
        }

        return list.get(0);
    }

}
