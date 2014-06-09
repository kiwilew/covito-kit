/*
 * Copyright 2010-2014  All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy
 * of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 *
 */
package org.covito.kit.file.support;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.covito.kit.file.FileInfos;
import org.covito.kit.file.FileServiceException;
import org.covito.kit.file.common.AbstractFileServiceImpl;
import org.covito.kit.utility.ResourceReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * Ftp实现
 * <p>
 * 功能详细描述
 * </p>
 * 
 * @author covito
 * @version [v1.0, 2014-6-4]
 */
public class FtpFileServiceImpl extends AbstractFileServiceImpl {

	protected String confPath = "ftpFile_client.properties";

	protected final Logger log = LoggerFactory.getLogger(getClass());

	protected static final String metaEndFlag = "-meta.json";

	protected String url;

	protected String username;

	protected String password;

	protected int port = 21;

	protected boolean isPassiveMode;

	protected boolean isInit = false;
	
	protected FTPClient ftp;
	
	protected String rootWorkingDirectory="/";

	/**
	 * 获得连接
	 * <p>
	 * 功能详细描述
	 * </p>
	 * 
	 * @author covito
	 * @return
	 */
	protected FTPClient getConnect() {
		if(ftp!=null){
			if(!ftp.isConnected()){
				try {
					ftp.connect(url, port);
					ftp.login(username, password);
				} catch (Exception e) {
					e.printStackTrace();
					ftp=null;
					throw new FileServiceException("FTP 服务器登录失败");
				}
			}
			return ftp;
		}
		initFTPClient();
		return ftp;
	}
	
	/** 
	 * 初始化FTP Client
	 * <p>功能详细描述</p>
	 *
	 * @author  covito
	 */
	protected void initFTPClient(){
		ftp = new FTPClient();
		ftp.setControlEncoding("UTF-8");
		try {
			ftp.connect(url, port);
			if (isPassiveMode) {
				ftp.enterLocalPassiveMode();
			}
			ftp.login(username, password);
			int reply = ftp.getReplyCode();
			if (!FTPReply.isPositiveCompletion(reply)) {
				ftp.disconnect();
				ftp=null;
				throw new FileServiceException("FTP 服务器登录失败");
			}
			ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
		} catch (Exception e) {
			e.printStackTrace();
			throw new FileServiceException(e);
		}
	}

	/**
	 * 关闭连接
	 * <p>
	 * 功能详细描述
	 * </p>
	 * 
	 * @author covito
	 * @param ftp
	 */
	protected void close(FTPClient ftp) {
		try {
			ftp.logout();
			if (ftp.isConnected()) {
				ftp.disconnect();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Init for spring init-method
	 * <p>
	 * 功能详细描述
	 * </p>
	 * 
	 * @author covito
	 */
	public void init() {
		if(isInit){
			return;
		}
		try {
			url = ResourceReader.getValue(confPath, "url","127.0.0.1");
			port = Integer.parseInt(ResourceReader.getValue(confPath, "port","21"));
			username = ResourceReader.getValue(confPath, "username","root");
			password = ResourceReader.getValue(confPath, "password","root");
			isPassiveMode = Boolean.parseBoolean(ResourceReader.getValue(confPath, "isPassiveMode","true"));
			rootWorkingDirectory = ResourceReader.getValue(confPath, "rootWorkingDirectory","/");
		} catch (Exception e) {
			e.printStackTrace();
			log.error(confPath+" can't find!");
		}
		
		isInit=true;
	}
	
	protected String getMetaPath(String path){
		StringBuffer sb=new StringBuffer();
		sb.append(path.substring(path.indexOf(separate)+1, path.indexOf(".")));
		sb.append(metaEndFlag);
		return sb.toString();
	}
	
	/** 
	 * 切换到批定目录
	 * <p>功能详细描述</p>
	 *
	 * @author  covito
	 * @param ftp
	 * @param path
	 * @param mkdir 为true时，当目录不存在时新建目录
	 * @return
	 */
	protected boolean dealDocPath(FTPClient ftp,String path,boolean mkdir){
		String doc=path.substring(0, path.indexOf("/"));
		try {
			boolean success=ftp.changeWorkingDirectory(rootWorkingDirectory+"/"+doc);
			if(!success){
				if(mkdir){
					ftp.mkd(rootWorkingDirectory+"/"+doc);
					return ftp.changeWorkingDirectory(rootWorkingDirectory+"/"+doc);
				}
				return false;
			}
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/** 
	 * 获取path
	 * <p>功能详细描述</p>
	 *
	 * @author  covito
	 * @param path
	 * @return
	 */
	protected String getFilePath(String path){
		StringBuffer sb=new StringBuffer();
		sb.append(path.substring(path.indexOf(separate)+1,path.length()));
		return sb.toString();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @author covito
	 * @param path
	 * @return
	 */
	@Override
	public int deleteFile(String path) {
		init();
		try {
			FTPClient client=getConnect();
			if(!dealDocPath(client, path,false)){
				return 0;
			}
			boolean sucess=client.deleteFile(getFilePath(path));
			if(!sucess){
				log.warn(client.getReplyString());
			}
			sucess=client.deleteFile(getMetaPath(path));
			if(!sucess){
				log.warn(client.getReplyString());
			}
			return 0;
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new FileServiceException(e);
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @author covito
	 * @param path
	 * @return
	 */
	@Override
	public FileInfos getFileInfo(String path) {
		init();
		if (path == null || path.length() == 0) {
			return null;
		}
		FileInfos file = new FileInfos();
		FTPClient client=getConnect();
		if(!dealDocPath(client, path,false)){
			throw new FileServiceException("path not exist!");
		}
		
		ByteArrayOutputStream bos=new ByteArrayOutputStream();
		try {
			boolean sucess=client.retrieveFile(getMetaPath(path), bos);
			if(!sucess){
				log.error(client.getReplyString());
				throw new FileServiceException("path is not exist!");
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new FileServiceException("path is not exist!");
		}
		JSONObject json = JSON.parseObject(bos.toString());
		
		Map<String,String> meta=JSON.parseObject(json.toString(), Map.class);
		file.setMeta(meta);
		file.setFileName(meta.get(FileInfos.KEY_FILENAME));
		
		try {
			FTPFile ftpfile=client.mlistFile(rootWorkingDirectory+"/"+path);
			file.setCreateTime(new Date(ftpfile.getTimestamp().getTimeInMillis()));
			file.setFileSize(ftpfile.getSize());
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		return file;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @author covito
	 * @param path
	 * @param os
	 */
	@Override
	public void outputFile(String path, OutputStream os) {
		init();
		if (os == null) {
			throw new FileServiceException("OutputStream is null");
		}
		FTPClient client=getConnect();
		try {
			if(!dealDocPath(client, path,false)){
				throw new FileServiceException("path not exist!");
			}
			boolean sucess=client.retrieveFile(getFilePath(path), os);
			if(!sucess){
				log.error(client.getReplyString());
				throw new FileServiceException(client.getReplyString());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @author covito
	 * @param is
	 * @param fileName
	 * @param meta
	 * @return
	 */
	@Override
	public String upload(InputStream is, String fileName,
			Map<String, String> meta) {
		init();
		if (null == meta) {
			meta = new HashMap<String, String>();
		}
		if (fileName == null || fileName.length() == 0) {
			fileName = "Unkown";
		}
		
		meta.put(FileInfos.KEY_FILENAME, fileName);
		String path=generatePath(fileName);
			
		FTPClient client=getConnect();
		
		try {
			if(!dealDocPath(client, path,true)){
				throw new FileServiceException(client.getReplyString());
			}
			boolean sucess=client.storeFile(getFilePath(path), is);
			if(!sucess){
				log.error(client.getReplyString());
				throw new FileServiceException(client.getReplyString());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Map<String, Object> m=new HashMap<String, Object>();
		m.putAll(meta);
		JSONObject josn=new JSONObject(m);
		
		ByteArrayInputStream bis=new ByteArrayInputStream(josn.toString().getBytes());
		try {
			boolean sucess=client.storeFile(getMetaPath(path), bis);
			if(!sucess){
				log.error(client.getReplyString());
				throw new FileServiceException(client.getReplyString());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return path;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @author covito
	 * @param path
	 * @param is
	 */
	@Override
	public void append(String path, InputStream is) {
		init();
		if(is==null){
			throw new FileServiceException("InputStream is null");
		}
		FTPClient client=getConnect();
		try {
			if(!dealDocPath(client, path,false)){
				throw new FileServiceException("path not exist!");
			}
			boolean sucess=client.appendFile(getFilePath(path), is);
			if(!sucess){
				log.error(client.getReplyString());
				throw new FileServiceException(client.getReplyString());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @author covito
	 * @param path
	 * @param meta
	 */
	@Override
	public void updataMeta(String path, Map<String, String> meta) {
		init();
		FTPClient client=getConnect();
		if(!dealDocPath(client, path,false)){
			throw new FileServiceException("path not exist!");
		}
		ByteArrayOutputStream bos=new ByteArrayOutputStream();
		try {
			boolean sucess=client.retrieveFile(getMetaPath(path), bos);
			if(!sucess){
				log.error(client.getReplyString());
				throw new FileServiceException(client.getReplyString());
			}
		} catch (IOException e1) {
			e1.printStackTrace();
			throw new FileServiceException("path not exist!");
		}
		JSONObject json=JSON.parseObject(bos.toString());
		json.putAll(meta);
		ByteArrayInputStream bis=new ByteArrayInputStream(json.toString().getBytes());
		try {
			boolean sucess=client.storeFile(getMetaPath(path), bis);
			if(!sucess){
				log.error(client.getReplyString());
				throw new FileServiceException(client.getReplyString());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Set confPath
	 * 
	 * @param confPath
	 *            the confPath to set
	 */
	public void setConfPath(String confPath) {
		this.confPath = confPath;
	}

	/**
	 * Set url
	 * 
	 * @param url
	 *            the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * Set username
	 * 
	 * @param username
	 *            the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * Set password
	 * 
	 * @param password
	 *            the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * Set port
	 * 
	 * @param port
	 *            the port to set
	 */
	public void setPort(int port) {
		this.port = port;
	}

	/**
	 * Set isPassiveMode
	 * 
	 * @param isPassiveMode
	 *            the isPassiveMode to set
	 */
	public void setPassiveMode(boolean isPassiveMode) {
		this.isPassiveMode = isPassiveMode;
	}

	/**
	 * Set rootWorkingDirectory
	 *
	 * @param rootWorkingDirectory the rootWorkingDirectory to set
	 */
	public void setRootWorkingDirectory(String rootWorkingDirectory) {
		this.rootWorkingDirectory = rootWorkingDirectory;
	}

	
}
