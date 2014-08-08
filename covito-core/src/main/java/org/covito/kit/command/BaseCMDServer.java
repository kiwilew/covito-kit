package org.covito.kit.command;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.covito.kit.utility.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BaseCMDServer implements CommandServer {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private final static Pattern pat = Pattern
			.compile("\\s+(([^'\\\" \\t\\r\\n]+)|('[^'\\r\\n]*')|(\\\"[^\\\"\\r\\n]*\\\"))");

	protected String prompt = ""; // 提示符

	protected String encoding = "UTF-8"; // 字符编码

	public BaseCMDServer() {

	}

	private static Pair<String, String[]> parseCmd(String cmdLine) {
		cmdLine = cmdLine.trim();
		Matcher m = pat.matcher(cmdLine);
		ArrayList<String> argv = new ArrayList<String>();
		while (m.find()) {
			String arg = m.group(1);
			char c1 = arg.charAt(0);
			char c2 = arg.charAt(arg.length() - 1);
			if ((c1 == '\'' && c2 == '\'') || (c1 == '\"' && c2 == '\"'))
				arg = arg.substring(1, arg.length() - 1);
			argv.add(arg);
		}
		String cmd = cmdLine;
		int n = cmdLine.indexOf(' ');
		if (n > 0){
			cmd = cmd.substring(0, n);
		}
		String[] argvs = new String[argv.size()];
		argvs = argv.toArray(argvs);
		return Pair.makePair(cmd, argvs);
	}

	/** 
	 * 解析命令并输出到输出流
	 * <p>功能详细描述</p>
	 *
	 * @param cmd
	 * @param os
	 */
	@Override
	public void processCMD(String cmd, OutputStream os) {
		try {
			PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(os, this.encoding)));

			if (this.prompt.length() > 0) {
				out.print(this.prompt);
				out.flush();
			}
			if (cmd == null) {
				return;
			}
			if (cmd.length() == 0) {
				return;
			}

			Pair<String, String[]> pr = parseCmd(cmd);
			Command c = CommandManager.getCommand(pr.first);
			if (c != null) {
				c.execute(pr.second, out);
			} else {
				out.println("Invalid command. Please type help for more information.");
			}
			out.flush();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		}
	}

	public String getPrompt() {
		return prompt;
	}

	public void setPrompt(String prompt) {
		this.prompt = prompt;
	}

	public String getEncoding() {
		return encoding;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	@Override
	public void startServer() {
		
	}
	
}
