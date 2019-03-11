package fixedass.xiangrong.com.fixedass.bean;

import java.io.Serializable;

public class AssetCountBill implements Serializable {
    private String countBillUUID;

    private Integer rowID;

    private String countBillCode;

    private Integer countIndex;

    private String createPeople;

    private User cPeoInfo;
    
    private String createDate;

    private String countNote;

    private Boolean isDel;
    
    private String sysUUID;

    public AssetCountBill() { }

    public AssetCountBill(String countBillCode, String createPeople, String createDate, String countNote) {
        this.countBillCode = countBillCode;
        this.createPeople = createPeople;
        this.createDate = createDate;
        this.countNote = countNote;
    }

    public AssetCountBill(String countBillCode, String createPeople, String createDate, String countNote, String sysUUID) {
        this.countBillCode = countBillCode;
        this.createPeople = createPeople;
        this.createDate = createDate;
        this.countNote = countNote;
        this.sysUUID = sysUUID;
    }

    public String getCountBillUUID() {
        return countBillUUID;
    }

    public void setCountBillUUID(String countBillUUID) {
        this.countBillUUID = countBillUUID;
    }

    public Integer getRowID() {
        return rowID;
    }

    public void setRowID(Integer rowID) {
        this.rowID = rowID;
    }

    public String getCountBillCode() {
        return countBillCode;
    }

    public void setCountBillCode(String countBillCode) {
        this.countBillCode = countBillCode;
    }

    public Integer getCountIndex() {
        return countIndex;
    }

    public void setCountIndex(Integer countIndex) {
        this.countIndex = countIndex;
    }

    public String getCreatePeople() {
        return createPeople;
    }

    public void setCreatePeople(String createPeople) {
        this.createPeople = createPeople;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getCountNote() {
        return countNote;
    }

    public void setCountNote(String countNote) {
        this.countNote = countNote;
    }

    public Boolean getIsDel() {
        return isDel;
    }

    public void setIsDel(Boolean isDel) {
        this.isDel = isDel;
    }

	public User getcPeoInfo() {
		return cPeoInfo;
	}

	public void setcPeoInfo(User cPeoInfo) {
		this.cPeoInfo = cPeoInfo;
	}

	public String getSysUUID() {
		return sysUUID;
	}

	public void setSysUUID(String sysUUID) {
		this.sysUUID = sysUUID;
	}
}