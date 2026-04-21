package com.example.demo.product.presentation.dto;

import java.math.BigDecimal;

public record ProductUpdateRequest(
        String name,
        String description,
        BigDecimal price,
        Integer stock,
        String status,
        String modifiedId
) {
}
