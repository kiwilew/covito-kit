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

import org.covito.kit.utility.Pair;
import org.covito.kit.utility.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdminServer implements Runnable {

	private List<CmdInfo> cmdinfos = new CopyOnWriteArrayList<CmdInfo>(); // 命令信息列表

	private Map<String, Command> cmds = new ConcurrentHashMap<String, Command>(); // 命令名（长或短）和命令接口的对应关系

	private int port; // 监听端口

	private String address; // 监听地址

	private Thread thread = new Thread(this); // Accept线程

	boolean keepAlive = true; // 是否允许客户端保持连接

	String prompt = ""; // 提示符

	String encoding = "GBK"; // 字符编码

	private static final BlockingQueue<Runnable> processQueue = new LinkedBlockingQueue<Runnable>(); // 处理队列(全局公用)

	private static final ThreadPoolExecutor threadPool = new ThreadPoolExecutor(10, 50, 30,
			TimeUnit.SECONDS, processQueue, new ThreadPoolExecutor.DiscardOldestPolicy()); // 处理线程池(全局共用)

	private static final Logger log = LoggerFactory.getLogger(AdminServer.class);

	/**
	 * 构造
	 * 
	 * @param address
	 *            要监听的地址
	 * @param port
	 *            要监听的端口
	 */
	public AdminServer(String address, int port) {
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
	public AdminServer(String address, int port, boolean keepAlive) {
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
	public AdminServer(String address, int port, boolean keepAlive, String prompt) {
		this(address, port, keepAlive, prompt, "GBK");
	}

	public AdminServer(String address, int port, boolean keepAlive, String prompt, String encoding) {
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

		addCommand("help", new Command() {
			private final int LENGTH = 20;

			public void execute(String[] argv, PrintWriter out) {
				if (argv.length > 0) {
					String name = argv[0];
					Command c = cmds.get(name);
					if (c == null) {
						out.println("can not find command: " + name);
					} else {
						out.println(c.getInfo());
						out.println();
						out.println(c.getUsage());
					}
					return;
				}
				out.println("commands: ");
				for (CmdInfo c : cmdinfos) {
					out.print(c.getName());
					out.print(getSpace(LENGTH - c.getName().length()));
					out.print(c.getCmd().getInfo());
					out.print("\n");
				}
			}

			@Override
			public String getInfo() {
				return "provider command help info.";
			}

			@Override
			public String getUsage() {
				return "help [cmd]";
			}
		});

		this.keepAlive = keepAlive;
		this.prompt = prompt;
		this.encoding = encoding;
	}

	private static String getSpace(int count) {
		if (count <= 0)
			return " ";
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < count; ++i)
			sb.append(" ");
		return sb.toString();
	}

	private static Pair<String, String> parseCmd(String fullname) {
		String[] ns = StringUtil.split(fullname, ",");
		if (ns.length <= 1)
			return Pair.makePair(fullname, null);
		return Pair.makePair(ns[0], ns[1]);
	}

	/**
	 * 添加一个管理命令
	 * 
	 * @param name
	 *            命令名
	 * @param cmd
	 *            命令执行的接口
	 * @return 本对象
	 */
	public AdminServer addCommand(String name, Command cmd) {
		Pair<String, String> pr = parseCmd(name);
		cmdinfos.add(new CmdInfo(name, cmd));
		cmds.put(pr.first, cmd);
		if (pr.second != null)
			cmds.put(pr.second, cmd);
		return this;
	}

	/**
	 * 获取一个管理命令
	 * 
	 * @param name
	 *            命令名
	 * @return
	 */
	public Command getCommand(String name) {
		return cmds.get(name);
	}

	public void start() {
		thread.start();
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
		AdminServer as = new AdminServer("0.0.0.0", 12345, true, "admin> ");
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
		as.run();
	}

}
