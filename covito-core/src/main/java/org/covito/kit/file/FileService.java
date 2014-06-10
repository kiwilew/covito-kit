package org.covito.kit.file;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;


/**
 * 文件操作服务
 * <p>功能详细描述</p>
 * 
 * @author  covito
 * @version  [v1.0, 2014-6-3]
 */
public interface FileService {

	/**
	 * 删除文件
	 * @param path 服务器路径
	 * @return 0成功，非0失败
	 */
	int deleteFile(String path) ;
	
	/**
	 * 获取文件信息（原始文件名、文件大小、创建日期、文件其它属性如图片的高，宽，缩略图ID等等）
	 * @param path 服务器路径
	 * @return 文件信息对象
	 */
	FileMeta getFileInfo(String path);
	
	/**
	 * 将文件输出到输出流
	 * @param path 服务器路径
	 * @param os 输出流
	 */
	void outputFile(String path,OutputStream os);
	
	/**
	 * 上传文件
	 * @param 输入流 
	 * @param fileName 原始文件名 
	 * @param meta 文件其它属性（原始文件名、文件大小、创建日期、文件其它属性如图片的高，宽，缩略图ID等等）
	 * @return 文件Path
	 */
	String upload(InputStream is,String fileName,Map<String,String> meta);
	
	/**
	 * 追加到文件
	 * @param path  服务器路径
	 * @param content 追加内容的输入流
	 */
	void append(String path,InputStream is);
	
	/**
	 * 更新文件Meta信息
	 * @param path  服务器路径
	 * @param meta 文件属附信息Map
	 * @ 文件异常
	 */
	void updataMeta(String path,Map<String,String> meta);
	
}
