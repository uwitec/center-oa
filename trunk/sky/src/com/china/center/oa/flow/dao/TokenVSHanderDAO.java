/**
 * File Name: TokenVSHanderDAO.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2009-4-26<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.flow.dao;


import java.util.List;

import net.sourceforge.sannotations.annotation.Bean;

import com.china.center.jdbc.inter.impl.BaseDAO2;
import com.china.center.oa.flow.vo.TokenVSHanderVO;
import com.china.center.oa.flow.vs.TokenVSHanderBean;


/**
 * TokenVSHanderDAO
 * 
 * @author zhuzhu
 * @version 2009-4-26
 * @see TokenVSHanderDAO
 * @since 1.0
 */
@Bean(name = "tokenVSHanderDAO")
public class TokenVSHanderDAO extends BaseDAO2<TokenVSHanderBean, TokenVSHanderVO>
{
    /**
     * queryTokenVSHanderByTokenIdAndType
     * 
     * @param tokenId
     * @param type
     * @return List[TokenVSHanderBean]
     */
    public List<TokenVSHanderBean> queryTokenVSHanderByTokenIdAndType(String tokenId, int type)
    {
        return this.jdbcOperation.queryForList("where tokenId = ? and type = ?", claz, tokenId,
            type);
    }
}
