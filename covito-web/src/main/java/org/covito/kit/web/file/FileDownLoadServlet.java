package org.covito.kit.web.file;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.covito.kit.file.FileMeta;
import org.covito.kit.file.FileService;
import org.covito.kit.file.FileServiceUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileDownLoadServlet extends HttpServlet {
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	private Logger logger = LoggerFactory.getLogger(getClass());

	protected FileService fileService;

	protected final String[] ext = { "ez", "hqx", "cpt", "doc", "bin", "dms", "lha", "lzh", "exe", "class", "so", "dll", "oda", "pdf", "ai", "eps", "ps",
			"smi", "smil", "mif", "xls", "ppt", "wbxml", "wmlc", "wmlsc", "bcpio", "vcd", "pgn", "cpio", "csh", "dcr", "dir", "dxr", "dvi", "spl", "gtar",
			"hdf", "js", "skp", "skd", "skt", "skm", "latex", "nc", "cdf", "sh", "shar", "swf", "sit", "sv4cpio", "sv4crc", "tar", "tcl", "tex", "texinfo",
			"texi", "t", "tr", "roff", "man", "me", "ms", "ustar", "src", "xhtml", "xht", "zip", "au", "snd", "mid", "midi", "kar", "mpga", "mp2", "mp3",
			"aif", "aiff", "aifc", "m3u", "ram", "rm", "rpm", "ra", "wav", "pdb", "xyz", "bmp", "gif", "ief", "jpeg", "jpg", "jpe", "png", "tiff", "tif",
			"djvu", "djv", "wbmp", "ras", "pnm", "pbm", "pgm", "ppm", "rgb", "xbm", "xpm", "xwd", "igs", "iges", "msh", "mesh", "silo", "wrl", "vrml", "css",
			"html", "htm", "asc", "txt", "rtx", "rtf", "sgml", "sgm", "tsv", "wml", "wmls", "etx", "xsl", "xml", "mpeg", "mpg", "mpe", "qt", "mov", "mxu",
			"avi", "movie", "ice" };

	protected final String[] contentType = { "application/andrew-inset", "application/mac-binhex40", "application/mac-compactpro", "application/msword",
			"application/octet-stream", "application/octet-stream", "application/octet-stream", "application/octet-stream", "application/octet-stream",
			"application/octet-stream", "application/octet-stream", "application/octet-stream", "application/oda", "application/pdf", "application/postscript",
			"application/postscript", "application/postscript", "application/smil", "application/smil", "application/vnd.mif", "application/vnd.ms-excel",
			"application/vnd.ms-powerpoint", "application/vnd.wap.wbxml", "application/vnd.wap.wmlc", "application/vnd.wap.wmlscriptc", "application/x-bcpio",
			"application/x-cdlink", "application/x-chess-pgn", "application/x-cpio", "application/x-csh", "application/x-director", "application/x-director",
			"application/x-director", "application/x-dvi", "application/x-futuresplash", "application/x-gtar", "application/x-hdf", "application/x-javascript",
			"application/x-koan", "application/x-koan", "application/x-koan", "application/x-koan", "application/x-latex", "application/x-netcdf",
			"application/x-netcdf", "application/x-sh", "application/x-shar", "application/x-shockwave-flash", "application/x-stuffit",
			"application/x-sv4cpio", "application/x-sv4crc", "application/x-tar", "application/x-tcl", "application/x-tex", "application/x-texinfo",
			"application/x-texinfo", "application/x-troff", "application/x-troff", "application/x-troff", "application/x-troff-man", "application/x-troff-me",
			"application/x-troff-ms", "application/x-ustar", "application/x-wais-source", "application/xhtml+xml", "application/xhtml+xml", "application/zip",
			"audio/basic", "audio/basic", "audio/midi", "audio/midi", "audio/midi", "audio/mpeg", "audio/mpeg", "audio/mpeg", "audio/x-aiff", "audio/x-aiff",
			"audio/x-aiff", "audio/x-mpegurl", "audio/x-pn-realaudio", "audio/x-pn-realaudio", "audio/x-pn-realaudio-plugin", "audio/x-realaudio",
			"audio/x-wav", "chemical/x-pdb", "chemical/x-xyz", "image/bmp", "image/gif", "image/ief", "image/jpeg", "image/jpeg", "image/jpeg", "image/png",
			"image/tiff", "image/tiff", "image/vnd.djvu", "image/vnd.djvu", "image/vnd.wap.wbmp", "image/x-cmu-raster", "image/x-portable-anymap",
			"image/x-portable-bitmap", "image/x-portable-graymap", "image/x-portable-pixmap", "image/x-rgb", "image/x-xbitmap", "image/x-xpixmap",
			"image/x-xwindowdump", "model/iges", "model/iges", "model/mesh", "model/mesh", "model/mesh", "model/vrml", "model/vrml", "text/css", "text/html",
			"text/html", "text/plain", "text/plain", "text/richtext", "text/rtf", "text/sgml", "text/sgml", "text/tab-separated-values", "text/vnd.wap.wml",
			"text/vnd.wap.wmlscript", "text/x-setext", "text/xml", "text/xml", "video/mpeg", "video/mpeg", "video/mpeg", "video/quicktime", "video/quicktime",
			"video/vnd.mpegurl", "video/x-msvideo", "video/x-sgi-movie", "x-conference/x-cooltalk" };

	/**
	 * {@inheritDoc}
	 * 
	 * @author covito
	 */
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setContentType("application/octet-stream");
		response.setDateHeader("expires", System.currentTimeMillis() + 60000L);
		response.addHeader("Cache-Control", "max-age=60");
		String path = request.getPathInfo();
		if ((path == null) || (path.length() < 1)) {
			noFileError(response, path);
			return;
		}
		if (fileService == null) {
			fileService = FileServiceUtil.getFileService();
		}
		try {
			this.outputFile(response, path);
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
			noFileError(response, path);
			return;
		}
	}

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
		response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
		String extname=fileName.substring(fileName.lastIndexOf(".")+1, fileName.length());
		int index=-1;
		for(int i=0;i<ext.length;i++){
			if(ext[i].equalsIgnoreCase(extname)){
				index=i;
				break;
			}
		}
		if(index!=-1){
			response.setContentType(contentType[index]);
		}
		ServletOutputStream os = response.getOutputStream();
		fileService.outputFile(path, os);
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
			writer.println("<br><br>Could not get file path: " + fileName);
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