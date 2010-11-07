/**
 * File Name: SimpleDAO.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2010-11-7<br>
 * Grant: open source to everybody
 */
package com.china.center.jdbc.inter;


import java.io.Serializable;


/**
 * SimpleDAO
 * 
 * @author ZHUZHU
 * @version 2010-11-7
 * @see SimpleDAO
 * @since 1.0
 */
public interface SimpleDAO<Bean extends Serializable> extends DAO<Bean, Bean>
{

}
