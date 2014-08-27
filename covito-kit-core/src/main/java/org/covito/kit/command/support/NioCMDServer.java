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
			throw new IllegalStateException(e);
		}

		Set<SelectionKey> selectedKeys = selector.selectedKeys();
		Iterator<SelectionKey> it = selectedKeys.iterator();
		while (it.hasNext()) {
			SelectionKey key = (SelectionKey) it.next();
			it.remove();
			ServerSocketChannel server = (ServerSocketChannel) key.channel();  
			try {
				if (key.isValid() && key.isAcceptable()) {
					 // 返回为之创建此键的通道。  
		            // 接受到此通道套接字的连接。  
		            // 此方法返回的套接字通道（如果有）将处于阻塞模式。  
		            SocketChannel client = server.accept();  
		            // 配置为非阻塞  
		            client.configureBlocking(false);  
		            // 注册到selector，等待连接  
		            client.register(selector, SelectionKey.OP_READ);  
				}else if (key.isValid() && key.isReadable()) {
					 // 返回为之创建此键的通道。  
					SocketChannel client = (SocketChannel) key.channel();  
					
					ByteBuffer buffer  = ByteBuffer.allocate(4096);
		            //将缓冲区清空以备下次读取  
					buffer.clear();  
					ByteArrayOutputStream bo=new ByteArrayOutputStream();
		            while (true){
		            	int i=client.read(buffer);
                        if (i == -1){
                        	break;
                        }else{
                        	bo.write(buffer.array(), 0, i);
                        }
                    }
		            buffer.flip();
		            System.out.println(bo.toString());
				}else if (key.isValid() && key.isWritable()) {
					
				}
			} catch (Exception e) {
				
			} finally{
	            key.cancel();
			}
		}

	}
}
