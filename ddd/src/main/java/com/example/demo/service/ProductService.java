package com.example.demo.service;

import com.example.demo.domain.Product;
import com.example.demo.dto.ProductCreateRequest;
import com.example.demo.dto.ProductUpdateRequest;

import java.util.List;
import java.util.UUID;

public interface ProductService {

    Product create(ProductCreateRequest request);

    Product getById(UUID productId);

    List<Product> getAll();

    Product update(UUID productId, ProductUpdateRequest request);

    void delete(UUID productId);
}
