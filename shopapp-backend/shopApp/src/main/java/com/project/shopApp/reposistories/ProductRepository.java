package com.project.shopApp.reposistories;

import com.project.shopApp.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
    boolean existsByName(String name);

    //Select * fron product
    Page<Product> findAll(Pageable pageable);
}
