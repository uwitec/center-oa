/**
 * File Name: ProductExamineVO.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2009-2-14<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.examine.vo;

import java.util.List;

import com.china.center.annotation.Entity;
import com.china.center.annotation.Ignore;
import com.china.center.annotation.Relationship;
import com.china.center.oa.examine.bean.ProductExamineBean;

/**
 * ProductExamineVO
 * 
 * @author ZHUZHU
 * @version 2009-2-14
 * @see ProductExamineVO
 * @since 1.0
 */
@Entity(inherit = true)
public class ProductExamineVO extends ProductExamineBean
{
    @Relationship(relationField = "createrId")
    private String createrName = "";
    
    @Relationship(relationField = "locationId")
    private String locationName = "";
    
    @Relationship(relationField = "productId")
    private String productName = "";
    
    @Relationship(relationField = "productId", tagField = "code")
    private String productCode = "";
    
    @Ignore
    private List<ProductExamineItemVO> itemVOs = null;

    public ProductExamineVO()
    {}

    /**
     * @return the createrName
     */
    public String getCreaterName()
    {
        return createrName;
    }

    /**
     * @param createrName the createrName to set
     */
    public void setCreaterName(String createrName)
    {
        this.createrName = createrName;
    }

    /**
     * @return the locationName
     */
    public String getLocationName()
    {
        return locationName;
    }

    /**
     * @param locationName the locationName to set
     */
    public void setLocationName(String locationName)
    {
        this.locationName = locationName;
    }

    /**
     * @return the productName
     */
    public String getProductName()
    {
        return productName;
    }

    /**
     * @param productName the productName to set
     */
    public void setProductName(String productName)
    {
        this.productName = productName;
    }

    /**
     * @return the itemVOs
     */
    public List<ProductExamineItemVO> getItemVOs()
    {
        return itemVOs;
    }

    /**
     * @param itemVOs the itemVOs to set
     */
    public void setItemVOs(List<ProductExamineItemVO> itemVOs)
    {
        this.itemVOs = itemVOs;
    }

    /**
     * @return the productCode
     */
    public String getProductCode()
    {
        return productCode;
    }

    /**
     * @param productCode the productCode to set
     */
    public void setProductCode(String productCode)
    {
        this.productCode = productCode;
    }
}
