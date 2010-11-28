/**
 *
 */
package com.china.center.tools;


import javax.servlet.http.HttpServletRequest;


/**
 * @author Administrator
 */
public abstract class RequestTools
{
    public static String getRootUrl(HttpServletRequest request)
    {
        String scheme = request.getScheme();

        String serverName = request.getServerName();

        String serverPort = String.valueOf(request.getServerPort());

        return scheme + "://" + serverName + ':' + serverPort + "/";
    }

    /**
     * 在action里面转到另一个方法时候的初始化查询标识
     * 
     * @param request
     */
    public static void actionInitQuery(HttpServletRequest request)
    {
        request.setAttribute("forward", "1");
    }

    /**
     * 从parameter和attribute里面获取属性(parameter优先)
     * 
     * @param request
     * @param key
     * @return
     */
    public static String getValueFromRequest(HttpServletRequest request, String key)
    {
        // 这里是过滤
        String keyValue = request.getParameter(key);

        if (StringTools.isNullOrNone(keyValue))
        {
            Object attribute = request.getAttribute(key);

            if (attribute != null)
            {
                keyValue = attribute.toString();
            }
        }

        return keyValue;
    }
}
