package com.project.shopApp.reposistories;

import com.project.shopApp.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    //find the order of 1 user -- Select * from order where user_id = ?
    List<Order> findByUserId(Long id);
}
