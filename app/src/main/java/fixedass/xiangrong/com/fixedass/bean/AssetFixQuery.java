package fixedass.xiangrong.com.fixedass.bean;

import java.io.Serializable;

public class AssetFixQuery extends AssetFix implements Serializable {
	
	private AssetOperate<AssetFix> assetOperate;
	private Boolean selected;

	public AssetOperate<AssetFix> getAssetOperate() {
		return assetOperate;
	}
	public void setAssetOperate(AssetOperate<AssetFix> assetOperate) {
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
		return "AssetFixQuery{" +
				"assetOperate=" + assetOperate +
				'}';
	}
}
