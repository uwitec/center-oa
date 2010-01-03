/**
 * 
 */
package com.china.centet.yongyin.bean;


import java.io.Serializable;

import com.china.center.annotation.Entity;
import com.china.center.annotation.Id;
import com.china.center.annotation.Table;


/**
 * @author Administrator
 */
@Entity(name = "会员卡等级")
@Table(name = "T_CENTER_MEMBERGRADE")
public class GradeBean implements Serializable
{
    @Id
    private int id = 0;

    private String name = "";

    private double rebate = 0.0d;

    private int basepoint = 0;

    private int mincost = 0;

    private String description = "";

    public GradeBean()
    {}

    /**
     * @return the name
     */
    public String getName()
    {
        return name;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * @return the rebate
     */
    public double getRebate()
    {
        return rebate;
    }

    /**
     * @param rebate
     *            the rebate to set
     */
    public void setRebate(double rebate)
    {
        this.rebate = rebate;
    }

    /**
     * @return the basepoint
     */
    public int getBasepoint()
    {
        return basepoint;
    }

    /**
     * @param basepoint
     *            the basepoint to set
     */
    public void setBasepoint(int basepoint)
    {
        this.basepoint = basepoint;
    }

    /**
     * @return the mincost
     */
    public int getMincost()
    {
        return mincost;
    }

    /**
     * @param mincost
     *            the mincost to set
     */
    public void setMincost(int mincost)
    {
        this.mincost = mincost;
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

    /**
     * @return the id
     */
    public int getId()
    {
        return id;
    }

    /**
     * @param id
     *            the id to set
     */
    public void setId(int id)
    {
        this.id = id;
    }
}
