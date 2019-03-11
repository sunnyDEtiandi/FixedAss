package fixedass.xiangrong.com.fixedass.bean;

import java.io.Serializable;

public class AssetBorrow implements Serializable {
  
	private String borrowUUID;

    private String borrowTime;

    private String borrowDate;

    private String returnTime;

    private String returnDate;

    private String operTime;

    private String borrowGroup;

    private String borrowCompany;

    private String borrowDepatment;

    private String borrowPeople;//借用人id
    
    private DptPeople deptPeople;//借用人对象

    private String borrowListNote;

    private Integer borrowDays;

    private Integer isReturn;

    private String barCode;//资产编码
    
    private AssetStorage storage;//资产对象

    private String operbillCode;

    private String operPeople;

    private String operDate;

    private String borrowInfo;
    
    private String borrowGroupName;
    
    private String borrowCompanyName;
    
    private String borrowDeptName;
    
    private String sysUUID;
    
    public String getSysUUID() {
		return sysUUID;
	}

	public void setSysUUID(String sysUUID) {
		this.sysUUID = sysUUID;
	}

	public String getBorrowGroupName() {
		return borrowGroupName;
	}

	public void setBorrowGroupName(String borrowGroupName) {
		this.borrowGroupName = borrowGroupName;
	}

	public String getBorrowCompanyName() {
		return borrowCompanyName;
	}

	public void setBorrowCompanyName(String borrowCompanyName) {
		this.borrowCompanyName = borrowCompanyName;
	}

	public String getBorrowDeptName() {
		return borrowDeptName;
	}

	public void setBorrowDeptName(String borrowDeptName) {
		this.borrowDeptName = borrowDeptName;
	}

	public AssetStorage getStorage() {
		return storage;
	}

	public void setStorage(AssetStorage storage) {
		this.storage = storage;
	}

	public DptPeople getDeptPeople() {
		return deptPeople;
	}

	public void setDeptPeople(DptPeople deptPeople) {
		this.deptPeople = deptPeople;
	}

	public String getBorrowUUID() {
        return borrowUUID;
    }

    public void setBorrowUUID(String borrowUUID) {
        this.borrowUUID = borrowUUID;
    }

    public String getBorrowTime() {
        return borrowTime;
    }

    public void setBorrowTime(String borrowTime) {
        this.borrowTime = borrowTime;
    }

    public String getBorrowDate() {
        return borrowDate;
    }

    public void setBorrowDate(String borrowDate) {
        this.borrowDate = borrowDate;
    }

    public String getReturnTime() {
        return returnTime;
    }

    public void setReturnTime(String returnTime) {
        this.returnTime = returnTime;
    }

    public String getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(String returnDate) {
        this.returnDate = returnDate;
    }

    public String getOperTime() {
        return operTime;
    }

    public void setOperTime(String operTime) {
        this.operTime = operTime;
    }

    public String getBorrowGroup() {
        return borrowGroup;
    }

    public void setBorrowGroup(String borrowGroup) {
        this.borrowGroup = borrowGroup;
    }

    public String getBorrowCompany() {
        return borrowCompany;
    }

    public void setBorrowCompany(String borrowCompany) {
        this.borrowCompany = borrowCompany;
    }

    public String getBorrowDepatment() {
        return borrowDepatment;
    }

    public void setBorrowDepatment(String borrowDepatment) {
        this.borrowDepatment = borrowDepatment;
    }

    public String getBorrowPeople() {
        return borrowPeople;
    }

    public void setBorrowPeople(String borrowPeople) {
        this.borrowPeople = borrowPeople;
    }

    public String getBorrowListNote() {
        return borrowListNote;
    }

    public void setBorrowListNote(String borrowListNote) {
        this.borrowListNote = borrowListNote;
    }

    public Integer getBorrowDays() {
        return borrowDays;
    }

    public void setBorrowDays(Integer borrowDays) {
        this.borrowDays = borrowDays;
    }

    public Integer getIsReturn() {
        return isReturn;
    }

    public void setIsReturn(Integer isReturn) {
        this.isReturn = isReturn;
    }

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public String getOperbillCode() {
        return operbillCode;
    }

    public void setOperbillCode(String operbillCode) {
        this.operbillCode = operbillCode;
    }

    public String getOperPeople() {
        return operPeople;
    }

    public void setOperPeople(String operPeople) {
        this.operPeople = operPeople;
    }

    public String getOperDate() {
        return operDate;
    }

    public void setOperDate(String operDate) {
        this.operDate = operDate;
    }

    public String getBorrowInfo() {
        return borrowInfo;
    }

    public void setBorrowInfo(String borrowInfo) {
        this.borrowInfo = borrowInfo;
    }

    @Override
    public String toString() {
        return "AssetBorrow{" +
                "borrowUUID='" + borrowUUID + '\'' +
                ", borrowTime='" + borrowTime + '\'' +
                ", borrowDate='" + borrowDate + '\'' +
                ", returnTime='" + returnTime + '\'' +
                ", returnDate='" + returnDate + '\'' +
                ", operTime='" + operTime + '\'' +
                ", borrowGroup='" + borrowGroup + '\'' +
                ", borrowCompany='" + borrowCompany + '\'' +
                ", borrowDepatment='" + borrowDepatment + '\'' +
                ", borrowPeople='" + borrowPeople + '\'' +
                ", deptPeople=" + deptPeople +
                ", borrowListNote='" + borrowListNote + '\'' +
                ", borrowDays=" + borrowDays +
                ", isReturn=" + isReturn +
                ", barCode='" + barCode + '\'' +
                ", storage=" + storage +
                ", operbillCode='" + operbillCode + '\'' +
                ", operPeople='" + operPeople + '\'' +
                ", operDate='" + operDate + '\'' +
                ", borrowInfo='" + borrowInfo + '\'' +
                ", borrowGroupName='" + borrowGroupName + '\'' +
                ", borrowCompanyName='" + borrowCompanyName + '\'' +
                ", borrowDeptName='" + borrowDeptName + '\'' +
                ", sysUUID='" + sysUUID + '\'' +
                '}';
    }
}