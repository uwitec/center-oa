/**
 *
 */
package com.china.centet.yongyin.vo;


import java.util.List;

import com.china.center.annotation.Entity;
import com.china.center.annotation.Ignore;
import com.china.center.annotation.Relationship;
import com.china.centet.yongyin.bean.StockBean;
import com.china.centet.yongyin.constant.StockConstant;


/**
 * @author Administrator
 */
@Entity(inherit = true)
public class StockBeanVO extends StockBean
{
    @Relationship(relationField = "userId", tagField = "stafferName")
    private String userName = "";

    @Relationship(relationField = "locationId", tagField = "locationName")
    private String locationName = "";

    /**
     * ÊÇ·ñ±»²Ù×÷
     */
    @Ignore
    private int display = 0;

    @Ignore
    private int overTime = StockConstant.STOCK_OVERTIME_NO;

    @Ignore
    private List<StockItemBeanVO> itemVO = null;

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
     * @return the locationName
     */
    public String getLocationName()
    {
        return locationName;
    }

    /**
     * @param locationName
     *            the locationName to set
     */
    public void setLocationName(String locationName)
    {
        this.locationName = locationName;
    }

    /**
     * @return the itemVO
     */
    public List<StockItemBeanVO> getItemVO()
    {
        return itemVO;
    }

    /**
     * @param itemVO
     *            the itemVO to set
     */
    public void setItemVO(List<StockItemBeanVO> itemVO)
    {
        this.itemVO = itemVO;
    }

    /**
     * @return the display
     */
    public int getDisplay()
    {
        return display;
    }

    /**
     * @param display
     *            the display to set
     */
    public void setDisplay(int display)
    {
        this.display = display;
    }

    /**
     * @return the overTime
     */
    public int getOverTime()
    {
        return overTime;
    }

    /**
     * @param overTime
     *            the overTime to set
     */
    public void setOverTime(int overTime)
    {
        this.overTime = overTime;
    }
}
