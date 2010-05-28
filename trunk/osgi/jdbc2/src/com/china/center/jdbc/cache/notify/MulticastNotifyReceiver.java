/**
 *
 */
package com.china.center.jdbc.cache.notify;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.china.center.jdbc.cache.CacheManager;

/**
 * Multicast 通知
 *
 * @author Administrator
 *
 */
public class MulticastNotifyReceiver extends Thread
{
	private static final Log _logger = LogFactory.getLog(MulticastNotifyReceiver.class.getName());

	private InetAddress group = null;

	private int port = 10221;

	private boolean stop = false;

	private MulticastSocket socket;

	private CacheManager cacheManager = null;

	public MulticastNotifyReceiver(InetAddress group, int port, CacheManager cacheManager)
	{
		this.group = group;
		this.port = port;
		this.cacheManager = cacheManager;

		try
		{
			socket = new MulticastSocket(this.port);

			socket.joinGroup(this.group);
		}
		catch (IOException e)
		{
			_logger.error(e, e);
		}
	}

	public void close()
	{
		stop = true;

		try
		{
			if (socket != null && !socket.isClosed())
			{
				try
				{
					socket.leaveGroup(group);
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
				socket.leaveGroup(group);
			}
			catch (IOException ex)
			{
				_logger.error(e, e);
			}

			socket.close();
		}

		this.interrupt();
	}

	public void run()
	{
		while (!stop)
		{
			byte[] buffer = new byte[256];

			DatagramPacket dgp = new DatagramPacket(buffer, buffer.length);

			try
			{
				socket.receive(dgp);
			}
			catch (IOException e)
			{
				_logger.error(e, e);
			}

			byte[] buffer2 = new byte[dgp.getLength()];

			System.arraycopy(dgp.getData(), 0, buffer2, 0, dgp.getLength());

			String notify = new String(buffer2);

			if (_logger.isDebugEnabled())
			{
				_logger.debug("receive MulticastSocket notify:" + notify);
			}

			this.cacheManager.noyify(notify);

			if (_logger.isDebugEnabled())
			{
				_logger.debug("process MulticastSocket end:" + notify);
			}

		}
	}
}
