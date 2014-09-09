package org.covito.kit.test.poi;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelTest {
	
	Map<CellRangeAddress,Object> ranges=new HashMap<CellRangeAddress,Object>();
	
	Sheet sheet;
	
	public ExcelTest(String file) throws Exception {
		FileInputStream is = new FileInputStream(file);
		Workbook wb = new XSSFWorkbook(is);
		sheet=wb.getSheetAt(0);
		
		int num=sheet.getNumMergedRegions();
		for(int i=0;i<num;i++){
			CellRangeAddress cr=sheet.getMergedRegion(i);
			Cell cell=sheet.getRow(cr.getFirstRow()).getCell(cr.getFirstColumn());
			ranges.put(cr,getCellValue(cell));
		}
	}
	

	public static void main(String[] args) throws Exception {
		String file = "target/export.xlsx";
		ExcelTest ei=new ExcelTest(file);
		for (int i = 0; i <= ei.getLastDataRowNum(); i++) {
			Row row = ei.getRow(i);
			for (int j = 0; j < ei.getLastCellNum(); j++) {
				Object val = ei.getCellValue(row, j);
				System.out.print(val+", ");
			}
			System.out.print("\n");
		}
	}

	public int getLastDataRowNum() {
		return this.sheet.getLastRowNum();
	}

	/**
	 * 获取最后一个列号
	 * @return
	 */
	public int getLastCellNum(){
		return this.getRow(0).getLastCellNum();
	}

	public Row getRow(int i) {
		return sheet.getRow(i);
	}


	/**
	 * 获取单元格值
	 * @param row 获取的行
	 * @param column 获取单元格列号
	 * @return 单元格值
	 */
	public Object getCellValue(Cell cell){
		Object val = "";
		try{
			if (cell != null){
				if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC){
					val = cell.getNumericCellValue();
				}else if (cell.getCellType() == Cell.CELL_TYPE_STRING){
					val = cell.getStringCellValue();
				}else if (cell.getCellType() == Cell.CELL_TYPE_BOOLEAN){
					val = cell.getBooleanCellValue();
				}else if (cell.getCellType() == Cell.CELL_TYPE_ERROR){
					val = cell.getErrorCellValue();
				}
			}
		}catch (Exception e) {
			return val;
		}
		return val;
	}
	
	
	/**
	 * 获取单元格值
	 * @param row 获取的行
	 * @param column 获取单元格列号
	 * @return 单元格值
	 */
	public Object getCellValue(Row row, int column){
		int rownum=row.getRowNum();
		for(CellRangeAddress cr:ranges.keySet()){
			if(cr.isInRange(rownum, column)){
				return ranges.get(cr);
			}
		}
		
		return getCellValue(row.getCell(column));
	}
}
