package fixedass.xiangrong.com.fixedass.bean;

import java.io.Serializable;

public class AssetChangeQuery extends AssetChange implements Serializable {
	
	private AssetOperate<AssetChange> assetOperate;
	private Boolean selected;

	public AssetOperate<AssetChange> getAssetOperate() {
		return assetOperate;
	}
	public void setAssetOperate(AssetOperate<AssetChange> assetOperate) {
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
		return "AssetChangeQuery{" +
				"assetOperate=" + assetOperate +
				'}';
	}
}
