package com.example.demo.service;

import com.example.demo.domain.Product;
import com.example.demo.dto.ProductCreateRequest;
import com.example.demo.dto.ProductUpdateRequest;
import com.example.demo.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    @Transactional
    public Product create(ProductCreateRequest request) {
        Product product = Product.create(
                toUuid(request.sellerId(), "sellerId"),
                request.name(),
                request.description(),
                request.price(),
                request.stock(),
                request.status(),
                toUuid(request.creatorId(), "creatorId")
        );

        return productRepository.save(product);
    }

    @Override
    public Product getById(UUID productId) {
        return getOrElseThrow(productId);
    }

    @Override
    public List<Product> getAll() {
        return productRepository.findAll();
    }

    @Override
    @Transactional
    public Product update(UUID productId, ProductUpdateRequest request) {
        Product product = getOrElseThrow(productId);

        product.update(
                request.name(),
                request.description(),
                request.price(),
                request.stock(),
                request.status(),
                toUuid(request.modifiedId(), "modifiedId")
        );

        return productRepository.save(product);
    }

    @Override
    @Transactional
    public void delete(UUID productId) {
        productRepository.deleteById(productId);
    }

    private Product getOrElseThrow(UUID productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("product not found"));
    }

    private UUID toUuid(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " is required");
        }

        try {
            return UUID.fromString(value);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(fieldName + " must be a valid UUID");
        }
    }
}
