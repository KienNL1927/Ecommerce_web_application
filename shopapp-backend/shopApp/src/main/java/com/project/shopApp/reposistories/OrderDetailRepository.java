package com.project.shopApp.reposistories;

import com.project.shopApp.models.OrderDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderDetailRepository extends JpaRepository<OrderDetails, Long> {
    //find the order details for 1 order
    //Select * from OrderDetails where order_id = ?
    List<OrderDetails> findByOrderId(Long orderId);

}
