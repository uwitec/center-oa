/**
 * File Name: MakeTokenItemDAO.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: ZHUACHEN<br>
 * CreateTime: 2009-10-11<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.customize.make.dao;


import net.sourceforge.sannotations.annotation.Bean;

import com.china.center.jdbc.inter.impl.BaseDAO2;
import com.china.center.oa.constant.MakeConstant;
import com.china.center.oa.customize.make.bean.MakeTokenItemBean;


/**
 * MakeTokenItemDAO
 * 
 * @author ZHUZHU
 * @version 2009-10-11
 * @see MakeTokenItemDAO
 * @since 1.0
 */
@Bean(name = "makeTokenItemDAO")
public class MakeTokenItemDAO extends BaseDAO2<MakeTokenItemBean, MakeTokenItemBean>
{
    /**
     * findEndTokenByParentId
     * 
     * @param pid
     * @return
     */
    public MakeTokenItemBean findEndTokenByParentId(int pid)
    {
        return this.findUnique("where pid = ? and ends = ?", pid, MakeConstant.END_TOKEN_YES);
    }
}
