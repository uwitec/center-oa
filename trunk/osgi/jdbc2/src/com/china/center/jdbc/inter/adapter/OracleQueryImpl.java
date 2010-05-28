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
public class OracleQueryImpl extends BaseQueryImpl
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

	public String getLastSql()
	{
		// 不限制
		if (begin == 0 && max == 0)
		{
			return this.sqlString;
		}

		return "select * from (select rownum as R_CENTER_UNM, " + "t_oracle_query.* from ("
				+ this.sqlString + ") t_oracle_query where rownum <= " + (this.begin + this.max)
				+ ") t_oracle_last where t_oracle_last.R_CENTER_UNM > " + this.begin;
	}
}
