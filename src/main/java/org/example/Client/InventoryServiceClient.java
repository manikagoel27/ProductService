package org.example.Client;

import org.example.Entity.InventoryRequest;
import org.example.Entity.Product;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "INVENTORY-SERVICE")
public interface InventoryServiceClient {
    @GetMapping("/inventory/{productId}")
    ResponseEntity<Integer> getStock(@PathVariable("productId") String productId);
    @PostMapping("/inventory/addStock")
    ResponseEntity<String> addStock(@RequestBody InventoryRequest inventoryRequest);
    @DeleteMapping("/inventory/{productId}")
    ResponseEntity<String> removeProduct(@PathVariable String productId);
}
