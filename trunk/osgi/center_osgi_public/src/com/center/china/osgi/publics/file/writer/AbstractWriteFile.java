/*
 * File Name: AbstractWriteFile.java
 * CopyRight: Copyright by www.center.china
 * Description:
 * Creater: zhuAchen
 * CreateTime: 2007-1-21
 * Grant: open source to everybody
 */
package com.center.china.osgi.publics.file.writer;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;


/**
 * the abstract class which implements WriteFile
 * 
 * @author zhuzhu
 * @version 2007-1-21
 * @see
 * @since
 */
public abstract class AbstractWriteFile implements WriteFile
{
    protected OutputStream out = null;

    protected int current = 0;

    public void close()
        throws IOException
    {
        if (out != null)
        {
            out.close();
        }
    }

    public void writeContent(String content)
        throws IOException
    {
        return;
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
    }

    public void openFile(OutputStream out)
        throws IOException
    {
        this.out = out;
    }

    public abstract void writeLine(String... columns)
        throws IOException;

    public void flush()
        throws IOException
    {
        out.flush();
    }
}
