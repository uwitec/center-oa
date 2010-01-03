/**
 *
 */
package com.china.center.cache.bean;

import java.io.Serializable;

/**
 * ʵ�ʴ���ڻ�������Ķ���
 *
 * @author Administrator
 *
 */
public class InnerSingleCacheBean<T> implements Serializable
{
	private String key = "";

	/**
	 * ��ѯ��sql
	 */
	private String sql = "";

	/**
	 * ��ѯ�Ĳ���
	 */
	private Object[] parameters = null;

	/**
	 * ���صĽ��
	 */
	private T result = null;

	/**
	 *
	 */
	public InnerSingleCacheBean()
	{
	}

	/**
	 * @return the key
	 */
	public String getKey()
	{
		return key;
	}

	/**
	 * @param key the key to set
	 */
	public void setKey(String key)
	{
		this.key = key;
	}

	/**
	 * @return the sql
	 */
	public String getSql()
	{
		return sql;
	}

	/**
	 * @param sql
	 *            the sql to set
	 */
	public void setSql(String sql)
	{
		this.sql = sql;
	}

	/**
	 * @return the parameters
	 */
	public Object[] getParameters()
	{
		return parameters;
	}

	/**
	 * @param parameters
	 *            the parameters to set
	 */
	public void setParameters(Object[] parameters)
	{
		this.parameters = parameters;
	}

	/**
	 * @return the result
	 */
	public T getResult()
	{
		return result;
	}

	/**
	 * @param result
	 *            the result to set
	 */
	public void setResult(T result)
	{
		this.result = result;
	}
}