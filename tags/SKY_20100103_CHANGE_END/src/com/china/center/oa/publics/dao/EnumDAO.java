/**
 * File Name: EnumDAO.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2008-11-9<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.publics.dao;


import net.sourceforge.sannotations.annotation.Bean;

import com.china.center.jdbc.inter.impl.BaseDAO2;
import com.china.center.oa.publics.bean.EnumBean;


/**
 * EnumDAO
 * 
 * @author zhuzhu
 * @version 2008-11-9
 * @see EnumDAO
 * @since 1.0
 */
@Bean(name = "enumDAO")
public class EnumDAO extends BaseDAO2<EnumBean, EnumBean>
{
    /**
     * findByTypeAndEnumIndex
     * @param type
     * @param key
     * @return
     */
    public EnumBean findByTypeAndEnumIndex(int type, String key)
    {
        return this.findUnique("where type = ? and keyss = ?", type, key);
    }
}
