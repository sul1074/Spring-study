package com.example.demo.dto;

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
