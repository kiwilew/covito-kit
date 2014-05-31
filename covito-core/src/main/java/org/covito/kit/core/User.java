package org.covito.kit.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.covito.kit.utility.FileUtil;


/**
 * 登录用户模型<br/>
 * <功能详细描述>
 * 
 * @author  eighteencold
 * @version  [v1.0, 2014-1-23]
 */
public class User {
	
	private static Log log = LogFactory.getLog(User.class);
	
	public static final String UserAttrName = "_TECH_USER";
	
	private static ThreadLocal<UserData> UserLocal = new ThreadLocal<UserData>();

	public static String getUserName() {
		return getCurrent().UserName;
	}
	
	public static void setUserName(String UserName) {
		getCurrent().UserName=UserName;
		if (Config.isDebugMode()){
			cacheUser(getCurrent());
		}
	}

	public static String getRealName() {
		return getCurrent().RealName;
	}

	public static void setRealName(String RealName) {
		getCurrent().RealName = RealName;
		if (Config.isDebugMode()) {
			cacheUser(getCurrent());
		}
	}

	public static String getBranchInnerCode() {
		return getCurrent().BranchInnerCode;
	}

	public static void setBranchInnerCode(String BranchInnerCode) {
		getCurrent().BranchInnerCode = BranchInnerCode;
		if (Config.isDebugMode()) {
			cacheUser(getCurrent());
		}
	}

	public static boolean isBranchAdministrator() {
		return getCurrent().BranchAdminFlag;
	}

	public static void setBranchAdministrator(boolean flag) {
		getCurrent().BranchAdminFlag = flag;
		if (Config.isDebugMode()) {
			cacheUser(getCurrent());
		}
	}

	public static String getType() {
		return getCurrent().Type;
	}

	public static void setType(String Type) {
		getCurrent().Type = Type;
		if (Config.isDebugMode()) {
			cacheUser(getCurrent());
		}
	}

	public static String getStatus() {
		return getCurrent().Status;
	}

	public static void setStatus(String Status) {
		getCurrent().Status = Status;
		if (Config.isDebugMode()){
			cacheUser(getCurrent());
		}
	}

	public static int getValueCount() {
		return getCurrent().Map.size();
	}

	public static Object getValue(Object key) {
		return getCurrent().Map.get(key);
	}

	public static Map<Object, Object> getValues() {
		return getCurrent().Map;
	}

	public static void setValue(Object key, Object value) {
		Map<Object, Object> map = getCurrent().Map;
		synchronized (map) {
			map.put(key, value);
		}
		if (Config.isDebugMode()){
			cacheUser(getCurrent());
		}
	}

	public static boolean isLogin() {
		return getCurrent().isLogin;
	}

	public static void setLogin(boolean isLogin) {
		if (isLogin) {
			if (!getCurrent().isLogin) {
				Config.LoginUserCount += 1;
			}
		} else if ((getCurrent().isLogin) && (Config.LoginUserCount > 0)) {
			Config.LoginUserCount -= 1;
		}
		getCurrent().isLogin = isLogin;
		if (Config.isDebugMode()){
			cacheUser(getCurrent());
		}
	}

	/** 
	 * <设置当前线程的用户数据><br/>
	 * <功能详细描述>
	 *
	 * @author  eighteencold
	 * @param user
	 */
	public static void setCurrent(UserData user) {
		UserLocal.set(user);
	}
	

	/** 
	 * <获取当前线程的用户数据><br/>
	 * <需要在请求刚到的时候调用setCurrent(user)>
	 *
	 * @author  eighteencold
	 * @return
	 */
	public static UserData getCurrent() {
		UserData ud = (UserData) UserLocal.get();
		if (ud == null) {
			ud = new UserData();
			setCurrent(ud);
		}
		return ud;
	}

	/** 
	 * <缓存用户数据><br/>
	 * <功能详细描述>
	 *
	 * @author  eighteencold
	 * @param u
	 */
	public static void cacheUser(UserData u) {
		if ((getCurrent() != u) || (getCurrent() == null)) {
			return;
		}
		if(!Config.isDebugMode()){
			return;
		}
		try {
			File pdr = new File(Config.getClassPath()).getParentFile();
			File dir = new File(pdr.getAbsolutePath()+ "/cache/");
			if (!dir.exists()) {
				dir.mkdirs();
			}
			if (u.getSessionID() == null) {
				return;
			}
			FileOutputStream f = new FileOutputStream(dir.getAbsoluteFile() +"/"+ u.getSessionID());
			ObjectOutputStream s = new ObjectOutputStream(f);
			s.writeObject(u);
			s.flush();
			s.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** 
	 * <获取debug模式下缓存的数据><br/>
	 * <功能详细描述>
	 *
	 * @author  eighteencold
	 * @param sessionID
	 * @return
	 */
	public static UserData getCachedUser(String sessionID) {
		if(!Config.isDebugMode()){
			return null;
		}
		try {
			File pdr = new File(Config.getClassPath()).getParentFile();
			File in = new File( pdr.getAbsolutePath()+ "/cache/"+ sessionID);
			if (in.exists()) {
				ObjectInputStream s = new ObjectInputStream(
						new FileInputStream(in));
				Object o = s.readObject();
				if (UserData.class.isInstance(o)) {
					s.close();
					in.delete();
					UserData u = (UserData) o;
					if (u.isLogin()) {
						Config.LoginUserCount += 1;
					}
					return u;
				}
				s.close();
			}
		} catch (Exception e) {
			log.warn("getCachedUser() failed");
		}
		return null;
	}

	/** 
	 * <销毁用户><br/>
	 * <功能详细描述>
	 *
	 * @author  eighteencold
	 */
	public static void destory() {
		if (Config.isDebugMode()){
			delCacheUser(getCurrent().getSessionID());
		}
		setCurrent(new UserData());
	}

	/** 
	 * <删除用户缓存数据><br/>
	 * <功能详细描述>
	 *
	 * @author  eighteencold
	 * @param current
	 */
	public static void delCacheUser(String sessionID) {
		if(sessionID==null){
			return;
		}
		File pdr = new File(Config.getClassPath()).getParentFile();
		FileUtil.delete(pdr.getAbsolutePath() + "/cache/" + sessionID);
	}

	public static String getSessionID() {
		return getCurrent().SessionID;
	}

	protected static void setSessionID(String sessionID) {
		getCurrent().SessionID = sessionID;
		if (Config.isDebugMode()){
			cacheUser(getCurrent());
		}
	}

	public static String getLanguage() {
		return getCurrent().Language;
	}

	public static void setLanguage(String language) {
		getCurrent().Language = language;
		if (Config.isDebugMode()){
			cacheUser(getCurrent());
		}
	}

	public static Privilege getPrivilege() {
		return getCurrent().Priv;
	}

	public static void setPrivilege(Privilege priv) {
		getCurrent().Priv = priv;
		if (Config.isDebugMode()){
			cacheUser(getCurrent());
		}
	}

	public static String getUserId() {
		return getCurrent().UserId;
	}

	public static void setUserId(String userId) {
		getCurrent().UserId = userId;
		if (Config.isDebugMode()){
			cacheUser(getCurrent());
		}
	}

	public static String getSsoId() {
		return getCurrent().SsoId;
	}

	public static void setSsoId(String ssoId) {
		getCurrent().SsoId = ssoId;
		if (Config.isDebugMode()){
			cacheUser(getCurrent());
		}
	}

	public static String getIdCard() {
		return getCurrent().IdCard;
	}

	public static void setIdCard(String idCard) {
		getCurrent().IdCard = idCard;
		if (Config.isDebugMode()){
			cacheUser(getCurrent());
		}
	}
	
	/**
	 * 用户数据类<br/>
	 * <功能详细描述>
	 * 
	 * @author  eighteencold
	 * @version  [v1.0, 2014-1-23]
	 */
	public static class UserData implements Serializable {
		private static final long serialVersionUID = 1L;
		private String UserId;
		private String SsoId;
		private String IdCard;
		private String Type;
		private String Status;
		private String UserName;
		private String RealName;
		private String BranchInnerCode;
		private boolean BranchAdminFlag=false;
		private boolean isLogin = false;
		private String SessionID;
		private String Language = "zh-cn";

		private Privilege Priv = new Privilege();

		private Map<Object, Object> Map = new HashMap<Object, Object>();

		public String getType() {
			return this.Type;
		}

		public void setType(String type) {
			this.Type = type;
			User.cacheUser(User.getCurrent());
		}

		public String getStatus() {
			return this.Status;
		}

		public void setStatus(String status) {
			this.Status = status;
			User.cacheUser(User.getCurrent());
		}

		public String getUserName() {
			return this.UserName;
		}

		public void setUserName(String userName) {
			this.UserName = userName;
			User.cacheUser(User.getCurrent());
		}

		public String getRealName() {
			return this.RealName;
		}

		public void setRealName(String realName) {
			this.RealName = realName;
			User.cacheUser(User.getCurrent());
		}

		public String getBranchInnerCode() {
			return this.BranchInnerCode;
		}

		public void setBranchInnerCode(String branchInnerCode) {
			this.BranchInnerCode = branchInnerCode;
			User.cacheUser(User.getCurrent());
		}

		public boolean isBranchAdministrator() {
			return this.BranchAdminFlag;
		}

		public void setBranchAdministrator(boolean flag) {
			this.BranchAdminFlag = flag;
			User.cacheUser(User.getCurrent());
		}

		public boolean isLogin() {
			return this.isLogin;
		}

		public void setLogin(boolean isLogin) {
			this.isLogin = isLogin;
			User.cacheUser(User.getCurrent());
		}

		public String getSessionID() {
			return this.SessionID;
		}

		public void setSessionID(String sessionID) {
			this.SessionID = sessionID;
			User.cacheUser(User.getCurrent());
		}

		public Map<Object, Object> getMap() {
			return this.Map;
		}

		public void setMap(Map<Object, Object> map) {
			this.Map = map;
			User.cacheUser(User.getCurrent());
		}

		public String getLanguage() {
			return this.Language;
		}

		public void setLanguage(String language) {
			this.Language = language;
		}

		public Privilege getPrivilege() {
			return this.Priv;
		}

		public void setPrivilege(Privilege priv) {
			this.Priv = priv;
		}

		public String getUserId() {
			return UserId;
		}

		public void setUserId(String userId) {
			UserId = userId;
		}

		public String getSsoId() {
			return SsoId;
		}

		public void setSsoId(String ssoId) {
			SsoId = ssoId;
		}

		public String getIdCard() {
			return IdCard;
		}

		public void setIdCard(String idCard) {
			IdCard = idCard;
		}
		
	}
}
