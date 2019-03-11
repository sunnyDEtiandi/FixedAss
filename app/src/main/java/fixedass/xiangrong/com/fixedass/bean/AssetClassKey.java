package fixedass.xiangrong.com.fixedass.bean;

import java.io.Serializable;

public class AssetClassKey implements Serializable {
	private String classCode;

    private String sysUUID;

    public String getClassCode() {
        return classCode;
    }

    public void setClassCode(String classCode) {
        this.classCode = classCode;
    }

    public String getSysUUID() {
        return sysUUID;
    }

    public void setSysUUID(String sysUUID) {
        this.sysUUID = sysUUID;
    }
}