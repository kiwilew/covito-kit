package org.covito.kit.command;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.covito.kit.utility.Pair;
import org.covito.kit.utility.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbsAdminServer implements AdminServer {

	private final Logger log = LoggerFactory.getLogger(getClass());

	private List<CmdInfo> cmdinfos = new CopyOnWriteArrayList<CmdInfo>(); // 命令信息列表

	private Map<String, Command> cmds = new ConcurrentHashMap<String, Command>(); // 命令名（长或短）和命令接口的对应关系

	private final static Pattern pat = Pattern
			.compile("\\s+(([^'\\\" \\t\\r\\n]+)|('[^'\\r\\n]*')|(\\\"[^\\\"\\r\\n]*\\\"))");

	protected String prompt = ""; // 提示符

	protected String encoding = "UTF-8"; // 字符编码

	public AbsAdminServer() {
		addCommand("help", new Command() {

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
				String format = "%1$-30s%1$-200s";
				for (CmdInfo c : cmdinfos) {
					out.println(String.format(format, new Object[] { c.getName(),
							c.getCmd().getInfo() }));
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

	private static Pair<String, String[]> parseCmd(String cmdLine) {
		cmdLine = cmdLine.trim();
		Matcher m = pat.matcher(cmdLine);
		ArrayList<String> argv = new ArrayList<String>();
		while (m.find()) {
			String arg = m.group(1);
			char c1 = arg.charAt(0);
			char c2 = arg.charAt(arg.length() - 1);
			if ((c1 == '\'' && c2 == '\'') || (c1 == '\"' && c2 == '\"'))
				arg = arg.substring(1, arg.length() - 1);
			argv.add(arg);
		}
		String cmd = cmdLine;
		int n = cmdLine.indexOf(' ');
		if (n > 0){
			cmd = cmd.substring(0, n);
		}
		String[] argvs = new String[argv.size()];
		argvs = argv.toArray(argvs);
		return Pair.makePair(cmd, argvs);
	}

	/** 
	 * 解析命令并输出到输出流
	 * <p>功能详细描述</p>
	 *
	 * @param cmd
	 * @param os
	 */
	@Override
	public void processCMD(String cmd, OutputStream os) {
		try {
			PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(os, this.encoding)));

			if (this.prompt.length() > 0) {
				out.print(this.prompt.getBytes());
				out.flush();
			}
			if (cmd == null) {
				return;
			}
			if (cmd.length() == 0) {
				return;
			}

			Pair<String, String[]> pr = parseCmd(cmd);
			Command c = getCommand(pr.first);
			if (c != null) {
				c.execute(pr.second, out);
			} else {
				out.println("Invalid command. Please type help for more information.");
			}
			out.flush();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
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
		Pair<String, String> pr = parseCmdForAdd(name);
		cmdinfos.add(new CmdInfo(name, cmd));
		cmds.put(pr.first, cmd);
		if (pr.second != null) {
			cmds.put(pr.second, cmd);
		}
	}

	/**
	 * 添加时多个命令解析
	 * <p>
	 * 功能详细描述
	 * </p>
	 * 
	 * @param fullname
	 * @return
	 */
	private Pair<String, String> parseCmdForAdd(String fullname) {
		String[] ns = StringUtil.split(fullname, ",");
		if (ns.length <= 1) {
			return Pair.makePair(fullname, null);
		}
		return Pair.makePair(ns[0], ns[1]);
	}

}
