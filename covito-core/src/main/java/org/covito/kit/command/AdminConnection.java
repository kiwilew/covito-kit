package org.covito.kit.command;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.covito.kit.utility.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

final class AdminConnection implements Runnable {
	private Socket socket;

	private SocketAdminServer as;

	private static final Pattern pat = Pattern
			.compile("\\s+(([^'\\\" \\t\\r\\n]+)|('[^'\\r\\n]*')|(\\\"[^\\\"\\r\\n]*\\\"))");

	private static final Logger debugLog = LoggerFactory.getLogger(AdminConnection.class);

	public AdminConnection(SocketAdminServer as, Socket socket) {
		this.as = as;
		this.socket = socket;
	}

	private void closeAll(Closeable... c) {
		for (int i = 0; i < c.length; ++i)
			try {
				c[i].close();
			} catch (Exception e) {
			}
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
		if (n > 0)
			cmd = cmd.substring(0, n);
		String[] argvs = new String[argv.size()];
		argvs = argv.toArray(argvs);
		return Pair.makePair(cmd, argvs);
	}

	public void run() {
		BufferedReader in = null;
		PrintWriter out = null;
		try {
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
					socket.getOutputStream(), as.encoding)));

			do {
				if (as.prompt.length() > 0) {
					out.print(as.prompt);
					out.flush();
				}
				String cmdLine = in.readLine();
				if (cmdLine == null)
					return;
				if (cmdLine.length() == 0)
					continue;

				Pair<String, String[]> pr = parseCmd(cmdLine);
				Command c = as.getCommand(pr.first);
				if (c != null) {
					c.execute(pr.second, out);
				} else {
					out.println("Invalid command. Please type help for more information.");
				}
				out.flush();
			} while (as.keepAlive);
		} catch (Exception e) {
			// e.printStackTrace();
		} finally {
			try {
				socket.close();
			} catch (IOException e) {
				debugLog.error("", e);
			}
			closeAll(in, out);
		}
	}

	public static void main(String[] argv) {
		System.out.println(Arrays.toString(argv));
	}
}
