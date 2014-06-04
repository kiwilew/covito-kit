package org.covito.kit.web.file.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.covito.kit.web.file.FileService;
import org.covito.kit.web.file.FileServiceUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractFileServlet extends HttpServlet {
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	private FileService fileService;
	
	/**
	 * {@inheritDoc}
	 *
	 * @author  covito
	 * @throws ServletException
	 */
	@Override
	public void init() throws ServletException {
		super.init();
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @author  covito
	 */
	@Override
	public void destroy() {
		super.destroy();
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @author  covito
	 */
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		response.setContentType("image/jpeg");
		response.setDateHeader("expires", System.currentTimeMillis() + 60000L);
		response.addHeader("Cache-Control", "max-age=60");
		setContentInfo(response);
		String path = request.getPathInfo();
		if ((path == null) || (path.length() < 1)){
			noFileError(response, path);
			return;
		}
		this.outputFile(response, path);
	}
	
	/** 
	 * 设置下载头信息
	 * 
	 * <p>
	 * response.setContentType("image/jpeg");
	 * response.setDateHeader("expires", System.currentTimeMillis() + 60000L);
	 * response.addHeader("Cache-Control", "max-age=60");
	 * </p>
	 *
	 * @author  covito
	 * @param request
	 */
	protected abstract void setContentInfo(HttpServletResponse response);
	
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
	protected void outputFile(HttpServletResponse response, String path)
			throws IOException {
		ServletOutputStream os = response.getOutputStream();
		if(fileService==null){
			fileService=FileServiceUtil.getFileService();
		}
		fileService.outputFile(path, os);
	}

	/**
	 * 转存到输出流
	 * <p>
	 * 功能详细描述
	 * </p>
	 * 
	 * @author covito
	 * @param is
	 * @param os
	 * @throws IOException
	 */
	protected void dumpFile(InputStream is, OutputStream os) throws IOException {
		byte[] b = new byte[4096];
		try {
			int len;
			while ((len = is.read(b)) != -1) {
				os.write(b, 0, len);
			}
		} catch (Exception e) {
			logger.error("dumpFile: {}", e.getLocalizedMessage());
		} finally {
			if (os != null)
				os.close();
			if (is != null)
				is.close();
		}
	}

	/**
	 * 文件不存在时输出信息
	 * <p>
	 * 功能详细描述
	 * </p>
	 * 
	 * @author covito
	 * @param response
	 * @param fileName
	 */
	protected void noFileError(HttpServletResponse response, String fileName) {
		PrintWriter writer = null;
		try {
			response.setContentType("text/html");
			writer = response.getWriter();
			writer.println("<html>");
			writer.println("<br><br>Could not get file id "+fileName);
			writer.println("<br><br>");
			writer.println("</html>");
		} catch (Exception e) {
			logger.error("noFileError", e);
		} finally {
			if (writer != null) {
				writer.flush();
				writer.close();
			}
		}
		logger.error("there is no image file id {}", fileName);
	}
}