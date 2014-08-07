package org.covito.kit.web.helper.svl;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.covito.kit.utility.ClassUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClassSrcServlet extends HttpServlet {
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	private Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * {@inheritDoc}
	 * 
	 * @author covito
	 */
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String className = request.getParameter("className");
		
		response.setContentType("text/html");
		PrintWriter pw=response.getWriter();
		
		if (className == null) {
			pw.print("usage method like: xxx?className=java.net.URL");
		} else {
			URL url = ClassUtil.getClassLocation(className);
			if (url != null) {
				pw.print("class:" + className + " location：");
				pw.print("<hr>");
				pw.print(url);
				pw.print("<br>");
			} else {
				pw.print("class:" + className + " not found!<br>");
			}
		}
		pw.flush();
		pw.close();
	}
}