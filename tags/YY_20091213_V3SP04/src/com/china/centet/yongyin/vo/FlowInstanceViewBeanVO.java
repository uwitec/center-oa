/**
 * File Name: FlowInstanceViewBeanVO.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2008-9-3<br>
 * Grant: open source to everybody
 */
package com.china.centet.yongyin.vo;


import com.china.center.annotation.Entity;
import com.china.center.annotation.Relationship;
import com.china.centet.yongyin.bean.FlowInstanceViewBean;


/**
 * <ÃèÊö>
 * 
 * @author zhuzhu
 * @version 2008-9-3
 * @see
 * @since
 */
@Entity(inherit = true)
public class FlowInstanceViewBeanVO extends FlowInstanceViewBean
{
    @Relationship(relationField = "instanceId", tagField = "title")
    private String instanceTitle = "";

    /**
     * default constructor
     */
    public FlowInstanceViewBeanVO()
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
}
