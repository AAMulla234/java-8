package com.java8.features;

public class ProductPrice {
    private String productUid;
    private Double unitPrice;
    private Double unitMesurePrice;
    private Double unitMeasureAmount;

    public String getProductUid() {
        return productUid;
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
    public void setProductUid(String productUid) {
        this.productUid = productUid;
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

    
}
