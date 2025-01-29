package org.example.Controller;

import feign.FeignException;
import org.example.Client.InventoryServiceClient;
import org.example.Client.ProductDetailsServiceClient;
import org.example.Entity.InventoryRequest;
import org.example.Entity.Product;
import org.example.Entity.ProductResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {
    private final ProductDetailsServiceClient productServiceClient;
    private final InventoryServiceClient inventoryServiceClient;

    public ProductController(ProductDetailsServiceClient productServiceClient, InventoryServiceClient inventoryServiceClient) {
        this.productServiceClient = productServiceClient;
        this.inventoryServiceClient = inventoryServiceClient;
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        try{
            ResponseEntity<List<Product>> allProducts = productServiceClient.getAllProducts();
            List<ProductResponse> productDetailsList = new ArrayList<>();
            List<Product> productsList = allProducts.getBody();
            if(productsList.size() > 0) {
                for (Product product: productsList){
                    ProductResponse.ProductResponseBuilder productDetailsBuilder= ProductResponse.builder().id(product.getId())
                            .name(product.getName())
                            .design(product.getDesign())
                            .size(product.getSize())
                            .price(product.getPrice());
                    ResponseEntity<Integer> stock = inventoryServiceClient.getStock(product.getId());
                    if(stock.getStatusCode() == HttpStatus.OK) {
                        productDetailsBuilder.count(stock.getBody());
                        productDetailsList.add(productDetailsBuilder.build());
                    }
                }
            }
            return ResponseEntity.ok(new ArrayList<>(productDetailsList));

        } catch (FeignException ex){
            return ResponseEntity.badRequest().body(new ArrayList<>());
        }
    }

    @PostMapping
    public ResponseEntity<String> addProduct(@RequestBody ProductResponse productDetails) {

        try {
            Product product = Product.builder().id(productDetails.getId())
                    .name(productDetails.getName())
                    .size(productDetails.getSize())
                    .design(productDetails.getDesign())
                    .price(productDetails.getPrice())
                    .build();
            productServiceClient.addProduct(product);
            inventoryServiceClient.addStock(InventoryRequest.builder()
                    .productId(productDetails.getId())
                    .quantity(productDetails.getCount()).build());
            return ResponseEntity.ok("Product added successfully");
        } catch (FeignException ex){
            return ResponseEntity.badRequest().body("Downstream Service Failed with Exception" + ex.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> removeProduct(@PathVariable String id) {
        try {
            productServiceClient.removeProduct(id);
            inventoryServiceClient.removeProduct(id);
            return ResponseEntity.ok("Product removed successfully");
        } catch (FeignException ex) {
            return ResponseEntity.badRequest().body("Downstream Service Failed with Exception" + ex.getMessage());
        }
    }
}