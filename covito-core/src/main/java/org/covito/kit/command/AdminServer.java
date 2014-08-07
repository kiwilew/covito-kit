package org.covito.kit.command;

import java.io.OutputStream;

public interface AdminServer {

	/**
	 * 获取一个管理命令
	 * 
	 * @param name
	 *            命令名
	 * @return
	 */
	Command getCommand(String name);

	/**
	 * 添加一个管理命令
	 * 
	 * @param name
	 *            命令名
	 * @param cmd
	 *            命令执行的接口
	 * @return 本对象
	 */
	void addCommand(String name, Command cmd);

	/** 
	 * 解析命令并输出到输出流
	 * <p>功能详细描述</p>
	 *
	 * @param cmd 命令
	 * @param os
	 */
	void processCMD(String cmd, OutputStream os);

}
