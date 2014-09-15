package org.covito.kit.commons.time;

import java.text.NumberFormat;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang3.time.DurationFormatUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;


public class StopWatch extends org.apache.commons.lang3.time.StopWatch{

	private final Map<Pair<Integer, String>,Long> aliasMap = new LinkedHashMap<Pair<Integer, String>,Long>();
	
	private AtomicInteger index=new AtomicInteger();
	
	/**
	 * {@inheritDoc}
	 *
	 */
	@Override
	public void split() {
		this.split("");
	}
	
	
	/** 
	 *  加别名
	 * <p>功能详细描述</p>
	 *
	 * @param alias
	 */
	public void split(String alias) {
		super.split();
		int i=index.getAndIncrement();
		aliasMap.put(new ImmutablePair<Integer, String>(i, alias), this.getSplitTime());
	}
	
	/**
	 * {@inheritDoc}
	 *
	 */
	@Override
	public void stop() {
		super.stop();
		int i=index.getAndIncrement();
		aliasMap.put(new ImmutablePair<Integer, String>(i, "stop"), this.getSplitTime());
	}
	
	/**
	 * Return a string with a table describing all tasks performed.
	 * For custom reporting, call getTaskInfo() and use the task info directly.
	 */
	public String prettyPrint() {
		StringBuilder sb = new StringBuilder();
		sb.append('\n');
		sb.append("-----------------------------------------\n");
		sb.append("time           %  step(ms)    Alias name\n");
		sb.append("-----------------------------------------\n");
		NumberFormat nf = NumberFormat.getNumberInstance();
		nf.setMinimumIntegerDigits(5);
		nf.setGroupingUsed(false);
		NumberFormat pf = NumberFormat.getPercentInstance();
		pf.setMinimumIntegerDigits(3);
		pf.setGroupingUsed(false);
		Entry<Pair<Integer,String>,Long> pre=null;
		for (Entry<Pair<Integer,String>,Long> t : aliasMap.entrySet()) {
			sb.append(DurationFormatUtils.formatDurationHMS(t.getValue())).append("  ");
			long times=0l;
			if(pre==null){
				times=t.getValue();
			}else{
				times=t.getValue()-pre.getValue();
			}
			pre=t;
			sb.append(pf.format(times/1.0/ this.getSplitTime())).append("  ");
			sb.append(nf.format(times)).append("        ");
			Pair<Integer, String> k= t.getKey();
			sb.append(k.getLeft()).append("[").append(k.getRight()).append("]").append("\n");
		}
		return sb.toString();
	}
}
