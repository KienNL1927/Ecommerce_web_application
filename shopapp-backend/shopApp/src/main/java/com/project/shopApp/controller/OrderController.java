package com.project.shopApp.controller;

import com.project.shopApp.dtos.OrderDTO;
import com.project.shopApp.dtos.ProductDTO;
import com.project.shopApp.models.Order;
import com.project.shopApp.reposistories.OrderRepository;
import com.project.shopApp.responses.OrderResponse;
import com.project.shopApp.services.IOrderService;
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
@RequestMapping("${api.prefix}/orders")
@RequiredArgsConstructor

public class OrderController {
    private final IOrderService orderService;
    @PostMapping("")
    public ResponseEntity<?> order(@RequestBody @Valid OrderDTO orderDTO,
                                   BindingResult result){
        try{
            if (result.hasErrors()) {
                List<String> erorrMessage = result.getFieldErrors().stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(erorrMessage);
            }
            Order order = orderService.createOrder(orderDTO);
            return ResponseEntity.ok().body(order);
        }
        catch(Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/user/{user_id}") //Take all order from 1 user with userId = ?
    public ResponseEntity<?> getOrders(@Valid @PathVariable("user_id") Long userID){
        try{
            List<Order> orderList = orderService.findOrderByUserId(userID);
            return ResponseEntity.ok(orderList);
        }
        catch(Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/{id}") // Take the details of order with id
    public ResponseEntity<?> getOrder(@Valid @PathVariable("id") Long orderId){
        try{
            Order existingOrder = orderService.getOrder(orderId);
            return ResponseEntity.ok(existingOrder);
        }
        catch(Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrder(@Valid @PathVariable("id") Long id,
                                         @Valid @RequestBody OrderDTO orderDTO){
        try {
            Order order = orderService.updateOrder(id, orderDTO);
            return ResponseEntity.ok(order);
        } catch(Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteOrder(@Valid @PathVariable("id") Long id){
        //Deleted by update active = false, not completely delete the order from the database
        orderService.deleteOrder(id);
        return ResponseEntity.ok("Order deleted successfully");
    }
}
