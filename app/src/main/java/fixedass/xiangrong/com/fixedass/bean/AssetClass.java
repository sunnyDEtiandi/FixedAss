package fixedass.xiangrong.com.fixedass.bean;

import java.io.Serializable;

public class AssetClass extends AssetClassKey implements Serializable {
    private String parentClassCode;

    private String className;

    private String classIden;

    private String classUnit;

    private Integer brokenTime;

    private Integer checkCyc;

    private Boolean isCheck;

    private String classType;

    private Integer classIndex;

    public String getParentClassCode() {
        return parentClassCode;
    }

    public void setParentClassCode(String parentClassCode) {
        this.parentClassCode = parentClassCode;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getClassIden() {
        return classIden;
    }

    public void setClassIden(String classIden) {
        this.classIden = classIden;
    }

    public String getClassUnit() {
        return classUnit;
    }

    public void setClassUnit(String classUnit) {
        this.classUnit = classUnit;
    }

    public Integer getBrokenTime() {
        return brokenTime;
    }

    public void setBrokenTime(Integer brokenTime) {
        this.brokenTime = brokenTime;
    }

    public Integer getCheckCyc() {
        return checkCyc;
    }

    public void setCheckCyc(Integer checkCyc) {
        this.checkCyc = checkCyc;
    }

    public Boolean getIsCheck() {
        return isCheck;
    }

    public void setIsCheck(Boolean isCheck) {
        this.isCheck = isCheck;
    }

    public String getClassType() {
        return classType;
    }

    public void setClassType(String classType) {
        this.classType = classType;
    }

    public Integer getClassIndex() {
        return classIndex;
    }

    public void setClassIndex(Integer classIndex) {
        this.classIndex = classIndex;
    }
}