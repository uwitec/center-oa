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
    String BAD_REVEIVE_PRODUCT = "1133-16";

    /**
     * 其他应收款-样品(部门/职员/产品/仓库 )
     */
    String OTHER_REVEIVE_PRODUCT = "1133-20";

    /**
     * 库存商品(产品/仓库)
     */
    String DEPOR_PRODUCT = "1243";

    /**
     * 库存商品-中转(产品/仓库)
     */
    String DEPOR_PRODUCT_TEMP = "1244";

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
     * 主营业务收入(部门/职员)
     */
    String MAIN_RECEIVE = "5101";

    /**
     * 主营业务成本(部门/职员)
     */
    String MAIN_COST = "5401";

    /**
     * 调价收入(部门)
     */
    String PRICECHANGE_REVEIVE = "5202";

    /**
     * 营业费用-业务招待费(部门/职员)
     */
    String RECEIVE_COMMON = "5501-05";

    /**
     * 银行手续费-部门(部门/职员)
     */
    String HAND_BANK_DEPARTMENT = "5501-11";

    /**
     * 营业费用-运输费(部门/职员)
     */
    String TRAN_FEE = "5501-04";

    /**
     * 银行手续费-个人(部门/职员)
     */
    String HAND_BANK_PERSON = "5501-12";

    /**
     * 营业外支出(无)
     */
    String OTHER_PAY = "5601";
}
