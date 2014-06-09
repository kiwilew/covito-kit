package org.covito.kit.file;


/**
 * 文件服务工具类<br/>
 * <功能详细描述>
 * 
 * @author  eighteencold
 * @version  [v1.0, 2014-2-13]
 */
public class FileServiceUtil {

	private FileService fileService;

	private static FileServiceUtil instance=new FileServiceUtil();
	
	private FileServiceUtil(){
		
	}

	/** 
	 * spring 注入 setter<br/>
	 * <功能详细描述>
	 *
	 * @author  eighteencold
	 * @param fileService
	 */
	public void setFileService(FileService fileService) {
		instance.fileService = fileService;
	}

	/** 
	 * 获取文件服务<br/>
	 * <功能详细描述>
	 *
	 * @author  eighteencold
	 * @return
	 */
	public static FileService getFileService() {
		return instance.fileService;
	}

}
