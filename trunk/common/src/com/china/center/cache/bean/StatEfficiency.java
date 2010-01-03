/**
 *
 */
package com.china.center.cache.bean;

import java.io.Serializable;
import java.text.DecimalFormat;

/**
 * 统计缓存使用的bean
 *
 * @author zhuzhu
 *
 */
public class StatEfficiency implements Serializable
{
	private int addCache = 0;

	private int useCache = 0;

	private int clear = 0;

	/**
	 *
	 */
	public StatEfficiency()
	{
	}

	public String efficiency()
	{
		DecimalFormat df = new DecimalFormat("####0.00");

		if (addCache + useCache == 0)
		{
			return "0.0";
		}

		double e = (useCache + 0.0d) / (addCache + useCache + 0.0d);

		return df.format(e);
	}

	public String toString()
	{
		return "addCache:" + this.addCache + ",useCache:" + this.useCache + ",clear:" + this.clear
				+ ",efficiency:" + efficiency();
	}

	public void clear_incr()
	{
		this.clear++;
	}

	public void addCache_incr()
	{
		this.addCache++;
	}

	public void useCache_incr()
	{
		this.useCache++;
	}

	/**
	 * @return the addCache
	 */
	public int getAddCache()
	{
		return addCache;
	}

	/**
	 * @param addCache
	 *            the addCache to set
	 */
	public void setAddCache(int addCache)
	{
		this.addCache = addCache;
	}

	/**
	 * @return the useCache
	 */
	public int getUseCache()
	{
		return useCache;
	}

	/**
	 * @param useCache
	 *            the useCache to set
	 */
	public void setUseCache(int useCache)
	{
		this.useCache = useCache;
	}

	/**
	 * @return the clear
	 */
	public int getClear()
	{
		return clear;
	}

	/**
	 * @param clear
	 *            the clear to set
	 */
	public void setClear(int clear)
	{
		this.clear = clear;
	}
}
