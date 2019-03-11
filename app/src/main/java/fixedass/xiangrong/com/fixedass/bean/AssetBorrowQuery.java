package fixedass.xiangrong.com.fixedass.bean;

import java.io.Serializable;

public class AssetBorrowQuery extends AssetBorrow implements Serializable{
	
	private AssetOperate<AssetBorrow> assetOperate;
	private Boolean selected;

	public AssetOperate<AssetBorrow> getAssetOperate() {
		return assetOperate;
	}

	public void setAssetOperate(AssetOperate<AssetBorrow> assetOperate) {
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
		return "AssetBorrowQuery{" +
				"assetOperate=" + assetOperate +
				'}';
	}
}
