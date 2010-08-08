/*
 * File Name: MyWORDReadFile.java
 * CopyRight: Copyright by www.center.china
 * Description:
 * Creater: zhuAchen
 * CreateTime: 2007-1-19
 * Grant: open source to everybody
 */
package com.center.china.osgi.publics.file.read;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.textmining.text.extraction.WordExtractor;


/**
 * use org.textmining.text to read word
 * 
 * @author zhuzhu
 * @version 2007-1-19
 * @see
 * @since
 */
public class MyWORDReader extends AbstractReaderFile
{
    private String text = null;

    private WordExtractor extractor = null;

    /**
     * default constructor
     */
    public MyWORDReader()
    {
        extractor = new WordExtractor();
    }

    @Override
    public String getContent()
        throws Exception
    {
        if ( !hasRead)
        {
            text = extractor.extractText(inputStream);
            hasRead = true;
        }

        return text;
    }

    @Override
    public boolean hasNext()
        throws IOException
    {
        return false;
    }

    @Override
    public Object next()
    {
        return null;
    }

    @Override
    public void readFile(String filePath)
        throws IOException
    {
        inputStream = new FileInputStream(filePath);
    }

    @Override
    public void readFile(File srcFile)
        throws IOException
    {
        inputStream = new FileInputStream(srcFile);
    }

}
