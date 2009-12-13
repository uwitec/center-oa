/**
 *
 */
package com.china.centet.yongyin.vo;


import java.util.List;

import com.china.center.annotation.Entity;
import com.china.center.annotation.Ignore;
import com.china.center.annotation.Relationship;
import com.china.centet.yongyin.bean.FlowInstanceBean;


/**
 * @author Administrator
 */
@Entity(inherit = true)
public class FlowInstanceBeanVO extends FlowInstanceBean
{
    @Relationship(relationField = "flowId", tagField = "name")
    private String flowName = "";

    @Relationship(relationField = "userId", tagField = "stafferName")
    private String userName = "";

    @Ignore
    private String currentTokenName = "";

    @Ignore
    private List<FlowInstanceLogBeanVO> logsVO = null;

    /**
     *
     */
    public FlowInstanceBeanVO()
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
     * @return the logsVO
     */
    public List<FlowInstanceLogBeanVO> getLogsVO()
    {
        return logsVO;
    }

    /**
     * @param logsVO
     *            the logsVO to set
     */
    public void setLogsVO(List<FlowInstanceLogBeanVO> logsVO)
    {
        this.logsVO = logsVO;
    }

    /**
     * @return the currentTokenName
     */
    public String getCurrentTokenName()
    {
        return currentTokenName;
    }

    /**
     * @param currentTokenName
     *            the currentTokenName to set
     */
    public void setCurrentTokenName(String currentTokenName)
    {
        this.currentTokenName = currentTokenName;
    }
}
