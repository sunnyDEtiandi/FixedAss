package fixedass.xiangrong.com.fixedass.bean;

import java.io.Serializable;

public class DptPeople implements Serializable {
    private String pUUID;

    private Integer rowID;

    private String pName;

    private String pSex;

    private String pPhone;

    private String pID;

    private String pPlace;

    private String pAddInfo;

    private String sysUUID;

    private String pDptCode;

    private String pDptUUID;

    private Boolean isUse;

    private Boolean isCareMan;

    private Boolean isDel;

	private Dept dept;   //一对一

    public String getpUUID() {
        return pUUID;
    }

    public void setpUUID(String pUUID) {
        this.pUUID = pUUID;
    }

    public Integer getRowID() {
        return rowID;
    }

    public void setRowID(Integer rowID) {
        this.rowID = rowID;
    }

    public String getpName() {
        return pName;
    }

    public void setpName(String pName) {
        this.pName = pName;
    }

    public String getpSex() {
        return pSex;
    }

    public void setpSex(String pSex) {
        this.pSex = pSex;
    }

    public String getpPhone() {
        return pPhone;
    }

    public void setpPhone(String pPhone) {
        this.pPhone = pPhone;
    }

    public String getpID() {
        return pID;
    }

    public void setpID(String pID) {
        this.pID = pID;
    }

    public String getpPlace() {
        return pPlace;
    }

    public void setpPlace(String pPlace) {
        this.pPlace = pPlace;
    }

    public String getpAddInfo() {
        return pAddInfo;
    }

    public void setpAddInfo(String pAddInfo) {
        this.pAddInfo = pAddInfo;
    }

    public String getSysUUID() {
        return sysUUID;
    }

    public void setSysUUID(String sysUUID) {
        this.sysUUID = sysUUID;
    }

    public String getpDptCode() {
        return pDptCode;
    }

    public void setpDptCode(String pDptCode) {
        this.pDptCode = pDptCode;
    }

    public String getpDptUUID() {
        return pDptUUID;
    }

    public void setpDptUUID(String pDptUUID) {
        this.pDptUUID = pDptUUID;
    }

    public Boolean getIsUse() {
        return isUse;
    }

    public void setIsUse(Boolean isUse) {
        this.isUse = isUse;
    }

    public Boolean getIsCareMan() {
        return isCareMan;
    }

    public void setIsCareMan(Boolean isCareMan) {
        this.isCareMan = isCareMan;
    }

    public Boolean getIsDel() {
        return isDel;
    }

    public void setIsDel(Boolean isDel) {
        this.isDel = isDel;
    }

	public Dept getDept() {
		return dept;
	}

	public void setDept(Dept dept) {
		this.dept = dept;
	}

    @Override
    public String toString() {
        return "DptPeople{" +
                "pUUID='" + pUUID + '\'' +
                ", rowID=" + rowID +
                ", pName='" + pName + '\'' +
                ", pSex='" + pSex + '\'' +
                ", pPhone='" + pPhone + '\'' +
                ", pID='" + pID + '\'' +
                ", pPlace='" + pPlace + '\'' +
                ", pAddInfo='" + pAddInfo + '\'' +
                ", sysUUID='" + sysUUID + '\'' +
                ", pDptCode='" + pDptCode + '\'' +
                ", pDptUUID='" + pDptUUID + '\'' +
                ", isUse=" + isUse +
                ", isCareMan=" + isCareMan +
                ", isDel=" + isDel +
                ", dept=" + dept +
                '}';
    }
}