/**
 * File Name: BuffaloOSGiConfigurer.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2010-7-3<br>
 * Grant: open source to everybody
 */
package com.china.center.webplugin.inter;

/**
 * 动态注入Buffalo的实现
 * 
 * @author ZHUZHU
 * @version 2010-7-3
 * @see BuffaloOSGiConfigurer
 * @since 1.0
 */
public interface BuffaloOSGiConfigurer
{
    void putBuffaloService(String key, Object service);

    void removeBuffaloService(String key);
}
