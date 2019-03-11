package fixedass.xiangrong.com.fixedass.bean;

import java.io.Serializable;

public class AssetTest implements Serializable {
    private String testUUID;

    private String operbillCode;

    private String barCode;	//资产编码
    
    private AssetStorage storage;

    private String testCompany;

    private String testPeople;

    private String testDate;

    private String testTime;

    private Float testPrice;

    private String testListNote;

    private String operPeople;

    private String operDate;

    private String operTime;

    private String testState;

    private String testNextDate;

    private Integer isTest;
    
    private String sysUUID;

    public AssetTest() { }

    public AssetTest(String barCode, String testCompany, String testPeople, String testDate, Float testPrice, String testListNote, String testState, String sysUUID) {
        this.barCode = barCode;
        this.testCompany = testCompany;
        this.testPeople = testPeople;
        this.testDate = testDate;
        this.testPrice = testPrice;
        this.testListNote = testListNote;
        this.testState = testState;
        this.sysUUID = sysUUID;
    }

    public String getSysUUID() {
		return sysUUID;
	}

	public void setSysUUID(String sysUUID) {
		this.sysUUID = sysUUID;
	}

	public AssetStorage getStorage() {
		return storage;
	}

	public void setStorage(AssetStorage storage) {
		this.storage = storage;
	}

	public String getTestUUID() {
        return testUUID;
    }

    public void setTestUUID(String testUUID) {
        this.testUUID = testUUID;
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

    public String getTestCompany() {
        return testCompany;
    }

    public void setTestCompany(String testCompany) {
        this.testCompany = testCompany;
    }

    public String getTestPeople() {
        return testPeople;
    }

    public void setTestPeople(String testPeople) {
        this.testPeople = testPeople;
    }

    public String getTestDate() {
        return testDate;
    }

    public void setTestDate(String testDate) {
        this.testDate = testDate;
    }

    public String getTestTime() {
        return testTime;
    }

    public void setTestTime(String testTime) {
        this.testTime = testTime;
    }

    public Float getTestPrice() {
        return testPrice;
    }

    public void setTestPrice(Float testPrice) {
        this.testPrice = testPrice;
    }

    public String getTestListNote() {
        return testListNote;
    }

    public void setTestListNote(String testListNote) {
        this.testListNote = testListNote;
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

    public String getOperTime() {
        return operTime;
    }

    public void setOperTime(String operTime) {
        this.operTime = operTime;
    }

    public String getTestState() {
        return testState;
    }

    public void setTestState(String testState) {
        this.testState = testState;
    }

    public String getTestNextDate() {
        return testNextDate;
    }

    public void setTestNextDate(String testNextDate) {
        this.testNextDate = testNextDate;
    }

    public Integer getIsTest() {
        return isTest;
    }

    public void setIsTest(Integer isTest) {
        this.isTest = isTest;
    }

    @Override
    public String toString() {
        return "AssetTest{" +
                "testUUID='" + testUUID + '\'' +
                ", operbillCode='" + operbillCode + '\'' +
                ", barCode='" + barCode + '\'' +
                ", testCompany='" + testCompany + '\'' +
                ", testPeople='" + testPeople + '\'' +
                ", testDate='" + testDate + '\'' +
                ", testTime='" + testTime + '\'' +
                ", testPrice=" + testPrice +
                ", testListNote='" + testListNote + '\'' +
                ", operPeople='" + operPeople + '\'' +
                ", operDate='" + operDate + '\'' +
                ", operTime='" + operTime + '\'' +
                ", testState='" + testState + '\'' +
                ", testNextDate='" + testNextDate + '\'' +
                ", isTest=" + isTest +
                ", sysUUID='" + sysUUID + '\'' +
                ", storage=" + storage +
                '}';
    }
}