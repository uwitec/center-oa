package com.china.center.jdbc.clone;

/**
 * DataClone
 * 
 * @author ZHUZHU
 * @version 2010-5-16
 * @see DataClone
 * @since 1.0
 */
public interface DataClone<T> extends Cloneable
{
    T clones();
}
