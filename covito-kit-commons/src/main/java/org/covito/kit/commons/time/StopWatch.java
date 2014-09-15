package org.covito.kit.commons.time;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DurationFormatUtils;

public class StopWatch extends org.apache.commons.lang3.time.StopWatch{

	private final List<WatchSnapshot> snapshots = new ArrayList<WatchSnapshot>();
	
	private AtomicInteger index=new AtomicInteger(1);
	
	/**
	 * {@inheritDoc}
	 *
	 */
	@Override
	public void split() {
		this.split("");
	}
	
	
	/** 
	 * split 时候加别名
	 * <p>功能详细描述</p>
	 *
	 * @param name
	 */
	public void split(String name) {
		name=StringUtils.defaultString(name, "");
		super.split();
		int i=index.getAndIncrement();
		snapshots.add(new WatchSnapshot(i, name, this.getSplitTime()));
	}
	
	/**
	 * {@inheritDoc}
	 *
	 */
	@Override
	public void stop() {
		super.stop();
		int i=index.getAndIncrement();
		snapshots.add(new WatchSnapshot(i, "stop", this.getSplitTime()));
	}
	
	/**
	 * 获取时间快照(ms)
	 * @param index
	 * @return
	 */
	public Long getSnapshot(int index){
		if(index<1){
			return 0L;
		}
		for(WatchSnapshot snp:snapshots){
			if(snp.index==index){
				return snp.getTimeMillis();
			}
		}
		return null;
	}
	
	/**
	 * 获取时间快照(ms)
	 * @param index
	 * @return
	 */
	public Long getSnapshot(String name){
		name=StringUtils.defaultString(name, "");
		for(WatchSnapshot snp:snapshots){
			if(snp.name.equals(name)){
				return snp.getTimeMillis();
			}
		}
		return null;
	}
	
	/**
	 * Return a string with a table describing all tasks performed.
	 * For custom reporting, call getTaskInfo() and use the task info directly.
	 */
	public String prettyPrint() {
		StringBuilder sb = new StringBuilder("StopWatch: running time (millis) = " + getSplitTime());
		sb.append('\n');
		sb.append("-----------------------------------------\n");
		sb.append("time           %  step(ms)    snapshot\n");
		sb.append("-----------------------------------------\n");
		NumberFormat nf = NumberFormat.getNumberInstance();
		nf.setMinimumIntegerDigits(5);
		nf.setGroupingUsed(false);
		NumberFormat pf = NumberFormat.getPercentInstance();
		pf.setMinimumIntegerDigits(3);
		pf.setGroupingUsed(false);
		WatchSnapshot pre=null;
		for (WatchSnapshot t : snapshots) {
			sb.append(DurationFormatUtils.formatDurationHMS(t.getTimeMillis())).append("  ");
			long times=0l;
			if(pre==null){
				times=t.getTimeMillis();
			}else{
				times=t.getTimeMillis()-pre.getTimeMillis();
			}
			pre=t;
			sb.append(pf.format(times/1.0/ this.getSplitTime())).append("  ");
			sb.append(nf.format(times)).append("      ");
			sb.append(t.getIndex()).append("[").append(t.getName()).append("]").append("\n");
		}
		return sb.toString();
	}
	
	public static class WatchSnapshot{
		private Integer index;
		
		private String name;
		
		private Long timeMillis;
		
		public WatchSnapshot(Integer index, String name, Long timeMillis) {
			super();
			this.index = index;
			this.name = name;
			this.timeMillis = timeMillis;
		}

		public Integer getIndex() {
			return index;
		}

		public void setIndex(Integer index) {
			this.index = index;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public Long getTimeMillis() {
			return timeMillis;
		}

		public void setTimeMillis(Long timeMillis) {
			this.timeMillis = timeMillis;
		}
		
	}
}
