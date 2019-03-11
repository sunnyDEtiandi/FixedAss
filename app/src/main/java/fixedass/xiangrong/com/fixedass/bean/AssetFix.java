package fixedass.xiangrong.com.fixedass.bean;

import java.io.Serializable;

public class AssetFix implements Serializable {
    private String fixUUID;

    private String operbillCode;

    private String barCode;		//资产编码
    
    private AssetStorage storage;//资产对象

    private String fixInfo;

    private String fixCompany;

    private String fixPeople;

    private String fixTelephone;

    private String fixDate;

    private String fixTime;

    private Float fixPrice;

    private String fixListNote;

    private String operPeople;

    private String finishDate;

    private String finishTime;

    private String fixState;
    
    private String sysUUID;

    public AssetFix() { }

    public AssetFix(String barCode, String fixInfo, String fixCompany, String fixPeople, String fixTelephone,
                    String fixDate, Float fixPrice, String fixListNote, String fixState, String sysUUID) {
        this.barCode = barCode;
        this.fixInfo = fixInfo;
        this.fixCompany = fixCompany;
        this.fixPeople = fixPeople;
        this.fixTelephone = fixTelephone;
        this.fixDate = fixDate;
        this.fixPrice = fixPrice;
        this.fixListNote = fixListNote;
        this.fixState = fixState;
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

	public String getFixUUID() {
        return fixUUID;
    }

    public void setFixUUID(String fixUUID) {
        this.fixUUID = fixUUID;
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

    public String getFixInfo() {
        return fixInfo;
    }

    public void setFixInfo(String fixInfo) {
        this.fixInfo = fixInfo;
    }

    public String getFixCompany() {
        return fixCompany;
    }

    public void setFixCompany(String fixCompany) {
        this.fixCompany = fixCompany;
    }

    public String getFixPeople() {
        return fixPeople;
    }

    public void setFixPeople(String fixPeople) {
        this.fixPeople = fixPeople;
    }

    public String getFixTelephone() {
        return fixTelephone;
    }

    public void setFixTelephone(String fixTelephone) {
        this.fixTelephone = fixTelephone;
    }

    public String getFixDate() {
        return fixDate;
    }

    public void setFixDate(String fixDate) {
        this.fixDate = fixDate;
    }

    public String getFixTime() {
        return fixTime;
    }

    public void setFixTime(String fixTime) {
        this.fixTime = fixTime;
    }

    public Float getFixPrice() {
        return fixPrice;
    }

    public void setFixPrice(Float fixPrice) {
        this.fixPrice = fixPrice;
    }

    public String getFixListNote() {
        return fixListNote;
    }

    public void setFixListNote(String fixListNote) {
        this.fixListNote = fixListNote;
    }

    public String getOperPeople() {
        return operPeople;
    }

    public void setOperPeople(String operPeople) {
        this.operPeople = operPeople;
    }


    public String getFinishDate() {
		return finishDate;
	}

	public void setFinishDate(String finishDate) {
		this.finishDate = finishDate;
	}

	public String getFinishTime() {
		return finishTime;
	}

	public void setFinishTime(String finishTime) {
		this.finishTime = finishTime;
	}

	public String getFixState() {
        return fixState;
    }

    public void setFixState(String fixState) {
        this.fixState = fixState;
    }

    @Override
    public String toString() {
        return "AssetFix{" +
                "fixUUID='" + fixUUID + '\'' +
                ", operbillCode='" + operbillCode + '\'' +
                ", barCode='" + barCode + '\'' +
                ", storage=" + storage +
                ", fixInfo='" + fixInfo + '\'' +
                ", fixCompany='" + fixCompany + '\'' +
                ", fixPeople='" + fixPeople + '\'' +
                ", fixTelephone='" + fixTelephone + '\'' +
                ", fixDate='" + fixDate + '\'' +
                ", fixTime='" + fixTime + '\'' +
                ", fixPrice=" + fixPrice +
                ", fixListNote='" + fixListNote + '\'' +
                ", operPeople='" + operPeople + '\'' +
                ", finishDate='" + finishDate + '\'' +
                ", finishTime='" + finishTime + '\'' +
                ", fixState='" + fixState + '\'' +
                ", sysUUID='" + sysUUID + '\'' +
                '}';
    }
}