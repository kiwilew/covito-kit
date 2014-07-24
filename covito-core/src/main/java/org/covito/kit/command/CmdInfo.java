package org.covito.kit.command;

public class CmdInfo {

	private String name;
	private Command cmd;

	CmdInfo(String name, Command cmd) {
		this.name = name;
		this.cmd = cmd;
	}

	public String getName() {
		return name;
	}

	public Command getCmd() {
		return cmd;
	}

}
