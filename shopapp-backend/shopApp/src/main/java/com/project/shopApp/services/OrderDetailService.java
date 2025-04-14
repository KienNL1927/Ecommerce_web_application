package com.project.shopApp.services;

import com.project.shopApp.dtos.OrderDetailsDTO;
import com.project.shopApp.exceptions.DataNotFoundException;
import com.project.shopApp.models.Order;
import com.project.shopApp.models.OrderDetails;
import com.project.shopApp.models.Product;
import com.project.shopApp.reposistories.OrderDetailRepository;
import com.project.shopApp.reposistories.OrderRepository;
import com.project.shopApp.reposistories.ProductRepository;
import com.project.shopApp.responses.OrderDetailResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor

public class OrderDetailService implements IOrderDetailsService {
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final ProductRepository productRepository;

    @Override
    public OrderDetails createOrderDetails(OrderDetailsDTO orderDetailsDTO)
            throws Exception {
        Order order = orderRepository.findById(orderDetailsDTO.getOrderID())
                .orElseThrow(() -> new Exception(
                        "Cannot find order with Id: " + orderDetailsDTO.getOrderID()));

        Product product = productRepository.findById(orderDetailsDTO.getOrderID())
                .orElseThrow(() -> new Exception(
                        "Cannot find order with Id: " + orderDetailsDTO.getOrderID()));
        OrderDetails orderDetails = OrderDetails.builder()
                .order(order)
                .product(product)
                .price(orderDetailsDTO.getPrice())
                .numberOfProducts(orderDetailsDTO.getNumberOfProduct())
                .totalMoney(orderDetailsDTO.getTotalMoney())
                .color(orderDetailsDTO.getColor())
                .build();
        //Save to database
        OrderDetails newOder = orderDetailRepository.save(orderDetails);
        return newOder;
    }

    @Override
    public OrderDetails updateOrderDetails(Long id, OrderDetailsDTO orderDetailsDTO) throws Exception {
        //find if order Detail exist
        OrderDetails existingOrderDetail = orderDetailRepository.findById(id).orElseThrow(
                () -> new DataNotFoundException("Cannot find Order details with id: " + id)
        );
        //Checking if order_id belongs to 1 order
        Order existingOrder = orderRepository.findById(orderDetailsDTO.getOrderID()).orElseThrow(
                () -> new DataNotFoundException("Cannot find Order with id: " + orderDetailsDTO.getOrderID()));
        //Checking if the product is exist
        Product existingproduct = productRepository.findById(orderDetailsDTO.getOrderID())
                .orElseThrow(() -> new Exception(
                        "Cannot find order with Id: " + orderDetailsDTO.getOrderID()));

        existingOrderDetail.setPrice(orderDetailsDTO.getPrice());
        existingOrderDetail.setNumberOfProducts(orderDetailsDTO.getNumberOfProduct());
        existingOrderDetail.setTotalMoney(orderDetailsDTO.getTotalMoney());
        existingOrderDetail.setColor(orderDetailsDTO.getColor());
        existingOrderDetail.setOrder(existingOrder);
        existingOrderDetail.setProduct(existingproduct);

        return orderDetailRepository.save(existingOrderDetail);
    }

    @Override
    public void deleteOrderDetails(Long id) {
        orderDetailRepository.deleteById(id);
    }

    @Override
    public OrderDetails getOrderDetail(Long orderDetailId) throws DataNotFoundException{
        return orderDetailRepository.findById(orderDetailId).orElseThrow(() ->
                new DataNotFoundException("Cannot find OrderDetail with id: " + orderDetailId));
    }

    @Override
    public List<OrderDetails> findByOrderId(Long orderId) {
        return orderDetailRepository.findByOrderId(orderId);
    }
}
