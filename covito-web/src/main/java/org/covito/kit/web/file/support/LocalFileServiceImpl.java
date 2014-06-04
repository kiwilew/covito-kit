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
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.covito.kit.utility.FileUtil;
import org.covito.kit.utility.ResourceReader;
import org.covito.kit.web.file.FileInfos;
import org.covito.kit.web.file.FileServiceException;
import org.covito.kit.web.file.common.AbstractFileServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * Local File System实现
 * <p>功能详细描述</p>
 * 
 * @author  covito
 * @version  [v1.0, 2014-6-4]
 */
public class LocalFileServiceImpl extends AbstractFileServiceImpl {
	
	protected String confPath = "localFile_client.properties";

	protected final Logger log = LoggerFactory.getLogger(getClass());
	
	protected String docRoot;
	
	protected static final String metaEndFlag="-meta.json";
	
	protected static final String CREAT_ETIME="CreateTime";
	
	protected boolean isInit=false;
	
	
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
		Map<String, String> config;
		try {
			config=ResourceReader.getAll(confPath);
		} catch (Exception e) {
			config=new HashMap<String, String>();
			log.error(confPath+" can't find!");
		}
		if(docRoot==null){
			if(config.containsKey("doc_root")){
				docRoot=config.get("doc_root");
			}else{
				docRoot=getClass().getResource("/").getPath();
				docRoot=docRoot.substring(0, docRoot.lastIndexOf("/"));
				docRoot=docRoot.substring(0, docRoot.lastIndexOf("/"));
				docRoot+="/DocRoot";
			}
		}
		if(!docRoot.endsWith("/")){
			docRoot+="/";
		}
		log.debug(docRoot);
		isInit=true;
	}

	/**
	 * Set confPath for spring
	 *
	 * @param confPath the confPath to set
	 */
	public void setConfPath(String confPath) {
		this.confPath = confPath;
	}

	/**
	 * Set docRoot  for spring
	 *
	 * @param docRoot the docRoot to set
	 */
	public void setDocRoot(String docRoot) {
		this.docRoot = docRoot;
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
		init();
		try {
			File f=new File(getFilePath(path));
			if(f.exists()){
				f.delete();
			}
			File mf=new File(getMetaPath(path));
			if(mf.exists()){
				mf.delete();
			}
			return 0;
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new FileServiceException(e);
		}
	}

	/**
	 * {@inheritDoc} for spring
	 *
	 * @author  covito
	 * @param path
	 * @return
	 */
	@Override
	public FileInfos getFileInfo(String path) {
		init();
		if(path==null||path.length()==0){
			return null;
		}
		FileInfos file = new FileInfos();
		
		File f=new File(getFilePath(path));
		if(!f.exists()){
			throw new FileServiceException("path is not exist!");
		}
		File mf=new File(getMetaPath(path));
		JSONObject json;
		if(mf.exists()){
			json=JSON.parseObject(FileUtil.readText(mf));
		}else{
			json=new JSONObject();
		}
		
		Map<String,String> meta=JSON.parseObject(json.toString(), Map.class);
		file.setMeta(meta);
		file.setFileName(meta.get(FileInfos.KEY_FILENAME));
		file.setCreateTime(new Date(Long.parseLong(meta.get(CREAT_ETIME))));
		file.setFileSize(f.length());
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
		init();
		if(os==null){
			throw new FileServiceException("OutputStream is null");
		}
		File f=new File(getFilePath(path));
		try {
			FileInputStream fis=new FileInputStream(f);
			output(fis, os);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new org.covito.kit.exception.FileNotFoundException(e);
		} catch (IOException e) {
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
		init();
		if (null == meta) {
			meta = new HashMap<String, String>();
		}
		if (fileName == null||fileName.length()==0) {
			fileName="Unkown";
		}
		meta.put(FileInfos.KEY_FILENAME, fileName);
		meta.put(CREAT_ETIME, new Date().getTime()+"");
		String path=generatePath(fileName);
		File f=new File(getFilePath(path));
		try {
			if(!f.getParentFile().exists()){
				f.getParentFile().mkdirs();
			}
			FileOutputStream fos=new FileOutputStream(f);
			output(is, fos);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new FileServiceException(e);
		} catch (IOException e) {
			e.printStackTrace();
			throw new FileServiceException(e);
		}
		File mf=new File(getMetaPath(path));
		Map<String, Object> m=new HashMap<String, Object>();
		m.putAll(meta);
		JSONObject josn=new JSONObject(m);
		FileUtil.writeText(mf.getAbsolutePath(),josn.toString());
		return path;
	}
	
	protected void output(InputStream is,OutputStream os) throws IOException{
		int len;
		byte[] b=new byte[4096];
		try {
			while((len=is.read(b))!=-1){
				os.write(b, 0, len);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			if(is!=null){
				is.close();
			}
			if(os!=null){
				os.close();
			}
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
		init();
		if(is==null){
			throw new FileServiceException("InputStream is null");
		}
		File f=new File(getFilePath(path));
		try {
			FileOutputStream os=new FileOutputStream(f,true);
			output(is, os);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new org.covito.kit.exception.FileNotFoundException(e);
		} catch (IOException e) {
			e.printStackTrace();
		}
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
		init();
		File f=new File(getFilePath(path));
		if(!f.exists()){
			throw new FileServiceException("path is not exist!");
		}
		File mf=new File(getMetaPath(path));
		JSONObject json;
		if(mf.exists()){
			json=JSON.parseObject(FileUtil.readText(mf));
		}else{
			json=new JSONObject();
		}
		json.putAll(meta);
		FileUtil.writeText(mf.getAbsolutePath(), json.toString());
		
	}
	
	protected String getMetaPath(String path){
		StringBuffer sb=new StringBuffer();
		sb.append(docRoot);
		sb.append(path.substring(0, path.indexOf(".")));
		sb.append(metaEndFlag);
		return sb.toString();
	}
	
	protected String getFilePath(String path){
		StringBuffer sb=new StringBuffer();
		sb.append(docRoot);
		sb.append(path);
		return sb.toString();
	}

}
