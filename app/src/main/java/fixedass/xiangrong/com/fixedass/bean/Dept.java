package fixedass.xiangrong.com.fixedass.bean;

import java.io.Serializable;
import java.util.Date;

public class Dept implements Serializable {
    private String deptUUID;

    private Integer rowID;

    private String pDeptUUID;
    
   private Dept pDept;		  //父部门

    private String deptName;
    
    private String deptIden; //部门编码，使用者自己定义

	private String deptCode;

    private String pDeptCode;

    private String deptType;

    private String deptAddr;

    private String deptPhone;

    private String deptAddInfo;

    private String sysUUID;

    private String creatorID;

    private String createTime;

    private String editorID;

    private String editTime;
    
    public Dept getpDept() {
		return pDept;
	}

	public void setpDept(Dept pDept) {
		this.pDept = pDept;
	}

	public String getDeptIden() {
    	return deptIden;
    }
    
    public void setDeptIden(String deptIden) {
    	this.deptIden = deptIden;
    }
    public String getDeptUUID() {
        return deptUUID;
    }

    public void setDeptUUID(String deptUUID) {
        this.deptUUID = deptUUID;
    }

    public Integer getRowID() {
        return rowID;
    }

    public void setRowID(Integer rowID) {
        this.rowID = rowID;
    }

    public String getpDeptUUID() {
        return pDeptUUID;
    }

    public void setpDeptUUID(String pDeptUUID) {
        this.pDeptUUID = pDeptUUID;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getDeptCode() {
        return deptCode;
    }

    public void setDeptCode(String deptCode) {
        this.deptCode = deptCode;
    }

    public String getpDeptCode() {
        return pDeptCode;
    }

    public void setpDeptCode(String pDeptCode) {
        this.pDeptCode = pDeptCode;
    }

    public String getDeptType() {
        return deptType;
    }

    public void setDeptType(String deptType) {
        this.deptType = deptType;
    }

    public String getDeptAddr() {
        return deptAddr;
    }

    public void setDeptAddr(String deptAddr) {
        this.deptAddr = deptAddr;
    }

    public String getDeptPhone() {
        return deptPhone;
    }

    public void setDeptPhone(String deptPhone) {
        this.deptPhone = deptPhone;
    }

    public String getDeptAddInfo() {
        return deptAddInfo;
    }

    public void setDeptAddInfo(String deptAddInfo) {
        this.deptAddInfo = deptAddInfo;
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

	@Override
	public String toString() {
		return String
				.format("Dept [deptUUID=%s, rowID=%s, pDeptUUID=%s, deptName=%s, deptIden=%s, deptCode=%s, pDeptCode=%s, deptType=%s, deptAddr=%s, deptPhone=%s, deptAddInfo=%s, sysUUID=%s, creatorID=%s, createTime=%s, editorID=%s, editTime=%s]",
						deptUUID, rowID, pDeptUUID, deptName, deptIden,
						deptCode, pDeptCode, deptType, deptAddr, deptPhone,
						deptAddInfo, sysUUID, creatorID, createTime, editorID,
						editTime);
	}
    
}