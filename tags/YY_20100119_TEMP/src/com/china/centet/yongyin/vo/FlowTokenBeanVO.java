/**
 *
 */
package com.china.centet.yongyin.vo;


import com.china.center.annotation.Entity;
import com.china.center.annotation.Ignore;
import com.china.center.annotation.Relationship;
import com.china.centet.yongyin.bean.FlowTokenBean;


/**
 * @author Administrator
 */
@Entity(inherit = true)
public class FlowTokenBeanVO extends FlowTokenBean
{
    @Relationship(relationField = "flowId", tagField = "name")
    private String flowName = "";

    @Ignore
    private String processerName = "";

    /**
     *
     */
    public FlowTokenBeanVO()
    {}

    /**
     * @return the flowName
     */
    public String getFlowName()
    {
        return flowName;
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
     * @return the processerName
     */
    public String getProcesserName()
    {
        return processerName;
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
