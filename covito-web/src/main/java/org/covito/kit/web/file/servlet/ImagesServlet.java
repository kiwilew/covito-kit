package org.covito.kit.web.file.servlet;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ImagesServlet extends AbstractFileServlet {
	
	private static final long serialVersionUID = -358253459242958398L;
	
	Logger logger = LoggerFactory.getLogger(getClass());
	
	/**
	 * {@inheritDoc}
	 *
	 * @author  covito
	 * @param request
	 */
	@Override
	protected void setContentInfo(HttpServletResponse response) {
		response.setContentType("image/jpeg");
	}

	
}