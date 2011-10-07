/**
 * File Name: HandleHint.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2011-5-2<br>
 * Grant: open source to everybody
 */
package com.china.center.actionhelper.query;


import java.util.List;


/**
 * HandleHint
 * 
 * @author ZHUZHU
 * @version 2011-5-2
 * @see HandleHint
 * @since 3.0
 */
public interface HandleHint<V>
{
    String getHint(List<V> list);
}
