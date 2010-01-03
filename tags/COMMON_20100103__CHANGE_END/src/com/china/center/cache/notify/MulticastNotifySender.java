/**
 *
 */
package com.china.center.cache.notify;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Multicast ·¢ËÍ
 *
 * @author Administrator
 *
 */
public class MulticastNotifySender
{
	private static final Log _logger = LogFactory.getLog(MulticastNotifySender.class.getName());

	private InetAddress groupMulticastAddress;

	private int groupMulticastPort;

	private int timeToLive;

	private MulticastSocket socket = null;

	public MulticastNotifySender(InetAddress multicastAddress, int multicastPort, int timeToLive)
	{
		this.groupMulticastAddress = multicastAddress;

		this.groupMulticastPort = multicastPort;

		this.timeToLive = timeToLive;
	}

	public void sendNotify(String notigfy)
	{
		try
		{
			socket = new MulticastSocket(groupMulticastPort);

			socket.setTimeToLive(timeToLive);

			socket.joinGroup(groupMulticastAddress);

			byte[] buffer = notigfy.getBytes();

			DatagramPacket packet = new DatagramPacket(buffer, buffer.length,
					groupMulticastAddress, groupMulticastPort);

			socket.send(packet);

			if (_logger.isDebugEnabled())
			{
				_logger.debug("send MulticastSocket notify:" + notigfy);
			}
		}
		catch (IOException e)
		{
			_logger.error(e, e);
		}
		catch (Throwable e)
		{
			_logger.error(e, e);
		}
		finally
		{
			closeSocket();
		}

	}

	private void closeSocket()
	{
		try
		{
			if (socket != null && !socket.isClosed())
			{
				try
				{
					socket.leaveGroup(groupMulticastAddress);
				}
				catch (IOException e)
				{
					_logger.error(e, e);
				}
				socket.close();
			}
		}
		catch (NoSuchMethodError e)
		{
			try
			{
				socket.leaveGroup(groupMulticastAddress);
			}
			catch (IOException ex)
			{
				_logger.error(e, e);
			}
			socket.close();
		}
	}
}
