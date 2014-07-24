package org.covito.kit.command;

import java.io.PrintWriter;

public interface Command {

	void execute(String[] argv, PrintWriter out);
	
	String getInfo();
	
	String getUsage();
}
