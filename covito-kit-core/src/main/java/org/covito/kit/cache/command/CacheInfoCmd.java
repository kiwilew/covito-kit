package org.covito.kit.cache.command;

import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Iterator;

import org.covito.kit.cache.Cache;
import org.covito.kit.cache.CacheManager;
import org.covito.kit.cache.monitor.Visitor;
import org.covito.kit.command.Command;

public class CacheInfoCmd implements Command {
	
	private String name="cache";

	@Override
	public void execute(String[] argv, PrintWriter out) {
		try {
			if (argv.length == 0) {// list cache
				list(out);
			} else if (argv.length == 2 && "-c".equalsIgnoreCase(argv[0])) {
				listKeys(argv[1], out);
			} else if (argv.length == 3 && "-c".equalsIgnoreCase(argv[0])) {
				if (argv[2].matches("[\\d]+")) {
					showValue(argv[1], Integer.parseInt(argv[2]), out);
				} else {
					showValue(argv[1], argv[2], out);
				}
			} else if (argv.length == 4 && "-c".equalsIgnoreCase(argv[0])) {
				if ("-i".equalsIgnoreCase(argv[1])) {
					try {
						int key = Integer.parseInt(argv[3]);
						showValue(argv[2], key, out);
					} catch (Exception e) {
						out.println("invalid argment.");
					}
				} else if ("-s".equalsIgnoreCase(argv[1])) {
					showValue(argv[2], argv[3], out);
				} else {
					out.println("invalid argment.");
				}
			} else if (argv.length == 2 && "-d".equalsIgnoreCase(argv[0])) {
				clearCache(argv[1], out);
			} else if (argv.length == 3 && "-d".equalsIgnoreCase(argv[0])) {
				if (argv[2].matches("[\\d]+")) {
					showValue(argv[1], Integer.parseInt(argv[2]), out);
				} else {
					removeCache(argv[1], argv[2], out);
				}

			} else if (argv.length == 4 && "-d".equalsIgnoreCase(argv[0])) {
				if ("-i".equalsIgnoreCase(argv[1])) {
					try {
						int key = Integer.parseInt(argv[3]);
						removeCache(argv[2], key, out);
					} catch (Exception e) {
						out.println("invalid argment.");
					}
				} else if ("-s".equalsIgnoreCase(argv[1])) {
					removeCache(argv[2], argv[3], out);
				} else {
					out.println("invalid argment.");
				}
			} else {
				out.print(getHelp());
			}
		} catch (Exception e) {
			out.println("Exception:" + e.getMessage());
		}

	}

	private void listKeys(String cacheName, PrintWriter out) {
		Cache c = CacheManager.getCache(cacheName);
		if (c != null) {
			Iterator it = c.keySet().iterator();
			while (it.hasNext()) {
				out.println(it.next());
			}
		} else {
			out.println("Cache " + cacheName + " not exist.");
		}
	}

	@Override
	public String getInfo() {
		return "provider manager cache info.";
	}

	@Override
	public String getUsage() {
		return getHelp();
	}

	private String getHelp() {
		StringBuffer sb = new StringBuffer();
		sb.append("list all cache\n");
		sb.append("-c [cacheName]\tshow cache keys");
		sb.append("-c [cacheName] [key]\tshow cache info\n");
		sb.append("-c -i [cacheName] [key]\tshow cache info, key as integer.\n");
		sb.append("-c -s [cacheName] [key]\tshow cache info, key as string\n");
		sb.append("-d [cacheName]\tclear cache\n");
		sb.append("-d [cacheName] [key]\tdelete cache item by key\n");
		sb.append("-d -i [cacheName] [key]\tdelete cache item by key, key as integer\n");
		sb.append("-d -s [cacheName] [key]\tdelete cache item by key, key as string\n");
		sb.append("? help");
		return sb.toString();
	}

	private void removeCache(String cacheName, String key, PrintWriter out) {
		Cache c = CacheManager.getCache(cacheName);
		if (c != null) {
			c.evict(key);
		} else {
			out.println("Cache " + cacheName + " not exist.");
		}
	}

	private void removeCache(String cacheName, int key, PrintWriter out) {
		Cache c = CacheManager.getCache(cacheName);
		if (c != null) {
			c.evict(key);
		} else {
			out.println("Cache " + cacheName + " not exist.");
		}
	}

	private void clearCache(String cacheName, PrintWriter out) {
		Cache c = CacheManager.getCache(cacheName);
		if (c != null) {
			c.clear();
			out.println("clear cache " + cacheName + " success");
		} else {
			out.println("Cache " + cacheName + " not exist.");
		}
	}

	private void showValue(String cacheName, String key, PrintWriter out) {
		Cache c = CacheManager.getCache(cacheName);
		if (c != null) {
			out.println(c.get(key));
		} else {
			out.println("Cache " + cacheName + " not exist.");
		}
	}

	private void showValue(String cacheName, int key, PrintWriter out) {
		Cache c = CacheManager.getCache(cacheName);
		if (c != null) {
			out.println(c.get(key));
		} else {
			out.println("Cache " + cacheName + " not exist.");
		}
	}

	private void list(PrintWriter out) {
		String format = "|%1$-30s|%2$-60s|%3$-12s|%4$-12s|%5$-12s|%6$-12s|%7$-12s";

		String outStr = String.format(format, new Object[] { "CacheName", "Class", "QueryCount",
				"HitCount", "HitRate", "Size", "ReflushTime" });
		out.println(outStr);
		Collection<String> names = CacheManager.getCacheNames();
		for (String name : names) {
			Cache c = CacheManager.getCache(name);
			if (c == null) {
				out.println(String.format(format, new Object[] { name, "-", "0", "0", "N/A", "0",
						"0" }));
			} else {
				if (c instanceof Visitor) {
					Visitor v = (Visitor) c;
					long qc = v.getQueryCount();
					long hc = v.getHitCount();
					String rate;
					if (qc > 0) {
						BigDecimal bd = new BigDecimal(((double) hc * 100) / qc).setScale(2,
								BigDecimal.ROUND_HALF_UP);
						bd.setScale(2);
						rate = bd.floatValue() + "%";
					} else {
						rate = "N/A";
					}
					out.println(String.format(format, new Object[] { name, c.getClass().getName(),
							qc, hc, rate, v.size(), v.getReflushTime() }));
				}

			}
		}
	}

	@Override
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
}
