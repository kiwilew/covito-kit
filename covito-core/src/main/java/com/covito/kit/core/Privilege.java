package com.covito.kit.core;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.covito.kit.utility.ObjectUtil;
import com.covito.kit.utility.StringUtil;

/**
 * 权限模型实体<br/>
 * <功能详细描述>
 * 
 * @author  eighteencold
 * @version  [v1.0, 2014-1-23]
 */
public class Privilege implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Log log = LogFactory.getLog(getClass());
	
	public static final String OwnerType_Branch = "B";
	public static final String OwnerType_Role = "R";
	public static final String OwnerType_User = "U";
	public static final int Flag_Allow = 1;
	public static final int Flag_Deny = -1;
	public static final int Flag_NotSet = 0;
	public static final String Or = "||";
	
	/**权限项集合*/
	private Map<String, Integer> AllPrivs = new HashMap<String, Integer>();

	protected Map<String, Integer> getAllPrivs() {
		return this.AllPrivs;
	}

	/** 
	 * <查询用户对指定的权限项是否有权限><br/>
	 * <功能详细描述>
	 *
	 * @author  eighteencold
	 * @param privID
	 * @return
	 */
	public boolean hasPriv(String privID) {
		return this.AllPrivs.get(privID).intValue() == Flag_Allow;
	}

	/** 
	 * <设置权限><br/>
	 * <功能详细描述>
	 *
	 * @author  eighteencold
	 * @param privID
	 * @param flag
	 */
	public void put(String privID,int flag) {
		this.AllPrivs.put(privID, flag);
	}

	/** 
	 * <移除权限><br/>
	 * <功能详细描述>
	 *
	 * @author  eighteencold
	 * @param privID
	 */
	public void remove(String privID) {
		this.AllPrivs.remove(privID);
	}

	/** 
	 * <合并权限><br/>
	 * <功能详细描述>
	 *
	 * @author  eighteencold
	 * @param p
	 */
	public void union(Privilege p) {
		if (p == null) {
			return;
		}
		for (String id : p.AllPrivs.keySet()) {
			Integer flag1 = this.AllPrivs.get(id);
			Integer flag2 = p.AllPrivs.get(id);
			if(flag1==null){
				this.AllPrivs.put(id, flag2);
				continue;
			}
			if ((flag2 < 0) && (flag1 >= 0)) {//自己有权限+没有权限=没有权限
				this.AllPrivs.put(id, Flag_Deny);
				continue;
			}
			if ((flag2 > 0) && (flag1 >= 0)){//自己有权限+有权限=有权限
				this.AllPrivs.put(id, Flag_Allow);
				continue;
			}
		}
	}

	/** 
	 * <解析权限字符串><br/>
	 * <功能详细描述>
	 *
	 * @author  eighteencold
	 * @param str
	 */
	public void parse(String str) {
		clear();
		if (ObjectUtil.empty(str)) {
			return;
		}
		String[] lines = StringUtil.split(str, "\n");
		for (String line : lines) {
			String[] arr = line.trim().split("\\=");
			if (arr.length != 2)
				continue;
			try {
				this.AllPrivs.put(arr[0], Integer.valueOf(Integer
						.parseInt(arr[1])));
			} catch (Exception e) {
				log.error("Invalid privilege:" + line);
			}
		}
	}

	/** 
	 * <移除所以权限><br/>
	 * <功能详细描述>
	 *
	 * @author  eighteencold
	 */
	public void clear() {
		this.AllPrivs.clear();
	}

	/**
	 * 重载方法
	 *
	 * @author  eighteencold
	 * @return
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (String id : this.AllPrivs.keySet()) {
			if (ObjectUtil.empty(id)) {
				continue;
			}
			sb.append(id + "="
					+ StringUtil.javaEncode(this.AllPrivs.get(id).toString()));
			sb.append("\n");
		}
		return sb.toString();
	}
}
