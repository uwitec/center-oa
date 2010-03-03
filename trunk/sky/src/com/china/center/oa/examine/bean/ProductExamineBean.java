/**
 * File Name: ProductExamineBean.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2009-2-14<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.examine.bean;


import java.io.Serializable;
import java.util.List;

import com.china.center.annotation.Entity;
import com.china.center.annotation.Html;
import com.china.center.annotation.Id;
import com.china.center.annotation.Ignore;
import com.china.center.annotation.Join;
import com.china.center.annotation.Table;
import com.china.center.annotation.enums.Element;
import com.china.center.oa.constant.ExamineConstant;
import com.china.center.oa.product.bean.ProductBean;
import com.china.center.oa.publics.bean.LocationBean;
import com.china.center.oa.publics.bean.StafferBean;


/**
 * ProductExamineBean
 * 
 * @author ZHUZHU
 * @version 2009-2-14
 * @see ProductExamineBean
 * @since 1.0
 */
@Entity
@Table(name = "T_CENTER_PROEXAMINE")
public class ProductExamineBean implements Serializable
{
    @Id
    private String id = "";

    @Html(title = "考核名称", must = true, maxLength = 40)
    private String name = "";

    @Join(tagClass = StafferBean.class)
    private String createrId = "";
    
    @Join(tagClass = LocationBean.class)
    @Html(title = "分公司", must = true, type = Element.SELECT)
    private String locationId = "";

    @Join(tagClass = ProductBean.class)
    @Html(title = "考核产品", must = true, maxLength = 40, name="productName")
    private String productId = "";

    private int status = ExamineConstant.EXAMINE_STATUS_INIT;
    
    /**
     * 持续的时间(月)
     */
    private int month = 1;

    private String logTime = "";

    @Html(title = "开始时间", must = true, type = Element.DATE)
    private String beginTime = "";

    @Html(title = "结束时间", must = true, type = Element.DATE)
    private String endTime = "";

    @Html(title = "其他", type = Element.TEXTAREA, maxLength = 200)
    private String description = "";
    
    @Ignore
    private List<ProductCityExamineItemBean> items = null;


    /**
     * default constructor
     */
    public ProductExamineBean()
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
     * @return the createrId
     */
    public String getCreaterId()
    {
        return createrId;
    }

    /**
     * @param createrId
     *            the createrId to set
     */
    public void setCreaterId(String createrId)
    {
        this.createrId = createrId;
    }

    /**
     * @return the locationId
     */
    public String getLocationId()
    {
        return locationId;
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
     * @return the productId
     */
    public String getProductId()
    {
        return productId;
    }

    /**
     * @param productId
     *            the productId to set
     */
    public void setProductId(String productId)
    {
        this.productId = productId;
    }

    /**
     * @return the status
     */
    public int getStatus()
    {
        return status;
    }

    /**
     * @param status
     *            the status to set
     */
    public void setStatus(int status)
    {
        this.status = status;
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
     * @return the beginTime
     */
    public String getBeginTime()
    {
        return beginTime;
    }

    /**
     * @param beginTime
     *            the beginTime to set
     */
    public void setBeginTime(String beginTime)
    {
        this.beginTime = beginTime;
    }

    /**
     * @return the endTime
     */
    public String getEndTime()
    {
        return endTime;
    }

    /**
     * @param endTime
     *            the endTime to set
     */
    public void setEndTime(String endTime)
    {
        this.endTime = endTime;
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

    public int getMonth()
    {
        return month;
    }

    public void setMonth(int month)
    {
        this.month = month;
    }

    public List<ProductCityExamineItemBean> getItems()
    {
        return items;
    }

    public void setItems(List<ProductCityExamineItemBean> items)
    {
        this.items = items;
    }
}
