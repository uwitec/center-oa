/**
 *
 */
package com.china.center.tools;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Administrator
 *
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
}
