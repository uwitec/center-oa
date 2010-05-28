/**
 * File Name: CMDTools.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2008-2-17<br>
 * Grant: open source to everybody
 */
package com.china.center.tools;


import java.io.BufferedReader;
import java.io.InputStreamReader;


/**
 * <描述>
 *
 * @author ZHUZHU
 * @version 2008-2-17
 * @see
 * @since
 */
public abstract class CMDTools
{
    public static void cmdAndShow(String cmdline)
    {
        try
        {
            String line;
            Process p = Runtime.getRuntime().exec(cmdline);

            BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));

            while ( (line = input.readLine()) != null)
            {
                System.out.println(line);
            }
            input.close();
        }
        catch (Exception err)
        {
            err.printStackTrace();
        }
    }

    public static boolean cmd(String cmdline)
    {
        try
        {
            Runtime.getRuntime().exec(cmdline);
        }
        catch (Exception err)
        {
            return false;
        }

        return true;
    }
}
