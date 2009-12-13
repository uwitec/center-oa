/**
 * File Name: ShortMessageTaskDAO.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2009-7-28<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.note.dao;


import net.sourceforge.sannotations.annotation.Bean;

import com.china.center.annosql.tools.BeanTools;
import com.china.center.jdbc.inter.impl.BaseDAO2;
import com.china.center.oa.note.bean.ShortMessageConstant;
import com.china.center.oa.note.bean.ShortMessageTaskBean;
import com.china.center.tools.TimeTools;


/**
 * ShortMessageTaskDAO
 * 
 * @author zhuzhu
 * @version 2009-7-28
 * @see ShortMessageTaskDAO
 * @since 1.0
 */
@Bean(name = "shortMessageTaskDAO")
public class ShortMessageTaskDAO extends BaseDAO2<ShortMessageTaskBean, ShortMessageTaskBean>
{
    /**
     * findByReceiverAndHandId
     * 
     * @param receiver
     * @param handId
     * @return ShortMessageTaskBean
     */
    public ShortMessageTaskBean findByReceiverAndHandId(String receiver, String handId)
    {
        return this.findUnique("where receiver = ? and handId = ?", receiver, handId);
    }

    public boolean updateInitToWaitSend()
    {
        String sql = BeanTools.getUpdateHead(claz)
                     + "set status = ? where sendTime <= ? and status = ?";

        this.jdbcOperation.update(sql, ShortMessageConstant.STATUS_WAIT_SEND, TimeTools.now(),
            ShortMessageConstant.STATUS_INIT);

        return true;
    }
}
