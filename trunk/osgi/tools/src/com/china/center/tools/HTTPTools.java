/**
 * File Name: HttpToos.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2009-4-24<br>
 * Grant: open source to everybody
 */
package com.china.center.tools;


import javax.servlet.http.HttpServletRequest;


/**
 * HttpToos
 * 
 * @author ZHUZHU
 * @version 2009-4-24
 * @see HTTPTools
 * @since 1.0
 */
public abstract class HTTPTools
{
    /**
     * get http url<br>
     * eg:http://127.0.0.1:8080/
     * @param request
     * @return
     */
    public static String getHTTPURL(HttpServletRequest request)
    {
        String scheme = request.getScheme();
        String serverName = request.getServerName();
        String serverPort = String.valueOf(request.getServerPort());

        // http://127.0.0.1:8080/
        return scheme + "://" + serverName + ':' + serverPort + "/";
    }
}
