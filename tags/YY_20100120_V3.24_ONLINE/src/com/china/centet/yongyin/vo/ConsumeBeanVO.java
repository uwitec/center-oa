/**
 *
 */
package com.china.centet.yongyin.vo;


import com.china.center.annotation.Entity;
import com.china.center.annotation.Relationship;
import com.china.centet.yongyin.bean.ConsumeBean;


/**
 * @author Administrator
 */
@Entity(name = "会员消费", inherit = true)
public class ConsumeBeanVO extends ConsumeBean
{
    @Relationship(relationField = "userId", tagField = "stafferName")
    private String userName = "";

    @Relationship(relationField = "memberId", tagField = "name")
    private String memberName = "";

    @Relationship(relationField = "memberId")
    private String cardNo = "";

    @Relationship(relationField = "locationId")
    private String locationName = "";

    @Relationship(relationField = "productId", tagField = "name")
    private String productName = "";

    /**
     *
     */
    public ConsumeBeanVO()
    {}

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
     * @return the cardNo
     */
    public String getCardNo()
    {
        return cardNo;
    }

    /**
     * @param cardNo
     *            the cardNo to set
     */
    public void setCardNo(String cardNo)
    {
        this.cardNo = cardNo;
    }

    /**
     * @return the memberName
     */
    public String getMemberName()
    {
        return memberName;
    }

    /**
     * @param memberName
     *            the memberName to set
     */
    public void setMemberName(String memberName)
    {
        this.memberName = memberName;
    }

    /**
     * @return the productName
     */
    public String getProductName()
    {
        return productName;
    }

    /**
     * @param productName
     *            the productName to set
     */
    public void setProductName(String productName)
    {
        this.productName = productName;
    }
}
