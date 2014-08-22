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
package org.covito.kit.utility;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.color.ColorSpace;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.awt.image.CropImageFilter;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.imageio.ImageIO;

/**
 * 图片处理工具类
 * <p>
 * 功能详细描述
 * </p>
 * 
 * @author covito
 * @version [v1.0, 2014-5-30]
 */
public class ImageUtil {

	/**
	 * 几种常见的图片格式
	 */
	public static String IMAGE_TYPE_GIF = "gif";// 图形交换格式
	public static String IMAGE_TYPE_JPG = "jpg";// 联合照片专家组
	public static String IMAGE_TYPE_JPEG = "jpeg";// 联合照片专家组
	public static String IMAGE_TYPE_BMP = "bmp";// Windows操作系统中的标准图像文件格式
	public static String IMAGE_TYPE_PNG = "png";// 可移植网络图形
	public static String IMAGE_TYPE_PSD = "psd";// Photoshop的专用格式

	/**
	 * 程序入口：用于测试
	 * 
	 * @param args
	 * @throws FileNotFoundException
	 */
	public static void main(String[] args) throws FileNotFoundException {
		ImageUtil.scale(new FileInputStream("e:/abc.jpg"),
				new FileOutputStream("e:/abc_scale.jpg"), 2);
		
		ImageUtil.scale(new FileInputStream("e:/abc.jpg"),
				new FileOutputStream("e:/abc_scale2.jpg"), 0.5);
		
		ImageUtil.scale(new FileInputStream("e:/abc.jpg"),
				new FileOutputStream("e:/abc_scale3.jpg"), 800, 800, false);
		
		ImageUtil.tensile(new FileInputStream("e:/abc.jpg"),
				new FileOutputStream("e:/abc_tensile.jpg"), 400, 400);

		ImageUtil.cut(new FileInputStream("e:/abc.jpg"), new FileOutputStream(
				"e:/abc_cut.jpg"), 0, 0, 1000, 600);

		ImageUtil.convert(new FileInputStream("e:/abc.jpg"),
				new FileOutputStream("e:/abc_convert.gif"),
				ImageUtil.IMAGE_TYPE_GIF);

		ImageUtil.gray(new FileInputStream("e:/abc.jpg"), new FileOutputStream(
				"e:/abc_gray.jpg"));

		ImageUtil.pressText(new FileInputStream("e:/abc.jpg"),
				new FileOutputStream("e:/abc_pressText.jpg"), "我是水印文字", "宋体",
				Font.BOLD, Color.green, 24, 10, 50, 0.5f);

		ImageUtil.pressImage(new FileInputStream("e:/abc.jpg"),
				new FileInputStream("e:/press.jpg"), new FileOutputStream(
						"e:/abc_pressImage.jpg"), 100, 100, 200, 200, 0.3f);
	}

	/**
	 * 缩放图像（按比例缩放）
	 * 
	 * @param is
	 *            源图像输入流
	 * @param result
	 *            缩放后的输出流
	 * @param scale
	 *            缩放比例
	 */
	public final static void scale(InputStream is, OutputStream os, double scale) {
		try {
			BufferedImage src = ImageIO.read(is);
			int width = src.getWidth(); // 得到源图宽
			int height = src.getHeight(); // 得到源图长
			width = new Double(width * scale).intValue();
			height = new Double(height * scale).intValue();
			Image image = src.getScaledInstance(width, height,
					Image.SCALE_DEFAULT);
			BufferedImage tag = new BufferedImage(width, height,
					BufferedImage.TYPE_INT_RGB);
			Graphics g = tag.getGraphics();
			g.drawImage(image, 0, 0, null); // 绘制缩小后的图
			g.dispose();
			ImageIO.write(tag, IMAGE_TYPE_JPEG, os);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 缩放图像（指定高度和宽度按比例缩放）
	 * <p>
	 * 功能详细描述
	 * </p>
	 * 
	 * @author covito
	 * @param is
	 *            源图像输入流
	 * @param os
	 *            缩放后输出流
	 * @param width
	 *            缩放后的高度
	 * @param height
	 *            缩放后的宽度
	 * @param bb
	 */
	public final static void scale(InputStream is, OutputStream os, int width,
			int height, boolean bb) {
		try {
			double ratio = 0.0; // 缩放比例
			BufferedImage bi = ImageIO.read(is);
			Image itemp = bi.getScaledInstance(width, height,
					BufferedImage.SCALE_SMOOTH);
			
			ratio = (new Integer(height)).doubleValue()
					/ bi.getHeight();
			
			double ratio2 = (new Integer(width)).doubleValue() / bi.getWidth();
			if (ratio > ratio2) {
				ratio = ratio2;
			} 
			AffineTransformOp op = new AffineTransformOp(
					AffineTransform.getScaleInstance(ratio, ratio), null);
			itemp = op.filter(bi, null);
			
			if (bb) {// 补白
				BufferedImage image = new BufferedImage(width, height,
						BufferedImage.TYPE_INT_RGB);
				Graphics2D g = image.createGraphics();
				g.setColor(Color.white);
				g.fillRect(0, 0, width, height);
				if (width == itemp.getWidth(null))
					g.drawImage(itemp, 0, (height - itemp.getHeight(null)) / 2,
							itemp.getWidth(null), itemp.getHeight(null),
							Color.white, null);
				else
					g.drawImage(itemp, (width - itemp.getWidth(null)) / 2, 0,
							itemp.getWidth(null), itemp.getHeight(null),
							Color.white, null);
				g.dispose();
				itemp = image;
			}
			ImageIO.write((BufferedImage) itemp, IMAGE_TYPE_JPEG, os);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 拉伸图像（指定高度和宽度）
	 * <p>
	 * 功能详细描述
	 * </p>
	 * 
	 * @author covito
	 * @param is
	 *            源图像输入流
	 * @param os
	 *            缩放后输出流
	 * @param width
	 *            缩放后的高度
	 * @param height
	 *            缩放后的宽度
	 */
	public final static void tensile(InputStream is, OutputStream os, int width,
			int height) {
		try {
			
			BufferedImage bi = ImageIO.read(is);
			Image itemp = bi.getScaledInstance(width, height,
					BufferedImage.SCALE_SMOOTH);
			double ratio = (new Integer(height)).doubleValue()
					/ bi.getHeight();
			
			double ratio2 = (new Integer(width)).doubleValue() / bi.getWidth();
			
			AffineTransformOp op = new AffineTransformOp(
					AffineTransform.getScaleInstance(ratio2, ratio), null);
			itemp = op.filter(bi, null);
			ImageIO.write((BufferedImage) itemp, IMAGE_TYPE_JPEG, os);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 图像切割(按指定起点坐标和宽高切割)
	 * 
	 * @param is
	 *            源图像输入流
	 * @param os
	 *            切割后输出流
	 * @param x
	 *            目标切片起点坐标X
	 * @param y
	 *            目标切片起点坐标Y
	 * @param width
	 *            目标切片宽度
	 * @param height
	 *            目标切片高度
	 */
	public final static void cut(InputStream is, OutputStream os, int x, int y,
			int width, int height) {
		try {
			// 读取源图像
			BufferedImage bi = ImageIO.read(is);
			int srcWidth = bi.getHeight(); // 源图宽度
			int srcHeight = bi.getWidth(); // 源图高度
			if (srcWidth > 0 && srcHeight > 0) {

				Image image = bi.getScaledInstance(srcWidth, srcHeight,
						Image.SCALE_DEFAULT);
				// 四个参数分别为图像起点坐标和宽高
				ImageFilter cropFilter = new CropImageFilter(x, y, width,
						height);
				Image img = Toolkit.getDefaultToolkit().createImage(
						new FilteredImageSource(image.getSource(), cropFilter));
				BufferedImage tag = new BufferedImage(width, height,
						BufferedImage.TYPE_INT_RGB);
				Graphics g = tag.getGraphics();
				g.drawImage(img, 0, 0, width, height, null); // 绘制切割后的图
				g.dispose();
				// 输出为文件
				ImageIO.write(tag, IMAGE_TYPE_JPEG, os);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 图像类型转换：GIF->JPG、GIF->PNG、PNG->JPG、PNG->GIF(X)、BMP->PNG
	 * 
	 * @param is
	 *            源图像输入流
	 * @param os
	 *            图像输出流
	 * @param image_type
	 *            包含格式非正式名称的 String：如JPG、JPEG、GIF等 目标图像地址
	 */
	public final static void convert(InputStream is, OutputStream os,
			String image_type) {
		try {
			BufferedImage src = ImageIO.read(is);
			ImageIO.write(src, image_type, os);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 彩色转为黑白
	 * 
	 * @param is
	 *            源图像输入流
	 * @param os
	 *            图像输出流
	 */
	public final static void gray(InputStream is, OutputStream os) {
		try {
			BufferedImage src = ImageIO.read(is);
			ColorSpace cs = ColorSpace.getInstance(ColorSpace.CS_GRAY);
			ColorConvertOp op = new ColorConvertOp(cs, null);
			src = op.filter(src, null);
			ImageIO.write(src, IMAGE_TYPE_JPEG, os);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 给图片添加文字水印
	 * 
	 * @param pressText
	 *            水印文字
	 * @param is
	 *            源图像输入流
	 * @param os
	 *            图像输出流
	 * @param fontName
	 *            水印的字体名称 如："宋体"
	 * @param fontStyle
	 *            水印的字体样式 如：Font.BOLD
	 * @param color
	 *            水印的字体颜色 如：Color.white
	 * @param fontSize
	 *            水印的字体大小
	 * @param x
	 *            修正值
	 * @param y
	 *            修正值
	 * @param alpha
	 *            透明度：alpha 必须是范围 [0.0, 1.0] 之内（包含边界值）的一个浮点数字
	 */
	public final static void pressText(InputStream is, OutputStream os,
			String pressText, String fontName, int fontStyle, Color color,
			int fontSize, int x, int y, float alpha) {
		try {
			Image src = ImageIO.read(is);
			int width = src.getWidth(null);
			int height = src.getHeight(null);
			BufferedImage image = new BufferedImage(width, height,
					BufferedImage.TYPE_INT_RGB);
			Graphics2D g = image.createGraphics();
			g.drawImage(src, 0, 0, width, height, null);
			g.setColor(color);
			g.setFont(new Font(fontName, fontStyle, fontSize));
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP,
					alpha));
			// 在指定坐标绘制水印文字
			g.drawString(pressText, (width - (getLength(pressText) * fontSize))
					/ 2 + x, (height - fontSize) / 2 + y);
			g.dispose();
			ImageIO.write((BufferedImage) image, IMAGE_TYPE_JPEG, os);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 给图片添加图片水印
	 * 
	 * @param pressImg
	 *            水印图片输入流
	 * @param is
	 *            源图像输入流
	 * @param os
	 *            图像输出流
	 * @param x
	 *            修正值。 默认在中间
	 * @param y
	 *            修正值。 默认在中间
	 * @param width
	 *            水印宽度
	 * @param height
	 *            水印高度
	 * @param alpha
	 *            透明度：alpha 必须是范围 [0.0, 1.0] 之内（包含边界值）的一个浮点数字
	 */
	public final static void pressImage(InputStream is, InputStream pressImg,
			OutputStream os, int x, int y, int width, int height, float alpha) {
		try {
			Image src = ImageIO.read(is);
			int srcwideth = src.getWidth(null);
			int srcheight = src.getHeight(null);
			BufferedImage image = new BufferedImage(srcwideth, srcheight,
					BufferedImage.TYPE_INT_RGB);
			Graphics2D g = image.createGraphics();
			g.drawImage(src, 0, 0, srcwideth, srcheight, null);
			// 水印文件
			Image src_biao = ImageIO.read(pressImg);
			int wideth_biao = src_biao.getWidth(null);
			int height_biao = src_biao.getHeight(null);
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP,
					alpha));
			g.drawImage(src_biao, (srcwideth - wideth_biao) / 2 + x,
					(srcheight - height_biao) / 2 + y, width, height, null);
			// 水印文件结束
			g.dispose();
			ImageIO.write((BufferedImage) image, IMAGE_TYPE_JPEG, os);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 计算text的长度（一个中文算两个字符）
	 * 
	 * @param text
	 * @return
	 */
	private final static int getLength(String text) {
		int length = 0;
		for (int i = 0; i < text.length(); i++) {
			if (new String(text.charAt(i) + "").getBytes().length > 1) {
				length += 2;
			} else {
				length += 1;
			}
		}
		return length / 2;
	}
}
