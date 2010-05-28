/**
 * File Name: FireFoxTools.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2008-9-15<br>
 * Grant: open source to everybody
 */
package com.china.center.tools;

/**
 * <描述>
 * 
 * @author ZHUZHU
 * @version 2008-9-15
 * @see
 * @since
 */
public class FireFoxTools
{

    /**
     * @param args
     */
    public static void main(String[] args)
    {
        String[] pars = new String[] {"productName", "num"};

        for (String each : pars)
        {
            System.out.println("HTMLElement.prototype.__defineGetter__('" + each
                               + "',function(){ ");

            System.out.println("return this.getAttribute('" + each + "'); ");

            System.out.println("}); ");

            System.out.println("HTMLElement.prototype.__defineSetter__('" + each
                               + "',function(){ ");

            System.out.println("this.setAttribute('" + each + "', sText);");

            System.out.println("return sText; ");

            System.out.println("}); ");
            
            System.out.println();
        }
    }
}
