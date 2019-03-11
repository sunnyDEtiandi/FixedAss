package fixedass.xiangrong.com.fixedass.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AssetOperate<T> implements Serializable {
    private String operUUID;

    private String operbillCode;
    
    private Integer operbillIndex;

    private User user;				//创建人
    
    private String createID;		//创建人id
    
	private String createDeptID;	//创建部门id
	
	private String updateID;		//修改人id
	
	private User updateUser;
	
	private String updateDate;
	
	private String updatetime;
    
	private String createdate;

    private String createtime;

    private Integer ischeck;

    private String examID;			//报审人id
    
    private User examUser;			//报审人

    private String examdate;

    private String examtime;

    private String examnote;
    
    private String reviewID;		//审核人id
    
    private User reviewUser;

    private String reviewdate;

    private String reviewtime;

    private String operNote;

    private String opertype;

    private String reviewnote;

    private String billpre;
    
    private Integer state;
    
    private String sysUUID;
    
    public String getSysUUID() {
		return sysUUID;
	}

	public void setSysUUID(String sysUUID) {
		this.sysUUID = sysUUID;
	}

	public User getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(User updateUser) {
		this.updateUser = updateUser;
	}

	public User getReviewUser() {
		return reviewUser;
	}

	public void setReviewUser(User reviewUser) {
		this.reviewUser = reviewUser;
	}

	private List<T> list = new ArrayList<T>(); //添加主从表一对多的关系
    
    private List<AssetFix> fixList = new ArrayList<AssetFix>(); //一对多的关系资产维修
    
    private List<AssetTest> checkList = new ArrayList<AssetTest>(); //一对多的关系资产维修
    
    private List<AssetScrap> scrapList = new ArrayList<AssetScrap>(); //一对多的关系资产报废
    
	public List<AssetScrap> getScrapList() {
		return scrapList;
	}

	public void setScrapList(List<AssetScrap> scrapList) {
		this.scrapList = scrapList;
	}

	public List<AssetTest> getCheckList() {
		return checkList;
	}

	public void setCheckList(List<AssetTest> checkList) {
		this.checkList = checkList;
	}

	public List<AssetFix> getFixList() {
		return fixList;
	}

	public void setFixList(List<AssetFix> fixList) {
		this.fixList = fixList;
	}
	
	public User getExamUser() {
		return examUser;
	}

	public void setExamUser(User examUser) {
		this.examUser = examUser;
	}

	public String getExamID() {
		return examID;
	}

	public void setExamID(String examID) {
		this.examID = examID;
	}

	public String getExamdate() {
		return examdate;
	}

	public void setExamdate(String examdate) {
		this.examdate = examdate;
	}

	public String getExamtime() {
		return examtime;
	}

	public void setExamtime(String examtime) {
		this.examtime = examtime;
	}

	public String getExamnote() {
		return examnote;
	}

	public void setExamnote(String examnote) {
		this.examnote = examnote;
	}

	public String getUpdateID() {
		return updateID;
	}

	public void setUpdateID(String updateID) {
		this.updateID = updateID;
	}

	public String getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}

	public String getUpdatetime() {
		return updatetime;
	}

	public void setUpdatetime(String updatetime) {
		this.updatetime = updatetime;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public List<T> getList() {
		return list;
	}

	public void setList(List<T> list) {
		this.list = list;
	}

	public String getOperUUID() {
        return operUUID;
    }

    public void setOperUUID(String operUUID) {
        this.operUUID = operUUID;
    }

    public String getOperbillCode() {
        return operbillCode;
    }

    public void setOperbillCode(String operbillCode) {
        this.operbillCode = operbillCode;
    }

    public Integer getOperbillIndex() {
        return operbillIndex;
    }

    public void setOperbillIndex(Integer operbillIndex) {
        this.operbillIndex = operbillIndex;
    }

    public String getCreatedate() {
        return createdate;
    }

    public void setCreatedate(String createdate) {
        this.createdate = createdate;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public Integer getIscheck() {
        return ischeck;
    }

    public void setIscheck(Integer ischeck) {
        this.ischeck = ischeck;
    }

    public String getReviewID() {
        return reviewID;
    }

    public void setReviewID(String reviewID) {
        this.reviewID = reviewID;
    }

    public String getReviewdate() {
        return reviewdate;
    }

    public void setReviewdate(String reviewdate) {
        this.reviewdate = reviewdate;
    }

    public String getReviewtime() {
        return reviewtime;
    }

    public void setReviewtime(String reviewtime) {
        this.reviewtime = reviewtime;
    }

    public String getOperNote() {
        return operNote;
    }

    public void setOperNote(String operNote) {
        this.operNote = operNote;
    }

    public String getOpertype() {
        return opertype;
    }

    public void setOpertype(String opertype) {
        this.opertype = opertype;
    }

    public String getReviewnote() {
        return reviewnote;
    }

    public void setReviewnote(String reviewnote) {
        this.reviewnote = reviewnote;
    }

    public String getBillpre() {
        return billpre;
    }

    public void setBillpre(String billpre) {
        this.billpre = billpre;
    }
    
    public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

    public String getCreateDeptID() {
		return createDeptID;
	}

	public void setCreateDeptID(String createDeptID) {
		this.createDeptID = createDeptID;
	}
	
	public String getCreateID() {
		return createID;
	}

	public void setCreateID(String createID) {
		this.createID = createID;
	}

	@Override
	public String toString() {
		return String
				.format("AssetOperate [operUUID=%s, operbillCode=%s, operbillIndex=%s, user=%s, createID=%s, createDeptID=%s, updateID=%s, updateUser=%s, updateDate=%s, updatetime=%s, createdate=%s, createtime=%s, ischeck=%s, examID=%s, examUser=%s, examdate=%s, examtime=%s, examnote=%s, reviewID=%s, reviewUser=%s, reviewdate=%s, reviewtime=%s, operNote=%s, opertype=%s, reviewnote=%s, billpre=%s, state=%s, sysUUID=%s, list=%s, fixList=%s, checkList=%s, scrapList=%s]",
						operUUID, operbillCode, operbillIndex, user, createID,
						createDeptID, updateID, updateUser, updateDate,
						updatetime, createdate, createtime, ischeck, examID,
						examUser, examdate, examtime, examnote, reviewID,
						reviewUser, reviewdate, reviewtime, operNote, opertype,
						reviewnote, billpre, state, sysUUID, list, fixList,
						checkList, scrapList);
	}
	
}