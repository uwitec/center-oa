/*
 * File Name: WriteFile.java
 * CopyRight: Copyright by www.center.china
 * Description:
 * Creater: zhuAchen
 * CreateTime: 2007-1-21
 * Grant: open source to everybody
 */
package com.center.china.osgi.publics.file.writer;


import java.io.File;
import java.io.IOException;
import java.io.OutputStream;


/**
 * the inyerface of write file<br>
 * beacuse txt,xls,word,pdf have different writer format<br>
 * so the interface support some simple method to write file
 * 
 * @author zhuzhu
 * @version 2007-1-21
 * @see
 * @since
 */
public interface WriteFile
{
    /**
     * write file from a special path
     * 
     * @param filePath
     *            special the file path
     * @throws IOException
     */
    void openFile(String filePath)
        throws IOException;

    /**
     * write file from a special file
     * 
     * @param srcFile
     *            special file
     * @throws IOException
     */
    void openFile(File srcFile)
        throws IOException;

    /**
     * write file from a special OutputStream
     * 
     * @param out
     *            special OutputStream
     * @throws IOException
     */
    void openFile(OutputStream out)
        throws IOException;

    /**
     * wirte content to file <br>
     * the method only for <font color = red>word and pdf</font>
     * 
     * @param content
     * @return
     */
    void writeContent(String content)
        throws IOException;

    /**
     * write a line to file<br>
     * if file type is txt,the columns length is 1<br>
     * if file type is xls,because the xls must construct all cells<br>
     * so the max cells has better less than 120000, when your computer memory is 512MB
     * 
     * @param columns
     * @return void
     * @throws IOException
     */
    void writeLine(String... columns)
        throws IOException;

    /**
     * Close the reader<br>
     * If you use the inteface,you must use the method when you end
     * 
     * @throws IOException
     */
    void close()
        throws IOException;

    void flush()
        throws IOException;
}
