package fixedass.xiangrong.com.fixedass.bean;

import java.io.Serializable;

public class AssetChange implements Serializable {
    private String changeUUID;

    private String operbillCode;

    private String barCode;		//资产编码
    
	private String oldGroup;
	private Dept oldGroupObj;
	
	private String oldCompany;
	private Dept oldCompanyObj;
	
	private String oldDepartment;
	private Dept oldDeptObj;

    private String oldPlace;
    private String oldPlaceCode;

    private String oldPeople;//原使用人id
    private DptPeople oldPeopleObj;//原使用人
    
    private String newGroup;
    private Dept newGroupObj;

    private String newGompany;
    private Dept newCompanyObj;
    
    private String newDepartment;
    private Dept newDeptObj;
    
    private String newPlaceCode;//id
    private String newPlace;

    private String newPeople;//新使用人id
    
    private String changeDate;

    private String careMan;	//新公司保管员id
    private DptPeople careManObj;//转入单位保管员

    private String confirmPeople;//确认人id
    
    private String confirmTime;	//确认时间

    private String sysUUID;
    
    private AssetStorage storage;//资产对象
    
    private DptPeople newDeptPeople;//新使用人
    
    public DptPeople getCareManObj() {
		return careManObj;
	}

	public void setCareManObj(DptPeople careManObj) {
		this.careManObj = careManObj;
	}

	public DptPeople getOldPeopleObj() {
		return oldPeopleObj;
	}

	public void setOldPeopleObj(DptPeople oldPeopleObj) {
		this.oldPeopleObj = oldPeopleObj;
	}

	public Dept getOldGroupObj() {
		return oldGroupObj;
	}

	public void setOldGroupObj(Dept oldGroupObj) {
		this.oldGroupObj = oldGroupObj;
	}

	public Dept getOldCompanyObj() {
		return oldCompanyObj;
	}

	public void setOldCompanyObj(Dept oldCompanyObj) {
		this.oldCompanyObj = oldCompanyObj;
	}

	public Dept getOldDeptObj() {
		return oldDeptObj;
	}

	public void setOldDeptObj(Dept oldDeptObj) {
		this.oldDeptObj = oldDeptObj;
	}

	public String getConfirmPeople() {
		return confirmPeople;
	}

	public void setConfirmPeople(String confirmPeople) {
		this.confirmPeople = confirmPeople;
	}

	public String getConfirmTime() {
		return confirmTime;
	}

	public void setConfirmTime(String confirmTime) {
		this.confirmTime = confirmTime;
	}

	public AssetStorage getStorage() {
    	return storage;
    }
    
    public void setStorage(AssetStorage storage) {
    	this.storage = storage;
    }
    
	public DptPeople getNewDeptPeople() {
		return newDeptPeople;
	}

	public void setNewDeptPeople(DptPeople newDeptPeople) {
		this.newDeptPeople = newDeptPeople;
	}

	public String getChangeUUID() {
        return changeUUID;
    }

    public void setChangeUUID(String changeUUID) {
        this.changeUUID = changeUUID;
    }

    public String getOperbillCode() {
        return operbillCode;
    }

    public void setOperbillCode(String operbillCode) {
        this.operbillCode = operbillCode;
    }

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public String getOldGroup() {
        return oldGroup;
    }

    public void setOldGroup(String oldGroup) {
        this.oldGroup = oldGroup;
    }

    public String getOldCompany() {
        return oldCompany;
    }

    public void setOldCompany(String oldCompany) {
        this.oldCompany = oldCompany;
    }

    public String getOldDepartment() {
        return oldDepartment;
    }

    public void setOldDepartment(String oldDepartment) {
        this.oldDepartment = oldDepartment;
    }

    public String getOldPlace() {
        return oldPlace;
    }

    public void setOldPlace(String oldPlace) {
        this.oldPlace = oldPlace;
    }

    public String getOldPeople() {
        return oldPeople;
    }

    public void setOldPeople(String oldPeople) {
        this.oldPeople = oldPeople;
    }

    public String getNewGroup() {
        return newGroup;
    }

    public void setNewGroup(String newGroup) {
        this.newGroup = newGroup;
    }

    public String getNewGompany() {
        return newGompany;
    }

    public void setNewGompany(String newGompany) {
        this.newGompany = newGompany;
    }

    public String getNewDepartment() {
        return newDepartment;
    }

    public void setNewDepartment(String newDepartment) {
        this.newDepartment = newDepartment;
    }

    public Dept getNewGroupObj() {
		return newGroupObj;
	}

	public void setNewGroupObj(Dept newGroupObj) {
		this.newGroupObj = newGroupObj;
	}

	public Dept getNewCompanyObj() {
		return newCompanyObj;
	}

	public void setNewCompanyObj(Dept newCompanyObj) {
		this.newCompanyObj = newCompanyObj;
	}

	public Dept getNewDeptObj() {
		return newDeptObj;
	}

	public void setNewDeptObj(Dept newDeptObj) {
		this.newDeptObj = newDeptObj;
	}

	public String getNewPlace() {
        return newPlace;
    }

    public void setNewPlace(String newPlace) {
        this.newPlace = newPlace;
    }

    public String getNewPeople() {
        return newPeople;
    }

    public void setNewPeople(String newPeople) {
        this.newPeople = newPeople;
    }

    public String getChangeDate() {
        return changeDate;
    }

    public void setChangeDate(String changeDate) {
        this.changeDate = changeDate;
    }

    public String getCareMan() {
        return careMan;
    }

    public void setCareMan(String careMan) {
        this.careMan = careMan;
    }

    public String getOldPlaceCode() {
        return oldPlaceCode;
    }

    public void setOldPlaceCode(String oldPlaceCode) {
        this.oldPlaceCode = oldPlaceCode;
    }

    public String getNewPlaceCode() {
        return newPlaceCode;
    }

    public void setNewPlaceCode(String newPlaceCode) {
        this.newPlaceCode = newPlaceCode;
    }

    public String getSysUUID() {
        return sysUUID;
    }

    public void setSysUUID(String sysUUID) {
        this.sysUUID = sysUUID;
    }

    @Override
    public String toString() {
        return "AssetChange{" +
                "changeUUID='" + changeUUID + '\'' +
                ", operbillCode='" + operbillCode + '\'' +
                ", barCode='" + barCode + '\'' +
                ", oldGroup='" + oldGroup + '\'' +
                ", oldGroupObj=" + oldGroupObj +
                ", oldCompany='" + oldCompany + '\'' +
                ", oldCompanyObj=" + oldCompanyObj +
                ", oldDepartment='" + oldDepartment + '\'' +
                ", oldDeptObj=" + oldDeptObj +
                ", oldPlace='" + oldPlace + '\'' +
                ", oldPlaceCode='" + oldPlaceCode + '\'' +
                ", oldPeople='" + oldPeople + '\'' +
                ", oldPeopleObj=" + oldPeopleObj +
                ", newGroup='" + newGroup + '\'' +
                ", newGroupObj=" + newGroupObj +
                ", newGompany='" + newGompany + '\'' +
                ", newCompanyObj=" + newCompanyObj +
                ", newDepartment='" + newDepartment + '\'' +
                ", newDeptObj=" + newDeptObj +
                ", newPlaceCode='" + newPlaceCode + '\'' +
                ", newPlace='" + newPlace + '\'' +
                ", newPeople='" + newPeople + '\'' +
                ", changeDate='" + changeDate + '\'' +
                ", careMan='" + careMan + '\'' +
                ", careManObj=" + careManObj +
                ", confirmPeople='" + confirmPeople + '\'' +
                ", confirmTime='" + confirmTime + '\'' +
                ", sysUUID='" + sysUUID + '\'' +
                ", storage=" + storage +
                ", newDeptPeople=" + newDeptPeople +
                '}';
    }
}