package org.covito.kit.file;

import java.util.Date;
import java.util.Map;

public class FileMeta{

	public static String KEY_FILENAME="KEY_FileName";
	
	private String path;
	
	/**
	 * 原始文件名
	 */
	private String fileName;
	
	/**
	 * 文件大小
	 */
	private Long fileSize;
	
	/**
	 * 创建时间
	 */
	private Date createTime;
	
	/**
	 * 浏览器中的下载地址
	 */
	private String httpUrl;
	
	/**
	 * 文件附属信息，如作者，上传者，图片的高，宽等等
	 */
	private Map<String,String> meta;

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Map<String, String> getMeta() {
		return meta;
	}

	public void setMeta(Map<String, String> meta) {
		this.meta = meta;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Long getFileSize() {
		return fileSize;
	}

	public void setFileSize(Long fileSize) {
		this.fileSize = fileSize;
	}

	public String getHttpUrl() {
		return httpUrl;
	}

	public void setHttpUrl(String httpUrl) {
		this.httpUrl = httpUrl;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
	/**
	 * {@inheritDoc}
	 *
	 * @author  covito
	 * @return
	 */
	@Override
	public String toString() {
		return "fileName:"+fileName+",fileSize="+fileSize+",createTime="+createTime+",meta="+meta;
	}
}
