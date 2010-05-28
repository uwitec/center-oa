/**
 * File Name: MySqlQueryImpl.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2008-3-2<br>
 * Grant: open source to everybody
 */
package com.china.center.jdbc.inter.adapter;

import com.china.center.jdbc.inter.Query;

/**
 * <描述>
 *
 * @author ZHUZHU
 * @version 2008-3-2
 * @see
 * @since
 */
public class AccessQueryImpl extends BaseQueryImpl
{
	private int begin = 0;

	private int max = 0;

	/*
	 * (non-Javadoc)
	 *
	 * @see com.china.center.annosql.adapter.BaseQueryImpl#setFirstResult(int)
	 */
	@Override
	public Query setFirstResult(int firstResult)
	{
		this.begin = firstResult;
		return this;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.china.center.annosql.adapter.BaseQueryImpl#setMaxResults(int)
	 */
	@Override
	public Query setMaxResults(int maxResults)
	{
		this.max = maxResults;
		return this;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.china.center.annosql.adapter.BaseQueryImpl#setResultsRange(int,
	 *      int)
	 */
	@Override
	public Query setResultsRange(int begin, int end)
	{
		this.begin = begin;

		this.max = end - begin;

		return this;
	}

	/**
	 * 这里必须支持ID的倒排
	 */
	public String getLastSql()
	{
		// 不限制
		if (begin == 0 && max == 0)
		{
			return this.sqlString;
		}

		if (this.prefix == null || "".equals(this.prefix.trim()))
		{
			throw new RuntimeException(
					"access use top must need prefix,please use queryObjects method");
		}

		String sql = "";

		if (this.sqlString.toLowerCase().indexOf("order by") == -1)
		{
			sql = this.sqlString + " order by " + this.prefix + "." + this.idColumn + " desc";
		}
		else
		{
			sql = this.sqlString;
		}

		return "select * from (select top " + max + " * from (select top " + (begin + max)
				+ " * from (" + sql + ")) order by " + this.prefix + "." + this.idColumn
				+ " asc) t_access_table order by t_access_table." + this.idColumn + " desc ";
	}

}
