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

import com.china.center.jdbc.inter.DAO;
import com.china.center.jdbc.inter.JdbcOperation;

/**
 * 普通的dao(事务性的dao)
 *
 * @author zhuzhu
 * @version 2008-3-10
 * @see
 * @since
 */
public abstract class BaseDAO2<Bean extends Serializable, VO extends Serializable> extends
		BaseDAO<Bean, VO> implements DAO<Bean, VO>
{
	protected JdbcOperation jdbcOperation2 = null;

	/**
	 * @param jdbcOperation2
	 *            the jdbcOperation2 to set
	 */
	public void setJdbcOperation2(JdbcOperation jdbcOperation2)
	{
		this.jdbcOperation2 = jdbcOperation2;

		this.jdbcOperation = this.jdbcOperation2;
	}
}
