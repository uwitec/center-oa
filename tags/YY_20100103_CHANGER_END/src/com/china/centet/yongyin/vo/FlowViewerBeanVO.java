/**
 * File Name: FlowInstanceViewerBeanVO.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2008-9-3<br>
 * Grant: open source to everybody
 */
package com.china.centet.yongyin.vo;


import com.china.center.annotation.Entity;
import com.china.center.annotation.Ignore;
import com.china.center.annotation.Relationship;
import com.china.centet.yongyin.bean.FlowViewerBean;


/**
 * FlowInstanceViewerBeanVO
 * 
 * @author zhuzhu
 * @version 2008-9-3
 * @see
 * @since
 */
@Entity(inherit = true)
public class FlowViewerBeanVO extends FlowViewerBean
{
    @Relationship(relationField = "flowId", tagField = "name")
    private String flowName = "";

    @Ignore
    private String processerName = "";

    /**
     * default constructor
     */
    public FlowViewerBeanVO()
    {}

    /**
     * @return the flowName
     */
    public String getFlowName()
    {
        return flowName;
    }

    /**
     * @return the processerName
     */
    public String getProcesserName()
    {
        return processerName;
    }

    /**
     * @param flowName
     *            the flowName to set
     */
    public void setFlowName(String flowName)
    {
        this.flowName = flowName;
    }

    /**
     * @param processerName
     *            the processerName to set
     */
    public void setProcesserName(String processerName)
    {
        this.processerName = processerName;
    }
}
