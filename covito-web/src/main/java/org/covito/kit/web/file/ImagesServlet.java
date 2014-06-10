package org.covito.kit.web.file;

import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.covito.kit.file.FileMeta;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ImagesServlet extends FileDownLoadServlet {
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	private Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * 文件下载
	 * <p>
	 * 功能详细描述
	 * </p>
	 * 
	 * @author covito
	 * @param response
	 * @param is
	 * @throws IOException
	 */
	protected void outputFile(HttpServletResponse response, String path) throws IOException {
		FileMeta info = fileService.getFileInfo(path);
		String fileName=info.getFileName();
		Long size = info.getFileSize();
		response.setContentLength(size.intValue());
		String extname=fileName.substring(fileName.lastIndexOf(".")+1, fileName.length());
		int index=-1;
		for(int i=0;i<ext.length;i++){
			if(ext[i].equalsIgnoreCase(extname)){
				index=i;
				break;
			}
		}
		ServletOutputStream os = response.getOutputStream();
		if(index!=-1){
			response.setContentType(contentType[index]);
		}
		fileService.outputFile(path, os);
	}

	
}