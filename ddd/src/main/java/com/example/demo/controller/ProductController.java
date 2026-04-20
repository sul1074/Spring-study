package com.example.demo.controller;

import com.example.demo.domain.Product;
import com.example.demo.dto.ProductCreateRequest;
import com.example.demo.dto.ProductUpdateRequest;
import com.example.demo.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<Product> create(@RequestBody ProductCreateRequest req) {
        Product product = productService.create(req);

        return ResponseEntity.status(HttpStatus.CREATED).body(product);
    }

    @GetMapping("/{productId}")
    public Product getById(@PathVariable UUID productId) {
        return productService.getById(productId);
    }

    @GetMapping
    public List<Product> getAll() {
        return productService.getAll();
    }

    @PutMapping("/{productId}")
    public Product update(@PathVariable UUID productId, @RequestBody ProductUpdateRequest req) {
        return productService.update(productId, req);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> delete(@PathVariable UUID productId) {
        productService.delete(productId);
        return ResponseEntity.noContent().build();
    }
}
