package fixedass.xiangrong.com.fixedass.bean;

import java.io.Serializable;
import java.util.List;

public class Function implements Serializable {
    private String funcUUID;

    private Integer rowID;
    
    private String pFuncUUID;
    
	private Function pFunction; //父功能，自关联

	private String menuUUID;

    private String funcName;

    private String funcURL;

    private String modelName;

    private String sysUUID;

    private Integer orderNo;

    private String creatorID;

    private String createTime;
    
    private List<Function> list;	//用来存放所有子功能的集合
    
    public List<Function> getList() {
		return list;
	}

	public void setList(List<Function> list) {
		this.list = list;
	}

	public String getpFuncUUID() {
		return pFuncUUID;
	}

	public void setpFuncUUID(String pFuncUUID) {
		this.pFuncUUID = pFuncUUID;
	}
    
    public Function getpFunction() {
		return pFunction;
	}

	public void setpFunction(Function pFunction) {
		this.pFunction = pFunction;
	}

    public String getFuncUUID() {
        return funcUUID;
    }

    public void setFuncUUID(String funcUUID) {
        this.funcUUID = funcUUID;
    }

    public Integer getRowID() {
        return rowID;
    }

    public void setRowID(Integer rowID) {
        this.rowID = rowID;
    }

    public String getMenuUUID() {
        return menuUUID;
    }

    public void setMenuUUID(String menuUUID) {
        this.menuUUID = menuUUID;
    }

    public String getFuncName() {
        return funcName;
    }

    public void setFuncName(String funcName) {
        this.funcName = funcName;
    }

    public String getFuncURL() {
        return funcURL;
    }

    public void setFuncURL(String funcURL) {
        this.funcURL = funcURL;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getSysUUID() {
        return sysUUID;
    }

    public void setSysUUID(String sysUUID) {
        this.sysUUID = sysUUID;
    }

    public Integer getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(Integer orderNo) {
        this.orderNo = orderNo;
    }

    public String getCreatorID() {
        return creatorID;
    }

    public void setCreatorID(String creatorID) {
        this.creatorID = creatorID;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}