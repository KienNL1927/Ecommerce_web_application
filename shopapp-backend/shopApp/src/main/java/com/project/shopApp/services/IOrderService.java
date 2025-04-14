package com.project.shopApp.services;

import com.project.shopApp.dtos.CategoryDTO;
import com.project.shopApp.dtos.OrderDTO;
import com.project.shopApp.exceptions.DataNotFoundException;
import com.project.shopApp.models.Category;
import com.project.shopApp.models.Order;
import com.project.shopApp.responses.OrderResponse;

import java.util.List;

public interface IOrderService {
    Order createOrder(OrderDTO orderDTO) throws Exception;

    Order getOrder(Long id);

    Order updateOrder(Long id, OrderDTO orderDTO) throws DataNotFoundException;

    void deleteOrder(Long id);

    List<Order> findOrderByUserId(Long userId);
}
