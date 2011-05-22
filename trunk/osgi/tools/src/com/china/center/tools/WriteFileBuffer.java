/**
 * File Name: FileRWTools.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2011-5-22<br>
 * Grant: open source to everybody
 */
package com.china.center.tools;


import java.io.IOException;

import com.center.china.osgi.publics.file.writer.WriteFile;


/**
 * FileRWTools
 * 
 * @author ZHUZHU
 * @version 2011-5-22
 * @see WriteFileBuffer
 * @since 3.0
 */
public class WriteFileBuffer
{
    private WriteFile writeFile = null;

    private StringBuffer sb = new StringBuffer();

    private String split = ",";

    /**
     * default constructor
     */
    public WriteFileBuffer(WriteFile writeFile)
    {
        this.writeFile = writeFile;
    }

    public WriteFileBuffer writeColumn(String content)
    {
        sb.append(StringTools.getExportString(content)).append(this.split);

        return this;
    }

    public void reset()
    {
        if (sb.length() > 0)
        {
            sb.delete(0, sb.length());
        }
    }

    public WriteFileBuffer writeColumn(int content)
    {
        sb.append(content).append(this.split);

        return this;
    }

    public void writeLine()
        throws IOException
    {
        String content = sb.toString();

        if (content.endsWith(","))
        {
            content = content.substring(0, content.length() - 1);
        }

        this.writeFile.writeLine(content);

        this.reset();
    }

    public WriteFileBuffer writeColumn(double content)
    {
        sb.append(MathTools.formatNum(content)).append(this.split);

        return this;
    }

    /**
     * @return the writeFile
     */
    public WriteFile getWriteFile()
    {
        return writeFile;
    }

    /**
     * @param writeFile
     *            the writeFile to set
     */
    public void setWriteFile(WriteFile writeFile)
    {
        this.writeFile = writeFile;
    }

    /**
     * @return the split
     */
    public String getSplit()
    {
        return split;
    }

    /**
     * @param split
     *            the split to set
     */
    public void setSplit(String split)
    {
        this.split = split;
    }
}
