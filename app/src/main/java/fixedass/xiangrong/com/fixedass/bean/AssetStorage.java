package fixedass.xiangrong.com.fixedass.bean;

import java.io.Serializable;
import java.util.Objects;

public class AssetStorage implements Serializable {
    private String assetUUID;

    private Integer rowID;

    private String barCode;

    private Integer codeIndex;

    private String infoCode;

    private String assName;

    private String oldName;

    private String simpName;

    private String classCode;

    private String className;

    private String assImg;

    private String assType;

    private String facType;

    private Double assPrice;

    private String assUnit;

    private Integer assNum;

    private String assBrand;

    private String facName;

    private String provider;

    private Boolean isCheck;

    private Integer checkCyc;

    private String checkDate;

    private String nextCheckDate;

    private Integer brokenTime;

    private String assSource;

    private Boolean isOne;

    private String addInfo;

    private String assSN;

    private String depreciation;

    private Boolean isPrint;

    private String state;

    private String inDate;

    private String buyDate;

    private String bookDate;

    private String financialCode;

    private String createPeople;

    private String createTime;

    private String careMan; //保管员id
    
    private String oldCareMan;

    private String usePeople;

    private String belongGroup;

    private String useGroup;

    private String oldUseGroup;

    private String useCompany;

    private String oldUseCompany;

    private String useDept;

    private String oldUseDept;

    private String storeAddress;
    
    private Address addressInfo;

    private String oldStoreAddress;

    private String useDate;

    private String updatePeople;

    private String updateTime;

    private Boolean isDel;

    private String sysUUID;

    private String extend1;

    private String extend3;

    private String extend2;

    private String extend4;

    private String extend5;

    private Provider proInfo;	//供应商
    
    private DptPeople peoInfo;	//员工信息

    private Dept bGroupInfo;	//所属公司
    
	private Dept groupInfo;	//使用集团
    
    private Dept companyInfo;	//使用公司

    private Dept deptInfo;	//使用部门

    private User createInfo;	//创建人信息
    
    private User updateInfo;	//更新人信息
    
    private DptPeople careUser;  //保管员
    
    private DptPeople usePeopleEntity;//使用人
    
    private Address address;//新地址

    public AssetStorage() { }

    public AssetStorage(String className, String state, String useGroup, String useCompany, String useDept, String extend1) {
        this.className = className;
        this.state = state;
        this.useGroup = useGroup;
        this.useCompany = useCompany;
        this.useDept = useDept;
        this.extend1 = extend1;
    }

    public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public DptPeople getUsePeopleEntity() {
		return usePeopleEntity;
	}

	public void setUsePeopleEntity(DptPeople usePeopleEntity) {
		this.usePeopleEntity = usePeopleEntity;
	}

	public DptPeople getCareUser() {
		return careUser;
	}
    
	public User getCreateInfo() {
		return createInfo;
	}

	public void setCreateInfo(User createInfo) {
		this.createInfo = createInfo;
	}

	public User getUpdateInfo() {
		return updateInfo;
	}

	public void setUpdateInfo(User updateInfo) {
		this.updateInfo = updateInfo;
	}

	public Dept getbGroupInfo() {
		return bGroupInfo;
	}

	public void setbGroupInfo(Dept bGroupInfo) {
		this.bGroupInfo = bGroupInfo;
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
    
	public Dept getDeptInfo() {
		return deptInfo;
	}

	public void setDeptInfo(Dept deptInfo) {
		this.deptInfo = deptInfo;
	}

	public DptPeople getPeoInfo() {
		return peoInfo;
	}

	public void setPeoInfo(DptPeople peoInfo) {
		this.peoInfo = peoInfo;
	}

	public Provider getProInfo() {
		return proInfo;
	}

	public void setProInfo(Provider proInfo) {
		this.proInfo = proInfo;
	}

	public void setCareUser(DptPeople careUser) {
		this.careUser = careUser;
	}
    
    public String getAssetUUID() {
        return assetUUID;
    }

    public void setAssetUUID(String assetUUID) {
        this.assetUUID = assetUUID;
    }

    public Integer getRowID() {
        return rowID;
    }

    public void setRowID(Integer rowID) {
        this.rowID = rowID;
    }

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public Integer getCodeIndex() {
        return codeIndex;
    }

    public void setCodeIndex(Integer codeIndex) {
        this.codeIndex = codeIndex;
    }

    public String getInfoCode() {
        return infoCode;
    }

    public void setInfoCode(String infoCode) {
        this.infoCode = infoCode;
    }

    public String getAssName() {
        return assName;
    }

    public void setAssName(String assName) {
        this.assName = assName;
    }

    public String getOldName() {
        return oldName;
    }

    public void setOldName(String oldName) {
        this.oldName = oldName;
    }

    public String getSimpName() {
        return simpName;
    }

    public void setSimpName(String simpName) {
        this.simpName = simpName;
    }

    public String getClassCode() {
        return classCode;
    }

    public void setClassCode(String classCode) {
        this.classCode = classCode;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getAssImg() {
        return assImg;
    }

    public void setAssImg(String assImg) {
        this.assImg = assImg;
    }

    public String getAssType() {
        return assType;
    }

    public void setAssType(String assType) {
        this.assType = assType;
    }

    public String getFacType() {
        return facType;
    }

    public void setFacType(String facType) {
        this.facType = facType;
    }

    public Double getAssPrice() {
        return assPrice;
    }

    public void setAssPrice(Double assPrice) {
        this.assPrice = assPrice;
    }

    public String getAssUnit() {
        return assUnit;
    }

    public void setAssUnit(String assUnit) {
        this.assUnit = assUnit;
    }

    public Integer getAssNum() {
        return assNum;
    }

    public void setAssNum(Integer assNum) {
        this.assNum = assNum;
    }

    public String getAssBrand() {
        return assBrand;
    }

    public void setAssBrand(String assBrand) {
        this.assBrand = assBrand;
    }

    public String getFacName() {
        return facName;
    }

    public void setFacName(String facName) {
        this.facName = facName;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public Boolean getIsCheck() {
        return isCheck;
    }

    public void setIsCheck(Boolean isCheck) {
        this.isCheck = isCheck;
    }

    public Integer getCheckCyc() {
        return checkCyc;
    }

    public void setCheckCyc(Integer checkCyc) {
        this.checkCyc = checkCyc;
    }

    public String getCheckDate() {
        return checkDate;
    }

    public void setCheckDate(String checkDate) {
        this.checkDate = checkDate;
    }

    public String getNextCheckDate() {
        return nextCheckDate;
    }

    public void setNextCheckDate(String nextCheckDate) {
        this.nextCheckDate = nextCheckDate;
    }

    public Integer getBrokenTime() {
        return brokenTime;
    }

    public void setBrokenTime(Integer brokenTime) {
        this.brokenTime = brokenTime;
    }

    public String getAssSource() {
        return assSource;
    }

    public void setAssSource(String assSource) {
        this.assSource = assSource;
    }

    public Boolean getIsOne() {
        return isOne;
    }

    public void setIsOne(Boolean isOne) {
        this.isOne = isOne;
    }

    public String getAddInfo() {
        return addInfo;
    }

    public void setAddInfo(String addInfo) {
        this.addInfo = addInfo;
    }

    public String getAssSN() {
        return assSN;
    }

    public void setAssSN(String assSN) {
        this.assSN = assSN;
    }

    public String getDepreciation() {
        return depreciation;
    }

    public void setDepreciation(String depreciation) {
        this.depreciation = depreciation;
    }

    public Boolean getIsPrint() {
        return isPrint;
    }

    public void setIsPrint(Boolean isPrint) {
        this.isPrint = isPrint;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getInDate() {
        return inDate;
    }

    public void setInDate(String inDate) {
        this.inDate = inDate;
    }

    public String getBuyDate() {
        return buyDate;
    }

    public void setBuyDate(String buyDate) {
        this.buyDate = buyDate;
    }

    public String getBookDate() {
        return bookDate;
    }

    public void setBookDate(String bookDate) {
        this.bookDate = bookDate;
    }

    public String getFinancialCode() {
        return financialCode;
    }

    public void setFinancialCode(String financialCode) {
        this.financialCode = financialCode;
    }

    public String getCreatePeople() {
        return createPeople;
    }

    public void setCreatePeople(String createPeople) {
        this.createPeople = createPeople;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCareMan() {
        return careMan;
    }

    public void setCareMan(String careMan) {
        this.careMan = careMan;
    }

    public String getOldCareMan() {
        return oldCareMan;
    }

    public void setOldCareMan(String oldCareMan) {
        this.oldCareMan = oldCareMan;
    }

    public String getUsePeople() {
        return usePeople;
    }

    public void setUsePeople(String usePeople) {
        this.usePeople = usePeople;
    }

    public String getBelongGroup() {
        return belongGroup;
    }

    public void setBelongGroup(String belongGroup) {
        this.belongGroup = belongGroup;
    }

    public String getUseGroup() {
        return useGroup;
    }

    public void setUseGroup(String useGroup) {
        this.useGroup = useGroup;
    }

    public String getOldUseGroup() {
        return oldUseGroup;
    }

    public void setOldUseGroup(String oldUseGroup) {
        this.oldUseGroup = oldUseGroup;
    }

    public String getUseCompany() {
        return useCompany;
    }

    public void setUseCompany(String useCompany) {
        this.useCompany = useCompany;
    }

    public String getOldUseCompany() {
        return oldUseCompany;
    }

    public void setOldUseCompany(String oldUseCompany) {
        this.oldUseCompany = oldUseCompany;
    }

    public String getUseDept() {
        return useDept;
    }

    public void setUseDept(String useDept) {
        this.useDept = useDept;
    }

    public String getOldUseDept() {
        return oldUseDept;
    }

    public void setOldUseDept(String oldUseDept) {
        this.oldUseDept = oldUseDept;
    }

    public String getStoreAddress() {
        return storeAddress;
    }

    public void setStoreAddress(String storeAddress) {
        this.storeAddress = storeAddress;
    }

    public String getOldStoreAddress() {
        return oldStoreAddress;
    }

    public void setOldStoreAddress(String oldStoreAddress) {
        this.oldStoreAddress = oldStoreAddress;
    }

    public String getUseDate() {
        return useDate;
    }

    public void setUseDate(String useDate) {
        this.useDate = useDate;
    }

    public String getUpdatePeople() {
        return updatePeople;
    }

    public void setUpdatePeople(String updatePeople) {
        this.updatePeople = updatePeople;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public Boolean getIsDel() {
        return isDel;
    }

    public void setIsDel(Boolean isDel) {
        this.isDel = isDel;
    }

    public String getSysUUID() {
        return sysUUID;
    }

    public void setSysUUID(String sysUUID) {
        this.sysUUID = sysUUID;
    }

    public String getExtend1() {
        return extend1;
    }

    public void setExtend1(String extend1) {
        this.extend1 = extend1;
    }

    public String getExtend3() {
        return extend3;
    }

    public void setExtend3(String extend3) {
        this.extend3 = extend3;
    }

    public String getExtend2() {
        return extend2;
    }

    public void setExtend2(String extend2) {
        this.extend2 = extend2;
    }

    public String getExtend4() {
        return extend4;
    }

    public void setExtend4(String extend4) {
        this.extend4 = extend4;
    }

    public String getExtend5() {
        return extend5;
    }

    public void setExtend5(String extend5) {
        this.extend5 = extend5;
    }

	public Address getAddressInfo() {
		return addressInfo;
	}

	public void setAddressInfo(Address addressInfo) {
		this.addressInfo = addressInfo;
	}

	@Override
	public String toString() {
		return String
				.format("AssetStorage [ barCode=%s, codeIndex=%s, infoCode=%s, assName=%s, oldName=%s, simpName=%s, classCode=%s, className=%s, assImg=%s, assType=%s, facType=%s, assPrice=%s, assUnit=%s, assNum=%s, assBrand=%s, facName=%s, provider=%s, isCheck=%s, checkCyc=%s, checkDate=%s, nextCheckDate=%s, brokenTime=%s, assSource=%s, isOne=%s, addInfo=%s, assSN=%s, depreciation=%s, isPrint=%s, state=%s, inDate=%s, buyDate=%s, bookDate=%s, financialCode=%s, createPeople=%s, createTime=%s, careMan=%s, oldCareMan=%s, usePeople=%s, belongGroup=%s, useGroup=%s, oldUseGroup=%s, useCompany=%s, oldUseCompany=%s, useDept=%s, oldUseDept=%s, storeAddress=%s, addressInfo=%s, oldStoreAddress=%s, useDate=%s, updatePeople=%s, updateTime=%s, isDel=%s, sysUUID=%s, extend1=%s, extend3=%s, extend2=%s, extend4=%s, extend5=%s, proInfo=%s, peoInfo=%s, bGroupInfo=%s, groupInfo=%s, companyInfo=%s, deptInfo=%s, createInfo=%s, updateInfo=%s, careUser=%s, usePeopleEntity=%s, address=%s]",
						barCode, codeIndex, infoCode,
						assName, oldName, simpName, classCode, className,
						assImg, assType, facType, assPrice, assUnit, assNum,
						assBrand, facName, provider, isCheck, checkCyc,
						checkDate, nextCheckDate, brokenTime, assSource, isOne,
						addInfo, assSN, depreciation, isPrint, state, inDate,
						buyDate, bookDate, financialCode, createPeople,
						createTime, careMan, oldCareMan, usePeople,
						belongGroup, useGroup, oldUseGroup, useCompany,
						oldUseCompany, useDept, oldUseDept, storeAddress,
						addressInfo, oldStoreAddress, useDate, updatePeople,
						updateTime, isDel, sysUUID, extend1, extend3, extend2,
						extend4, extend5, proInfo, peoInfo, bGroupInfo,
						groupInfo, companyInfo, deptInfo, createInfo,
						updateInfo, careUser, usePeopleEntity, address);
	}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AssetStorage storage = (AssetStorage) o;
        return Objects.equals(assetUUID, storage.assetUUID) &&
                Objects.equals(rowID, storage.rowID) &&
                Objects.equals(barCode, storage.barCode) &&
                Objects.equals(codeIndex, storage.codeIndex) &&
                Objects.equals(infoCode, storage.infoCode) &&
                Objects.equals(assName, storage.assName) &&
                Objects.equals(oldName, storage.oldName) &&
                Objects.equals(simpName, storage.simpName) &&
                Objects.equals(classCode, storage.classCode) &&
                Objects.equals(className, storage.className) &&
                Objects.equals(assImg, storage.assImg) &&
                Objects.equals(assType, storage.assType) &&
                Objects.equals(facType, storage.facType) &&
                Objects.equals(assPrice, storage.assPrice) &&
                Objects.equals(assUnit, storage.assUnit) &&
                Objects.equals(assNum, storage.assNum) &&
                Objects.equals(assBrand, storage.assBrand) &&
                Objects.equals(facName, storage.facName) &&
                Objects.equals(provider, storage.provider) &&
                Objects.equals(isCheck, storage.isCheck) &&
                Objects.equals(checkCyc, storage.checkCyc) &&
                Objects.equals(checkDate, storage.checkDate) &&
                Objects.equals(nextCheckDate, storage.nextCheckDate) &&
                Objects.equals(brokenTime, storage.brokenTime) &&
                Objects.equals(assSource, storage.assSource) &&
                Objects.equals(isOne, storage.isOne) &&
                Objects.equals(addInfo, storage.addInfo) &&
                Objects.equals(assSN, storage.assSN) &&
                Objects.equals(depreciation, storage.depreciation) &&
                Objects.equals(isPrint, storage.isPrint) &&
                Objects.equals(state, storage.state) &&
                Objects.equals(inDate, storage.inDate) &&
                Objects.equals(buyDate, storage.buyDate) &&
                Objects.equals(bookDate, storage.bookDate) &&
                Objects.equals(financialCode, storage.financialCode) &&
                Objects.equals(createPeople, storage.createPeople) &&
                Objects.equals(createTime, storage.createTime) &&
                Objects.equals(careMan, storage.careMan) &&
                Objects.equals(oldCareMan, storage.oldCareMan) &&
                Objects.equals(usePeople, storage.usePeople) &&
                Objects.equals(belongGroup, storage.belongGroup) &&
                Objects.equals(useGroup, storage.useGroup) &&
                Objects.equals(oldUseGroup, storage.oldUseGroup) &&
                Objects.equals(useCompany, storage.useCompany) &&
                Objects.equals(oldUseCompany, storage.oldUseCompany) &&
                Objects.equals(useDept, storage.useDept) &&
                Objects.equals(oldUseDept, storage.oldUseDept) &&
                Objects.equals(storeAddress, storage.storeAddress) &&
                Objects.equals(addressInfo, storage.addressInfo) &&
                Objects.equals(oldStoreAddress, storage.oldStoreAddress) &&
                Objects.equals(useDate, storage.useDate) &&
                Objects.equals(updatePeople, storage.updatePeople) &&
                Objects.equals(updateTime, storage.updateTime) &&
                Objects.equals(isDel, storage.isDel) &&
                Objects.equals(sysUUID, storage.sysUUID) &&
                Objects.equals(extend1, storage.extend1) &&
                Objects.equals(extend3, storage.extend3) &&
                Objects.equals(extend2, storage.extend2) &&
                Objects.equals(extend4, storage.extend4) &&
                Objects.equals(extend5, storage.extend5) &&
                Objects.equals(proInfo, storage.proInfo) &&
                Objects.equals(peoInfo, storage.peoInfo) &&
                Objects.equals(bGroupInfo, storage.bGroupInfo) &&
                Objects.equals(groupInfo, storage.groupInfo) &&
                Objects.equals(companyInfo, storage.companyInfo) &&
                Objects.equals(deptInfo, storage.deptInfo) &&
                Objects.equals(createInfo, storage.createInfo) &&
                Objects.equals(updateInfo, storage.updateInfo) &&
                Objects.equals(careUser, storage.careUser) &&
                Objects.equals(usePeopleEntity, storage.usePeopleEntity) &&
                Objects.equals(address, storage.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(assetUUID, rowID, barCode, codeIndex, infoCode, assName, oldName, simpName, classCode, className, assImg, assType, facType, assPrice, assUnit, assNum, assBrand, facName, provider, isCheck, checkCyc, checkDate, nextCheckDate, brokenTime, assSource, isOne, addInfo, assSN, depreciation, isPrint, state, inDate, buyDate, bookDate, financialCode, createPeople, createTime, careMan, oldCareMan, usePeople, belongGroup, useGroup, oldUseGroup, useCompany, oldUseCompany, useDept, oldUseDept, storeAddress, addressInfo, oldStoreAddress, useDate, updatePeople, updateTime, isDel, sysUUID, extend1, extend3, extend2, extend4, extend5, proInfo, peoInfo, bGroupInfo, groupInfo, companyInfo, deptInfo, createInfo, updateInfo, careUser, usePeopleEntity, address);
    }
}