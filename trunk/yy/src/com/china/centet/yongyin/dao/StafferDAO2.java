/**
 * File Name: StafferDAO.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2008-8-24<br>
 * Grant: open source to everybody
 */
package com.china.centet.yongyin.dao;


import java.util.List;

import net.sourceforge.sannotations.annotation.Bean;

import com.china.center.jdbc.inter.impl.BaseDAO;
import com.china.centet.yongyin.bean.StafferBean2;


/**
 * StafferDAO
 * 
 * @author zhuzhu
 * @version 2008-8-24
 * @see
 * @since
 */
@Bean(name = "stafferDAO2")
public class StafferDAO2 extends BaseDAO<StafferBean2, StafferBean2>
{
    /**
     * queryByPostId
     * 
     * @param postId
     * @return
     */
    public List<StafferBean2> queryByPostId(String postId)
    {
        return this.queryEntityBeansByCondition("where postId = ? and status = ?", postId, 0);
    }
}
