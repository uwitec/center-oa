/**
 *
 */
package com.china.centet.yongyin.vo;


import com.china.center.annotation.Entity;
import com.china.center.annotation.Relationship;
import com.china.centet.yongyin.bean.ExchangeBean;


/**
 * @author Administrator
 */
@Entity(inherit = true)
public class ExchangeBeanVO extends ExchangeBean
{
    @Relationship(relationField = "userId", tagField = "stafferName")
    private String userName = "";

    @Relationship(relationField = "memberId", tagField = "name")
    private String memberName = "";

    @Relationship(relationField = "memberId")
    private String cardNo = "";

    /**
     *
     */
    public ExchangeBeanVO()
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
}
