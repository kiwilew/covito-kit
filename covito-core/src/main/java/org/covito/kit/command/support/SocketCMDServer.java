package org.covito.kit.command.support;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.covito.kit.cache.CacheManager;
import org.covito.kit.cache.command.CacheInfoCmd;
import org.covito.kit.cache.support.MapCache;
import org.covito.kit.command.BaseCMDServer;
import org.covito.kit.command.Command;
import org.covito.kit.command.CommandManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SocketCMDServer extends BaseCMDServer {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private int port; // 监听端口

	private String address; // 监听地址

	boolean keepAlive = true; // 是否允许客户端保持连接

	protected final BlockingQueue<Runnable> processQueue = new LinkedBlockingQueue<Runnable>(50); // 处理队列(全局公用)

	protected final ThreadPoolExecutor threadPool = new ThreadPoolExecutor(10, 50, 30,
			TimeUnit.SECONDS, processQueue, new ThreadPoolExecutor.DiscardOldestPolicy()); // 处理线程池(全局共用)

	ServerSocket serverSocket = null;

	/**
	 * 开始提供服务
	 */
	public void startServer() {
		if (serverSocket != null) {
			return;
		}
		try {
			serverSocket = new ServerSocket();
			serverSocket.setReuseAddress(true);
			serverSocket.bind(new InetSocketAddress(InetAddress.getByName(address), port), 50);
			thread.start();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
		}
	}

	private Thread thread = new Thread(new Runnable() {
		@Override
		public void run() {
			while (true) {
				try {
					final Socket socket = serverSocket.accept();
					threadPool.execute(new Runnable() {
						@Override
						public void run() {
							BufferedReader in = null;
							OutputStream os=null;
							try {
								in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
								os=socket.getOutputStream();
								while(true){
									String cmd = in.readLine();
									if (cmd == null) {
										return;
									}
									processCMD(cmd, os);
								}
							} catch (IOException e) {
							} finally{
								try {
									socket.close();
									if(in!=null){
										in.close();
									}
									if(os!=null){
										os.close();
									}
								} catch (IOException e) {
									e.printStackTrace();
								}
							}
						}
					});
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
				}
			}
		}
	}); // Accept线程

	/**
	 * 构造
	 * 
	 * @param address
	 *            要监听的地址
	 * @param port
	 *            要监听的端口
	 */
	public SocketCMDServer(String address, int port) {
		this(address, port, true);
	}

	/**
	 * 构造
	 * 
	 * @param address
	 *            要监听的地址
	 * @param port
	 *            要监听的端口
	 * @param keepAlive
	 *            执行完一个命令后是否保持连接
	 */
	public SocketCMDServer(String address, int port, boolean keepAlive) {
		this(address, port, keepAlive, "");
	}

	/**
	 * 构造
	 * 
	 * @param address
	 *            要监听的地址
	 * @param port
	 *            要监听的端口
	 * @param keepAlive
	 *            执行完一个命令后是否保持连接
	 * @param prompt
	 *            命令行提示
	 */
	public SocketCMDServer(String address, int port, boolean keepAlive, String prompt) {
		this(address, port, keepAlive, prompt, "GBK");
	}

	public SocketCMDServer(String address, int port, boolean keepAlive, String prompt,
			String encoding) {
		this.address = address;
		this.port = port;

		CommandManager.addCommand("quit,q", new Command() {
			public void execute(String[] argv, PrintWriter out) {
				out.flush();
				out.close();
			}

			@Override
			public String getInfo() {
				return "close this connection.";
			}

			@Override
			public String getUsage() {
				return "";
			}
		});

		this.keepAlive = keepAlive;
		this.prompt = prompt;
		this.encoding = encoding;
	}


}
