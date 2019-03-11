package fixedass.xiangrong.com.fixedass.bean;

import java.io.Serializable;

public class AssetTestQuery extends AssetTest implements Serializable {
	
	private AssetOperate<AssetTest> assetOperate;
	private Boolean selected;

	public AssetOperate<AssetTest> getAssetOperate() {
		return assetOperate;
	}
	public void setAssetOperate(AssetOperate<AssetTest> assetOperate) {
		this.assetOperate = assetOperate;
	}

	public Boolean getSelected() {
		return selected;
	}

	public void setSelected(Boolean selected) {
		this.selected = selected;
	}

	@Override
	public String toString() {
		return "AssetTestQuery{" +
				"assetOperate=" + assetOperate +
				'}';
	}
}
