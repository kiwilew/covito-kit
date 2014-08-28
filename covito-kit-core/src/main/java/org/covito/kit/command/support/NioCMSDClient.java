package org.covito.kit.command.support;

import java.io.ByteArrayOutputStream;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class NioCMSDClient {

	public static String sendCmd(String ip, int port, String cmd) {
		try {
			// 建立到服务端的链接
			SocketAddress address = new InetSocketAddress(ip, port);
			SocketChannel client = SocketChannel.open(address);
			
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
			// 输出缓冲区的数据
			for (int i = 0; i < buffer.array().length; i++) {
				System.out.println(buffer.array()[i]);
			}
			return "";
		} catch (Exception e) {
			return "Cmd exception:" + e.getMessage();
		}
	}
}
