package com.project.shopApp.controller;

import com.project.shopApp.dtos.OrderDTO;
import com.project.shopApp.dtos.OrderDetailsDTO;
import com.project.shopApp.dtos.ProductDTO;
import com.project.shopApp.exceptions.DataNotFoundException;
import com.project.shopApp.models.OrderDetails;
import com.project.shopApp.responses.OrderDetailResponse;
import com.project.shopApp.services.IOrderDetailsService;
import com.project.shopApp.services.IOrderService;
import com.project.shopApp.services.OrderDetailService;
import com.project.shopApp.services.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.util.StringUtils;
import org.springframework.validation.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.naming.Binding;
import java.io.*;
import java.nio.*;
import java.text.Bidi;
import java.util.*;

@RestController
@RequestMapping("${api.prefix}/order_details")
@RequiredArgsConstructor

public class OrderDetailsController {
    private final IOrderDetailsService orderDetailsService;

    @PostMapping("")
    public ResponseEntity<?> createOrderDetails(@RequestBody @Valid OrderDetailsDTO newOrderDTO,
                                                BindingResult result){
        try{
            if (result.hasErrors()) {
                List<String> erorrMessage = result.getFieldErrors().stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(erorrMessage);
            }
            OrderDetails newOrderDetails = orderDetailsService.createOrderDetails(newOrderDTO);
            return ResponseEntity.ok().body(OrderDetailResponse.fromOrderDetail(newOrderDetails));
        }
        catch(Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderDetail(@Valid @PathVariable("id") Long id) throws DataNotFoundException {
        OrderDetails orderDetail = orderDetailsService.getOrderDetail(id);
        return ResponseEntity.ok(OrderDetailResponse.fromOrderDetail(orderDetail));
    }

    //Get the list of order_details from 1 order
    @GetMapping("/order/{orderId}")
    public ResponseEntity<?> getOrderDetails(@Valid @PathVariable("orderId") Long orderID){
        List<OrderDetails> orderDetails = orderDetailsService.findByOrderId(orderID);
        List<OrderDetailResponse> orderDetailResponses = orderDetails
                .stream()
                .map(OrderDetailResponse::fromOrderDetail)
                .toList();
        return ResponseEntity.ok(orderDetailResponses);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrderDetail (
            @Valid @PathVariable("id") Long id,
            @RequestBody @Valid OrderDetailsDTO orderDetailsDTO) throws Exception{
        OrderDetails orderDetail = orderDetailsService.updateOrderDetails(id, orderDetailsDTO);
        return ResponseEntity.ok("New Order Deteils Data: " + OrderDetailResponse.fromOrderDetail(orderDetail));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteOrderDetail(@Valid @PathVariable("id") Long id){
        orderDetailsService.deleteOrderDetails(id);
        return ResponseEntity.ok().body("Order Details with id: " + id + " Deleted Successfully");
    }
}
