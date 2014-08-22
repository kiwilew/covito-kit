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
package org.covito.kit.web.springmvc.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

/**
 * BaseController
 * <p>功能详细描述</p>
 * 
 * @author  covito
 * @version  [v1.0, 2014年6月19日]
 */
public class BaseController extends MultiActionController implements Controller {

	public void copyProperties(Object source, Object target) {
		BeanUtils.copyProperties(source, target);
	}


	/** 
	 * 保存普通消息
	 * <p>功能详细描述</p>
	 *
	 * @author  covito
	 * @param request
	 * @param msg
	 */
	protected void saveMessage(HttpServletRequest request, String msg) {
		if (StringUtils.isNotBlank(msg)) {
			List<String> list = getOrCreateRequestAttribute(request,
					"springMessages", new ArrayList<String>());
			list.add(msg);
		}
	}

	/** 
	 * 保存异常消息
	 * <p>功能详细描述</p>
	 *
	 * @author  covito
	 * @param request
	 * @param paramString
	 */
	protected void saveError(HttpServletRequest request, String paramString) {
		if (StringUtils.isNotBlank(paramString)) {
			List<String> list = getOrCreateRequestAttribute(request,
					"springErrors", new ArrayList<String>());
			list.add(paramString);
		}
	}

	/** 
	 * 获取或创建reuqest属性
	 * <p>功能详细描述</p>
	 *
	 * @author  covito
	 * @param request
	 * @param name
	 * @param obj 当属性不存时set此对象
	 * @return
	 */
	protected <T> T getOrCreateRequestAttribute(HttpServletRequest request,
			String name, T obj) {
		T value = (T) request.getAttribute(name);
		if (value == null) {
			value=obj;
			request.setAttribute(name, value);
		}
		return value;
	}

	/** 
	 * 将参数拼到原url中
	 * <p>功能详细描述</p>
	 *
	 * @author  covito
	 * @param uri
	 * @param name
	 * @param value
	 * @return
	 */
	protected static String appendParamToURI(String uri, String name,
			String value) {
		String str = uri;
		if (null != str) {
			if (str.indexOf("?") < 0)
				str = str + "?";
			else
				str = str + "&";
			str = str + name + "=" + value;
		}
		return str;
	}

	/** 
	 * 获取session中的属性
	 * <p>功能详细描述</p>
	 *
	 * @author  covito
	 * @param request
	 * @param name
	 * @return
	 */
	protected Object getSessionAttribute(HttpServletRequest request,
			String name) {
		Object value = null;
		HttpSession session = request.getSession(false);
		if (session != null){
			value = session.getAttribute(name);
		}
		return value;
	}

	/** 
	 * 向session中存属性
	 * <p>功能详细描述</p>
	 *
	 * @author  covito
	 * @param request
	 * @param paramString
	 * @param paramObject
	 */
	protected void setSessionAttribute(HttpServletRequest request,
			String paramString, Object paramObject) {
		HttpSession session = request.getSession(false);
		if (session != null){
			session.setAttribute(paramString, paramObject);
		}
	}
	
}
