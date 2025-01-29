package org.example.Client;

import org.example.Entity.Product;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "PRODUCT-DETAILS-SERVICE")
public interface ProductDetailsServiceClient {
    @PostMapping("/productdetails")
    void addProduct(@RequestBody Product product);

    @DeleteMapping("/productdetails/{id}")
    void removeProduct(@PathVariable String id);

    @GetMapping("/productdetails")
    ResponseEntity<List<Product>> getAllProducts();
}
