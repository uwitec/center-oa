/**
 * File Name: MailDAO.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2009-4-12<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.mail.dao;


import java.io.Serializable;
import java.util.List;
import java.util.Map;

import net.sourceforge.sannotations.annotation.Bean;

import com.china.center.annosql.tools.BeanTools;
import com.china.center.jdbc.inter.impl.BaseDAO2;
import com.china.center.oa.mail.bean.MailBean;
import com.china.center.tools.ListTools;


/**
 * MailDAO
 * 
 * @author zhuzhu
 * @version 2009-4-12
 * @see MailDAO
 * @since 1.0
 */
@Bean(name = "mailDAO")
public class MailDAO extends BaseDAO2<MailBean, MailBean>
{
    /**
     * updateStatus
     * 
     * @param id
     * @param status
     * @return
     */
    public boolean updateStatus(Serializable id, int status)
    {
        this.jdbcOperation.updateField("status", status, id, claz);

        return true;
    }

    /**
     * updateFeeback
     * 
     * @param id
     * @param feeback
     * @return
     */
    public boolean updateFeeback(Serializable id, int feeback)
    {
        this.jdbcOperation.updateField("feeback", feeback, id, claz);

        return true;
    }

    /**
     * find next mail id
     * 
     * @param currentId
     * @return
     */
    public Serializable findNextId(Serializable currentId, String stafferId)
    {
        List list = this.jdbcOperation.queryForList("select max(id) as maxId from"
                                                    + BeanTools.getTableName2(claz)
                                                    + "where reveiveId = ? and id < ?", stafferId, currentId);

        return returnId(list);
    }

    /**
     * find preview mail id
     * 
     * @param currentId
     * @return
     */
    public Serializable findPreviewId(Serializable currentId, String stafferId)
    {
        List list = this.jdbcOperation.queryForList("select min(id) as maxId from"
                                                    + BeanTools.getTableName2(claz)
                                                    + "where reveiveId = ? and id > ?", stafferId, currentId);

        return returnId(list);
    }

    /**
     * @param list
     * @return
     */
    private Serializable returnId(List list)
    {
        if (ListTools.isEmptyOrNull(list))
        {
            return "";
        }

        Map map = (Map)list.get(0);

        Object oo = map.get("maxId");

        if (oo == null)
        {
            return "";
        }

        return oo.toString();
    }
}
