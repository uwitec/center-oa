/**
 * File Name: ProductExamineItemVO.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2009-2-14<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.examine.vo;

import com.china.center.annotation.Entity;
import com.china.center.annotation.Relationship;
import com.china.center.oa.examine.bean.ProductExamineItemBean;

/**
 * ProductExamineItemVO(ÒÑ¾­·ÏÆú)
 * 
 * @author ZHUZHU
 * @version 2009-2-14
 * @see ProductExamineItemVO
 * @since 1.0
 */
@Entity(inherit = true)
public class ProductExamineItemVO extends ProductExamineItemBean
{
    @Relationship(relationField = "stafferId")
    private String stafferName = "";
    
    public ProductExamineItemVO()
    {
    }
    
    /**
     * @return the stafferName
     */
    public String getStafferName()
    {
        return stafferName;
    }
    
    /**
     * @param stafferName
     *            the stafferName to set
     */
    public void setStafferName(String stafferName)
    {
        this.stafferName = stafferName;
    }
}
