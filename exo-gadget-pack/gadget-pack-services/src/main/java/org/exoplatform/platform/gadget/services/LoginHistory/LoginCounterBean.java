package org.exoplatform.platform.gadget.services.LoginHistory;
/**
 * This class stores the logins of the user
 * @author Kate Adam
 *
 */
public class LoginCounterBean {
	private long loginDate;
	private long loginCount;
	
	public long getLoginDate() {
		return loginDate;
	}
	public void setLoginDate(long loginDate) {
		this.loginDate = loginDate;
	}
	public long getLoginCount() {
		return loginCount;
	}
	public void setLoginCount(long loginCount) {
		this.loginCount = loginCount;
	}
}
