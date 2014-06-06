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
package org.covito.kit.web.file.support;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import org.covito.kit.web.file.FileInfos;
import org.covito.kit.web.file.FileServiceException;
import org.covito.kit.web.file.common.AbstractFileServiceImpl;
import org.covito.kit.web.file.fastdfs.ClientGlobal;
import org.covito.kit.web.file.fastdfs.DownloadStream;
import org.covito.kit.web.file.fastdfs.FileInfo;
import org.covito.kit.web.file.fastdfs.ProtoCommon;
import org.covito.kit.web.file.fastdfs.StorageClient;
import org.covito.kit.web.file.fastdfs.TrackerClient;
import org.covito.kit.web.file.fastdfs.TrackerServer;
import org.covito.kit.web.file.fastdfs.UploadCallback;
import org.covito.kit.web.file.fastdfs.UploadStream;
import org.covito.kit.web.file.fastdfs.common.FastdfsException;
import org.covito.kit.web.file.fastdfs.common.NameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Fastdfs实现
 * <p>
 * 功能详细描述
 * </p>
 * 
 * @author covito
 * @version [v1.0, 2014-6-4]
 */
public class FastdfsFileServiceImpl extends AbstractFileServiceImpl {

	public String confPath = "fastdfs_client.properties";

	protected final Logger log = LoggerFactory.getLogger(getClass());

	protected String tempRoot;
	
	protected boolean isInit = false;

	/** 
	 * Init for spring init-method
	 * <p>功能详细描述</p>
	 *
	 * @author  covito
	 */
	public void init() {
		if(isInit){
			return;
		}
		try {
			ClientGlobal.init("/" + confPath);
		} catch (FileNotFoundException e) {
			log.error("can't find config file[" + confPath + "]");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (FastdfsException e) {
			log.error("fastdfs init config failed!");
		}
		if(tempRoot==null){
			tempRoot = ClientGlobal.iniReader.getStrValue("docRoot");
			if(tempRoot==null){
				tempRoot=getClass().getResource("/").getPath();
				tempRoot=tempRoot.substring(0, tempRoot.lastIndexOf("/"));
				tempRoot=tempRoot.substring(0, tempRoot.lastIndexOf("/"));
				tempRoot+="/TempRoot";
			}
		}
		if(!tempRoot.endsWith("/")){
			tempRoot+="/";
		}
		isInit=true;
	}

	/**
	 * 创建连接
	 * 
	 * @return
	 * @throws FileServiceException
	 */
	private StorageClient createStoreClient() {
		TrackerClient trackerClient = new TrackerClient();
		TrackerServer trackerServer = null;
		try {
			trackerServer = trackerClient.getConnection();
		} catch (IOException e) {
			log.error(e.getMessage());
			throw new FileServiceException("connect file service error：" + e.getMessage());
		}
		StorageClient storageClient = new StorageClient(trackerServer, null);
		return storageClient;
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
			return createStoreClient().delete_file(getGroup(path), getFileName(path));
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
		try {
			StorageClient stc = createStoreClient();
			FileInfo fi = stc.get_file_info(getGroup(path), getFileName(path));
			if (fi == null) {
				return null;
			}
			file.setCreateTime(fi.getCreateTimestamp());
			file.setFileSize(fi.getFileSize());
			NameValuePair[] nv = stc.get_metadata(getGroup(path), getFileName(path));
			Map<String, String> meta = new HashMap<String, String>();
			for (NameValuePair n : nv) {
				if (FileInfos.KEY_FILENAME.equals(n.getName())) {
					file.setFileName(n.getValue());
				} else {
					meta.put(n.getName(), n.getValue());
				}
			}
			file.setPath(path);
			String pre = ClientGlobal.iniReader.getStrValue("httpURI." + fi.getSourceIpAddr());
			if (!pre.endsWith("/")) {
				pre = pre + "/";
			}
			file.setHttpUrl(pre + getFileName(path));

			file.setMeta(meta);
		} catch (Exception e) {
			e.printStackTrace();
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
		DownloadStream callback = new DownloadStream(os);
		try {
			createStoreClient().download_file(getGroup(path), getFileName(path), callback);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (FastdfsException e) {
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
	public String upload(InputStream is, String fileName, Map<String, String> meta) {
		init();
		if (null == meta) {
			meta = new HashMap<String, String>();
		}
		if (fileName == null || fileName.length() == 0) {
			fileName = "Unkown";
		}
		String file_ext_name = "";
		if (fileName.contains(".")) {
			file_ext_name = fileName.substring(fileName.lastIndexOf(".") + 1);
		}
		meta.put(FileInfos.KEY_FILENAME, fileName);
		NameValuePair[] meta_list = new NameValuePair[meta.size()];

		int i = 0;
		for (String key : meta.keySet()) {
			meta_list[i] = new NameValuePair(key, meta.get(key));
			i++;
		}

		try {
			long filesize;
			UploadCallback callback;
			String[] re;
			if (is instanceof FileInputStream) {
				filesize = ((FileInputStream)is).getChannel().size();
				callback = new UploadStream(is, filesize);
				re = createStoreClient().upload_appender_file(null, filesize, callback, file_ext_name, meta_list);
				is.close();
			} else {
				File temp = new File(tempRoot + fileNameGenerate.generate(fileName));
				FileOutputStream fos = new FileOutputStream(temp);
				output(is, fos);
				FileInputStream fis = new FileInputStream(temp);
				filesize=fis.getChannel().size();
				callback = new UploadStream(fis, filesize);
				re = createStoreClient().upload_appender_file(null, filesize, callback, file_ext_name, meta_list);
				fis.close();
				temp.delete();
			}
			return (re[0] + "/" + re[1]);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
			throw new FileServiceException(e.getMessage());
		}
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
		try {
			long filesize = 0;
			UploadCallback callback;
			int result=0;
			if (is instanceof FileInputStream) {
				filesize = ((FileInputStream)is).getChannel().size();
				callback = new UploadStream(is, filesize);
				result=createStoreClient().append_file(getGroup(path), getFileName(path), filesize, callback);
				is.close();
				
			} else {
				File temp = new File(tempRoot + getFileName(path));
				FileOutputStream fos = new FileOutputStream(temp);
				output(is, fos);
				FileInputStream fis = new FileInputStream(temp);
				filesize=fis.getChannel().size();
				callback = new UploadStream(fis, filesize);
				result=createStoreClient().append_file(getGroup(path), getFileName(path), filesize, callback);
				fis.close();
				temp.delete();
			}
			if(result!=0){
				throw new FileServiceException("file is not allow append!");
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
		}
	}

	private void output(InputStream is, OutputStream os) {
		if (is == null || os == null) {
			return;
		}
		int len = 0;
		byte[] b = new byte[4096];
		try {
			while ((len = is.read(b)) != -1) {
				os.write(b, 0, len);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (os != null) {
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
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
		if (null == meta) {
			meta = new HashMap<String, String>();
		}
		NameValuePair[] meta_list = new NameValuePair[meta.size()];
		int i = 0;
		for (String key : meta.keySet()) {
			meta_list[i] = new NameValuePair(key, meta.get(key));
			i++;
		}
		try {
			createStoreClient().set_metadata(getGroup(path), getFileName(path), meta_list, ProtoCommon.STORAGE_SET_METADATA_FLAG_MERGE);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
		}
	}

	/**
	 * Set tempRoot
	 *
	 * @param tempRoot the tempRoot to set
	 */
	public void setTempRoot(String tempRoot) {
		this.tempRoot = tempRoot;
	}

	/**
	 * Set confPath
	 *
	 * @param confPath the confPath to set
	 */
	public void setConfPath(String confPath) {
		this.confPath = confPath;
	}

}
