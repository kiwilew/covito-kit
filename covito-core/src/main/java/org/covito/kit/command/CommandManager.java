package org.covito.kit.command;

import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.covito.kit.utility.Pair;

public class CommandManager {

	private static List<Command> cmdinfos = new CopyOnWriteArrayList<Command>(); // 命令信息列表

	private static Map<String, Command> cmds = new ConcurrentHashMap<String, Command>(); // 命令名（长或短）和命令接口的对应关系
	
	static{
		addCommand(new Command() {
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
				String format = "%1$-30s%2$-200s";
				for (Command c : cmdinfos) {
					out.println(String.format(format, new Object[] { c.getName(),
							c.getInfo() }));
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
			
			@Override
			public String getName() {
				return "help";
			}
			
		});
	}
	
	private CommandManager() {
	}
	
	/**
	 * 获取一个管理命令
	 * 
	 * @param name
	 *            命令名
	 * @return
	 */
	public static Command getCommand(String name) {
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
	public static void addCommand(Command cmd) {
		cmdinfos.add(cmd);
		Pair<String, String> pr = parseCmdForAdd(cmd.getName());
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
	private static Pair<String, String> parseCmdForAdd(String fullname) {
		String[] ns = fullname.split(",");
		if (ns.length <= 1) {
			return Pair.makePair(fullname, null);
		}
		return Pair.makePair(ns[0], ns[1]);
	}
	
}
