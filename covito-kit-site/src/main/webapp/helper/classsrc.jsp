<%@page import="java.security.CodeSource"%>
<%@page import="java.security.ProtectionDomain"%>
<%@page import="java.net.MalformedURLException"%>
<%@page import="java.io.File"%>
<%@page import="java.net.URL"%>
<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>helper[find class location]</title>
</head>
<body>
<%!
public static URL getClassLocation(final String clsName) {
	if (clsName == null) {
		return null;
	}
	Class cls = null;
	try {
		cls = Class.forName(clsName);
	} catch (Exception e) {
		return null;
	}
	URL result = null;
	final String clsAsResource = cls.getName().replace('.', '/').concat(".class");
	final ProtectionDomain pd = cls.getProtectionDomain();
	// java.lang.Class contract does not specify if 'pd' can ever be null;
	// it is not the case for Sun's implementations, but guard against null
	// just in case:
	if (pd != null) {
		final CodeSource cs = pd.getCodeSource();
		// 'cs' can be null depending on the classloader behavior:
		if (cs != null)
			result = cs.getLocation();
		if (result != null) {
			// Convert a code source location into a full class file
			// location
			// for some common cases:
			if ("file".equals(result.getProtocol())) {
				try {
					if (result.toExternalForm().endsWith(".jar")
							|| result.toExternalForm().endsWith(".zip"))
						result = new URL("jar:".concat(result.toExternalForm()).concat("!/")
								.concat(clsAsResource));
					else if (new File(result.getFile()).isDirectory()) {
						result = new URL(result, clsAsResource);
					}
				} catch (MalformedURLException ignore) {
				}
			}
		}
	}
	if (result == null) {
		// Try to find 'cls' definition as a resource; this is not
		// document．d to be legal, but Sun's implementations seem to //allow
		// this:
		final ClassLoader clsLoader = cls.getClassLoader();
		result = clsLoader != null ? clsLoader.getResource(clsAsResource) : ClassLoader
				.getSystemResource(clsAsResource);
	}
	return result;
}
%>
<%
String className = request.getParameter("className");
if (className == null) {
	out.print("usage method like: xxx?className=java.net.URL");
} else {
	URL url = getClassLocation(className);
	if (url != null) {
		out.print("class:" + className + " location：");
		out.print("<hr>");
		out.print(url);
		out.print("<br>");
	} else {
		out.print("class:" + className + " not found!<br>");
	}
}
%>
</body>
</html>