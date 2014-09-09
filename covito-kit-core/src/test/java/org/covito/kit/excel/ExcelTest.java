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
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.covito.kit.excel.ExcelField.ValueHandler;
import org.junit.Test;

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
	public void testImport() throws IOException {
		ImportExcel ei = new ImportExcel("target/export.xls", 1);
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
	public void testImportUserBean() throws IOException, InstantiationException, IllegalAccessException {
		ImportExcel ei = new ImportExcel("target/export.xls", 7);
		List<Model> ml=ei.getDataList(Model.class);
		for(Model m:ml){
			System.out.println(m);
		}
	}
	
	public static class EnterHandler implements ValueHandler{

		/**
		 * {@inheritDoc}
		 *
		 * @param value
		 * @return
		 */
		@Override
		public Object dealValue(Object value) {
			return value.toString().replace("\n", "");
		}
		
	}
	
	public static class Model{
		@ExcelField(sort = 0)
		private String sn;
		@ExcelField(sort = 1)
		private String part;
		@ExcelField(sort = 2)
		private String name;
		@ExcelField(sort = 3, handler=EnterHandler.class )
		private String mobile;
		@ExcelField(sort = 4)
		private String phone;
		@ExcelField(sort = 5)
		private String email;
		/**
		 * Get sn
		 *
		 * @return the sn
		 */
		public String getSn() {
			return sn;
		}
		/**
		 * Set sn
		 *
		 * @param sn the sn to set
		 */
		public void setSn(String sn) {
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
		
		/**
		 * {@inheritDoc}
		 *
		 * @return
		 */
		@Override
		public String toString() {
			return String.format("%1$-5s|%2$-20s|%3$-30s|%4$-30s|%5$-8s|%6$-120s", sn,part,name,mobile,phone,email);
		}
		
	}
}
