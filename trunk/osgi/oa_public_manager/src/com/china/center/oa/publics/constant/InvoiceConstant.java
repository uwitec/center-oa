/**
 * File Name: InvoiceConstant.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2010-9-19<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.publics.constant;


import com.china.center.jdbc.annotation.Defined;


/**
 * InvoiceConstant
 * 
 * @author ZHUZHU
 * @version 2010-9-19
 * @see InvoiceConstant
 * @since 1.0
 */
public interface InvoiceConstant
{
    /**
     * 专用发票
     */
    @Defined(key = "invoiceType", value = "专用发票")
    int INVOICE_TYPE_SPECIAL = 0;

    /**
     * 服务费发票
     */
    @Defined(key = "invoiceType", value = "服务费发票")
    int INVOICE_TYPE_SERVICE = 1;

    /**
     * 普通发票
     */
    @Defined(key = "invoiceType", value = "普通发票")
    int INVOICE_TYPE_COMMON = 2;

    /**
     * 进货发票
     */
    @Defined(key = "invoiceForward", value = "进货发票")
    int INVOICE_FORWARD_IN = 0;

    /**
     * 销货发票
     */
    @Defined(key = "invoiceForward", value = "销货发票")
    int INVOICE_FORWARD_OUT = 1;

    /**
     * 不可抵扣
     */
    @Defined(key = "invoiceCounteract", value = "不可抵扣")
    int INVOICE_COUNTERACT_NO = 0;

    /**
     * 可抵扣
     */
    @Defined(key = "invoiceCounteract", value = "可抵扣")
    int INVOICE_COUNTERACT_YES = 1;
}
