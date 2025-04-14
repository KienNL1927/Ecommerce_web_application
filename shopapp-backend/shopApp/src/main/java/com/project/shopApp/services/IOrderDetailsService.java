package com.project.shopApp.services;

import com.project.shopApp.dtos.OrderDetailsDTO;
import com.project.shopApp.exceptions.DataNotFoundException;
import com.project.shopApp.models.OrderDetails;
import com.project.shopApp.responses.OrderDetailResponse;

import java.util.List;

public interface IOrderDetailsService {
    OrderDetails createOrderDetails(OrderDetailsDTO orderDetailsDTO) throws Exception;

    OrderDetails updateOrderDetails(Long id, OrderDetailsDTO orderDetailsDTO) throws Exception;

    void deleteOrderDetails(Long id);

    OrderDetails getOrderDetail(Long orderDetailId) throws DataNotFoundException;

    List<OrderDetails> findByOrderId(Long orderId);
}
