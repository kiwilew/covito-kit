package org.covito.kit.command;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Arrays;

import org.covito.kit.cache.CacheManager;
import org.covito.kit.cache.command.CacheInfoCmd;
import org.covito.kit.cache.support.MapCache;
import org.covito.kit.command.support.InternalCMDClient;
import org.covito.kit.command.support.SocketCMDClient;
import org.covito.kit.command.support.SocketCMDServer;
import org.junit.BeforeClass;
import org.junit.Test;

public class CMDTest {

	@Test
	public void testSocket() throws IOException{
		for (int i = 0; i < 10; i++) {
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			String c = br.readLine();
			System.out.println(SocketCMDClient.sendCmd("127.0.0.1", 12345, c));
		}
	}
	
	@Test
	public void testInternal() throws IOException{
		for (int i = 0; i < 10; i++) {
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			String c = br.readLine();
			System.out.println(InternalCMDClient.sendCmd( c));
		}
	}
	
	@BeforeClass
	public static void dataPre(){
		SocketCMDServer as = new SocketCMDServer("0.0.0.0", 12345, true, "admin> ");
		Command cmd = new Command() {
			public void execute(String[] argv, PrintWriter out) {
				out.println("test argv: " + Arrays.toString(argv));
			}

			@Override
			public String getInfo() {
				return "test command.";
			}

			@Override
			public String getUsage() {
				return "test [option]";
			}
		};
		CommandManager.addCommand("test", cmd);
		MapCache<String, String> ca = new MapCache<String, String>("cach");
		MapCache<String, String> ca1 = new MapCache<String, String>("cache");
		CacheManager.addCache(ca);
		CacheManager.addCache(ca1);
		CacheManager.getCache("cach").put("a", "dd");

		Command cache = new CacheInfoCmd();
		CommandManager.addCommand("cache", cache);
		as.startServer();
	}
}
