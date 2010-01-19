/**
 *
 */
package com.china.centet.yongyin.bean;


import java.io.Serializable;

import com.china.center.annotation.Entity;
import com.china.center.annotation.Html;
import com.china.center.annotation.Id;
import com.china.center.annotation.JCheck;
import com.china.center.annotation.Join;
import com.china.center.annotation.Table;
import com.china.center.annotation.enums.Element;
import com.china.center.annotation.enums.JoinType;


/**
 * 兑换bean
 * 
 * @author Administrator
 */
@Entity(name = "兑换")
@Table(name = "T_CENTER_EXCHANGE")
public class ExchangeBean implements Serializable
{
    @Id(autoIncrement = true)
    private String id = "";

    @Join(tagClass = User.class, tagField = "id", type = JoinType.LEFT)
    private String userId = "";

    @Join(tagClass = MemberBean.class, tagField = "id")
    private String memberId = "";

    @Html(title = "兑换物品", maxLength = 100, must = true)
    private String entity = "";

    @Html(title = "使用积分", maxLength = 5, oncheck = JCheck.ONLY_NUMBER, must = true)
    private int costpoint = 0;

    private int beforepoint = 0;

    private String logTime = "";

    @Html(title = "备注", maxLength = 100, type = Element.TEXTAREA)
    private String description = "";

    /**
     *
     */
    public ExchangeBean()
    {}

    /**
     * @return the id
     */
    public String getId()
    {
        return id;
    }

    /**
     * @param id
     *            the id to set
     */
    public void setId(String id)
    {
        this.id = id;
    }

    /**
     * @return the userId
     */
    public String getUserId()
    {
        return userId;
    }

    /**
     * @param userId
     *            the userId to set
     */
    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    /**
     * @return the memberId
     */
    public String getMemberId()
    {
        return memberId;
    }

    /**
     * @param memberId
     *            the memberId to set
     */
    public void setMemberId(String memberId)
    {
        this.memberId = memberId;
    }

    /**
     * @return the entity
     */
    public String getEntity()
    {
        return entity;
    }

    /**
     * @param entity
     *            the entity to set
     */
    public void setEntity(String entity)
    {
        this.entity = entity;
    }

    /**
     * @return the costpoint
     */
    public int getCostpoint()
    {
        return costpoint;
    }

    /**
     * @param costpoint
     *            the costpoint to set
     */
    public void setCostpoint(int costpoint)
    {
        this.costpoint = costpoint;
    }

    /**
     * @return the beforepoint
     */
    public int getBeforepoint()
    {
        return beforepoint;
    }

    /**
     * @param beforepoint
     *            the beforepoint to set
     */
    public void setBeforepoint(int beforepoint)
    {
        this.beforepoint = beforepoint;
    }

    /**
     * @return the logTime
     */
    public String getLogTime()
    {
        return logTime;
    }

    /**
     * @param logTime
     *            the logTime to set
     */
    public void setLogTime(String logTime)
    {
        this.logTime = logTime;
    }

    /**
     * @return the description
     */
    public String getDescription()
    {
        return description;
    }

    /**
     * @param description
     *            the description to set
     */
    public void setDescription(String description)
    {
        this.description = description;
    }

}
