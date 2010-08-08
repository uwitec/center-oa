/*
 * �ļ���AbstractReaderFile.java
 * ��Ȩ��Copyright by www.center.china
 * ����
 * �����ˣ�zhu
 * ����ʱ�䣺2007-1-19
 */
package com.center.china.osgi.publics.file.read;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;


/**
 * <����>
 * 
 * @author zhuzhu
 * @version 2007-1-19
 * @see
 * @since
 */
public abstract class AbstractReaderFile implements ReaderFile
{
    /**
     * Ĭ�Ϲ�����
     */
    protected AbstractReaderFile()
    {}

    protected int current = 0;

    protected String currentLine = null;

    protected InputStream inputStream = null;

    protected boolean hasRead = false;

    protected int rows = 0;

    /*
     * (non-Javadoc)
     * 
     * @see com.china.center.fileReader.ReaderFile#getCurrentLineNumber()
     */
    public int getCurrentLineNumber()
    {
        return current;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.china.center.fileReader.ReaderFile#hasNext()
     */
    public abstract boolean hasNext()
        throws IOException;

    /*
     * (non-Javadoc)
     * 
     * @see com.china.center.fileReader.ReaderFile#next()
     */
    public abstract Object next();

    public abstract void readFile(String filePath)
        throws IOException;

    public abstract void readFile(File srcFile)
        throws IOException;

    public String getContent()
        throws Exception
    {
        return null;
    }

    public void readFile(InputStream in)
        throws IOException
    {
        inputStream = in;
    }

    public void close()
        throws IOException
    {
        if (inputStream != null)
        {
            inputStream.close();
        }
    }
}
