/*
 * File Name: MyTXTWriteFile.java
 * CopyRight: Copyright by www.center.china
 * Description:
 * Creater: zhuAchen
 * CreateTime: 2007-1-21
 * Grant: open source to everybody
 */
package com.center.china.osgi.publics.file.writer;


import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.poifs.filesystem.DirectoryEntry;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;


/**
 * <����>
 * 
 * @author zhuzhu
 * @version 2007-1-21
 * @see
 * @since
 */
public class MyWordWriteFile extends AbstractWriteFile
{
    private String DEFAULT_CHARSET = "GBK";

    /**
     * default constructor
     */
    public MyWordWriteFile()
    {

    }

    /**
     * constructor
     */
    public MyWordWriteFile(String charset)
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
        throw new UnsupportedOperationException("MyWordWriteFile do not support writeLine method");
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

    /**
     * write Content to word
     */
    public void writeContent(String content)
        throws IOException
    {
        byte b[] = content.getBytes(DEFAULT_CHARSET);

        ByteArrayInputStream bais = new ByteArrayInputStream(b);

        try
        {
            POIFSFileSystem fs = new POIFSFileSystem();

            DirectoryEntry directory = fs.getRoot();

            directory.createDocument("WordDocument", bais);

            fs.writeFilesystem(out);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (bais != null)
            {
                bais.close();
            }
        }
    }
}
