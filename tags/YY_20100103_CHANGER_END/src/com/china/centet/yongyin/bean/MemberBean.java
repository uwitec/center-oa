/*
 * File Name: BankBean.java
 * CopyRight: Copyright by www.center.china
 * Description:
 * Creater: zhuAchen
 * CreateTime: 2007-12-16
 * Grant: open source to everybody
 */
package com.china.centet.yongyin.bean;


import java.io.Serializable;

import com.china.center.annotation.Entity;
import com.china.center.annotation.Html;
import com.china.center.annotation.Id;
import com.china.center.annotation.JCheck;
import com.china.center.annotation.Table;
import com.china.center.annotation.enums.Element;
import com.china.centet.yongyin.constant.Constant;
import com.china.centet.yongyin.constant.MemberContant;


/**
 * 银行
 * 
 * @author zhuzhu
 * @version 2007-12-16
 * @see
 * @since
 */
@Entity(name = "会员")
@Table(name = "T_CENTER_MEMBER")
public class MemberBean implements Serializable
{
    @Id(autoIncrement = true)
    private String id = "";

    @Html(title = "姓名", must = true, maxLength = 40)
    private String name = "";

    private String userId = "";

    private String locationId = "";

    @Html(title = "卡号", must = true, maxLength = 40)
    private String cardNo = "";

    @Html(title = "密码", type = Element.PASSWORD, maxLength = 40)
    private String password = "";

    @Html(title = "邮箱", maxLength = 200)
    private String email = "";

    @Html(title = "家庭电话", maxLength = 20)
    private String connect = "";

    @Html(title = "家庭地址", maxLength = 100)
    private String address = "";

    @Html(title = "手机", oncheck = JCheck.ONLY_NUMBER, maxLength = 20)
    private String handphone = "";

    @Html(title = "描述", type = Element.TEXTAREA, maxLength = 100)
    private String description = "";

    /**
     * 普通，银卡，金卡，铂金卡
     */
    @Html(title = "等级", type = Element.SELECT, must = true)
    private int grade = MemberContant.GRADE_COMMON;

    @Html(title = "性别", type = Element.SELECT)
    private int sex = Constant.SEX_MALE;

    /**
     * 积分
     */
    private int point = 0;

    /**
     * 可以使用的积分
     */
    private int usepoint = 0;

    /**
     * 类型 普通 永久
     */
    @Html(title = "类型", type = Element.SELECT, must = true)
    private int type = MemberContant.TYPE_COMMON;

    private String logTime = "";

    @Html(title = "公司", maxLength = 100)
    private String company = "";

    @Html(title = "职位", maxLength = 100)
    private String position = "";

    @Html(title = "省市", maxLength = 20)
    private String area = "";

    private double rebate = 0.0d;

    /**
     * default constructor
     */
    public MemberBean()
    {}

    /**
     * @return the id
     */
    public String getId()
    {
        return id;
    }

    /**
     * @return the name
     */
    public String getName()
    {
        return name;
    }

    /**
     * @return the userId
     */
    public String getUserId()
    {
        return userId;
    }

    /**
     * @return the locationId
     */
    public String getLocationId()
    {
        return locationId;
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
     * @param name
     *            the name to set
     */
    public void setName(String name)
    {
        this.name = name;
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
     * @param locationId
     *            the locationId to set
     */
    public void setLocationId(String locationId)
    {
        this.locationId = locationId;
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
     * @return the password
     */
    public String getPassword()
    {
        return password;
    }

    /**
     * @param password
     *            the password to set
     */
    public void setPassword(String password)
    {
        this.password = password;
    }

    /**
     * @return the email
     */
    public String getEmail()
    {
        return email;
    }

    /**
     * @param email
     *            the email to set
     */
    public void setEmail(String email)
    {
        this.email = email;
    }

    /**
     * @return the connect
     */
    public String getConnect()
    {
        return connect;
    }

    /**
     * @param connect
     *            the connect to set
     */
    public void setConnect(String connect)
    {
        this.connect = connect;
    }

    /**
     * @return the address
     */
    public String getAddress()
    {
        return address;
    }

    /**
     * @param address
     *            the address to set
     */
    public void setAddress(String address)
    {
        this.address = address;
    }

    /**
     * @return the handphone
     */
    public String getHandphone()
    {
        return handphone;
    }

    /**
     * @param handphone
     *            the handphone to set
     */
    public void setHandphone(String handphone)
    {
        this.handphone = handphone;
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
     * @return the grade
     */
    public int getGrade()
    {
        return grade;
    }

    /**
     * @param grade
     *            the grade to set
     */
    public void setGrade(int grade)
    {
        this.grade = grade;
    }

    /**
     * @return the sex
     */
    public int getSex()
    {
        return sex;
    }

    /**
     * @param sex
     *            the sex to set
     */
    public void setSex(int sex)
    {
        this.sex = sex;
    }

    /**
     * @return the point
     */
    public int getPoint()
    {
        return point;
    }

    /**
     * @param point
     *            the point to set
     */
    public void setPoint(int point)
    {
        this.point = point;
    }

    /**
     * @return the type
     */
    public int getType()
    {
        return type;
    }

    /**
     * @param type
     *            the type to set
     */
    public void setType(int type)
    {
        this.type = type;
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
     * @return the company
     */
    public String getCompany()
    {
        return company;
    }

    /**
     * @param company
     *            the company to set
     */
    public void setCompany(String company)
    {
        this.company = company;
    }

    /**
     * @return the position
     */
    public String getPosition()
    {
        return position;
    }

    /**
     * @param position
     *            the position to set
     */
    public void setPosition(String position)
    {
        this.position = position;
    }

    /**
     * @return the area
     */
    public String getArea()
    {
        return area;
    }

    /**
     * @param area
     *            the area to set
     */
    public void setArea(String area)
    {
        this.area = area;
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
     * @return the usepoint
     */
    public int getUsepoint()
    {
        return usepoint;
    }

    /**
     * @param usepoint
     *            the usepoint to set
     */
    public void setUsepoint(int usepoint)
    {
        this.usepoint = usepoint;
    }
}
