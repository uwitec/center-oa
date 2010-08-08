/*
 * File Name: FileReader.java
 * CopyRight: Copyright by www.center.china
 * Description:
 * Creater: zhuAchen
 * CreateTime: 2007-1-19
 * Grant: open source to everybody
 */
package com.center.china.osgi.publics.file.read;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;


/**
 * the interface of reade file
 * 
 * @author zhuzhu
 * @version 2007-1-19
 * @see
 * @since
 */
public interface ReaderFile
{
    /**
     * define if file has next line
     * 
     * @return boolean
     */
    boolean hasNext()
        throws IOException;

    /**
     * Return the next line, if next line is null ,return null<br>
     * If you use the next,you must use <font color = red>hasNext</font> befor this<br>
     * Or you will get the same object
     * 
     * @return Object
     */
    Object next();

    /**
     * return the current line number
     * 
     * @return int
     */
    int getCurrentLineNumber();

    /**
     * Specila for read word and pdf only<br>
     * Other use the method will return null *
     * 
     * @throws Exception
     */
    String getContent()
        throws Exception;

    /**
     * Read file from a special path
     * 
     * @param filePath
     *            special the file path
     * @throws IOException
     */
    void readFile(String filePath)
        throws IOException;

    /**
     * Read file from a special file
     * 
     * @param srcFile
     *            special file
     * @throws IOException
     */
    void readFile(File srcFile)
        throws IOException;

    /**
     * Read file from a special file
     * 
     * @param srcFile
     *            special file
     * @throws IOException
     */
    void readFile(InputStream in)
        throws IOException;

    /**
     * Close the reader<br>
     * If you use the inteface,you must use the method when you end
     * 
     * @throws IOException
     */
    void close()
        throws IOException;
}
