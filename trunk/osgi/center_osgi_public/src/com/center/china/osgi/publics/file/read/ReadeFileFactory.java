/*
 * �ļ���ReaderFileFactory.java
 * ��Ȩ��Copyright by www.center.china
 * ����
 * �����ˣ�zhu
 * ����ʱ�䣺2007-1-19
 */
package com.center.china.osgi.publics.file.read;

/**
 * constructor factory of ReaderFile class
 * 
 * @author zhuzhu
 * @version 2007-1-19
 * @see
 * @since
 */
public class ReadeFileFactory
{
    /**
     * default private constructor
     */
    private ReadeFileFactory()
    {}

    /**
     * read the special sheet
     * 
     * @param sheet
     *            special sheet begin 0
     * @return
     */
    public static ReaderFile getXLSReader(int sheet)
    {
        return new MyXLSReader(sheet);
    }

    public static ReaderFile getXLSReader()
    {
        return new MyXLSReader();
    }

    public static ReaderFile getTXTReader()
    {
        return new MyTXTReader();
    }

    public static ReaderFile getTXTReader(String charset)
    {
        return new MyTXTReader(charset);
    }

    public static ReaderFile getWORDReader()
    {
        return new MyWORDReader();
    }
}
