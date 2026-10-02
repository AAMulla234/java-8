package com.java8.features;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ProductService {

    @Autowired
    private ProductClient client;
    /*
    * Merge two differet object list into unified object list
    */
    public List<UnifiedProduct> getProducts(){
        Set<Product> productDetails = client.getProducts();
        Set<ProductPrice> productPrices = client.getProductPrice();

        Map<String, ProductPrice> productPriceMap = productPrices.stream().collect(Collectors.toMap(ProductPrice :: getProduct_uid ,  price -> price));
        return productDetails.stream().map(details -> {
            ProductPrice productPrice = productPriceMap.get(details.getProduct_uid());

            UnifiedProduct unifiedProduct = new UnifiedProduct();
            unifiedProduct.setName(details.getName());
            unifiedProduct.setProductType(details.getProduct_type());
            unifiedProduct.setProductUid(details.getProduct_uid());
            unifiedProduct.setUrl(details.getFull_url());
            
            if (productPrice != null) {
                unifiedProduct.setUnitPrice(productPrice.getUnit_price());
                unifiedProduct.setUnitMesurePrice(productPrice.getUnit_price_measure());
                unifiedProduct.setUnitMeasureAmount(productPrice.getUnit_price_measure_amount());
            }
            return unifiedProduct;
        }).collect(Collectors.toList());
    }

}
