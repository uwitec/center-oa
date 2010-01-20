/**
 * File Name: Test.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2008-11-24<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.common;


import java.io.IOException;

import com.china.center.fileReader.ReaderFile;
import com.china.center.fileReader.ReaderFileFactory;


/**
 * Test
 * 
 * @author zhuzhu
 * @version 2008-11-24
 * @see Test
 * @since 1.0
 */
public class Test
{

    /**
     * @param args
     * @throws IOException
     */
    public static void main(String[] args)
        throws IOException
    {
        ReaderFile reader = ReaderFileFactory.getXLSReader();

        reader.readFile("c:/SKY公司客户资料模板OA.xls");

        while (reader.hasNext())
        {
            String[] ss = (String[])reader.next();

            for (String string : ss)
            {
                System.out.println(string);
            }
        }

        reader.close();
    }

}
