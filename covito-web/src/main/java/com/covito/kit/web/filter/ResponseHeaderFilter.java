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
package com.covito.kit.web.filter;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

/**
 * 请求Header过滤器
 * <p>
 * 请求头部添加额外信息
 * 如：
 * Cache-Control：max-age=3600
 * Expires：86400
 * </p>
 * 
 * @author  covito
 * @version  [v1.0, 2014-5-21]
 */
public class ResponseHeaderFilter implements Filter {

	private FilterConfig filterConfig;
	
	/**
	 * {@inheritDoc}
	 *
	 * @author  covito
	 * @param filterConfig
	 * @throws ServletException
	 */
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		this.filterConfig=filterConfig;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @author  covito
	 * @param request
	 * @param response
	 * @param chain
	 * @throws IOException
	 * @throws ServletException
	 */
	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletResponse localHttpServletResponse = (HttpServletResponse) response;
		Enumeration en = this.filterConfig.getInitParameterNames();
		while (en.hasMoreElements()) {
			String str = (String) en.nextElement();
			localHttpServletResponse.setHeader(str,this.filterConfig.getInitParameter(str));
		}
		chain.doFilter(request, response);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @author  covito
	 */
	@Override
	public void destroy() {
		this.filterConfig=null;
	}

}
