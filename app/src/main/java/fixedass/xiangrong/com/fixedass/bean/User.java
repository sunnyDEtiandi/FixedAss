package fixedass.xiangrong.com.fixedass.bean;

import java.io.Serializable;

public class User implements Serializable {
    private String userUUID;

    private Integer rowID;

    private String deptUUID;

    private String userDeptName;

    private String userName;

    private String userNo;

    private String userPWD;

    private String userPhone;

    private String userEmail;

    private String userAddr;

    private String userWeiXin;

    private String userIdNum;

    private String userState;

    private String userRole;

    private String sysUUID;

    private String creatorID;

    private String createTime;

    private String editorID;

    private String editTime;

    private String lastLoginIP;

    private String lastLoginTime;

    private String currentLoginIP;

    private String currentLoginTime;

    private String currentState;

    public String getUserUUID() {
        return userUUID;
    }

    public void setUserUUID(String userUUID) {
        this.userUUID = userUUID;
    }

    public Integer getRowID() {
        return rowID;
    }

    public void setRowID(Integer rowID) {
        this.rowID = rowID;
    }

    public String getDeptUUID() {
        return deptUUID;
    }

    public void setDeptUUID(String deptUUID) {
        this.deptUUID = deptUUID;
    }

    public String getUserDeptName() {
        return userDeptName;
    }

    public void setUserDeptName(String userDeptName) {
        this.userDeptName = userDeptName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserNo() {
        return userNo;
    }

    public void setUserNo(String userNo) {
        this.userNo = userNo;
    }

    public String getUserPWD() {
        return userPWD;
    }

    public void setUserPWD(String userPWD) {
        this.userPWD = userPWD;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserAddr() {
        return userAddr;
    }

    public void setUserAddr(String userAddr) {
        this.userAddr = userAddr;
    }

    public String getUserWeiXin() {
        return userWeiXin;
    }

    public void setUserWeiXin(String userWeiXin) {
        this.userWeiXin = userWeiXin;
    }

    public String getUserIdNum() {
        return userIdNum;
    }

    public void setUserIdNum(String userIdNum) {
        this.userIdNum = userIdNum;
    }

    public String getUserState() {
        return userState;
    }

    public void setUserState(String userState) {
        this.userState = userState;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public String getSysUUID() {
        return sysUUID;
    }

    public void setSysUUID(String sysUUID) {
        this.sysUUID = sysUUID;
    }

    public String getCreatorID() {
        return creatorID;
    }

    public void setCreatorID(String creatorID) {
        this.creatorID = creatorID;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getEditorID() {
        return editorID;
    }

    public void setEditorID(String editorID) {
        this.editorID = editorID;
    }

    public String getEditTime() {
        return editTime;
    }

    public void setEditTime(String editTime) {
        this.editTime = editTime;
    }

    public String getLastLoginIP() {
        return lastLoginIP;
    }

    public void setLastLoginIP(String lastLoginIP) {
        this.lastLoginIP = lastLoginIP;
    }

    public String getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(String lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public String getCurrentLoginIP() {
        return currentLoginIP;
    }

    public void setCurrentLoginIP(String currentLoginIP) {
        this.currentLoginIP = currentLoginIP;
    }

    public String getCurrentLoginTime() {
        return currentLoginTime;
    }

    public void setCurrentLoginTime(String currentLoginTime) {
        this.currentLoginTime = currentLoginTime;
    }

    public String getCurrentState() {
        return currentState;
    }

    public void setCurrentState(String currentState) {
        this.currentState = currentState;
    }

	@Override
	public String toString() {
		return String
				.format("User [userUUID=%s, rowID=%s, deptUUID=%s, userDeptName=%s, userName=%s, userNo=%s, userPWD=%s, userPhone=%s, userEmail=%s, userAddr=%s, userWeiXin=%s, userIdNum=%s, userState=%s, userRole=%s, sysUUID=%s, creatorID=%s, createTime=%s, editorID=%s, editTime=%s, lastLoginIP=%s, lastLoginTime=%s, currentLoginIP=%s, currentLoginTime=%s, currentState=%s]",
						userUUID, rowID, deptUUID, userDeptName, userName,
						userNo, userPWD, userPhone, userEmail, userAddr,
						userWeiXin, userIdNum, userState, userRole, sysUUID,
						creatorID, createTime, editorID, editTime, lastLoginIP,
						lastLoginTime, currentLoginIP, currentLoginTime,
						currentState);
	}
}