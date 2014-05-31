package org.covito.kit.web.springmvc.bind;

import java.beans.PropertyEditorSupport;

/**
 * 日期编辑器
 * <p>根据日期字符串长度判断是长日期还是短日期。只支持yyyy-MM-dd，yyyy-MM-dd HH:mm:ss两种格式。
 * 扩展支持yyyy,yyyy-MM日期格式</p>
 * 
 * @author  covito
 * @version  [v1.0, 2014-5-14]
 */
public class DateTypeEditor extends PropertyEditorSupport{
	
	
	/**
	 * {@inheritDoc}
	 *
	 * @author  covito
	 * @return
	 */
	@Override
	public String getAsText() {
		return super.getAsText();
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @author  covito
	 * @param text
	 * @throws IllegalArgumentException
	 */
	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		super.setAsText(text);
	}
}
