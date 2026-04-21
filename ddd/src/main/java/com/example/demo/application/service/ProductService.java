package com.example.demo.application.service;

import com.example.demo.application.port.in.ProductUseCase;
import com.example.demo.application.port.out.ProductPersistencePort;
import com.example.demo.domain.Product;
import com.example.demo.dto.ProductCreateRequest;
import com.example.demo.dto.ProductUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService implements ProductUseCase {

    private final ProductPersistencePort productPersistencePort;

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

        return productPersistencePort.save(product);
    }

    @Override
    public Product getById(UUID productId) {
        return getOrElseThrow(productId);
    }

    @Override
    public List<Product> getAll() {
        return productPersistencePort.findAll();
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

        return productPersistencePort.save(product);
    }

    @Override
    @Transactional
    public void delete(UUID productId) {
        Product product = getOrElseThrow(productId);
        productPersistencePort.delete(product);
    }

    private Product getOrElseThrow(UUID productId) {
        return productPersistencePort.findById(productId)
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
