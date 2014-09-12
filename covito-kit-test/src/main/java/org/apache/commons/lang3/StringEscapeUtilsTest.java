package org.apache.commons.lang3;

import org.junit.Test;
import static org.apache.commons.lang3.StringEscapeUtils.*;

public class StringEscapeUtilsTest {

	@Test
	public void test(){
		System.out.println(escapeJava("\"hello\""));//\"hello\"
		System.out.println(unescapeJava("\"hello\""));//"hello"
		
		System.out.println(escapeCsv("\"'a=b'"));//"""'a=b'"
		System.out.println(unescapeCsv("\"\"\"'a=b'\""));//"'a=b'
		
		System.out.println(escapeHtml4("<html></html>"));//&lt;html&gt;&lt;/html&gt;
		System.out.println(unescapeHtml4("&lt;html&gt;&lt;/html&gt;"));//<html></html>
		
		System.out.println(escapeXml("a&b"));//a&amp;b
		System.out.println(unescapeXml("a&amp;b"));//a&b
		
		System.out.println(escapeEcmaScript("function a{var a='b'}"));//function a{var a=\'b\'}
	}
}
