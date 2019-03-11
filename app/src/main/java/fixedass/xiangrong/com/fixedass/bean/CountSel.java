package fixedass.xiangrong.com.fixedass.bean;

public class CountSel {
    private boolean isCheck;
    private String countBillCode;
    private String cPeoInfo;
    private String createDate;
    private String countNote;

    public CountSel(String countBillCode, String cPeoInfo, String createDate, String countNote) {
        this.countBillCode = countBillCode;
        this.cPeoInfo = cPeoInfo;
        this.createDate = createDate;
        this.countNote = countNote;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public String getCountBillCode() {
        return countBillCode;
    }

    public void setCountBillCode(String countBillCode) {
        this.countBillCode = countBillCode;
    }

    public String getcPeoInfo() {
        return cPeoInfo;
    }

    public void setcPeoInfo(String cPeoInfo) {
        this.cPeoInfo = cPeoInfo;
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

    @Override
    public String toString() {
        return "CountSel{" +
                "isCheck=" + isCheck +
                ", countBillCode='" + countBillCode + '\'' +
                ", cPeoInfo='" + cPeoInfo + '\'' +
                ", createDate='" + createDate + '\'' +
                ", countNote='" + countNote + '\'' +
                '}';
    }
}
