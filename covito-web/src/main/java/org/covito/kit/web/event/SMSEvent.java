package org.covito.kit.web.event;

import java.util.Arrays;
import java.util.List;

import org.springframework.context.ApplicationEvent;

/**
 * <一句话功能简述><br/>
 * <功能详细描述>
 * 
 * @author eighteencold
 * @version [v1.0, 2013-10-17]
 */
public class SMSEvent extends ApplicationEvent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * <默认构造函数>
	 * 
	 * @param source
	 */
	public SMSEvent(SMSDO source) {
		super(source);
	}

	/**
	 * <一句话功能简述><br/>
	 * <功能详细描述>
	 * 
	 * @author eighteencold
	 * @version [v1.0, 2013-10-17]
	 */
	public static class SMSDO {

		/** 内容 */
		private String content;

		/** 接收人 */
		private List<String> toUserList;
		
		/** 接收人号码 */
		private List<String> toUserNum;

		/**
		 * 获取 content
		 * 
		 * @return 返回 content
		 */
		public String getContent() {
			return content;
		}

		/**
		 * 设置 content
		 * 
		 * @param 对content进行赋值
		 */
		public void setContent(String content) {
			this.content = content;
		}

		/**
		 * 获取 toUserList
		 * 
		 * @return 返回 toUserList
		 */
		public List<String> getToUserList() {
			return toUserList;
		}

		/**
		 * 设置 toUserList
		 * 
		 * @param 对toUserList进行赋值
		 */
		public void setToUserList(String... toUserList) {
			this.toUserList = Arrays.asList(toUserList);
		}

		/**
		 * 获取 toUserNum
		 * @return 返回 toUserNum
		 */
		public List<String> getToUserNum() {
			return toUserNum;
		}

		/**
		 * 设置 toUserNum
		 * @param 对toUserNum进行赋值
		 */
		public void setToUserNum(String... toUserNum) {
			this.toUserNum = Arrays.asList(toUserNum);
		}
		
	}
}
