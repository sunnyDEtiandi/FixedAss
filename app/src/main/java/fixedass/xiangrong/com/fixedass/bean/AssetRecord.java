package fixedass.xiangrong.com.fixedass.bean;

import java.io.Serializable;
import java.util.List;

public class AssetRecord implements Serializable {
	
	private String operType;//单据类型
	
	private String createUser;//创建人
	
	private String createdate;//创建日期
	
	private String note;	//处理结果
	
	private String operbillCode;//订单编号
	
	private String barCode;//订单编号
	
	private List<String> operList; 	//处理详情

	public AssetRecord() { }

	public AssetRecord(String operType, String createUser, String createdate, String note, String operbillCode, String barCode) {
		this.operType = operType;
		this.createUser = createUser;
		this.createdate = createdate;
		this.note = note;
		this.operbillCode = operbillCode;
		this.barCode = barCode;
	}

	public String getBarCode() {
		return barCode;
	}

	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}

	public String getOperbillCode() {
		return operbillCode;
	}

	public void setOperbillCode(String operbillCode) {
		this.operbillCode = operbillCode;
	}

	public String getOperType() {
		return operType;
	}

	public void setOperType(String operType) {
		this.operType = operType;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public String getCreatedate() {
		return createdate;
	}

	public void setCreatedate(String createdate) {
		this.createdate = createdate;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public List<String> getOperList() {
		return operList;
	}

	public void setOperList(List<String> operList) {
		this.operList = operList;
	}

	@Override
	public String toString() {
		return "AssetRecord{" +
				"operType='" + operType + '\'' +
				", createUser='" + createUser + '\'' +
				", createdate='" + createdate + '\'' +
				", note='" + note + '\'' +
				", operbillCode='" + operbillCode + '\'' +
				", barCode='" + barCode + '\'' +
				", operList=" + operList +
				'}';
	}
}
