/*
 * File Name: MyTXTWriteFile.java
 * CopyRight: Copyright by www.center.china
 * Description:
 * Creater: zhuAchen
 * CreateTime: 2007-1-21
 * Grant: open source to everybody
 */
package com.center.china.osgi.publics.file.writer;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;


/**
 * <����>
 * 
 * @author zhuzhu
 * @version 2007-1-21
 * @see
 * @since
 */
public class MyTXTWriteFile extends AbstractWriteFile
{
    private BufferedWriter writer = null;

    private String DEFAULT_CHARSET = "GBK";

    /**
     * default constructor
     */
    public MyTXTWriteFile()
    {}

    /**
     * constructor
     */
    public MyTXTWriteFile(String charset)
    {
        this.DEFAULT_CHARSET = charset;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.china.center.fileWriter.AbstractWriteFile#writeNextLine(java.lang.String[])
     */
    @Override
    public void writeLine(String... columns)
        throws IOException
    {
        if (columns == null || columns.length == 0)
        {
            return;
        }

        writer.write(columns[0]);

        writer.write(System.getProperty("line.separator"));

        current++ ;

        if (current % 100 == 0)
        {
            writer.flush();
        }
    }

    public void openFile(String filePath)
        throws IOException
    {
        File file = new File(filePath);
        openFile(file);
    }

    public void openFile(File srcFile)
        throws IOException
    {
        out = new FileOutputStream(srcFile);
        writer = new BufferedWriter(new OutputStreamWriter(out, DEFAULT_CHARSET));
    }

    public void openFile(OutputStream out)
        throws IOException
    {
        this.out = out;
        writer = new BufferedWriter(new OutputStreamWriter(out, DEFAULT_CHARSET));
    }

    public void close()
        throws IOException
    {
        IOException e1 = null;
        if (writer != null)
        {
            try
            {
                writer.close();
            }
            catch (IOException e)
            {
                e1 = e;
            }
        }

        if (out != null)
        {
            try
            {
                out.close();
            }
            catch (IOException e)
            {
                e1 = e;
            }
        }

        if (e1 != null)
        {
            throw e1;
        }
    }

}
