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
package org.covito.kit.excel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.poi.ss.usermodel.Row;
import org.covito.kit.excel.ExcelField.ExAct;
import org.covito.kit.excel.ExcelField.ValueHandler;
import org.junit.Test;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;

/**
 * 一句话功能简述
 * <p>
 * 功能详细描述
 * </p>
 * 
 * @author covito
 * @version [v1.0, 2014年9月9日]
 */
public class ExcelTest {
	
	@Test
	public void testExport() {
		ExportExcel exe=new ExportExcel("测试导出", Model.class);
		List<Model> list=new ArrayList<ExcelTest.Model>();
		for(int i=0;i<100;i++){
			Model m=new Model();
			m.setSn(i+1);
			m.setName("Name"+i);
			m.setMobile("15902152565");
			m.setPart(i+"");
			m.setPhone("9562");
			m.setEmail("2343@34.com");
			m.setModel(m);
			list.add(m);
		}
		exe.setDataList(list);
		exe.writeFile("target/export.xlsx");
	}
	
	@Test
	public void testExportNormal() {
		
		List<String> headerList = Lists.newArrayList();
		for (int i = 1; i <= 10; i++) {
			headerList.add("表头" + i);
		}

		List<String> dataRowList = Lists.newArrayList();
		for (int i = 1; i <= headerList.size(); i++) {
			dataRowList.add("数据" + i);
		}

		List<List<String>> dataList = Lists.newArrayList();
		for (int i = 1; i <= 1000000; i++) {
			dataList.add(dataRowList);
		}

		ExportExcel ee = new ExportExcel("表格标题", headerList);

		ee.setDataList(dataList);

		ee.writeFile("target/exportNormal.xlsx");
	}

	@Test
	public void testImport() throws IOException {
		ImportExcel ei = new ImportExcel("target/export.xlsx", 1);
		for (int i = ei.getDataRowNum(); i < ei.getLastDataRowNum(); i++) {
			Row row = ei.getRow(i);
			for (int j = 0; j < ei.getLastCellNum(); j++) {
				Object val = ei.getCellValue(row, j);
				System.out.print(val + ", ");
			}
			System.out.print("\n");
		}
	}
	
	@Test
	public void testToString(){
		Model m=new Model();
		m.setSn(3);
		m.setName("Name"+4);
		m.setMobile("15902152565");
		m.setPart(2+"");
		m.setPhone("9562");
		m.setEmail("2343@34.com");
		m.setModel(m);
		System.out.println(m.toString());
	}
	
	@Test
	public void testImportUserBean(){
		ImportExcel ei = new ImportExcel("target/export.xlsx", 2);
		List<Model> ml=ei.getDataList(Model.class);
		for(Model m:ml){
			System.out.println(m);
		}
	}
	
	public static class EnterHandler implements ValueHandler<String, String>{
		@Override
		public String impConvert(String value) {
			return value.toString().replace("\n", "");
		}

		@Override
		public String expConvert(String value) {
			return value;
		}
	}
	
	public static class JsonHandler implements ValueHandler<String,Model>{

		@Override
		public Model impConvert(String value) {
			Model m=JSONObject.parseObject(value, Model.class);
			return m;
		}

		@Override
		public String expConvert(Model value) {
			String json=JSONObject.toJSONString(value);
			return json;
		}
		
	}
	public static class PartHandler implements ValueHandler<String,String>{
		
		@Override
		public String impConvert(String value) {
			return value;
		}
		
		@Override
		public String expConvert(String value) {
			if(value.length()==1){
				return "Part One";
			}else if(value.startsWith("1")||value.startsWith("2")||value.startsWith("3")){
				return "Part Two";
			}else{
				return "Part Three";
			}
		}
		
	}
	
	public static class Model{
		@ExcelField(sort = 0,title="序号")
		private Integer sn;
		@ExcelField(sort = 1,title="部门",handler=PartHandler.class)
		private String part;
		@ExcelField(sort = 2,title="姓名")
		private String name;
		@ExcelField(sort = 3,title="手机",handler=EnterHandler.class )
		private String mobile;
		@ExcelField(sort = 4,title="电话")
		private String phone;
		@ExcelField(sort = 5,title="邮箱")
		private String email;
		@ExcelField(sort = 6,title="Json对象",handler=JsonHandler.class,type=ExAct.exp)
		private Model model;
		/**
		 * Get sn
		 *
		 * @return the sn
		 */
		public Integer getSn() {
			return sn;
		}
		/**
		 * Set sn
		 *
		 * @param sn the sn to set
		 */
		public void setSn(Integer sn) {
			this.sn = sn;
		}
		/**
		 * Get part
		 *
		 * @return the part
		 */
		public String getPart() {
			return part;
		}
		/**
		 * Set part
		 *
		 * @param part the part to set
		 */
		public void setPart(String part) {
			this.part = part;
		}
		/**
		 * Get name
		 *
		 * @return the name
		 */
		public String getName() {
			return name;
		}
		/**
		 * Set name
		 *
		 * @param name the name to set
		 */
		public void setName(String name) {
			this.name = name;
		}
		/**
		 * Get mobile
		 *
		 * @return the mobile
		 */
		public String getMobile() {
			return mobile;
		}
		/**
		 * Set mobile
		 *
		 * @param mobile the mobile to set
		 */
		public void setMobile(String mobile) {
			this.mobile = mobile;
		}
		/**
		 * Get phone
		 *
		 * @return the phone
		 */
		public String getPhone() {
			return phone;
		}
		/**
		 * Set phone
		 *
		 * @param phone the phone to set
		 */
		public void setPhone(String phone) {
			this.phone = phone;
		}
		/**
		 * Get email
		 *
		 * @return the email
		 */
		public String getEmail() {
			return email;
		}
		/**
		 * Set email
		 *
		 * @param email the email to set
		 */
		public void setEmail(String email) {
			this.email = email;
		}
		
		public Model getModel() {
			return model;
		}
		public void setModel(Model model) {
			this.model = model;
		}
		/**
		 * {@inheritDoc}
		 *
		 * @return
		 */
		@Override
		public String toString() {
			return ToStringBuilder.reflectionToString(this, ToStringStyle.SIMPLE_STYLE);
			
			//return String.format("%1$-5s|%2$-20s|%3$-30s|%4$-30s|%5$-8s|%6$-120s", sn,part,name,mobile,phone,email);
		}
		
	}
}
