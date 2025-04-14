package com.project.shopApp.responses;

import com.project.shopApp.models.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor

public class ProductListResponse {
    private List<ProductResponse> products;
    private int totalPages;
}
