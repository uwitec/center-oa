/**
 * File Name: MakeStatWrap.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2010-2-24<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.customize.make.wrap;


import java.io.Serializable;


/**
 * MakeStatWrap
 * 
 * @author ZHUZHU
 * @version 2010-2-24
 * @see MakeStatWrap
 * @since 1.0
 */
public class MakeStatWrap implements Serializable
{
    private int amount = 0;

    private int endType = 0;

    private int position = 0;

    private String stat = "";

    private String positionName = "";

    private String tokenName = "";

    /**
     * default constructor
     */
    public MakeStatWrap()
    {}

    /**
     * @return the amount
     */
    public int getAmount()
    {
        return amount;
    }

    /**
     * @param amount
     *            the amount to set
     */
    public void setAmount(int amount)
    {
        this.amount = amount;
    }

    /**
     * @return the endType
     */
    public int getEndType()
    {
        return endType;
    }

    /**
     * @param endType
     *            the endType to set
     */
    public void setEndType(int endType)
    {
        this.endType = endType;
    }

    /**
     * @return the stat
     */
    public String getStat()
    {
        return stat;
    }

    /**
     * @param stat
     *            the stat to set
     */
    public void setStat(String stat)
    {
        this.stat = stat;
    }

    /**
     * @return the position
     */
    public int getPosition()
    {
        return position;
    }

    /**
     * @param position
     *            the position to set
     */
    public void setPosition(int position)
    {
        this.position = position;
    }

    /**
     * @return the positionName
     */
    public String getPositionName()
    {
        return positionName;
    }

    /**
     * @param positionName
     *            the positionName to set
     */
    public void setPositionName(String positionName)
    {
        this.positionName = positionName;
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
     * Constructs a <code>String</code> with all attributes in name = value format.
     * 
     * @return a <code>String</code> representation of this object.
     */
    public String toString()
    {
        final String tab = ",";

        StringBuilder retValue = new StringBuilder();

        retValue.append("MakeStatWrap ( ").append(super.toString()).append(tab).append("amount = ").append(
            this.amount).append(tab).append("endType = ").append(this.endType).append(tab).append(
            "position = ").append(this.position).append(tab).append("stat = ").append(this.stat).append(
            tab).append("positionName = ").append(this.positionName).append(tab).append(
            "tokenName = ").append(this.tokenName).append(tab).append(" )");

        return retValue.toString();
    }
}
