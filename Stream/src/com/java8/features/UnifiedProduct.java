package com.java8.features;

public class UnifiedProduct {

    private String productUid;
    private String productType;
    private String name;
    private String url;
    private Double unitPrice;
    private Double unitMesurePrice;
    private Double unitMeasureAmount;

    
    public void setProductUid(String productUid) {
        this.productUid = productUid;
    }
    public void setProductType(String productType) {
        this.productType = productType;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }
    public void setUnitMesurePrice(Double unitMesurePrice) {
        this.unitMesurePrice = unitMesurePrice;
    }
    public void setUnitMeasureAmount(Double unitMeasureAmount) {
        this.unitMeasureAmount = unitMeasureAmount;
    }
    public String getProductUid() {
        return productUid;
    }
    public String getProductType() {
        return productType;
    }
    public String getName() {
        return name;
    }
    public String getUrl() {
        return url;
    }
    public Double getUnitPrice() {
        return unitPrice;
    }
    public Double getUnitMesurePrice() {
        return unitMesurePrice;
    }
    public Double getUnitMeasureAmount() {
        return unitMeasureAmount;
    }

}
