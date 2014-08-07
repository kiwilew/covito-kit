package org.covito.kit.command;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.covito.kit.cache.CacheManager;
import org.covito.kit.cache.command.CacheInfoCmd;
import org.covito.kit.cache.support.MapCache;
import org.covito.kit.utility.Pair;
import org.covito.kit.utility.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SocketAdminServer extends AbsAdminServer {

	private final Logger log = LoggerFactory.getLogger(SocketAdminServer.class);

	private int port; // 监听端口

	private String address; // 监听地址

	boolean keepAlive = true; // 是否允许客户端保持连接

	protected final BlockingQueue<Runnable> processQueue = new LinkedBlockingQueue<Runnable>(50); // 处理队列(全局公用)
	
	protected final ThreadPoolExecutor threadPool = new ThreadPoolExecutor(10, 50, 30,
			TimeUnit.SECONDS, processQueue, new ThreadPoolExecutor.DiscardOldestPolicy()); // 处理线程池(全局共用)


	private Thread thread = new Thread(new Runnable() {
		@Override
		public void run() {
			try {
				while (true) {
					try {
						//threadPool.execute();
					} catch (Exception e) {
						log.error("", e);
					}
				}
			} catch (Exception e) {
				log.error(e.getMessage(), e);
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
	public SocketAdminServer(String address, int port) {
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
	public SocketAdminServer(String address, int port, boolean keepAlive) {
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
	public SocketAdminServer(String address, int port, boolean keepAlive, String prompt) {
		this(address, port, keepAlive, prompt, "GBK");
	}

	public SocketAdminServer(String address, int port, boolean keepAlive, String prompt, String encoding) {
		this.address = address;
		this.port = port;

		addCommand("quit,q", new Command() {
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

	/**
	 * 管理线程
	 */
	public void run() {
		ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket();
			serverSocket.setReuseAddress(true);
			InetSocketAddress addr = new InetSocketAddress(InetAddress.getByName(address), port);
			serverSocket.bind(addr, 50);
			while (true) {
				Socket socket = null;
				try {
					socket = serverSocket.accept();
					threadPool.execute(new AdminConnection(this, socket));
				} catch (Exception e) {
					log.error("", e);
				}
			}

		} catch (Exception e) {
			log.error(address + ":" + port, e);
		} finally {
			if (serverSocket != null) {
				try {
					serverSocket.close();
				} catch (IOException e) {

				}
			}
		}
	}

	public static void main(String[] argv) {
		SocketAdminServer as = new SocketAdminServer("0.0.0.0", 12345, true, "admin> ");
		Command cmd = new Command() {
			public void execute(String[] argv, PrintWriter out) {
				out.println("test argv: " + Arrays.toString(argv));
			}

			@Override
			public String getInfo() {
				return "test command.";
			}

			@Override
			public String getUsage() {
				return "test [option]";
			}
		};
		as.addCommand("test", cmd);
		MapCache<String, String> ca = new MapCache<String, String>("cach");
		MapCache<String, String> ca1 = new MapCache<String, String>("cache");
		CacheManager.addCache(ca);
		CacheManager.addCache(ca1);
		CacheManager.getCache("cach").put("a", "dd");
		
		Command cache =new CacheInfoCmd();
		as.addCommand("cache", cache);
		as.run();
	}

}
