package org.covito.kit.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFFormulaEvaluator;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.covito.kit.excel.ExcelField.ExAct;
import org.covito.kit.excel.ExcelField.NULLHolder;
import org.covito.kit.excel.ExcelField.ValueHandler;
import org.covito.kit.utility.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 导入Excel文件（支持“XLS”和“XLSX”格式）
 * 
 */
public class ImportExcel {

	private Logger log = LoggerFactory.getLogger(getClass());

	/**
	 * 工作薄对象
	 */
	private Workbook wb;

	/**
	 * 工作表对象
	 */
	private Sheet sheet;

	/**
	 * 标题行号
	 */
	private int headerNum;

	/**
	 * 是否为excel2007以上版本
	 */
	private boolean isXS;

	/**
	 * 合并单元格value Map
	 */
	private Map<CellRangeAddress, Object> rangesMap = new HashMap<CellRangeAddress, Object>();
	
	/**
	 * 注解列表（Object[]{ ExcelField, Field/Method, impHandler }）
	 */
	private List<Object[]> annotationList = null;

	/**
	 * 构造函数
	 * 
	 * @param path
	 *            导入文件，读取第一个工作表
	 * @param headerNum
	 *            标题行号，数据行号=标题行号+1
	 * @throws InvalidFormatException
	 * @throws IOException
	 */
	public ImportExcel(String fileName, int headerNum) {
		this(new File(fileName), headerNum);
	}

	/**
	 * 构造函数
	 * 
	 * @param path
	 *            导入文件对象，读取第一个工作表
	 * @param headerNum
	 *            标题行号，数据行号=标题行号+1
	 * @throws InvalidFormatException
	 * @throws IOException
	 */
	public ImportExcel(File file, int headerNum) {
		this(file, headerNum, 0);
	}

	/**
	 * 构造函数
	 * 
	 * @param path
	 *            导入文件
	 * @param headerNum
	 *            标题行号，数据行号=标题行号+1
	 * @param sheetIndex
	 *            工作表编号
	 * @throws InvalidFormatException
	 * @throws IOException
	 */
	public ImportExcel(String fileName, int headerNum, int sheetIndex){
		this(new File(fileName), headerNum, sheetIndex);
	}

	/**
	 * 构造函数
	 * 
	 * @param path
	 *            导入文件对象
	 * @param headerNum
	 *            标题行号，数据行号=标题行号+1
	 * @param sheetIndex
	 *            工作表编号
	 * @throws InvalidFormatException
	 * @throws IOException
	 */
	public ImportExcel(File file, int headerNum, int sheetIndex) {
		if (file == null) {
			throw new RuntimeException("导入文档为空!");
		} else if (file.getName().toLowerCase().endsWith("xls")) {
			isXS = false;
		} else if (file.getName().toLowerCase().endsWith("xlsx")) {
			isXS = true;
		} else {
			throw new RuntimeException("文档格式不正确!");
		}
		try {
			init(new FileInputStream(file), headerNum, sheetIndex);
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 构造函数
	 * 
	 * @param isXs
	 *            是否为xlsx文件
	 * @param path
	 *            导入文件对象
	 * @param headerNum
	 *            标题行号，数据行号=标题行号+1
	 * @param sheetIndex
	 *            工作表编号
	 * @throws InvalidFormatException
	 * @throws IOException
	 */
	public ImportExcel(boolean isXs, InputStream is, int headerNum, int sheetIndex)
			throws InvalidFormatException, IOException {
		this.isXS = isXs;
		init(is, headerNum, sheetIndex);
	}

	/**
	 * 初始化
	 * 
	 * @param isXs
	 * @param is
	 * @param headerNum
	 * @param sheetIndex
	 * @throws IOException
	 */
	private void init(InputStream is, int headerNum, int sheetIndex){
		if (is == null) {
			throw new RuntimeException("InputStream is null");
		}
		try {
			if (isXS) {
				this.wb = new XSSFWorkbook(is);
			} else {
				this.wb = new HSSFWorkbook(is);
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		if (this.wb.getNumberOfSheets() < sheetIndex) {
			throw new RuntimeException("文档中没有工作表!");
		}
		this.sheet = this.wb.getSheetAt(sheetIndex);
		this.headerNum = headerNum;

		int num = sheet.getNumMergedRegions();
		for (int i = 0; i < num; i++) {
			CellRangeAddress cr = sheet.getMergedRegion(i);
			Cell cell = sheet.getRow(cr.getFirstRow()).getCell(cr.getFirstColumn());
			rangesMap.put(cr, getCellValue(cell));
		}

		log.debug("Initialize success.");
	}

	/**
	 * 获取行对象
	 * 
	 * @param rownum
	 * @return
	 */
	public Row getRow(int rownum) {
		return this.sheet.getRow(rownum);
	}

	/**
	 * 获取数据行号
	 * 
	 * @return
	 */
	public int getDataRowNum() {
		return headerNum + 1;
	}

	/**
	 * 获取最后一个数据行号
	 * 
	 * @return
	 */
	public int getLastDataRowNum() {
		return this.sheet.getLastRowNum() + headerNum;
	}

	/**
	 * 获取最后一个列号
	 * 
	 * @return
	 */
	public int getLastCellNum() {
		return this.getRow(headerNum).getLastCellNum();
	}

	/**
	 * 获取单元格值
	 * 
	 * @param row
	 *            获取的行
	 * @param column
	 *            获取单元格列号
	 * @return 单元格值
	 */
	public Object getCellValue(Row row, int column) {
		int rownum = row.getRowNum();
		for (CellRangeAddress cr : rangesMap.keySet()) {
			if (cr.isInRange(rownum, column)) {
				return rangesMap.get(cr);
			}
		}
		return getCellValue(row.getCell(column));
	}

	/**
	 * 获取单元格值
	 * 
	 * @param row
	 *            获取的行
	 * @param column
	 *            获取单元格列号
	 * @return 单元格值
	 */
	private Object getCellValue(Cell cell) {
		Object val = "";
		try {
			if (cell != null) {
				if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
					val = cell.getNumericCellValue();
				} else if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
					val = cell.getStringCellValue();
				} else if (cell.getCellType() == Cell.CELL_TYPE_FORMULA) {
					FormulaEvaluator evaluator;
					if(isXS){
						evaluator = new XSSFFormulaEvaluator((XSSFWorkbook)sheet.getWorkbook());
					}else{
						evaluator = new HSSFFormulaEvaluator((HSSFWorkbook)sheet.getWorkbook());
					}
					int result=evaluator.evaluateFormulaCell(cell);
					if(HSSFCell.CELL_TYPE_ERROR==result){
						val = cell.getErrorCellValue();
					}else if (result == HSSFCell.CELL_TYPE_NUMERIC) {
						val = cell.getNumericCellValue();
					} else if (result == HSSFCell.CELL_TYPE_STRING) {
						val = cell.getRichStringCellValue().toString();
					}
				} else if (cell.getCellType() == Cell.CELL_TYPE_BOOLEAN) {
					val = cell.getBooleanCellValue();
				} else if (cell.getCellType() == Cell.CELL_TYPE_ERROR) {
					val = cell.getErrorCellValue();
				}
			}
		} catch (Exception e) {
			return val;
		}
		return val;
	}

	private <E> void initAnnotationList(Class<E> cls) throws InstantiationException, IllegalAccessException{
		if(annotationList!=null){
			return;
		}
		annotationList =new ArrayList<Object[]>();
		Field[] fs = cls.getDeclaredFields();
		for (Field f : fs) {
			ExcelField ef = f.getAnnotation(ExcelField.class);
			if (ef != null && ef.type() != ExAct.exp) {
				Class<?> valType = f.getType();
				ValueHandler handler=null;
				if (ef.handler() != NULLHolder.class) {
					handler= ef.handler().newInstance();
				}
				annotationList.add(new Object[] { ef, f, handler, valType });
			}
		}
		// Get annotation method
		Method[] ms = cls.getDeclaredMethods();
		for (Method m : ms) {
			ExcelField ef = m.getAnnotation(ExcelField.class);
			if (ef != null && ef.type() != ExAct.exp) {
				ValueHandler handler=null;
				if (ef.handler() != NULLHolder.class) {
					handler = ef.handler().newInstance();
				}
				
				Class<?> valType=Class.class;
				if ("get".equals(m.getName().substring(0, 3))) {
					valType = m.getReturnType();
				} else if ("set".equals(m.getName().substring(0, 3))) {
					valType = m.getParameterTypes()[0];
				}
				annotationList.add(new Object[] { ef, m, handler,valType });
			}
		}
		// Field sorting
		Collections.sort(annotationList, new Comparator<Object[]>() {
			public int compare(Object[] o1, Object[] o2) {
				return new Integer(((ExcelField) o1[0]).sort()).compareTo(new Integer(
						((ExcelField) o2[0]).sort()));
			};
		});
	}
	
	/**
	 * 获取导入数据列表
	 * 
	 * @param cls
	 *            导入对象类型
	 * @param groups
	 *            导入分组
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	public <E> List<E> getDataList(Class<E> cls) {
		
		try {
			initAnnotationList(cls);
		}catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		List<E> dataList = new ArrayList<E>();
		for (int i = this.getDataRowNum(); i < this.getLastDataRowNum(); i++) {
			Row row = this.getRow(i);
			if(row==null){
				continue;
			}
			
			E e;
			try {
				e = cls.newInstance();
			} catch (Exception e1) {
				throw new RuntimeException(e1);
			}
			int column = 0;
			for (Object[] os : annotationList) {
				Object val = this.getCellValue(row, column++);
				
				if (val == null) {
					continue;
				}
				
				ValueHandler handler=(ValueHandler)os[2];
				Class<?> valType = (Class<?>)os[3];
				try {
					
					if (valType == String.class) {
						String s = String.valueOf(val.toString());
						if (StringUtils.endsWith(s, ".0")) {
							val = StringUtils.substringBefore(s, ".0");
						} else {
							val = String.valueOf(val.toString());
						}
					} else if (valType == Integer.class) {
						val = Double.valueOf(val.toString()).intValue();
					} else if (valType == Long.class) {
						val = Double.valueOf(val.toString()).longValue();
					} else if (valType == Double.class) {
						val = Double.valueOf(val.toString());
					} else if (valType == Float.class) {
						val = Float.valueOf(val.toString());
					} else if (valType == Date.class) {
						val = DateUtil.getJavaDate((Double) val);
					}
					
					if(os[2]!=null){
						val=handler.impConvert(val);
					}
				} catch (Exception ex) {
					log.info("Get cell value [" + i + "," + column + "] error: " + ex.toString());
					val = null;
				}
				// set entity value
				if (os[1] instanceof Field) {
					Reflections.invokeSetter(e, ((Field) os[1]).getName(), val);
				} else if (os[1] instanceof Method) {
					String mthodName = ((Method) os[1]).getName();
					if ("get".equals(mthodName.substring(0, 3))) {
						mthodName = "set" + StringUtils.substringAfter(mthodName, "get");
					}
					Reflections.invokeMethod(e, mthodName, new Class[] { valType },new Object[] { val });
				}
			}
			dataList.add(e);
		}
		return dataList;
	}

}
