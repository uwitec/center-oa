/**
 *
 */
package com.china.centet.yongyin.vo;


import com.china.center.annotation.Entity;
import com.china.center.annotation.Relationship;
import com.china.centet.yongyin.bean.FlowInstanceLogBean;


/**
 * @author Administrator
 */
/**
 * @author Administrator
 */
@Entity(inherit = true)
public class FlowInstanceLogBeanVO extends FlowInstanceLogBean
{
    @Relationship(relationField = "flowId", tagField = "name")
    private String flowName = "";

    @Relationship(relationField = "userId", tagField = "stafferName")
    private String userName = "";

    @Relationship(relationField = "instanceId", tagField = "title")
    private String instanceTitle = "";

    @Relationship(relationField = "tokenId", tagField = "name")
    private String tokenName = "";

    @Relationship(relationField = "nextTokenId", tagField = "name")
    private String nextTokenName = "";

    /**
     *
     */
    public FlowInstanceLogBeanVO()
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
     * @return the userName
     */
    public String getUserName()
    {
        return userName;
    }

    /**
     * @param userName
     *            the userName to set
     */
    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    /**
     * @return the tokenName
     */
    public String getTokenName()
    {
        return tokenName;
    }

    /**
     * @param tokenName
     *            the tokenName to set
     */
    public void setTokenName(String tokenName)
    {
        this.tokenName = tokenName;
    }

    /**
     * @return the nextTokenName
     */
    public String getNextTokenName()
    {
        return nextTokenName;
    }

    /**
     * @param nextTokenName
     *            the nextTokenName to set
     */
    public void setNextTokenName(String nextTokenName)
    {
        this.nextTokenName = nextTokenName;
    }

    /**
     * @return the instanceTitle
     */
    public String getInstanceTitle()
    {
        return instanceTitle;
    }

    /**
     * @param instanceTitle
     *            the instanceTitle to set
     */
    public void setInstanceTitle(String instanceTitle)
    {
        this.instanceTitle = instanceTitle;
    }
}
