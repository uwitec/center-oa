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
     * 应收账款(客户/职员/部门)
     */
    String REVEIVE_PRODUCT = "1132";

    /**
     * 其它应收-坏账(职员/部门)
     */
    String BAD_REVEIVE_PRODUCT = "1133-10";

    /**
     * 其他应收款-样品(部门/职员/产品/仓库 )
     */
    String OTHER_REVEIVE_PRODUCT = "1133-14";

    /**
     * 库存商品(产品/仓库)
     */
    String DEPOR_PRODUCT = "1243";

    /**
     * 库存商品-中转(产品/仓库)
     */
    String DEPOR_PRODUCT_TEMP = "1245";

    /**
     * 委托代销商品(产品/仓库)
     */
    String CONSIGN_PRODUCT = "1261";

    /**
     * 应付账款-货款(单位)
     */
    String PAY_PRODUCT = "2122-01";

    /**
     * 预收账款(客户/职员/部门)
     */
    String PREREVEIVE_PRODUCT = "2131";

    /**
     * 主营业务税金及附加(部门)
     */
    String MAIN_TAX = "5402";

    /**
     * 主营业务收入(部门/职员/产品/仓区)
     */
    String MAIN_RECEIVE = "5101";

    /**
     * 主营业务成本(部门/职员/产品/仓区)
     */
    String MAIN_COST = "5401";

    /**
     * 调价收入(部门)
     */
    String PRICECHANGE_REVEIVE = "5202";

    /**
     * 业务招待费-招待应酬费(部门/职员)
     */
    String RECEIVE_COMMON = "5504-21-02";

    /**
     * 银行手续费-部门(部门/职员)
     */
    String HAND_BANK_DEPARTMENT = "5504-22-01";

    /**
     * 银行手续费-个人(部门/职员)
     */
    String HAND_BANK_PERSON = "5504-22-02";

    /**
     * 营业费用-运输费(部门/职员)
     */
    String TRAN_FEE = "5505-05";

    /**
     * 营业外支出(无)
     */
    String OTHER_PAY = "5601";

    /**
     * 本年利润(无)
     */
    String YEAR_PROFIT = "3131";

    /**
     * 未分配利润（无）
     */
    String NO_PROFIT = "3141-12";
}
