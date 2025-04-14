package com.project.shopApp.services;


import com.project.shopApp.dtos.OrderDTO;
import com.project.shopApp.exceptions.DataNotFoundException;
import com.project.shopApp.models.Order;
import com.project.shopApp.models.OrderStatus;
import com.project.shopApp.models.User;
import com.project.shopApp.reposistories.OrderRepository;
import com.project.shopApp.reposistories.UserRepository;
import com.project.shopApp.responses.OrderResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.time.LocalDate;
@Service
@RequiredArgsConstructor
@Configuration

public class OrderService implements IOrderService{
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    @Override
    public Order createOrder(OrderDTO orderDTO) throws Exception {
        //find if the userId exist
        User existedUser = userRepository
                .findById(orderDTO.getUserID())
                .orElseThrow(() -> new DataNotFoundException(
                        "Cannot find user with id " + orderDTO.getUserID()));
        //convert OrderDTO to Order to insert to Database
        //Using Model Mapper library
        modelMapper.typeMap(OrderDTO.class, Order.class)
                .addMappings(mapper -> mapper.skip(Order::setId));
        Order order = new Order();
        modelMapper.map(orderDTO, order);
        order.setUser(existedUser);
        order.setOrderDate(new Date());
        order.setStatus(OrderStatus.PENDING);
        //Shipping date must be in the next few day from the current day\
        LocalDate shippingDate = orderDTO.getShippingDate()
                == null ? LocalDate.now() : orderDTO.getShippingDate();
        if(shippingDate.isBefore(LocalDate.now())){
            throw new DataNotFoundException("Date must be at least today");
        }
        order.setShippingDate(shippingDate);
        order.setActive(true);
        orderRepository.save(order);
        return order;
    }

    @Override
    public Order getOrder(Long id) {
        return orderRepository.findById(id).orElse(null);
    }

    @Override
    public Order updateOrder(Long id, OrderDTO orderDTO) throws DataNotFoundException {
        Order existingOrder = orderRepository.findById(id).orElseThrow(() ->
                new DataNotFoundException("Cannot find order with id: " + id));
        User existingUser = userRepository.findById(
                orderDTO.getUserID()).orElseThrow(() ->
                new DataNotFoundException("Cannot find user with id: " + id));

        modelMapper.typeMap(OrderDTO.class, Order.class)
                .addMappings(mapper -> mapper.skip(Order::setId));
        modelMapper.map(orderDTO, existingOrder);
        existingOrder.setUser(existingUser);
        return orderRepository.save(existingOrder);
    }

    @Override
    public void deleteOrder(Long id) {
        Order order = orderRepository.findById(id).orElse(null);
        //soft delete
        if(order != null){
            order.setActive(false);
            orderRepository.save(order);
        }

    }

    @Override
    public List<Order> findOrderByUserId(Long userId) {
        return orderRepository.findByUserId(userId);
    }
}
