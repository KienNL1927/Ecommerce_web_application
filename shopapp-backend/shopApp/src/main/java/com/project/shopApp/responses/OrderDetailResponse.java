package com.project.shopApp.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.shopApp.models.Order;
import com.project.shopApp.models.OrderDetails;
import com.project.shopApp.models.Product;
import jakarta.persistence.*;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class OrderDetailResponse {
    private Long id;

    @JsonProperty("order_id")
    private Long orderId;

    @JsonProperty("product_id")
    private Long productId;

    @JsonProperty("price")
    private Float price;

    @JsonProperty("number_of_products")
    private Long numberOfProducts;

    @JsonProperty("total_money")
    private Float totalMoney;

    private String color;

    public static OrderDetailResponse fromOrderDetail(OrderDetails orderDetails) {
        return OrderDetailResponse.builder()
                .id(orderDetails.getId())
                .orderId(orderDetails.getId())
                .productId(orderDetails.getProduct().getId())
                .price(orderDetails.getPrice())
                .numberOfProducts(orderDetails.getNumberOfProducts())
                .totalMoney(orderDetails.getTotalMoney())
                .color(orderDetails.getColor())
                .build();
    }
}
