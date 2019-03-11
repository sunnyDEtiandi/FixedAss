package fixedass.xiangrong.com.fixedass.bean;

import java.io.Serializable;

public class Provider implements Serializable {
    private String proUUID;

    private Integer rowId;

    private String proCode;

    private String proName;

    private String proPlace;

    private String proTaxNo;

    private String proPeople;

    private String proPhone;

    private String proBank;

    private String proAddInfo;

    private String proBankNo;

    private String sysUUID;

    private Boolean isDel;

    public String getProUUID() {
        return proUUID;
    }

    public void setProUUID(String proUUID) {
        this.proUUID = proUUID;
    }

    public Integer getRowId() {
        return rowId;
    }

    public void setRowId(Integer rowId) {
        this.rowId = rowId;
    }

    public String getProCode() {
        return proCode;
    }

    public void setProCode(String proCode) {
        this.proCode = proCode;
    }

    public String getProName() {
        return proName;
    }

    public void setProName(String proName) {
        this.proName = proName;
    }

    public String getProPlace() {
        return proPlace;
    }

    public void setProPlace(String proPlace) {
        this.proPlace = proPlace;
    }

    public String getProTaxNo() {
        return proTaxNo;
    }

    public void setProTaxNo(String proTaxNo) {
        this.proTaxNo = proTaxNo;
    }

    public String getProPeople() {
        return proPeople;
    }

    public void setProPeople(String proPeople) {
        this.proPeople = proPeople;
    }

    public String getProPhone() {
        return proPhone;
    }

    public void setProPhone(String proPhone) {
        this.proPhone = proPhone;
    }

    public String getProBank() {
        return proBank;
    }

    public void setProBank(String proBank) {
        this.proBank = proBank;
    }

    public String getProAddInfo() {
        return proAddInfo;
    }

    public void setProAddInfo(String proAddInfo) {
        this.proAddInfo = proAddInfo;
    }

    public String getProBankNo() {
        return proBankNo;
    }

    public void setProBankNo(String proBankNo) {
        this.proBankNo = proBankNo;
    }

    public String getSysUUID() {
        return sysUUID;
    }

    public void setSysUUID(String sysUUID) {
        this.sysUUID = sysUUID;
    }

    public Boolean getIsDel() {
        return isDel;
    }

    public void setIsDel(Boolean isDel) {
        this.isDel = isDel;
    }

	@Override
	public String toString() {
		return String
				.format("Provider [proUUID=%s, rowId=%s, proCode=%s, proName=%s, proPlace=%s, proTaxNo=%s, proPeople=%s, proPhone=%s, proBank=%s, proAddInfo=%s, proBankNo=%s, sysUUID=%s, isDel=%s]",
						proUUID, rowId, proCode, proName, proPlace, proTaxNo,
						proPeople, proPhone, proBank, proAddInfo, proBankNo,
						sysUUID, isDel);
	}
}