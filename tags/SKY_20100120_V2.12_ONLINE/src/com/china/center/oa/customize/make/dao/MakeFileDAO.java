/**
 * File Name: MakeFileDAO.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: ZHUACHEN<br>
 * CreateTime: 2009-10-18<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.customize.make.dao;


import java.util.List;

import net.sourceforge.sannotations.annotation.Bean;

import com.china.center.jdbc.inter.impl.BaseDAO2;
import com.china.center.oa.customize.make.bean.MakeFileBean;


/**
 * MakeFileDAO
 * 
 * @author ZHUZHU
 * @version 2009-10-18
 * @see MakeFileDAO
 * @since 1.0
 */
@Bean(name = "makeFileDAO")
public class MakeFileDAO extends BaseDAO2<MakeFileBean, MakeFileBean>
{
    /**
     * queryByPidAndTokenId
     * 
     * @param pid
     * @param tokenId
     * @return
     */
    public List<MakeFileBean> queryByPidAndTokenId(String pid, int tokenId)
    {
        return this.queryEntityBeansByCondition("where pid = ? and tokenId = ?", pid, tokenId);
    }

    public List<MakeFileBean> queryByBetweenToken(String pid, int beginToken, int endToken)
    {
        return this.queryEntityBeansByCondition("where pid = ? and tokenId >= ? and tokenId <= ?",
            pid, beginToken, endToken);
    }

    public List<MakeFileBean> queryByTokenItemId(int tokenItemId)
    {
        return this.queryEntityBeansByCondition("where tokenItemId = ?", tokenItemId);
    }

    /**
     * findByPidAndTokenIdAndType
     * 
     * @param pid
     * @param tokenId
     * @param type
     * @return
     */
    public MakeFileBean findByPidAndTokenIdAndType(String pid, int tokenId, int type)
    {
        return this.findUnique("where pid = ? and tokenId = ? and type = ?", pid, tokenId, type);
    }
}
