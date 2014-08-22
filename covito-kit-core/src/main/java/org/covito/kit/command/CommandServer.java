package org.covito.kit.command;

import java.io.OutputStream;

public interface CommandServer {

	/** 
	 * 解析命令并输出到输出流
	 * <p>功能详细描述</p>
	 *
	 * @param cmd 命令
	 * @param os
	 */
	void processCMD(String cmd, OutputStream os);

	/**
	 * 开始提供服务
	 */
	void startServer();

}
