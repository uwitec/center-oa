/**
 * File Name: PercentBean.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2009-2-8<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.examine.bean;

import java.io.Serializable;

import com.china.center.annotation.Entity;
import com.china.center.annotation.Id;
import com.china.center.annotation.Table;

/**
 * PercentBean
 * 
 * @author zhuzhu
 * @version 2009-2-8
 * @see PercentBean
 * @since 1.0
 */
/**
 * PercentBean
 * 
 * @author zhuzhu
 * @version 2009-2-8
 * @see PercentBean
 * @since 1.0
 */
@Entity
@Table(name = "T_CENTER_CPPERCENT")
public class PercentBean implements Serializable
{
    @Id(autoIncrement = true)
    private String id = "";
    
    private int percent = 0;
    
    private int month = 0;

    public PercentBean()
    {}

    /**
     * @return the id
     */
    public String getId()
    {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id)
    {
        this.id = id;
    }

    /**
     * @return the percent
     */
    public int getPercent()
    {
        return percent;
    }

    /**
     * @param percent the percent to set
     */
    public void setPercent(int percent)
    {
        this.percent = percent;
    }

    /**
     * @return the month
     */
    public int getMonth()
    {
        return month;
    }

    /**
     * @param month the month to set
     */
    public void setMonth(int month)
    {
        this.month = month;
    }
}
