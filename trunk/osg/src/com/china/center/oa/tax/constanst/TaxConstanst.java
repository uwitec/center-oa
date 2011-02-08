/**
 * File Name: TaxConstanst.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2011-1-30<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.tax.constanst;


import com.china.center.jdbc.annotation.Defined;


/**
 * TaxConstanst
 * 
 * @author ZHUZHU
 * @version 2011-1-30
 * @see TaxConstanst
 * @since 1.0
 */
public interface TaxConstanst
{
    /**
     * 借方
     */
    @Defined(key = "taxForward", value = "借方")
    int TAX_FORWARD_IN = 0;

    /**
     * 贷方
     */
    @Defined(key = "taxForward", value = "贷方")
    int TAX_FORWARD_OUT = 1;

    @Defined(key = "taxCheck", value = "不核算")
    int TAX_CHECK_NO = 0;

    @Defined(key = "taxCheck", value = "核算")
    int TAX_CHECK_YES = 1;

    @Defined(key = "taxType", value = "现金")
    int TAX_TYPE_CASH = 0;

    @Defined(key = "taxType", value = "银行")
    int TAX_TYPE_BANK = 1;

    @Defined(key = "taxType", value = "其他")
    int TAX_TYPE_OTHER = 2;

    @Defined(key = "taxType", value = "存货")
    int TAX_TYPE_SAVE = 3;

    @Defined(key = "taxType", value = "应付")
    int TAX_TYPE_PAY = 4;

    @Defined(key = "taxType", value = "应收")
    int TAX_TYPE_REVEIVE = 5;

    /**
     * 管理凭证
     */
    @Defined(key = "financeType", value = "管理凭证")
    int FINANCE_TYPE_MANAGER = 0;

    /**
     * 税务凭证
     */
    @Defined(key = "financeType", value = "税务凭证")
    int FINANCE_TYPE_DUTY = 1;

    /**
     * 手工
     */
    @Defined(key = "financeCreateType", value = "手工")
    int FINANCE_CREATETYPE_HAND = 0;
}
