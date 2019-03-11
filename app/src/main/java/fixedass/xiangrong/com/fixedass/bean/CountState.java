package fixedass.xiangrong.com.fixedass.bean;

import java.io.Serializable;

public class CountState implements Serializable {
	private String state;
	private int num;
	
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	
	@Override
	public String toString() {
		return "CountState [state=" + state + ", num=" + num + "]";
	}
}
