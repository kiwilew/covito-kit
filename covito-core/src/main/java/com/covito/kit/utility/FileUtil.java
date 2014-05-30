package com.covito.kit.utility;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.URL;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.covito.kit.core.Config;

/**
 * 文件操作工具类<br/>
 * <功能详细描述>
 * 
 * @author  eighteencold
 * @version  [v1.0, 2014-1-23]
 */
public class FileUtil {
	
	private static Log log = LogFactory.getLog(FileUtil.class);
	
	/** 
	 * <路径正常化><br/>
	 * <\转换成/。../转成/。./转成/或去掉重复的/>
	 *
	 * @author  eighteencold
	 * @param path
	 * @return
	 */
	public static String normalizePath(String path) {
		path = path.replace('\\', '/');
		path = StringUtil.replace(path, "../", "/");
		path = StringUtil.replace(path, "./", "/");
		if (path.endsWith("..")) {
			path = path.substring(0, path.length() - 2);
		}
		path = path.replaceAll("/+", "/");
		return path;
	}

	/** 
	 * 路径正常化<br/>
	 * <功能详细描述>
	 *
	 * @author  eighteencold
	 * @param f
	 * @return
	 */
	public static File normalizeFile(File f) {
		String path = f.getAbsolutePath();
		path = normalizePath(path);
		return new File(path);
	}

	/** 
	 * 获取文件扩展名（文件类型）<br/>
	 * FileUtil.getExtension("fia.xml")
	 *
	 * @author  eighteencold
	 * @param fileName
	 * @return
	 */
	public static String getExtension(String fileName) {
		int index = fileName.lastIndexOf(".");
		if (index < 0) {
			return null;
		}
		String ext = fileName.substring(index + 1);
		return ext.toLowerCase();
	}

	/** 
	 * <将指定内容写入文件><br/>
	 * <功能详细描述>
	 *
	 * @author  eighteencold
	 * @param fileName
	 * @param content
	 * @return
	 */
	public static boolean writeText(String fileName, String content) {
		fileName = normalizePath(fileName);
		return writeText(fileName, content, Config.getGlobalCharset());
	}

	/** 
	 * <将指定内容写入文件><br/>
	 * <功能详细描述>
	 *
	 * @author  eighteencold
	 * @param fileName
	 * @param content
	 * @param encoding
	 * @return
	 */
	public static boolean writeText(String fileName, String content,
			String encoding) {
		fileName = normalizePath(fileName);
		return writeText(fileName, content, encoding, false);
	}

	/** 
	 * 将指定内容写入文件<br/>
	 * <功能详细描述>
	 *
	 * @author  eighteencold
	 * @param fileName
	 * @param content
	 * @param encoding
	 * @param bomFlag
	 * @return
	 */
	public static boolean writeText(String fileName, String content,
			String encoding, boolean bomFlag) {
		fileName = normalizePath(fileName);
		try {
			byte[] bs = content.getBytes(encoding);
			if ((encoding.equalsIgnoreCase("UTF-8")) && (bomFlag)) {
				bs = ArrayUtils.addAll(StringUtil.BOM, bs);
			}
			writeByte(fileName, bs);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return true;
	}

	/** 
	 * 读取文件内容为byte[]<br/>
	 * <功能详细描述>
	 *
	 * @author  eighteencold
	 * @param fileName
	 * @return
	 */
	public static byte[] readByte(String fileName) {
		fileName = normalizePath(fileName);
		try {
			FileInputStream fis = new FileInputStream(fileName);
			byte[] r = new byte[fis.available()];
			fis.read(r);
			fis.close();
			return r;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/** 
	 * 读取文件内容为byte[]<br/>
	 * <功能详细描述>
	 *
	 * @author  eighteencold
	 * @param f
	 * @return
	 */
	public static byte[] readByte(File f) {
		f = normalizeFile(f);
		try {
			FileInputStream fis = new FileInputStream(f);
			byte[] r = readByte(fis);
			fis.close();
			return r;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/** 
	 * 读取文件内容为byte[]<br/>
	 * <功能详细描述>
	 *
	 * @author  eighteencold
	 * @param is
	 * @return
	 */
	public static byte[] readByte(InputStream is) {
		if(is==null){
			throw new RuntimeException("InputStream is null");
		}
		byte[] buffer = new byte[8192];
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		while (true) {
			int bytesRead = -1;
			try {
				bytesRead = is.read(buffer);
			} catch (IOException e) {
				throw new RuntimeException("File.readByte() failed");
			}
			if (bytesRead == -1)
				break;
			try {
				os.write(buffer, 0, bytesRead);
			} catch (Exception e) {
				throw new RuntimeException("File.readByte() failed");
			}
		}
		return os.toByteArray();
	}

	/** 
	 * 将byte[]写入文件<br/>
	 * <功能详细描述>
	 *
	 * @author  eighteencold
	 * @param fileName
	 * @param b
	 * @return
	 */
	public static boolean writeByte(String fileName, byte[] b) {
		fileName = normalizePath(fileName);
		File p=new File(fileName).getParentFile();
		if(p!=null&&!p.exists()){
			mkdir(p.getAbsolutePath());
		}
		try {
			BufferedOutputStream fos = new BufferedOutputStream(
					new FileOutputStream(fileName));
			fos.write(b);
			fos.close();
			return true;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/** 
	 * 将byte[]写入文件<br/>
	 * <功能详细描述>
	 *
	 * @author  eighteencold
	 * @param f
	 * @param b
	 * @return
	 */
	public static boolean writeByte(File f, byte[] b) {
		f = normalizeFile(f);
		try {
			BufferedOutputStream fos = new BufferedOutputStream(
					new FileOutputStream(f));
			fos.write(b);
			fos.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/** 
	 * 读取文件内容<br/>
	 * <功能详细描述>
	 *
	 * @author  eighteencold
	 * @param f
	 * @return
	 */
	public static String readText(File f) {
		f = normalizeFile(f);
		return readText(f, Config.getGlobalCharset());
	}

	/** 
	 * 读取文件内容<br/>
	 * <功能详细描述>
	 *
	 * @author  eighteencold
	 * @param f
	 * @param encoding 编码格式
	 * @return
	 */
	public static String readText(File f, String encoding) {
		f = normalizeFile(f);
		try {
			InputStream is = new FileInputStream(f);
			String str = readText(is, encoding);
			is.close();
			return str;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/** 
	 * <读取文件内容><br/>
	 * <功能详细描述>
	 *
	 * @author  eighteencold
	 * @param is
	 * @return
	 */
	public static String readText(InputStream is) {
		return readText(is, Config.getGlobalCharset());
	}
	/** 
	 * <读取文件内容><br/>
	 * <功能详细描述>
	 *
	 * @author  eighteencold
	 * @param is
	 * @param encoding
	 * @return
	 */
	public static String readText(InputStream is, String encoding) {
		try {
			byte[] bs = readByte(is);
			//UTF乱码处理，所有采用UTF-8格式编码的文件的文件头三个字节用16进制表示是EFBBBF
			if ((encoding.equalsIgnoreCase("utf-8"))
					&& (EncodeUtil.hexEncode(ArrayUtils.subarray(bs, 0, 3))
							.equals("efbbbf"))) {
				//截去前面三个字符
				bs = ArrayUtils.subarray(bs, 3, bs.length);
			}
			return new String(bs, encoding);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/** 
	 * 读取文件内容<br/>
	 * <功能详细描述>
	 *
	 * @author  eighteencold
	 * @param fileName
	 * @return
	 */
	public static String readText(String fileName) {
		fileName = normalizePath(fileName);
		return readText(fileName, Config.getGlobalCharset());
	}

	/** 
	 * 读取文件内容<br/>
	 * <功能详细描述>
	 *
	 * @author  eighteencold
	 * @param fileName
	 * @param encoding
	 * @return
	 */
	public static String readText(String fileName, String encoding) {
		fileName = normalizePath(fileName);
		try {
			InputStream is = new FileInputStream(fileName);
			String str = readText(is, encoding);
			is.close();
			return str;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/** 
	 * <读取URL资源文本,默认UTF-8><br/>
	 * <功能详细描述>
	 *
	 * @author  eighteencold
	 * @param urlPath
	 * @return
	 */
	public static String readURLText(String urlPath) {
		return readURLText(urlPath, Config.getGlobalCharset());
	}

	/** 
	 * <读取URL资源文本><br/>
	 * <功能详细描述>
	 *
	 * @author  eighteencold
	 * @param urlPath
	 * @param encoding
	 * @return
	 */
	public static String readURLText(String urlPath, String encoding) {
		try {
			URL url = new URL(urlPath);
			BufferedReader in = new BufferedReader(new InputStreamReader(
					url.openStream(), encoding));

			StringBuilder sb = new StringBuilder();
			String line;
			while ((line = in.readLine()) != null) {
				sb.append(line + "\n");
			}
			in.close();
			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/** 
	 * 删除文件<br/>
	 * 如果是个文件夹，递归删除文件夹
	 *
	 * @author  eighteencold
	 * @param path
	 * @return
	 */
	public static boolean delete(String path) {
		path = normalizePath(path);
		File file = new File(path);
		return delete(file);
	}

	/** 
	 * 删除文件<br/>
	 * 如果是个文件夹，递归删除文件夹
	 *
	 * @author  eighteencold
	 * @param f
	 * @return
	 */
	public static boolean delete(File f) {
		f = normalizeFile(f);
		if (!f.exists()) {
			log.warn("File or directory not found" + f);
			return false;
		}
		if (f.isFile()) {
			return f.delete();
		}
		return deleteDir(f);
	}

	/** 
	 * 删除文件夹(包括本身)<br/>
	 * 递归删除下面所有子文件夹和文件
	 *
	 * @author  eighteencold
	 * @param dir
	 * @return
	 */
	private static boolean deleteDir(File dir) {
		dir = normalizeFile(dir);
		try {
			return (deleteFromDir(dir)) && (dir.delete());
		} catch (Exception e) {
			log.warn("Delete directory failed");
		}
		return false;
	}

	/** 
	 * <创建文件夹><br/>
	 * <当文件夹存在时不做任何操作>
	 *
	 * @author  eighteencold
	 * @param path
	 * @return
	 */
	public static boolean mkdir(String path) {
		path = normalizePath(path);
		File dir = new File(path);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		return true;
	}

	/** 
	 * <文件是否存在><br/>
	 * <功能详细描述>
	 *
	 * @author  eighteencold
	 * @param path
	 * @return
	 */
	public static boolean exists(String path) {
		path = normalizePath(path);
		File dir = new File(path);
		return dir.exists();
	}

	/** 
	 * 删除文件夹所有文件夹和文件，但不删除本身<br/>
	 * <功能详细描述>
	 *
	 * @author  eighteencold
	 * @param dirPath
	 * @return
	 */
	public static boolean deleteFromDir(String dirPath) {
		dirPath = normalizePath(dirPath);
		File file = new File(dirPath);
		return deleteFromDir(file);
	}

	/** 
	 * 删除文件夹所有文件夹和文件，但不删除本身<br/>
	 * <功能详细描述>
	 *
	 * @author  eighteencold
	 * @param dir
	 * @return
	 */
	public static boolean deleteFromDir(File dir) {
		dir = normalizeFile(dir);
		if (!dir.exists()) {
			log.warn("Directory not found " + dir);
			return false;
		}
		if (!dir.isDirectory()) {
			log.warn(dir + " is not directory");
			return false;
		}
		File[] tempList = dir.listFiles();
		for (int i = 0; i < tempList.length; i++) {
			if (!delete(tempList[i])) {
				return false;
			}
		}
		return true;
	}

	/** 
	 * 拷贝文件，或文件夹<br/>
	 * <忽略Thumbs.db文件目录文件夹不存在，新建文件夹>
	 *
	 * @author  eighteencold
	 * @param oldPath
	 * @param newPath
	 * @param filter
	 * @return
	 */
	public static boolean copy(String oldPath, String newPath, FileFilter filter) {
		oldPath = normalizePath(oldPath);
		newPath = normalizePath(newPath);
		File oldFile = new File(oldPath);
		File[] oldFiles = oldFile.listFiles(filter);
		boolean flag = true;
		if (oldFiles != null) {
			for (int i = 0; i < oldFiles.length; i++) {
				if (!copy(oldFiles[i], newPath + "/" + oldFiles[i].getName())) {
					flag = false;
				}
			}
		}
		return flag;
	}

	/** 
	 * 拷贝文件，或文件夹<br/>
	 * <忽略Thumbs.db文件目录文件夹不存在，新建文件夹>
	 *
	 * @author  eighteencold
	 * @param oldPath
	 * @param newPath
	 * @return
	 */
	public static boolean copy(String oldPath, String newPath) {
		oldPath = normalizePath(oldPath);
		newPath = normalizePath(newPath);
		File oldFile = new File(oldPath);
		return copy(oldFile, newPath);
	}

	/** 
	 * 拷贝文件，或文件夹<br/>
	 * <忽略Thumbs.db文件，目录文件夹不存在，新建文件夹>
	 *
	 * @author  eighteencold
	 * @param oldFile
	 * @param newPath
	 * @return
	 */
	public static boolean copy(File oldFile, String newPath) {
		oldFile = normalizeFile(oldFile);
		newPath = normalizePath(newPath);
		if (!oldFile.exists()) {
			log.warn("File not found:" + oldFile);
			return false;
		}
		if (oldFile.isFile()) {
			return copyFile(oldFile, newPath);
		}
		return copyDir(oldFile, newPath);
	}

	/** 
	 * 拷贝文件<br/>
	 * <功能详细描述>
	 *
	 * @author  eighteencold
	 * @param oldFile
	 * @param newPath
	 * @return
	 */
	private static boolean copyFile(File oldFile, String newPath) {
		oldFile = normalizeFile(oldFile);
		newPath = normalizePath(newPath);
		if (!oldFile.exists()) {
			log.warn("File not found:" + oldFile);
			return false;
		}
		if (!oldFile.isFile()) {
			log.warn(oldFile + " is not file");
			return false;
		}
		if (oldFile.getName().equalsIgnoreCase("Thumbs.db")) {
			log.warn(oldFile + " is ignored");
			return true;
		}
		try {
			int byteread = 0;
			InputStream inStream = new FileInputStream(oldFile);
			File newFile = new File(newPath);

			if (newFile.isDirectory()) {
				newFile = new File(newPath, oldFile.getName());
			}
			FileOutputStream fs = new FileOutputStream(newFile);
			byte[] buffer = new byte[1024];
			while ((byteread = inStream.read(buffer)) != -1) {
				fs.write(buffer, 0, byteread);
			}
			fs.close();
			newFile.setLastModified(oldFile.lastModified());
			inStream.close();
		} catch (Exception e) {
			log.warn("Copy file " + oldFile.getPath() + " failed,cause:"
					+ e.getMessage());
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/** 
	 * 拷贝文件夹<br/>
	 * <功能详细描述>
	 *
	 * @author  eighteencold
	 * @param oldDir
	 * @param newPath
	 * @return
	 */
	private static boolean copyDir(File oldDir, String newPath) {
		oldDir = normalizeFile(oldDir);
		newPath = normalizePath(newPath);
		if (!oldDir.exists()) {
			log.info("File not found:" + oldDir);
			return false;
		}
		if (!oldDir.isDirectory()) {
			log.info(oldDir + " is not directory");
			return false;
		}
		if (!new File(newPath).getParentFile().exists()) {
			new File(newPath).getParentFile().mkdirs();
		}
		try {
			new File(newPath).mkdirs();
			File[] files = oldDir.listFiles();
			File temp = null;
			for (int i = 0; i < files.length; i++) {
				temp = files[i];
				if (temp.isFile()) {
					if (!copyFile(temp, newPath + "/" + temp.getName()))
						return false;
				} else if ((temp.isDirectory())
						&& (!copyDir(temp, newPath + "/" + temp.getName()))) {
					return false;
				}
			}

			return true;
		} catch (Exception e) {
			log.info("Copy directory failed,cause:" + e.getMessage());
		}
		return false;
	}

	/** 
	 * 移动文件或文件夹<br/>
	 * <功能详细描述>
	 *
	 * @author  eighteencold
	 * @param oldPath
	 * @param newPath
	 * @return
	 */
	public static boolean move(String oldPath, String newPath) {
		oldPath = normalizePath(oldPath);
		newPath = normalizePath(newPath);
		return (copy(oldPath, newPath)) && (delete(oldPath));
	}

	/** 
	 * 移动文件或文件夹<br/>
	 * <功能详细描述>
	 *
	 * @author  eighteencold
	 * @param oldFile
	 * @param newPath
	 * @return
	 */
	public static boolean move(File oldFile, String newPath) {
		oldFile = normalizeFile(oldFile);
		newPath = normalizePath(newPath);
		return (copy(oldFile, newPath)) && (delete(oldFile));
	}

	/** 
	 * 将对象序列化至文件中保存<br/>
	 * <功能详细描述>
	 *
	 * @author  eighteencold
	 * @param obj
	 * @param fileName
	 */
	public static void serialize(Serializable obj, String fileName) {
		fileName = normalizePath(fileName);
		try {
			FileOutputStream f = new FileOutputStream(fileName);
			ObjectOutputStream s = new ObjectOutputStream(f);
			s.writeObject(obj);
			s.flush();
			s.close();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/** 
	 * 将对象序列化成byte[]<br/>
	 * <功能详细描述>
	 *
	 * @author  eighteencold
	 * @param obj
	 * @return
	 */
	public static byte[] serialize(Serializable obj) {
		try {
			ByteArrayOutputStream b = new ByteArrayOutputStream();
			ObjectOutputStream s = new ObjectOutputStream(b);
			s.writeObject(obj);
			s.flush();
			s.close();
			return b.toByteArray();
		} catch (Exception e) {
		}
		return null;
	}

	/** 
	 * 反序列化对象<br/>
	 * <功能详细描述>
	 *
	 * @author  eighteencold
	 * @param fileName
	 * @return
	 */
	public static Object unserialize(String fileName) {
		fileName = normalizePath(fileName);
		try {
			FileInputStream in = new FileInputStream(fileName);
			ObjectInputStream s = new ObjectInputStream(in);
			Object o = s.readObject();
			s.close();
			return o;
		} catch (Exception e) {
		}
		return null;
	}

	/** 
	 * 反序列化对象<br/>
	 * <功能详细描述>
	 *
	 * @author  eighteencold
	 * @param bs
	 * @return
	 */
	public static Object unserialize(byte[] bs) {
		try {
			ByteArrayInputStream in = new ByteArrayInputStream(bs);
			ObjectInputStream s = new ObjectInputStream(in);
			Object o = s.readObject();
			s.close();
			return o;
		} catch (Exception e) {
		}
		return null;
	}
	
}
