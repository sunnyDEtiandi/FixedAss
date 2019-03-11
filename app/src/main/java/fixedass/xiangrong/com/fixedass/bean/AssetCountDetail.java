package fixedass.xiangrong.com.fixedass.bean;

import java.io.Serializable;

public class AssetCountDetail implements Serializable {
    private String countUUID;

    private Integer rowID;

    private String countBillCode;

    private String barCode;
    
    private AssetStorage storage; //资产

    private String countGroup;

    private Dept groupInfo;
    
    private String countCompany;

    private Dept companyInfo;
    
    private String countDepartment;

    private Dept departmentInfo;
    
    private String countPlace;

    private Address placeInfo;
    
    private String countPeople;

    private User peoInfo;
    
    private String countTime;

    private Integer countState;
    
    private String sysUUID;

    public AssetStorage getStorage() {
		return storage;
	}

	public void setStorage(AssetStorage storage) {
		this.storage = storage;
	}

	public String getCountUUID() {
        return countUUID;
    }

    public void setCountUUID(String countUUID) {
        this.countUUID = countUUID;
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

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public String getCountGroup() {
        return countGroup;
    }

    public void setCountGroup(String countGroup) {
        this.countGroup = countGroup;
    }

    public String getCountCompany() {
        return countCompany;
    }

    public void setCountCompany(String countCompany) {
        this.countCompany = countCompany;
    }

    public String getCountDepartment() {
        return countDepartment;
    }

    public void setCountDepartment(String countDepartment) {
        this.countDepartment = countDepartment;
    }

    public String getCountPlace() {
        return countPlace;
    }

    public void setCountPlace(String countPlace) {
        this.countPlace = countPlace;
    }

    public String getCountPeople() {
        return countPeople;
    }

    public void setCountPeople(String countPeople) {
        this.countPeople = countPeople;
    }

    public String getCountTime() {
        return countTime;
    }

    public void setCountTime(String countTime) {
        this.countTime = countTime;
    }

    public Integer getCountState() {
        return countState;
    }

    public void setCountState(Integer countState) {
        this.countState = countState;
    }

	public Dept getGroupInfo() {
		return groupInfo;
	}

	public void setGroupInfo(Dept groupInfo) {
		this.groupInfo = groupInfo;
	}

	public Dept getCompanyInfo() {
		return companyInfo;
	}

	public void setCompanyInfo(Dept companyInfo) {
		this.companyInfo = companyInfo;
	}

	public Dept getDepartmentInfo() {
		return departmentInfo;
	}

	public void setDepartmentInfo(Dept departmentInfo) {
		this.departmentInfo = departmentInfo;
	}

	public Address getPlaceInfo() {
		return placeInfo;
	}

	public void setPlaceInfo(Address placeInfo) {
		this.placeInfo = placeInfo;
	}

	public User getPeoInfo() {
		return peoInfo;
	}

	public void setPeoInfo(User peoInfo) {
		this.peoInfo = peoInfo;
	}

	public String getSysUUID() {
		return sysUUID;
	}

	public void setSysUUID(String sysUUID) {
		this.sysUUID = sysUUID;
	}

	@Override
	public String toString() {
		return String
				.format("AssetCountDetail [countUUID=%s, rowID=%s, countBillCode=%s, barCode=%s, storage=%s, countGroup=%s, groupInfo=%s, countCompany=%s, companyInfo=%s, countDepartment=%s, departmentInfo=%s, countPlace=%s, placeInfo=%s, countPeople=%s, peoInfo=%s, countTime=%s, countState=%s, sysUUID=%s]",
						countUUID, rowID, countBillCode, barCode, storage,
						countGroup, groupInfo, countCompany, companyInfo,
						countDepartment, departmentInfo, countPlace, placeInfo,
						countPeople, peoInfo, countTime, countState, sysUUID);
	}
}