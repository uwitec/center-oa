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
     * 资产
     */
    @Defined(key = "taxPtype", value = "资产")
    String TAX_PTYPE_PROPERTY = "90000000000000000001";

    /**
     * 负债
     */
    @Defined(key = "taxPtype", value = "负债")
    String TAX_PTYPE_OBLIGATION = "90000000000000000002";

    /**
     * 权益
     */
    @Defined(key = "taxPtype", value = "权益")
    String TAX_PTYPE_EQUITY = "90000000000000000003";

    /**
     * 损益
     */
    @Defined(key = "taxPtype", value = "损益")
    String TAX_PTYPE_LOSS = "90000000000000000004";

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
     * 父级点
     */
    @Defined(key = "taxBottomFlag", value = "父级科目")
    int TAX_BOTTOMFLAG_ROOT = 0;

    /**
     * 子级点
     */
    @Defined(key = "taxBottomFlag", value = "最小科目")
    int TAX_BOTTOMFLAG_ITEM = 1;

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
     * 人工录入
     */
    @Defined(key = "financeCreateType", value = "人工录入")
    int FINANCE_CREATETYPE_HAND = 0;

    /**
     * 采购拿货
     */
    @Defined(key = "financeCreateType", value = "采购-采购拿货")
    int FINANCE_CREATETYPE_STOCK_IN = 1;

    /**
     * 采购付款
     */
    @Defined(key = "financeCreateType", value = "采购-采购付款")
    int FINANCE_CREATETYPE_STOCK_PAY = 2;

    /**
     * 产品合成
     */
    @Defined(key = "financeCreateType", value = "产品-产品合成")
    int FINANCE_CREATETYPE_PRODUCT_COMPOSE = 3;

    /**
     * 产品合成回滚
     */
    @Defined(key = "financeCreateType", value = "产品-产品合成回滚")
    int FINANCE_CREATETYPE_PRODUCT_COMPOSE_BACK = 4;

    /**
     * 产品调价
     */
    @Defined(key = "financeCreateType", value = "产品-产品调价")
    int FINANCE_CREATETYPE_PRODUCT_PRICE = 5;

    /**
     * 产品调价回滚
     */
    @Defined(key = "financeCreateType", value = "产品-产品调价回滚")
    int FINANCE_CREATETYPE_PRODUCT_PRICE_BACK = 6;

    /**
     * 入库-调出
     */
    @Defined(key = "financeCreateType", value = "入库-调出")
    int FINANCE_CREATETYPE_BUY_OUT = 7;

    /**
     * 入库-调入
     */
    @Defined(key = "financeCreateType", value = "入库-调入")
    int FINANCE_CREATETYPE_BUY_IN = 8;

    /**
     * 入库-调出回滚
     */
    @Defined(key = "financeCreateType", value = "入库-调出回滚")
    int FINANCE_CREATETYPE_BUY_OUTBACK = 9;

    /**
     * 入库-报废
     */
    @Defined(key = "financeCreateType", value = "入库-报废")
    int FINANCE_CREATETYPE_BUY_DROP = 10;

    /**
     * 入库-系统纠正
     */
    @Defined(key = "financeCreateType", value = "入库-系统纠正")
    int FINANCE_CREATETYPE_BUY_ERRORP = 11;

    /**
     * 入库-采购退货
     */
    @Defined(key = "financeCreateType", value = "入库-采购退货")
    int FINANCE_CREATETYPE_BUY_STOCKBACK = 12;

    /**
     * 入库-其它入库
     */
    @Defined(key = "financeCreateType", value = "入库-其它入库")
    int FINANCE_CREATETYPE_BUY_OTHER = 13;

    /**
     * 销售-销售出库
     */
    @Defined(key = "financeCreateType", value = "销售-销售出库")
    int FINANCE_CREATETYPE_SAIL_COMMON = 14;

    /**
     * 销售-个人领养
     */
    @Defined(key = "financeCreateType", value = "销售-个人领样")
    int FINANCE_CREATETYPE_SAIL_SWATCH = 15;

    /**
     * 销售-零售
     */
    @Defined(key = "financeCreateType", value = "销售-零售")
    int FINANCE_CREATETYPE_SAIL_RETAIL = 16;

    /**
     * 销售-委托代销
     */
    @Defined(key = "financeCreateType", value = "销售-委托代销")
    int FINANCE_CREATETYPE_SAIL_CONSIGN = 17;

    /**
     * 销售-赠送
     */
    @Defined(key = "financeCreateType", value = "销售-赠送")
    int FINANCE_CREATETYPE_SAIL_PRESENT = 18;

    /**
     * 入库-个人领养退库
     */
    @Defined(key = "financeCreateType", value = "入库-个人领样退库")
    int FINANCE_CREATETYPE_OUT_SWATCHBACK = 19;

    /**
     * 入库-销售退库
     */
    @Defined(key = "financeCreateType", value = "入库-销售退库")
    int FINANCE_CREATETYPE_OUT_SAILBACK = 20;

    /**
     * 资金-回款认领
     */
    @Defined(key = "financeCreateType", value = "资金-回款认领")
    int FINANCE_CREATETYPE_BILL_GETPAY = 21;

    /**
     * 资金-回款退领
     */
    @Defined(key = "financeCreateType", value = "资金-回款退领")
    int FINANCE_CREATETYPE_BILL_GETPAY_BACK = 22;

    /**
     * 资金-预收转应收
     */
    @Defined(key = "financeCreateType", value = "资金-预收转应收")
    int FINANCE_CREATETYPE_BILL_MAYTOREAL = 23;

    /**
     * 资金-预收转应收回滚
     */
    @Defined(key = "financeCreateType", value = "资金-预收转应收回滚")
    int FINANCE_CREATETYPE_BILL_MAYTOREAL_BACK = 24;

    /**
     * 销售-销售坏账
     */
    @Defined(key = "financeCreateType", value = "销售-销售坏账")
    int FINANCE_CREATETYPE_SAIL_BADMONEY = 25;

    /**
     * 销售-坏账取消
     */
    @Defined(key = "financeCreateType", value = "销售-坏账取消")
    int FINANCE_CREATETYPE_SAIL_BADMONEY_BACK = 26;

    /**
     * 资金-销售退款
     */
    @Defined(key = "financeCreateType", value = "资金-销售退款")
    int FINANCE_CREATETYPE_BILL_SAILBACK = 27;

    /**
     * 资金-预收退款
     */
    @Defined(key = "financeCreateType", value = "资金-预收退款")
    int FINANCE_CREATETYPE_BILL_MAYBACK = 28;

    /**
     * 客户
     */
    @Defined(key = "unitType", value = "客户")
    int UNIT_TYPE_CUSTOMER = 0;

    /**
     * 供应商
     */
    @Defined(key = "unitType", value = "供应商")
    int UNIT_TYPE_PROVIDE = 1;

    /**
     * 默认级别
     */
    int TAX_LEVEL_DEFAULT = 0;

    /**
     * 最大级别
     */
    int TAX_LEVEL_MAX = 8;

    /**
     * 最小计算单位转换倍数
     */
    int DOUBLE_TO_INT = 10000;

    /**
     * 未核对
     */
    @Defined(key = "financeStatus", value = "未核对")
    int FINANCE_STATUS_NOCHECK = 0;

    /**
     * 已核对
     */
    @Defined(key = "financeStatus", value = "已核对")
    int FINANCE_STATUS_CHECK = 1;
}
