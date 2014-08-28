package org.covito.kit.command.support;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

import org.covito.kit.command.BaseCMDServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NioCMDServer extends BaseCMDServer {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private ServerSocketChannel serverSocketChannel;

	private int port; // 监听端口

	private String address; // 监听地址

	private Selector selector;
	
	private Thread thread = new Thread(new Runnable() {
		@Override
		public void run() {
			while (true) {
				listen();
			}
		}
		
	});

	public void startServer() {
		try {
			serverSocketChannel = ServerSocketChannel.open();
			// 服务器配置为非阻塞
			serverSocketChannel.configureBlocking(false);
			ServerSocket serverSocket = serverSocketChannel.socket();
			serverSocket.setReuseAddress(true);
			serverSocket.bind(new InetSocketAddress(address, port));
			selector = Selector.open();
			serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
			thread.start();
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (ClosedChannelException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void listen() {
		try {
			if (selector.select(10) == 0) {
				return;
			}
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}

		Set<SelectionKey> selectedKeys = selector.selectedKeys();
		Iterator<SelectionKey> it = selectedKeys.iterator();
		while (it.hasNext()) {
			SelectionKey key = (SelectionKey) it.next();
			it.remove();
			try {
				if (key.isValid() && key.isAcceptable()) {
					ServerSocketChannel server = (ServerSocketChannel) key.channel();
					// 此方法返回的套接字通道（如果有）将处于阻塞模式。
					SocketChannel client = server.accept();
					// 配置为非阻塞
					client.configureBlocking(false);
					// 注册到selector，等待连接
					client.register(selector, SelectionKey.OP_READ);
				} else if (key.isValid() && key.isReadable()) {
					// 返回为之创建此键的通道。
					SocketChannel client = (SocketChannel) key.channel();

					ByteBuffer buffer = ByteBuffer.allocate(1024);
					// 将缓冲区清空以备下次读取
					buffer.clear();
					ByteArrayOutputStream bo = new ByteArrayOutputStream();
					while (true) {
						int i = client.read(buffer);
						if (i == -1) {
							break;
						} else {
							bo.write(buffer.array(), 0, i);
						}
					}
					buffer.flip();
					System.out.println(bo.toString());
				} else if (key.isValid() && key.isWritable()) {
					// 获取此通道的SocketChannel
					SocketChannel client = (SocketChannel) key.channel();
					ByteBuffer output = (ByteBuffer) key.attachment();
					// 如果缓存区没了,重置一下
					if (!output.hasRemaining()) {
						output.rewind();
					}
					// 在此通道内写东西
					client.write(output);
				}
			} catch (Exception e) {
				logger.error(e.getMessage());
				key.cancel();
			} finally {
				try {
					key.channel().close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}
}
