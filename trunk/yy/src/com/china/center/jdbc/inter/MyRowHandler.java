/*
 * 文件名：ISPRowHandler.java
 * 版权：Copyright 2002-2007 centerchina Tech. Co. Ltd. All Rights Reserved.
 * 描述：
 * 修改人：c60012340
 * 修改时间：2007-6-19
 * 跟踪单号：
 * 修改单号：
 * 修改内容：新增
 */
package com.china.center.jdbc.inter;


import java.lang.reflect.InvocationTargetException;

import com.ibatis.sqlmap.client.event.RowHandler;


/**
 * 〈一句话功能简述〉
 * 
 * @author c60012340
 * @version 2007-6-19
 * @see MyRowHandler
 * @since
 */

public class MyRowHandler implements RowHandler
{
    private Convert convertEncode = null;

    private RowHandler rowHandler = null;

    public MyRowHandler(RowHandler rowHandler, Convert convertEncode)
    {
        this.rowHandler = rowHandler;
        this.convertEncode = convertEncode;
    }

    public void handleRow(Object valueObject)
    {
        try
        {
            convertEncode.decodeObject(valueObject);
        }
        catch (IllegalArgumentException e)
        {
            e.printStackTrace();
        }
        catch (IllegalAccessException e)
        {
            e.printStackTrace();
        }
        catch (InvocationTargetException e)
        {
            e.printStackTrace();
        }
        rowHandler.handleRow(valueObject);
    }

}
