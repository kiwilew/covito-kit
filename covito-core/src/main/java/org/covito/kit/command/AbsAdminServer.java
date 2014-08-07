package org.covito.kit.command;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
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

public abstract class AbsAdminServer implements AdminServer {

	private final Logger log = LoggerFactory.getLogger(getClass());
	
	private List<CmdInfo> cmdinfos = new CopyOnWriteArrayList<CmdInfo>(); // 命令信息列表

	private Map<String, Command> cmds = new ConcurrentHashMap<String, Command>(); // 命令名（长或短）和命令接口的对应关系
	
	protected final BlockingQueue<Runnable> processQueue = new LinkedBlockingQueue<Runnable>(); // 处理队列(全局公用)
	
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
	 * 提供服务
	 */
	//abstract void onService();
	
	public AbsAdminServer() {
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
				String format="%1$-30s%1$-200s";
				for (CmdInfo c : cmdinfos) {
					out.println(String.format(format, new Object[]{c.getName(),c.getCmd().getInfo()}));
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

	}
	
	@Override
	public void start() {
		thread.start();
	}
	
	/**
	 * 获取一个管理命令
	 * 
	 * @param name
	 *            命令名
	 * @return
	 */
	@Override
	public Command getCommand(String name) {
		return cmds.get(name);
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
	@Override
	public void addCommand(String name, Command cmd) {
		Pair<String, String> pr = parseCmd(name);
		cmdinfos.add(new CmdInfo(name, cmd));
		cmds.put(pr.first, cmd);
		if (pr.second != null){
			cmds.put(pr.second, cmd);
		}
	}
	
	private Pair<String, String> parseCmd(String fullname) {
		String[] ns = StringUtil.split(fullname, ",");
		if (ns.length <= 1){
			return Pair.makePair(fullname, null);
		}
		return Pair.makePair(ns[0], ns[1]);
	}
	
}
