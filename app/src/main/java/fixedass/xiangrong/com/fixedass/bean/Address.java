package fixedass.xiangrong.com.fixedass.bean;

import java.io.Serializable;
import java.util.Date;

public class Address implements Serializable {
    private String addrUUID;

    private String deptUUID;//所属部门id
    
    private Dept dept;

    private String addrName;

    private String addrCode;

    private String addrNote;

    private String createID;//创建人id
    
    private User cUser;

    private String createTime;

    private String updateID;

    private String updateTime;
    
    public Dept getDept() {
		return dept;
	}

	public void setDept(Dept dept) {
		this.dept = dept;
	}

	public User getcUser() {
		return cUser;
	}

	public void setcUser(User cUser) {
		this.cUser = cUser;
	}

	public String getAddrUUID() {
        return addrUUID;
    }

    public void setAddrUUID(String addrUUID) {
        this.addrUUID = addrUUID;
    }

    public String getDeptUUID() {
        return deptUUID;
    }

    public void setDeptUUID(String deptUUID) {
        this.deptUUID = deptUUID;
    }

    public String getAddrName() {
        return addrName;
    }

    public void setAddrName(String addrName) {
        this.addrName = addrName;
    }

    public String getAddrCode() {
        return addrCode;
    }

    public void setAddrCode(String addrCode) {
        this.addrCode = addrCode;
    }

    public String getAddrNote() {
        return addrNote;
    }

    public void setAddrNote(String addrNote) {
        this.addrNote = addrNote;
    }

    public String getCreateID() {
        return createID;
    }

    public void setCreateID(String createID) {
        this.createID = createID;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateID() {
        return updateID;
    }

    public void setUpdateID(String updateID) {
        this.updateID = updateID;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
}