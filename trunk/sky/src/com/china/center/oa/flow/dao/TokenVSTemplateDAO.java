/**
 * File Name: TokenVSTemplateDAo.java<br>
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
import com.china.center.oa.flow.vo.TokenVSTemplateVO;
import com.china.center.oa.flow.vs.TokenVSTemplateBean;


/**
 * TokenVSTemplateDAo
 * 
 * @author zhuzhu
 * @version 2009-4-26
 * @see TokenVSTemplateDAO
 * @since 1.0
 */
@Bean(name = "tokenVSTemplateDAO")
public class TokenVSTemplateDAO extends BaseDAO2<TokenVSTemplateBean, TokenVSTemplateVO>
{
    /**
     * queryByFlowId
     * 
     * @param flowId
     * @return
     */
    public List<TokenVSTemplateBean> queryByFlowId(String flowId)
    {
        return this.queryEntityBeansByCondition("where flowid = ?", flowId);
    }
}
