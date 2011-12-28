package com.china.center.oa.product.constant;


import com.china.center.jdbc.annotation.Defined;


/**
 * @author ZHUZHU
 */
public interface ProductConstant
{
    /**
     * 正常
     */
    @Defined(key = "productStatus", value = "正常态")
    int STATUS_COMMON = 0;

    /**
     * 锁定
     */
    @Defined(key = "productStatus", value = "锁定态")
    int STATUS_LOCK = 1;

    /**
     * 注销
     */
    @Defined(key = "productStatus", value = "注销态")
    int STATUS_LOGOUT = 2;

    /**
     * 申请
     */
    @Defined(key = "productStatus", value = "申请态")
    int STATUS_APPLY = 3;

    /**
     * 自有
     */
    int TEMP_SELF = 0;

    /**
     * 非自有
     */
    int TEMP_PUBLIC = 1;

    /**
     * 正常态
     */
    @Defined(key = "productStatStatus", value = "正常态")
    int STAT_STATUS_COMMON = 0;

    /**
     * 预警态
     */
    @Defined(key = "productStatStatus", value = "预警态")
    int STAT_STATUS_ALERT = 1;

    /**
     * 正常
     */
    @Defined(key = "productOrderStatus", value = "预定")
    int ORDER_STATUS_COMMON = 0;

    /**
     * 结束
     */
    @Defined(key = "productOrderStatus", value = "结束")
    int ORDER_STATUS_END = 1;

    /**
     * 统计周期
     */
    int STAT_DAYS = 15;

    /**
     * 不启用
     */
    @Defined(key = "productStockType", value = "不启用库存模型")
    int STOCKTYPE_NO_USER = 0;

    /**
     * 启用
     */
    @Defined(key = "productStockType", value = "启用库存模型")
    int STOCKTYPE_USER = 1;

    /**
     * 普通产品
     */
    @Defined(key = "productAbstractType", value = "普通产品")
    int ABSTRACT_TYPE_NO = 0;

    /**
     * 虚拟产品
     */
    @Defined(key = "productAbstractType", value = "虚拟产品")
    int ABSTRACT_TYPE_YES = 1;

    /**
     * 产品
     */
    @Defined(key = "productPtype", value = "产品")
    int PTYPE_PRODUCT = 0;

    /**
     * 物料
     */
    @Defined(key = "productPtype", value = "物料")
    int PTYPE_WULIAO = 1;

    /**
     * 普通产品
     */
    @Defined(key = "productCtype", value = "普通产品")
    int CTYPE_NO = 0;

    /**
     * 合成产品
     */
    @Defined(key = "productCtype", value = "合成产品")
    int CTYPE_YES = 1;

    /**
     * 金银章
     */
    @Defined(key = "productType", value = "金银章")
    int PRODUCT_TYPE_OTHER = 0;

    /**
     * 金银币
     */
    @Defined(key = "productType", value = "金银币")
    int PRODUCT_TYPE_PAPER = 1;

    /**
     * 流通币
     */
    @Defined(key = "productType", value = "流通币")
    int PRODUCT_TYPE_METAL = 2;

    /**
     * 旧币
     */
    @Defined(key = "productType", value = "旧币")
    int PRODUCT_TYPE_NUMISMATICS = 3;

    /**
     * 邮票
     */
    @Defined(key = "productType", value = "邮票")
    int PRODUCT_TYPE_STAMP = 4;

    /**
     * 其他
     */
    @Defined(key = "productType", value = "其他")
    int PRODUCT_TYPE_MONCE = 5;

    /**
     * 自有
     */
    @Defined(key = "productSailType", value = "自有")
    int SAILTYPE_SELF = 0;

    /**
     * 经销
     */
    @Defined(key = "productSailType", value = "经销")
    int SAILTYPE_REPLACE = 1;

    /**
     * 定制
     */
    @Defined(key = "productSailType", value = "定制")
    int SAILTYPE_CUSTOMER = 2;

    /**
     * 可以调价
     */
    @Defined(key = "productAjustPrice", value = "可以调价")
    int ADJUSTPRICE_YES = 0;

    /**
     * 不可以
     */
    @Defined(key = "productAjustPrice", value = "不可以调价")
    int ADJUSTPRICE_NO = 1;

    /**
     * 抽检
     */
    @Defined(key = "productCheckType", value = "抽检")
    int CHECKTYPE_SUB = 0;

    /**
     * 全检
     */
    @Defined(key = "productCheckType", value = "全检")
    int CHECKTYPE_ALL = 1;

    /**
     * 出生
     */
    @Defined(key = "productStep", value = "出生")
    String PRODUTC_STEP_NEW = "0";

    @Defined(key = "productStep", value = "成长")
    String PRODUTC_STEP_INCR = "1";

    @Defined(key = "productStep", value = "成熟")
    String PRODUTC_STEP_COMMONR = "2";

    @Defined(key = "productStep", value = "衰退")
    String PRODUTC_STEP_SLOW = "3";

    @Defined(key = "productStep", value = "退市")
    String PRODUTC_STEP_DROP = "4";
}
