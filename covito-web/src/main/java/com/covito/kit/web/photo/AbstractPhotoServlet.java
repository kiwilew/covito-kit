package com.covito.kit.web.photo;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

public abstract class AbstractPhotoServlet extends HttpServlet {
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	protected static String NO_FILE_REPLACEMENT = "images/blank.png";

	/**
	 * 一句话功能简述
	 * <p>
	 * 功能详细描述
	 * </p>
	 * 
	 * @author covito
	 * @param file
	 * @param smallUrl
	 * @param os
	 * @param width
	 * @param height
	 * @throws IOException
	 */
	public synchronized void outLogo(File file, String smallUrl,
			OutputStream os, int width, int height) throws IOException {
		
	}

	public static void main(String[] args) {
		
	}

	/**
	 * 图片下载
	 * <p>
	 * 功能详细描述
	 * </p>
	 * 
	 * @author covito
	 * @param response
	 * @param file
	 * @throws IOException
	 */
	protected void outputFile(HttpServletResponse response, File file)
			throws IOException {
		response.setContentType("image/gif");
		response.setContentLength((int) file.length());
		ServletOutputStream os = response.getOutputStream();
		try {
			dumpFile(file, os);
		} catch (Exception e) {
			logger.error("outputFile: {}", e.getLocalizedMessage());
		} finally {
			os.close();
		}
	}

	/**
	 * 转存到输出流
	 * <p>
	 * 功能详细描述
	 * </p>
	 * 
	 * @author covito
	 * @param file
	 * @param os
	 * @throws IOException
	 */
	protected void dumpFile(File file, OutputStream os) throws IOException {
		byte[] b = new byte[4096];
		BufferedInputStream is = null;
		try {
			is = new BufferedInputStream(new FileInputStream(file));
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
			writer.println("<br><br>Could not get file name ");
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
		logger.error("there is no image file named {}", fileName);
	}
}