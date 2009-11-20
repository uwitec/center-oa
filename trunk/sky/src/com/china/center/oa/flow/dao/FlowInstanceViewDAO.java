/**
 * File Name: FlowInstanceViewDAO.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2009-5-3<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.flow.dao;

import net.sourceforge.sannotations.annotation.Bean;

import com.china.center.jdbc.inter.impl.BaseDAO2;
import com.china.center.oa.flow.bean.FlowInstanceViewBean;
import com.china.center.oa.flow.vo.FlowInstanceViewVO;

/**
 * FlowInstanceViewDAO
 * 
 * @author zhuzhu
 * @version 2009-5-3
 * @see FlowInstanceViewDAO
 * @since 1.0
 */
@Bean(name = "flowInstanceViewDAO")
public class FlowInstanceViewDAO extends BaseDAO2<FlowInstanceViewBean, FlowInstanceViewVO>
{

}
