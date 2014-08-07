package org.covito.kit.command;

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
	 * 启动服务
	 */
	void start();

}
