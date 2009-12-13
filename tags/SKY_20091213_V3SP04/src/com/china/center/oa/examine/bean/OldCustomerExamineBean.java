/**
 * File Name: NewCustomerExamine.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2009-1-11<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.examine.bean;


import com.china.center.annotation.Entity;
import com.china.center.annotation.Id;
import com.china.center.annotation.Table;


/**
 * OldCustomerExamineBean
 * 
 * @author zhuzhu
 * @version 2009-1-11
 * @see OldCustomerExamineBean
 * @since 1.0
 */
@Entity(inherit = true)
@Table(name = "T_CENTER_EXAMINE_OLDC")
public class OldCustomerExamineBean extends AbstractExamineItem
{
    @Id
    private String id = "";
    
    private int realValue = 0;

    private int planValue = 0;

    public OldCustomerExamineBean()
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
     * @return the realValue
     */
    public int getRealValue()
    {
        return realValue;
    }

    /**
     * @param realValue the realValue to set
     */
    public void setRealValue(int realValue)
    {
        this.realValue = realValue;
    }

    /**
     * @return the planValue
     */
    public int getPlanValue()
    {
        return planValue;
    }

    /**
     * @param planValue the planValue to set
     */
    public void setPlanValue(int planValue)
    {
        this.planValue = planValue;
    }
}
