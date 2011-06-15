/**
 * File Name: TaxItemConstanst.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2011-6-12<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.tax.constanst;

/**
 * TaxItemConstanst
 * 
 * @author ZHUZHU
 * @version 2011-6-12
 * @see TaxItemConstanst
 * @since 3.0
 */
public interface TaxItemConstanst
{
    /**
     * 库存商品(产品/仓库)
     */
    String DEPOR_PRODUCT = "1243";

    /**
     * 主营业务税金及附加
     */
    String MAIN_TAX = "5402";

    /**
     * 主营业务收入(部门/职员)
     */
    String MAIN_RECEIVE = "5101";

    /**
     * 主营业务成本(部门/职员)
     */
    String MAIN_COST = "5401";

    /**
     * 应付账款-货款
     */
    String PAY_PRODUCT = "2122-01";

    /**
     * 应收账款
     */
    String REVEIVE_PRODUCT = "1132";

    /**
     * 调价收入(部门)
     */
    String PRICECHANGE_REVEIVE = "5202";
}
