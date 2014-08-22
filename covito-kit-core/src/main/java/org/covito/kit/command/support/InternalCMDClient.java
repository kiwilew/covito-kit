package org.covito.kit.command.support;

import java.io.ByteArrayOutputStream;

import org.covito.kit.command.BaseCMDServer;
import org.covito.kit.command.CommandServer;

public class InternalCMDClient {

	public static String sendCmd(String cmd) {
		CommandServer b=new BaseCMDServer();
		ByteArrayOutputStream out=new ByteArrayOutputStream();
		b.processCMD(cmd, out);
		return out.toString();
	}
}
