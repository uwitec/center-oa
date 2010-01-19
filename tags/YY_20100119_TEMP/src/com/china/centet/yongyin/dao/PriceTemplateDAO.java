/**
 * File Name: PriceTemplateDAO.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2008-8-3<br>
 * Grant: open source to everybody
 */
package com.china.centet.yongyin.dao;


import net.sourceforge.sannotations.annotation.Bean;

import com.china.center.jdbc.inter.impl.BaseDAO2;
import com.china.centet.yongyin.bean.PriceTemplateBean;
import com.china.centet.yongyin.vo.PriceTemplateBeanVO;


/**
 * PriceTemplateDAO
 * 
 * @author zhuzhu
 * @version 2008-8-3
 * @see
 * @since
 */
@Bean(name = "priceTemplateDAO")
public class PriceTemplateDAO extends BaseDAO2<PriceTemplateBean, PriceTemplateBeanVO>
{
    public int countByPriceWebId(String priceWebId)
    {
        return this.countBycondition("where priceWebId = ?", priceWebId);
    }
}
