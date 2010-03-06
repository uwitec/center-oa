/**
 * 
 */
package com.china.centet.yongyin.constant;

/**
 * ProductConstant
 * 
 * @author ZHUZHU
 *
 */
public interface ProductConstant
{
    /**
     * 正常
     */
    int STATUS_COMMON = 0;
    
    /**
     * 锁定
     */
    int STATUS_LOCK = 1;
    
    /**
     * 注销
     */
    int STATUS_LOGOUT = 2;
    
    /**
     * 自有
     */
    int TEMP_SELF = 0;
    
    /**
     * 非自有
     */
    int TEMP_PUBLIC = 1;
}
