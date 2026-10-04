package com.java8.features;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ProductService {

    //@Autowired
    private ProductClient client;
    /*
    * Merge two differet object list into unified object list
    */
     public List<UnifiedProduct> getProducts(){
        Set<Product> productDetails = client.getProducts();
        Set<ProductPrice> productPrices = client.getProductPrice();

        Map<String, ProductPrice> productPriceMap = productPrices.stream().collect(Collectors.toMap(ProductPrice :: getProduct_uid ,  price -> price));
        return productDetails.stream().map(details -> {
            ProductPrice productPrice = productPriceMap.get(details.getProductUid());

            UnifiedProduct unifiedProduct = new UnifiedProduct();
            unifiedProduct.setName(details.getName());
            unifiedProduct.setProductType(details.getProductType());
            unifiedProduct.setProductUid(details.getProductUid());
            unifiedProduct.setUrl(details.getUrl());
            
            if (productPrice != null) {
                unifiedProduct.setUnitPrice(productPrice.getUnitPrice());
                unifiedProduct.setUnitMesurePrice(productPrice.getUnitMesurePrice());
                unifiedProduct.setUnitMeasureAmount(productPrice.getUnitMeasureAmount());
            }
            return unifiedProduct;
        }).collect(Collectors.toList());
    }

}
