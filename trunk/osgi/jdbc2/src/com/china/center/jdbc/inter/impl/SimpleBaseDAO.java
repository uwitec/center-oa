/**
 * File Name: BaseDAO.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2008-3-10<br>
 * Grant: open source to everybody
 */
package com.china.center.jdbc.inter.impl;


import java.io.Serializable;

import com.china.center.jdbc.inter.SimpleDAO;


/**
 * 普通的DAO(独立的DAO版本)
 * 
 * @author ZHUZHU
 * @version 2008-3-10
 * @see
 * @since
 */
public abstract class SimpleBaseDAO<Bean extends Serializable> extends BaseDAO<Bean, Bean> implements SimpleDAO<Bean>
{
}
