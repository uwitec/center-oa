/**
 * File Name: FlowInstanceViewBeanVO.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2008-9-3<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.flow.vo;


import com.china.center.annotation.Entity;
import com.china.center.annotation.Ignore;
import com.china.center.annotation.Relationship;
import com.china.center.oa.flow.bean.FlowInstanceViewBean;


/**
 * <ÃèÊö>
 * 
 * @author zhuzhu
 * @version 2008-9-3
 * @see
 * @since
 */
@Entity(inherit = true)
public class FlowInstanceViewVO extends FlowInstanceViewBean
{
    @Relationship( relationField = "instanceId", tagField = "title")
    private String title = "";
    
    @Relationship(relationField = "flowId")
    private String flowName = "";
    
    @Ignore
    private String tokenName = "";
    
    @Relationship( relationField = "instanceId", tagField = "stafferId")
    private String createId = "";
    
    @Ignore
    private String stafferName = "";
    
    


    /**
     * default constructor
     */
    public FlowInstanceViewVO()
    {}


    /**
     * @return the title
     */
    public String getTitle()
    {
        return title;
    }


    /**
     * @param title the title to set
     */
    public void setTitle(String title)
    {
        this.title = title;
    }


    /**
     * @return the flowName
     */
    public String getFlowName()
    {
        return flowName;
    }


    /**
     * @param flowName the flowName to set
     */
    public void setFlowName(String flowName)
    {
        this.flowName = flowName;
    }


    /**
     * @return the tokenName
     */
    public String getTokenName()
    {
        return tokenName;
    }


    /**
     * @param tokenName the tokenName to set
     */
    public void setTokenName(String tokenName)
    {
        this.tokenName = tokenName;
    }


    /**
     * @return the stafferName
     */
    public String getStafferName()
    {
        return stafferName;
    }


    /**
     * @param stafferName the stafferName to set
     */
    public void setStafferName(String stafferName)
    {
        this.stafferName = stafferName;
    }


    /**
     * @return the createId
     */
    public String getCreateId()
    {
        return createId;
    }


    /**
     * @param createId the createId to set
     */
    public void setCreateId(String createId)
    {
        this.createId = createId;
    }

   
}
