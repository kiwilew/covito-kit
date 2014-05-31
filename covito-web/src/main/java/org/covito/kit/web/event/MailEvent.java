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
public class MailEvent extends ApplicationEvent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * <默认构造函数>
	 * 
	 * @param source
	 */
	public MailEvent(MailDO source) {
		super(source);
	}

	/**
	 * <一句话功能简述><br/>
	 * <功能详细描述>
	 * 
	 * @author eighteencold
	 * @version [v1.0, 2013-10-17]
	 */
	public static class MailDO {

		/** 主题 */
		private String subject;

		/** 内容 */
		private String content;

		/** 接收人 */
		private List<String> toUserList;
		
		/** 接收人地址 */
		private List<String> toUserAddre;

		/** 发送人 */
		private String fromUser = "SYSTEM";

		/** 操作URL */
		private String redirectURL;

		/**
		 * 获取 subject
		 * 
		 * @return 返回 subject
		 */
		public String getSubject() {
			return subject;
		}

		/**
		 * 设置 subject
		 * 
		 * @param 对subject进行赋值
		 */
		public void setSubject(String subject) {
			this.subject = subject;
		}

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
		 * 获取 toUserAddre
		 * @return 返回 toUserAddre
		 */
		public List<String> getToUserAddre() {
			return toUserAddre;
		}

		/**
		 * 设置 toUserAddre
		 * @param 对toUserAddre进行赋值
		 */
		public void setToUserAddre(String... toUserAddre) {
			this.toUserAddre = Arrays.asList(toUserAddre);
		}

		/**
		 * 获取 fromUser
		 * 
		 * @return 返回 fromUser
		 */
		public String getFromUser() {
			return fromUser;
		}

		/**
		 * 设置 fromUser
		 * 
		 * @param 对fromUser进行赋值
		 */
		public void setFromUser(String fromUser) {
			this.fromUser = fromUser;
		}

		/**
		 * 获取 redirectURL
		 * 
		 * @return 返回 redirectURL
		 */
		public String getRedirectURL() {
			return redirectURL;
		}

		/**
		 * 设置 redirectURL
		 * 
		 * @param 对redirectURL进行赋值
		 */
		public void setRedirectURL(String redirectURL) {
			this.redirectURL = redirectURL;
		}

	}
}
