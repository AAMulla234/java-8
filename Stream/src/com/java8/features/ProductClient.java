package com.java8.features;

import java.util.Set;

public class ProductClient {
    
    public Set<Product> getProducts() {
         String productURL = "https://s3.eu-west-1.amazonaws.com/hackajob-assets1.p.hackajob/challenges/sainsbury_products/products_v2.json";
    
         RestTemplate restTemplate = new RestTemplate(); 
         Product[] products = restTemplate.getForObject(productURL, Product[].class);
         Set<Product> productsSet = Set.of(products);
         return productsSet;
    }

    public Set<ProductPrice> getProductPrice() {
        String productPURL = "https://s3.eu-west-1.amazonaws.com/hackajob-assets1.p.hackajob/challenges/sainsbury_products/products_price_v2.json";
        RestTemplate restTemplate = new RestTemplate(); 
        ProductPrice[] productsPrice = restTemplate.getForObject(productPURL, ProductPrice[].class);
        Set<ProductPrice> productPSet = Set.of(productsPrice);
        return productPSet;
    }
}
