package org.covito.kit.command.support;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

public class SocketCMDClient {

	public static String sendCmd(String ip, int port, String cmd) {
		try {
			Socket socket = new Socket();
			socket.connect(new InetSocketAddress(ip, port), 5000);
			InputStream masterInputStream = socket.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(masterInputStream));
			OutputStream masterOutputStream = socket.getOutputStream();
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(masterOutputStream));
			writer.write(cmd + "\n");
			writer.write("q\n");
			writer.flush();
			String result = "";
			int c;
			while ((c = reader.read()) != -1) {
				result += (char) c;
				// /System.out.println(line);
			}
			socket.close();
			// 把提示符去掉
			String start = "";
			String end = "";
			int len = result.length();
			for (int i = 0; i < 20; i++) {
				if (i > len / 2)
					break;
				start += result.charAt(i);
				end = result.charAt(len - i - 1) + end;
				// System.out.println(start +"-->"+ end);
				if (i >= 2 && start.equals(end)) {
					result = result.substring(i + 1, len - i - 1);
					break;
				}
			}
			return result;
		} catch (IOException e) {
			return "Cmd exception:" + e.getMessage();
		}
	}

	public static void main(String[] args) throws IOException {
		
		for (int i = 0; i < 10; i++) {
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			String c = br.readLine();
			System.out.println(sendCmd("127.0.0.1", 12345, c));
		}
	}
}
