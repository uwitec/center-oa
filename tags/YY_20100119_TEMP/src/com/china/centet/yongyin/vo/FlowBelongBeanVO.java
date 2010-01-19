/**
 *
 */
package com.china.centet.yongyin.vo;


import com.china.center.annotation.Entity;
import com.china.center.annotation.Relationship;
import com.china.centet.yongyin.bean.FlowBelongBean;


/**
 * @author Administrator
 */
@Entity(inherit = true)
public class FlowBelongBeanVO extends FlowBelongBean
{
    @Relationship(relationField = "instanceId", tagField = "title")
    private String instanceTitle = "";

    @Relationship(relationField = "userId", tagField = "stafferName")
    private String userName = "";

    @Relationship(relationField = "userId", tagField = "name")
    private String loginName = "";

    /**
     *
     */
    public FlowBelongBeanVO()
    {}

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
     * @return the loginName
     */
    public String getLoginName()
    {
        return loginName;
    }

    /**
     * @param loginName
     *            the loginName to set
     */
    public void setLoginName(String loginName)
    {
        this.loginName = loginName;
    }
}
