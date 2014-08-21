package org.covito.kit.command;

import java.io.PrintWriter;

public interface Command {

	/** 
	 * 获取命令名称
	 * <p>功能详细描述</p>
	 *
	 * @return
	 */
	String getName();
	
	/** 
	 * 参数解析执行并打印
	 * <p>功能详细描述</p>
	 *
	 * @param argv
	 * @param out
	 */
	void execute(String[] argv, PrintWriter out);
	
	/** 
	 * 获取命令说明
	 * <p>功能详细描述</p>
	 *
	 * @return
	 */
	String getInfo();
	
	/** 
	 * 获取帮助
	 * <p>功能详细描述</p>
	 *
	 * @return
	 */
	String getUsage();
}
