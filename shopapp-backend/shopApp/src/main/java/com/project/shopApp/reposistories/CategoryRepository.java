package com.project.shopApp.reposistories;

import com.project.shopApp.models.Category;
import com.project.shopApp.models.OrderDetails;
import com.project.shopApp.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Long> {
    //find the order details for 1 order
    //Select * from Category where order_id = ?
    Optional<Category> findById(Long categoryId);

}
