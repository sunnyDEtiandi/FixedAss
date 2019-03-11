package fixedass.xiangrong.com.fixedass.bean;

import java.io.Serializable;

public class AssetScrap implements Serializable {
    private String scrapUUID;

    private String operbillCode;

    private String barCode;			//资产编码
    
    private AssetStorage storage;	//资产对象

    private String unuseListNote;

    private String unuseInfo;

    private String operPeople;

    private String scrapDate;

    private String scrapTime;
    
    private String sysUUID;

    public AssetScrap() { }

    public AssetScrap(String barCode, String unuseListNote, String unuseInfo, String scrapDate, String sysUUID) {
        this.barCode = barCode;
        this.unuseListNote = unuseListNote;
        this.unuseInfo = unuseInfo;
        this.scrapDate = scrapDate;
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

	public String getScrapUUID() {
        return scrapUUID;
    }

    public void setScrapUUID(String scrapUUID) {
        this.scrapUUID = scrapUUID;
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

    public String getUnuseListNote() {
        return unuseListNote;
    }

    public void setUnuseListNote(String unuseListNote) {
        this.unuseListNote = unuseListNote;
    }

    public String getUnuseInfo() {
        return unuseInfo;
    }

    public void setUnuseInfo(String unuseInfo) {
        this.unuseInfo = unuseInfo;
    }

    public String getOperPeople() {
        return operPeople;
    }

    public void setOperPeople(String operPeople) {
        this.operPeople = operPeople;
    }

    public String getScrapDate() {
        return scrapDate;
    }

    public void setScrapDate(String scrapDate) {
        this.scrapDate = scrapDate;
    }

    public String getScrapTime() {
        return scrapTime;
    }

    public void setScrapTime(String scrapTime) {
        this.scrapTime = scrapTime;
    }

    @Override
    public String toString() {
        return "AssetScrap{" +
                "scrapUUID='" + scrapUUID + '\'' +
                ", operbillCode='" + operbillCode + '\'' +
                ", barCode='" + barCode + '\'' +
                ", unuseListNote='" + unuseListNote + '\'' +
                ", unuseInfo='" + unuseInfo + '\'' +
                ", operPeople='" + operPeople + '\'' +
                ", scrapDate='" + scrapDate + '\'' +
                ", scrapTime='" + scrapTime + '\'' +
                ", sysUUID='" + sysUUID + '\'' +
                ", storage=" + storage +
                '}';
    }
}