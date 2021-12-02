package com.ta.kfc.mercu.dto.item;

public class AddAssetDto {
    private Long roId;
    private Long assetId;

    public Long getRoId() {
        return roId;
    }

    public void setRoId(Long roId) {
        this.roId = roId;
    }

    public Long getAssetId() {
        return assetId;
    }

    public void setAssetId(Long assetId) {
        this.assetId = assetId;
    }
}
