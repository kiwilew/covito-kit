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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import org.covito.kit.web.file.FileInfos;
import org.covito.kit.web.file.FileService;
import org.covito.kit.web.file.FileServiceException;
import org.covito.kit.web.file.common.AbstractFileServiceImpl;
import org.covito.kit.web.file.fastdfs.ClientGlobal;
import org.covito.kit.web.file.fastdfs.DownloadStream;
import org.covito.kit.web.file.fastdfs.FileInfo;
import org.covito.kit.web.file.fastdfs.StorageClient;
import org.covito.kit.web.file.fastdfs.TrackerClient;
import org.covito.kit.web.file.fastdfs.TrackerServer;
import org.covito.kit.web.file.fastdfs.Upload;
import org.covito.kit.web.file.fastdfs.UploadCallback;
import org.covito.kit.web.file.fastdfs.common.FastdfsException;
import org.covito.kit.web.file.fastdfs.common.NameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Fastdfs实现
 * <p>功能详细描述</p>
 * 
 * @author  covito
 * @version  [v1.0, 2014-6-4]
 */
public class FastdfsFileServiceImpl extends AbstractFileServiceImpl{
	
	public String confPath = "fastdfs_client.properties";

	protected final Logger log = LoggerFactory.getLogger(getClass());
	
	/** 
	 * Constructor
	 */
	public FastdfsFileServiceImpl() {
		try {
			ClientGlobal.init("/"+confPath);
		} catch (FileNotFoundException e) {
			log.error("can't find config file[" + confPath + "]");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (FastdfsException e) {
			log.error("fastdfs init config failed!");
		}
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
			throw new FileServiceException("connect file service error："+e.getMessage());
		}
		StorageClient storageClient = new StorageClient(trackerServer, null);
		return storageClient;
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @author  covito
	 * @param path
	 * @return
	 */
	@Override
	public int deleteFile(String path) {
		try {
			return createStoreClient().delete_file(getGroup(path),
					getFileName(path));
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new FileServiceException(e);
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @author  covito
	 * @param path
	 * @return
	 */
	@Override
	public FileInfos getFileInfo(String path) {
		if(path==null||path.length()==0){
			return null;
		}
		FileInfos file = new FileInfos();
		try {
			StorageClient stc = createStoreClient();
			FileInfo fi = stc.get_file_info(getGroup(path), getFileName(path));
			if(fi==null){
				return null;
			}
			file.setCreateTime(fi.getCreateTimestamp());
			file.setFileSize(fi.getFileSize());
			NameValuePair[] nv = stc.get_metadata(getGroup(path),
					getFileName(path));
			Map<String, String> meta = new HashMap<String, String>();
			for (NameValuePair n : nv) {
				if (FileInfos.KEY_FILENAME.equals(n.getName())) {
					file.setFileName(n.getValue());
				} else {
					meta.put(n.getName(), n.getValue());
				}
			}
			file.setPath(path);
			String pre=ClientGlobal.iniReader.getStrValue("httpURI."+fi.getSourceIpAddr());
			if(!pre.endsWith("/")){
				pre=pre+"/";
			}
			file.setHttpUrl(pre+getFileName(path));
			
			file.setMeta(meta);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return file;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @author  covito
	 * @param path
	 * @param os
	 */
	@Override
	public void outputFile(String path, OutputStream os) {
		if(os==null){
			throw new FileServiceException("OutputStream is null");
		}
		DownloadStream callback=new DownloadStream(os);
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
	 * @author  covito
	 * @param is
	 * @param fileName
	 * @param meta
	 * @return
	 */
	@Override
	public String upload(InputStream is, String fileName,
			Map<String, String> meta) {
		if (null == meta) {
			meta = new HashMap<String, String>();
		}
		if (fileName == null||fileName.length()==0) {
			fileName="Unkown";
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
			UploadCallback callback=new Upload(is);
			String[] re = createStoreClient().upload_file(null, 100L, callback, file_ext_name, meta_list);
			return (re[0]+"/"+re[1]);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
			throw new FileServiceException(e.getMessage());
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @author  covito
	 * @param path
	 * @param is
	 */
	@Override
	public void append(String path, InputStream is) {
		// TODO Auto-generated method stub

	}

	/**
	 * {@inheritDoc}
	 *
	 * @author  covito
	 * @param path
	 * @param meta
	 */
	@Override
	public void updataMeta(String path, Map<String, String> meta) {
		// TODO Auto-generated method stub

	}

}
