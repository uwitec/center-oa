/**
 * File Name: FlowViewerDAO.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2009-4-26<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.flow.dao;


import java.io.Serializable;

import net.sourceforge.sannotations.annotation.Bean;

import com.china.center.jdbc.inter.impl.BaseDAO2;
import com.china.center.oa.flow.bean.FlowViewerBean;
import com.china.center.oa.flow.vo.FlowViewerVO;


/**
 * FlowViewerDAO
 * 
 * @author zhuzhu
 * @version 2009-4-26
 * @see FlowViewerDAO
 * @since 1.0
 */
@Bean(name = "flowViewerDAO")
public class FlowViewerDAO extends BaseDAO2<FlowViewerBean, FlowViewerVO>
{
    /**
     * countByProcesser
     * 
     * @param processer
     * @return
     */
    public int countByProcesser(Serializable processer)
    {
        return this.countByCondition("where processer = ?", processer);
    }
}
