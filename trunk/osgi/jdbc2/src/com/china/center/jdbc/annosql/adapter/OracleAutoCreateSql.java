/*
 * File Name: OracleAutoCreateSql.java
 * CopyRight: Copyright by www.center.china
 * Description:
 * Creater: zhuAchen
 * CreateTime: 2007-9-28
 * Grant: open source to everybody
 */
package com.china.center.jdbc.annosql.adapter;

import com.china.center.jdbc.annosql.MYSqlException;

/**
 * oracle的自动生成sql的实现
 *
 * @author ZHUZHU
 * @version 2007-9-28
 * @see
 * @since
 */
public class OracleAutoCreateSql extends BaseAutoCreateSql
{
	/**
	 * 由于oracle没有自增类型，依赖squence生成
	 */
	public String insertSql(Class claz) throws MYSqlException
	{
		return insertSqlInner(claz, false);
	}
}
