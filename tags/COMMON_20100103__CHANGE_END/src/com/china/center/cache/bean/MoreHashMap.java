/**
 *
 */
package com.china.center.cache.bean;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Administrator
 *
 */
public class MoreHashMap<K, V, T> implements Serializable
{
	private Map<K, V> map1 = null;

	private Map<K, T> map2 = null;

	/**
	 *
	 */
	public MoreHashMap()
	{
		map1 = new HashMap<K, V>();

		map2 = new HashMap<K, T>();
	}

	/**
	 *
	 */
	public MoreHashMap(int size)
	{
		map1 = new HashMap<K, V>(size);

		map2 = new HashMap<K, T>(size);
	}

	public void put(K key, V value1, T value2)
	{
		map1.put(key, value1);

		map2.put(key, value2);
	}

	public boolean containsKey(K key)
	{
		return map1.containsKey(key) || map2.containsKey(key);
	}

	public boolean isEmpty()
	{
		return map1.isEmpty() && map2.isEmpty();
	}

	public Set<K> keySet()
	{
		return map1.keySet();
	}

	public String toString()
	{
		return "[" + this.map1.toString() + ", " + this.map2.toString() + "]";
	}

	public void remove(K key)
	{
		map1.remove(key);

		map2.remove(key);
	}

	public V getValue1(K key)
	{
		return map1.get(key);
	}

	public T getValue2(K key)
	{
		return map2.get(key);
	}

	public void clear()
	{
		map1.clear();
		map2.clear();
	}

	public int size()
	{
		return Math.max(map1.size(), map2.size());
	}

}
