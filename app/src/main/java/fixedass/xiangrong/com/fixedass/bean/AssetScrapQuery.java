package fixedass.xiangrong.com.fixedass.bean;

import java.io.Serializable;

public class AssetScrapQuery extends AssetScrap implements Serializable {
	
	private AssetOperate<AssetScrap> assetOperate;
	private Boolean selected;

	public AssetOperate<AssetScrap> getAssetOperate() {
		return assetOperate;
	}
	public void setAssetOperate(AssetOperate<AssetScrap> assetOperate) {
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
		return "AssetScrapQuery{" +
				"assetOperate=" + assetOperate +
				'}';
	}
}
