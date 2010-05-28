package com.china.center.jdbc.inter.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.china.center.jdbc.inter.Convert;
import com.china.center.jdbc.inter.IbatisDaoSupport;
import com.china.center.jdbc.inter.MyRowHandler;
import com.ibatis.sqlmap.client.event.RowHandler;

/**
 * @see IbatisDaoSupportImpl
 */

public class IbatisDaoSupportImpl extends SqlMapClientDaoSupport implements IbatisDaoSupport
{
	/**
	 * 转码类
	 */
	private Convert convertEncode = null;

	public Object queryForObject(String statementName)
	{
		try
		{
			return queryForObject(statementName, null);
		}
		catch (DataAccessException e)
		{
			throw new RuntimeException(e);
		}
		catch (IllegalArgumentException e)
		{
			throw new RuntimeException(e);
		}
	}

	public Object queryForObject(String statementName, Object parameterObject)
	{
		try
		{
			Object srcObj = convertEncode.encodeObject(parameterObject);
			return convertEncode.decodeObject(getSqlMapClientTemplate().queryForObject(
					statementName, srcObj));
		}
		catch (DataAccessException e)
		{
			throw new RuntimeException(e);
		}
		catch (IllegalArgumentException e)
		{
			throw new RuntimeException(e);
		}
		catch (IllegalAccessException e)
		{
			throw new RuntimeException(e);
		}
		catch (InvocationTargetException e)
		{
			throw new RuntimeException(e);
		}
	}

	public Object queryForObject(String statementName, Object parameterObject, Object resultObject)
	{
		try
		{
			Object srcObj = convertEncode.encodeObject(parameterObject);
			Object decodeObj = convertEncode.decodeObject(resultObject);
			return convertEncode.decodeObject(getSqlMapClientTemplate().queryForObject(
					statementName, srcObj, decodeObj));
		}
		catch (DataAccessException e)
		{
			throw new RuntimeException(e);
		}
		catch (IllegalArgumentException e)
		{
			throw new RuntimeException(e);
		}
		catch (IllegalAccessException e)
		{
			throw new RuntimeException(e);
		}
		catch (InvocationTargetException e)
		{
			throw new RuntimeException(e);
		}
	}

	public List queryForList(String statementName)
	{
		try
		{
			return queryForList(statementName, null);
		}
		catch (DataAccessException e)
		{
			throw new RuntimeException(e);
		}
		catch (IllegalArgumentException e)
		{
			throw new RuntimeException(e);
		}
	}

	public List queryForList(String statementName, Object parameterObject)
	{
		try
		{
			Object srcObj = convertEncode.encodeObject(parameterObject);
			return convertEncode.decodeList(getSqlMapClientTemplate().queryForList(statementName,
					srcObj));
		}
		catch (DataAccessException e)
		{
			throw new RuntimeException(e);
		}
		catch (IllegalArgumentException e)
		{
			throw new RuntimeException(e);
		}
		catch (IllegalAccessException e)
		{
			throw new RuntimeException(e);
		}
		catch (InvocationTargetException e)
		{
			throw new RuntimeException(e);
		}
	}

	public List queryForList(String statementName, int skipResults, int maxResults)
	{
		try
		{
			return queryForList(statementName, null, skipResults, maxResults);
		}
		catch (DataAccessException e)
		{
			throw new RuntimeException(e);
		}
		catch (IllegalArgumentException e)
		{
			throw new RuntimeException(e);
		}

	}

	public List queryForList(String statementName, Object parameterObject, int skipResults,
			int maxResults)
	{
		try
		{
			Object srcObj = convertEncode.encodeObject(parameterObject);
			return convertEncode.decodeList(getSqlMapClientTemplate().queryForList(statementName,
					srcObj, skipResults, maxResults));
		}
		catch (DataAccessException e)
		{
			throw new RuntimeException(e);
		}
		catch (IllegalArgumentException e)
		{
			throw new RuntimeException(e);
		}
		catch (IllegalAccessException e)
		{
			throw new RuntimeException(e);
		}
		catch (InvocationTargetException e)
		{
			throw new RuntimeException(e);
		}
	}

	public void queryWithRowHandler(String statementName, RowHandler rowHandler)
	{
		getSqlMapClientTemplate().queryWithRowHandler(statementName,
				new MyRowHandler(rowHandler, convertEncode));
	}

	public void queryWithRowHandler(String statementName, Object parameterObject,
			RowHandler rowHandler)
	{
		try
		{
			getSqlMapClientTemplate().queryWithRowHandler(statementName,
					convertEncode.encodeObject(parameterObject),
					new MyRowHandler(rowHandler, convertEncode));
		}
		catch (DataAccessException e)
		{
			throw new RuntimeException(e);
		}
		catch (IllegalArgumentException e)
		{
			throw new RuntimeException(e);
		}
		catch (IllegalAccessException e)
		{
			throw new RuntimeException(e);
		}
		catch (InvocationTargetException e)
		{
			throw new RuntimeException(e);
		}
	}

	public Map queryForMap(String statementName, Object parameterObject, String keyProperty)
	{
		try
		{
			Map tmpMap = getSqlMapClientTemplate().queryForMap(statementName,
					convertEncode.encodeObject(parameterObject), keyProperty);
			convertEncode.decodeObject(tmpMap.get(keyProperty));
			return tmpMap;
		}
		catch (DataAccessException e)
		{
			throw new RuntimeException(e);
		}
		catch (IllegalArgumentException e)
		{
			throw new RuntimeException(e);
		}
		catch (IllegalAccessException e)
		{
			throw new RuntimeException(e);
		}
		catch (InvocationTargetException e)
		{
			throw new RuntimeException(e);
		}
	}

	public Map queryForMap(String statementName, Object parameterObject, String keyProperty,
			String valueProperty)
	{
		try
		{
			Map tmpMap = getSqlMapClientTemplate().queryForMap(statementName,
					convertEncode.encodeObject(parameterObject), keyProperty, valueProperty);
			convertEncode.decodeObject((String) tmpMap.get(keyProperty));
			return tmpMap;
		}
		catch (DataAccessException e)
		{
			throw new RuntimeException(e);
		}
		catch (IllegalArgumentException e)
		{
			throw new RuntimeException(e);
		}
		catch (IllegalAccessException e)
		{
			throw new RuntimeException(e);
		}
		catch (InvocationTargetException e)
		{
			throw new RuntimeException(e);
		}
	}

	public Object insert(String statementName)
	{
		try
		{
			return insert(statementName, null);
		}
		catch (DataAccessException e)
		{
			throw new RuntimeException(e);
		}
		catch (IllegalArgumentException e)
		{
			throw new RuntimeException(e);
		}

	}

	public Object insert(String statementName, Object parameterObject)
	{
		try
		{
			Object generatedKey = getSqlMapClientTemplate().insert(statementName,
					convertEncode.encodeObject(parameterObject));
			return generatedKey == null ? "" : convertEncode.decodeObject(generatedKey);
		}
		catch (DataAccessException e)
		{
			throw new RuntimeException(e);
		}
		catch (IllegalArgumentException e)
		{
			throw new RuntimeException(e);
		}
		catch (IllegalAccessException e)
		{
			throw new RuntimeException(e);
		}
		catch (InvocationTargetException e)
		{
			throw new RuntimeException(e);
		}
	}

	public int update(String statementName, Object parameterObject)
	{
		try
		{
			return getSqlMapClientTemplate().update(statementName,
					convertEncode.encodeObject(parameterObject));
		}
		catch (DataAccessException e)
		{
			throw new RuntimeException(e);
		}
		catch (IllegalArgumentException e)
		{
			throw new RuntimeException(e);
		}
		catch (IllegalAccessException e)
		{
			throw new RuntimeException(e);
		}
		catch (InvocationTargetException e)
		{
			throw new RuntimeException(e);
		}
	}

	public void update(String statementName, Object parameterObject, int requiredRowsAffected)
	{
		try
		{
			getSqlMapClientTemplate().update(statementName,
					convertEncode.encodeObject(parameterObject), requiredRowsAffected);
		}
		catch (DataAccessException e)
		{
			throw new RuntimeException(e);
		}
		catch (IllegalArgumentException e)
		{
			throw new RuntimeException(e);
		}
		catch (IllegalAccessException e)
		{
			throw new RuntimeException(e);
		}
		catch (InvocationTargetException e)
		{
			throw new RuntimeException(e);
		}
	}

	public int delete(String statementName, Object parameterObject)
	{
		try
		{
			return getSqlMapClientTemplate().delete(statementName,
					convertEncode.encodeObject(parameterObject));
		}
		catch (DataAccessException e)
		{
			throw new RuntimeException(e);
		}
		catch (IllegalArgumentException e)
		{
			throw new RuntimeException(e);
		}
		catch (IllegalAccessException e)
		{
			throw new RuntimeException(e);
		}
		catch (InvocationTargetException e)
		{
			throw new RuntimeException(e);
		}
	}

	public void delete(String statementName, Object parameterObject, int requiredRowsAffected)
	{
		try
		{
			getSqlMapClientTemplate().delete(statementName,
					convertEncode.encodeObject(parameterObject), requiredRowsAffected);
		}
		catch (DataAccessException e)
		{
			throw new RuntimeException(e);
		}
		catch (IllegalArgumentException e)
		{
			throw new RuntimeException(e);
		}
		catch (IllegalAccessException e)
		{
			throw new RuntimeException(e);
		}
		catch (InvocationTargetException e)
		{
			throw new RuntimeException(e);
		}
	}

	/**
	 * @return Returns the convertEncode.
	 */
	public Convert getConvertEncode()
	{
		return convertEncode;
	}

	/**
	 * @param convertEncode
	 *            The convertEncode to set.
	 */
	public void setConvertEncode(Convert convertEncode)
	{
		this.convertEncode = convertEncode;
	}

}
