/*
 * �ļ���MyReadFile.java
 * ��Ȩ��Copyright by www.center.china
 * ����
 * �����ˣ�zhu
 * ����ʱ�䣺2007-1-19
 */
package com.center.china.osgi.publics.file.read;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


/**
 * TXT reader
 * 
 * @author zhuzhu
 * @version 2007-1-19
 * @see
 * @since
 */
public class MyTXTReader extends AbstractReaderFile
{
    private BufferedReader reader = null;

    private String DEFAULT_CHARSET = "GBK";

    /**
     * default constructor
     */
    public MyTXTReader(String charset)
    {
        this.DEFAULT_CHARSET = charset;
    }

    /**
     * default constructor
     */
    public MyTXTReader()
    {}

    /*
     * (non-Javadoc)
     * 
     * @see com.china.center.fileReader.AbstractReaderFile#hasNext()
     */
    @Override
    public boolean hasNext()
        throws IOException
    {
        // if hasReader is false, it show me that the inputstream is not be readed
        if ( !hasRead)
        {
            return false;
        }

        currentLine = reader.readLine();

        current++ ;

        if (currentLine != null)
        {
            return true;
        }

        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.china.center.fileReader.AbstractReaderFile#next()
     */
    @Override
    public Object next()
    {
        return currentLine;
    }

    @Override
    public void readFile(String filePath)
        throws IOException
    {
        File file = new File(filePath);
        readFile(file);
    }

    @Override
    public void readFile(File srcFile)
        throws IOException
    {
        inputStream = new FileInputStream(srcFile);

        readFile(inputStream);
    }

    @Override
    public void readFile(InputStream in)
        throws IOException
    {
        reader = new BufferedReader(new InputStreamReader(in, DEFAULT_CHARSET));

        hasRead = true;
    }

    @Override
    public void close()
        throws IOException
    {
        if (reader != null)
        {
            reader.close();
        }

        if (inputStream != null)
        {
            inputStream.close();
        }
    }
}
