/*
 * 文 件 名:  FtpHelper.java
 * 描    述:  <描述>
 * 创 建 人:  eighteencold
 * 创建时间:  2014-3-7
 */
package org.covito.kit.utility;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketException;
import java.util.StringTokenizer;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Ftp连接工具类<br/>
 * <功能详细描述>
 * 
 * @author eighteencold
 * @version [v1.0, 2014-3-7]
 */
public class FtpHelper extends FTPClient {

	private Logger logger = LoggerFactory.getLogger(getClass());

	private String host;

	private int port = 21;

	private String username;

	private String password;

	private String encoding = "UTF-8";

	private int repeatTime = 1;

	public void init(InputStream input) throws IOException {
		FTPClientConfig conf = new FTPClientConfig();
		super.configure(conf);
		super.setFileType(FTP.BINARY_FILE_TYPE);
		int reply = super.sendCommand("OPTS UTF8 ON");
		if (reply == 200) { // UTF8 Command
			super.setControlEncoding(encoding);
		}

	}

	private void createDir(String dir) {
		StringTokenizer s = new StringTokenizer(dir, "/"); 
		

		

	}

	public boolean downFile(String path, OutputStream oStream)
			throws IOException {
		try {
			return retrieveFile(path, oStream);
		} finally {
			if (oStream != null) {
				oStream.close();
			}
		}
	}

	public boolean uploadFile(String path, OutputStream oStream)
			throws IOException {
		try {
			return retrieveFile(path, oStream);
		} finally {
			if (oStream != null) {
				oStream.close();
			}
		}
	}

	/**
	 * 登录<br/>
	 * <功能详细描述>
	 * 
	 * @author eighteencold
	 * @return
	 */
	public boolean login() {
		boolean isLogined = false;
		try {
			logger.debug("ftp login start ...");
			for (int i = 0; i < repeatTime; i++) {
				super.connect(host, port);
				isLogined = super.login(username, password);
				if (isLogined) {
					break;
				}
			}
			if (isLogined) {
				logger.debug("ftp login successfully ...");
			} else {
				logger.debug("ftp login failed ...");
			}
			return isLogined;
		} catch (SocketException e) {
			logger.error("", e);
			return false;
		} catch (IOException e) {
			logger.error("", e);
			return false;
		} catch (RuntimeException e) {
			logger.error("", e);
			return false;
		}
	}

	/**
	 * 登出<br/>
	 * <功能详细描述>
	 * 
	 * @author eighteencold
	 */
	public void close() {
		if (super.isConnected()) {
			try {
				super.logout();
				super.disconnect();
				logger.debug("ftp logout ....");
			} catch (Exception e) {
				logger.error(e.getMessage());
				throw new RuntimeException(e.toString());
			}
		}
	}

}
