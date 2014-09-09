package org.covito.kit.test.poi;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFFormulaEvaluator;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellReference;

/**
 * EXCEL文档解析工具类 该工具能将EXCEL文档中的表解析为由JAVA基础类构成的数据集合
 */
public class ExcelReader {

	private HSSFWorkbook workbook;

	public ExcelReader(File excelFile) throws FileNotFoundException, IOException {

		workbook = new HSSFWorkbook(new FileInputStream(excelFile));
	}

	/**
	 * 获得表中的数据 (测试)
	 * 
	 * @param sheetNumber
	 *            表格索引(EXCEL 是多表文档,所以需要输入表索引号)
	 */
	public void getDatasInSheet(int sheetNumber) throws FileNotFoundException, IOException {

		// 获得指定的表
		HSSFSheet sheet = workbook.getSheetAt(sheetNumber);
		this.headForm(sheet);

		// int rowCount = sheet.getLastRowNum();//获得数据总行数
		//
		// sheet.getSheetName(); //当前表的名称
		// //逐行读取数据
		// for (int rowIndex = 0; rowIndex <= rowCount; rowIndex++) {
		// HSSFRow row = sheet.getRow(rowIndex);//获得行对象
		// if (row != null) {
		// List<Object> rowData = new ArrayList<Object>();
		// int columnCount = row.getLastCellNum();//获得本行中单元格的个数
		// //获得本行中各单元格中的数据
		// for (int columnIndex = 0; columnIndex < columnCount; columnIndex++) {
		// HSSFCell cell = row.getCell(columnIndex);
		// //获得指定单元格中数据
		// Object cellStr = this.getCellString(cell);
		// rowData.add(cellStr);
		// }
		// }
		// }
	}

	/**
	 * 封装excel到map集合(入数据库应迭代map集合)
	 * 
	 * @param sheet
	 * @return
	 */
	public Map<String, List<List<String[]>>> headForm(HSSFSheet sheet) {

		Map<String, List<List<String[]>>> map = new HashMap<String, List<List<String[]>>>();

		String markstr = ""; // 标识,map的key

		List<List<String[]>> li = new ArrayList<List<String[]>>();

		List<ExcelCell> ecs = sysRange(sheet);
		ExcelCell ech = null, ecz = null;
		List<ExcelCell> echList = new ArrayList<ExcelCell>();
		List<ExcelCell> eczList = new ArrayList<ExcelCell>();
		List<ExcelCell> ecoList = new ArrayList<ExcelCell>();
		for (ExcelCell ec : ecs) {
			if (ec.getDirection() == 1) {
				if (ech == null)
					ech = ec;
				else
					ech = Math.min(ech.getFirstColumn(), ec.getFirstColumn()) == ech
							.getFirstColumn() ? ech : ec;
				echList.add(ec);
			} else if (ec.getDirection() == 0) {
				if (ecz == null)
					ecz = ec;
				else
					ecz = Math.min(ecz.getFirstRow(), ec.getFirstRow()) == ecz.getFirstRow() ? ecz
							: ec;
				eczList.add(ec);
			}
		}
		if (ech == null) { // 二维
			markstr = "2w";
			HSSFRow row = sheet.getRow(0);
			if (row != null) {
				int columnCount = row.getLastCellNum();
				for (int i = 0; i < columnCount; i++) {
					HSSFCell cell = row.getCell(i);
					Object cellStr = this.getCellString(cell);
					if (cellStr != null) {
						ExcelCell ec = new ExcelCell();
						ec.setCellValue(cellStr.toString());
						ec.setFirstRow(0);// 起始行
						ec.setLastRow(0);// 结束行
						ec.setFirstColumn(i);// 起始列
						ec.setLastColumn(i);// 结束列
						ec.setDirection(-1);
						ecoList.add(ec);
					}
				}
			}
			if (!ecoList.isEmpty()) {
				int rowCount = sheet.getLastRowNum();
				for (int i = 1; i <= rowCount; i++) {
					List<String[]> lol = new ArrayList<String[]>();
					String str = "";
					HSSFRow row1 = sheet.getRow(i);
					for (ExcelCell ec : ecoList) {
						if (row1 != null) {
							HSSFCell cell1 = row1.getCell(ec.getFirstColumn());
							Object cellStr1 = this.getCellString(cell1);
							str += ec.getCellValue() + ":" + cellStr1 + "  ";

							String[] str1 = new String[2];
							str1[0] = ec.getCellValue();
							str1[1] = cellStr1.toString();
							lol.add(str1);
						}
					}
					li.add(lol);
					System.out.println(str);
				}
			}
		} else if (ech.getFirstColumn() != null && ech.getFirstColumn() == 1) { // 三维
			markstr = "3w";
			int i = ech.getFirstRow() + 1;
			HSSFRow row = sheet.getRow(i);
			if (row != null) {
				for (int j = 0; j < ech.getFirstColumn(); j++) {
					HSSFCell cell = row.getCell(j);
					Object cellStr = this.getCellString(cell);
					if (cellStr != null) {
						ExcelCell ec = new ExcelCell();
						ec.setCellValue(cellStr.toString());
						ec.setFirstRow(i);// 起始行
						ec.setLastRow(i);// 结束行
						ec.setFirstColumn(j);// 起始列
						ec.setLastColumn(j);// 结束列
						ec.setDirection(-1);
						ecoList.add(ec);
					}
				}
			}
			for (ExcelCell ec1 : echList) {
				for (ExcelCell ec2 : ecoList) {
					int rowCount = sheet.getLastRowNum();
					for (int h = ec2.getFirstRow() + 1; h <= rowCount; h++) {
						HSSFRow row1 = sheet.getRow(h);
						HSSFCell cell1 = row1.getCell(ec2.getFirstColumn());
						Object cellStr1 = this.getCellString(cell1);

						List<String[]> lol = new ArrayList<String[]>();
						String[] str1 = new String[2];
						str1[0] = null;
						str1[1] = ec1.getCellValue();
						String[] str2 = new String[2];
						str2[0] = ec2.getCellValue();
						str2[1] = cellStr1.toString();
						lol.add(str1);
						lol.add(str2);

						String str = "";
						for (int j = ec1.getFirstColumn(); j <= ec1.getLastColumn(); j++) {
							HSSFRow row2 = sheet.getRow(i);
							HSSFCell cell2 = row2.getCell(j);
							Object cellStr2 = this.getCellString(cell2);

							HSSFCell cell3 = row1.getCell(j);
							Object cellStr3 = this.getCellString(cell3);

							str += cellStr2.toString() + ":" + cellStr3.toString() + "  ";

							String[] str3 = new String[2];
							str3[0] = cellStr2.toString();
							str3[1] = cellStr3.toString();
							lol.add(str3);
						}
						li.add(lol);
						System.out.println(ec1.getCellValue() + "  " + ec2.getCellValue() + ":"
								+ cellStr1 + "  " + str);
					}
				}
			}
		} else if (ech.getFirstColumn() != null && ech.getFirstColumn() > 1) { // 四维
			markstr = "4w";
			for (int i = ech.getFirstRow() + 1; i < ecz.getFirstRow(); i++) {
				HSSFRow row = sheet.getRow(i);
				if (row != null) {
					for (int j = ecz.getFirstColumn() + 1; j < ech.getFirstColumn(); j++) {
						HSSFCell cell = row.getCell(j);
						Object cellStr = this.getCellString(cell);
						if (cellStr != null) {
							ExcelCell ec = new ExcelCell();
							ec.setCellValue(cellStr.toString());
							ec.setFirstRow(i);// 起始行
							ec.setLastRow(i);// 结束行
							ec.setFirstColumn(j);// 起始列
							ec.setLastColumn(j);// 结束列
							ec.setDirection(-1);
							ecoList.add(ec);
						}
					}
				}
			}
			for (ExcelCell ec1 : echList) {
				for (ExcelCell ec2 : eczList) {
					for (int i = ec2.getFirstRow(); i <= ec2.getLastRow(); i++) {

						int ec3Lie = -1;
						HSSFRow hr = null;
						String st = "", str = "";
						List<String[]> lol = new ArrayList<String[]>();
						for (ExcelCell ec3 : ecoList) {
							HSSFRow row1 = sheet.getRow(i); // 行

							hr = row1;
							ec3Lie = ec3.getFirstRow();

							HSSFCell cell1 = row1.getCell(ec3.getFirstColumn()); // 列
							Object cellStr1 = this.getCellString(cell1);

							String[] str1 = new String[2];
							str1[0] = null;
							str1[1] = ec1.getCellValue();
							String[] str2 = new String[2];
							str2[0] = null;
							str2[1] = ec2.getCellValue();
							String[] str3 = new String[2];
							str3[0] = ec3.getCellValue();
							str3[1] = cellStr1.toString();
							lol.add(str1);
							lol.add(str2);
							lol.add(str3);

							st += ec3.getCellValue() + ":" + cellStr1 + "  ";
						}
						for (int j = ec1.getFirstColumn(); j <= ec1.getLastColumn(); j++) {
							HSSFRow row2 = sheet.getRow(ec3Lie);
							HSSFCell cell2 = row2.getCell(j);
							Object cellStr2 = this.getCellString(cell2);

							HSSFCell cell3 = hr.getCell(j);
							Object cellStr3 = this.getCellString(cell3);

							String[] str4 = new String[2];
							str4[0] = cellStr2.toString();
							str4[1] = cellStr3.toString();
							lol.add(str4);

							str += cellStr2.toString() + ":" + cellStr3.toString() + "  ";
						}

						System.out.println(ec1.getCellValue() + "  " + ec2.getCellValue() + "  "
								+ st + str);

						li.add(lol);

					}
				}
			}
		}
		if (null != markstr && !"".equals(markstr)) {
			map.put(markstr, li);
		}
		return map;
	}

	/**
	 * 得到sheet中的每个合并单元格区的位置信息
	 * 
	 * @param sheet
	 */
	public List<ExcelCell> sysRange(HSSFSheet sheet) {

		List<ExcelCell> li = new ArrayList<ExcelCell>();
		int count = sheet.getNumMergedRegions();// 找到当前sheet单元格中共有多少个合并区域
		for (int i = 0; i < count; i++) {
			CellRangeAddress range = sheet.getMergedRegion(i);// 一个合并单元格代表
																// CellRangeAddress

			ExcelCell ec = new ExcelCell();
			ec.setFormatAsString(range.formatAsString());// 打印合并区域的字符串表示方法 如：
															// B2:C4
			ec.setFirstRow(range.getFirstRow());// 起始行
			ec.setLastRow(range.getLastRow());// 结束行
			ec.setFirstColumn(range.getFirstColumn());// 起始列
			ec.setLastColumn(range.getLastColumn());// 结束列

			if (ec.getFirstRow() == ec.getLastRow())
				ec.setDirection(1); // 横向合并单元格
			else if (ec.getFirstColumn() == ec.getLastColumn())
				ec.setDirection(0); // 纵向合并单元格

			String ss = "";
			List<String> si = new ArrayList<String>();
			if (ec.getFormatAsString().indexOf(":") != -1) {
				String[] ll = ec.getFormatAsString().split(":");
				for (String s : ll) {
					si.add(s);
					ss += (String) this.getValueByCellCode(s, sheet);
				}
			}
			ec.setCellValue(ss);
			ec.setCellInclude(si);

			// System.out.println(range.formatAsString());//打印合并区域的字符串表示方法 如：
			// B2:C4
			// System.out.println(range.getFirstRow() + "." + range.getLastRow()
			// + ":"
			// + range.getFirstColumn() + "." + range.getLastColumn()
			// );//打印起始行、结束行、起始列、结束列

			li.add(ec);
		}
		return li;
	}

	/**
	 * 根据单元格坐标得出相应的值 如：B3
	 * 
	 * @param cellRowCode
	 *            :坐标
	 * @param sheet
	 *            ：工作表
	 * @return 返回值
	 */
	public Serializable getValueByCellCode(String cellRowCode, HSSFSheet sheet) {
		String thisSheetName = sheet.getSheetName();
		CellReference ref = new CellReference(cellRowCode);
		int xy[] = { ref.getRow(), ref.getCol() };

		HSSFCell cell = sheet.getRow(xy[0]).getCell(xy[1]);
		switch (cell.getCellType()) {
		case HSSFCell.CELL_TYPE_BLANK:
			return "";
		case HSSFCell.CELL_TYPE_NUMERIC:
			return (cell.getNumericCellValue());
		case HSSFCell.CELL_TYPE_STRING:
			return cell.getRichStringCellValue().toString();
		case HSSFCell.CELL_TYPE_FORMULA:
			HSSFFormulaEvaluator evaluator = new HSSFFormulaEvaluator(sheet.getWorkbook());
			try {
				evaluator.evaluateFormulaCell(cell);// 检测公式有效性
			} catch (java.lang.IllegalArgumentException ex) {
				throw new RuntimeException("错误的单元格[" + thisSheetName + "->" + cellRowCode + "]");
			}
			if (evaluator.evaluateFormulaCell(cell) == HSSFCell.CELL_TYPE_ERROR) {
				throw new RuntimeException("错误的单元格[" + thisSheetName + "->" + cellRowCode + "]");
			}
			if (evaluator.evaluateFormulaCell(cell) == HSSFCell.CELL_TYPE_NUMERIC) {
				return cell.getNumericCellValue();
			} else if (evaluator.evaluateFormulaCell(cell) == HSSFCell.CELL_TYPE_STRING) {
				return cell.getRichStringCellValue().toString();
			}
		case HSSFCell.CELL_TYPE_BOOLEAN:
			return (cell.getBooleanCellValue());
		default:
			System.out.print("*");
			break;
		}
		return null;
	}

	/**
	 * 获得单元格中的内容
	 * 
	 * @param cell
	 * @return
	 */
	protected Object getCellString(HSSFCell cell) {
		Object result = null;
		if (cell != null) {
			int cellType = cell.getCellType();
			switch (cellType) {
			case HSSFCell.CELL_TYPE_STRING:
				result = cell.getRichStringCellValue().getString();
				break;
			case HSSFCell.CELL_TYPE_NUMERIC:
				result = cell.getNumericCellValue();
				break;
			case HSSFCell.CELL_TYPE_FORMULA:
				result = cell.getNumericCellValue();
				break;
			case HSSFCell.CELL_TYPE_ERROR:
				result = null;
				break;
			case HSSFCell.CELL_TYPE_BOOLEAN:
				result = cell.getBooleanCellValue();
				break;
			case HSSFCell.CELL_TYPE_BLANK:
				result = null;
				break;
			}
		}
		return result;
	}

	//
	// /**
	// * 根据一行找出有效的起始列号
	// * @param row
	// * @return
	// */
	// public int getStartCell(HSSFRow row){
	// if(row==null){
	// throw new RuntimeException("无效行");
	// }
	// int start = 0;
	// for(int i = 0; i<row.getLastCellNum(); i++){
	// HSSFCell cell = row.getCell(i);
	// if(cell!=null){
	// if(cell.getCellType()!=HSSFCell.CELL_TYPE_BLANK){
	// start = i;
	// break;
	// }
	// }
	// }
	// return start;
	// }
	// /**
	// * 根据一行找出有效的结尾列,空白列不算、但是在合并区域时算
	// * @param row
	// * @return
	// */
	// public int getLastCell(HSSFRow row){
	//
	// if(row==null){
	// throw new RuntimeException("无效行");
	// }
	//
	// HSSFSheet sheet = row.getSheet();
	// int merged = sheet.getNumMergedRegions();//获取单元格区域数
	// int end = 0;
	// for(int x = row.getLastCellNum() - 1; x>=0; x--){
	// HSSFCell cell = row.getCell(x);//获取列
	// if(cell==null)
	// continue;
	// if(cell.getCellType()==HSSFCell.CELL_TYPE_BLANK){//空白单元格
	// int rowNumber = cell.getRowIndex();
	// int cellNumber = cell.getColumnIndex();
	// boolean flag = false;
	// for(int i = 0; i<merged; i++){
	// CellRangeAddress rane = sheet.getMergedRegion(i);
	// if(rowNumber>=rane.getFirstRow()&&rowNumber<=rane.getLastRow()){//确立在行里面
	// if(cellNumber>=rane.getFirstColumn()&&cellNumber<=rane.getLastColumn()){//确立在列里面
	// flag = true;
	// break;
	// }
	// }
	// }
	// if(flag){//说明当前单元格是空白单元格并且在合并区域中.可以认定为有效结束列
	// end = x;
	// break;
	// }
	// }else{//不为空白单元格
	// end = x;
	// break;
	// }
	// }
	// return end;
	// }
	//
	// /**
	// * 判断一行是否为空行或指定的前几列
	// * @param row
	// * @return
	// */
	// public boolean checkRowIsNull(HSSFRow row,int ...cd){
	// boolean boo = true;
	// if(row==null){
	// return boo;
	// }
	// int length = 0;
	// int j = 0;
	// if(cd.length==0){
	// j = 0;
	// length = row.getLastCellNum();
	// }else{
	// j = 1;
	// length = cd[0];
	// }
	// for(; j<length; j++){
	// HSSFCell cell = row.getCell(j);
	// if(cell!=null){
	// if(cell.getCellType()!=HSSFCell.CELL_TYPE_BLANK){
	// if(cell.getCellType()==HSSFCell.CELL_TYPE_STRING){
	// if(!cell.getRichStringCellValue().toString().trim().equals("")){
	// boo = false;
	// break;
	// }
	// }else{
	// boo = false;
	// }
	// }
	// }
	// }
	// return boo;
	// }

	public static void main(String[] args) throws Exception {
		File file = new File("C:\\Documents and Settings\\Administrator\\桌面\\待解析的demo.xls");
		ExcelReader parser = new ExcelReader(file);

		for (int i = 0; i < 255; i++) {
			try {
				parser.getDatasInSheet(i);
			} catch (RuntimeException e) {
				break;
			}
		}

	}
	
	public static class ExcelCell{

		public int getDirection() {
			// TODO Auto-generated method stub
			return 0;
		}

		public void setFormatAsString(String formatAsString) {
			// TODO Auto-generated method stub
			
		}

		public void setCellInclude(List<String> si) {
			// TODO Auto-generated method stub
			
		}

		public int getLastRow() {
			// TODO Auto-generated method stub
			return 0;
		}

		public int getLastColumn() {
			// TODO Auto-generated method stub
			return 0;
		}

		public String getFormatAsString() {
			// TODO Auto-generated method stub
			return null;
		}

		public String getCellValue() {
			// TODO Auto-generated method stub
			return null;
		}

		public void setDirection(int i) {
			// TODO Auto-generated method stub
			
		}

		public void setLastColumn(int i) {
			// TODO Auto-generated method stub
			
		}

		public void setFirstColumn(int i) {
			// TODO Auto-generated method stub
			
		}

		public void setLastRow(int i) {
			// TODO Auto-generated method stub
			
		}

		public void setFirstRow(int i) {
			// TODO Auto-generated method stub
			
		}

		public void setCellValue(String string) {
			// TODO Auto-generated method stub
			
		}

		public int getFirstRow() {
			// TODO Auto-generated method stub
			return 0;
		}

		public int getFirstColumn() {
			// TODO Auto-generated method stub
			return 0;
		}
		
	}
}
