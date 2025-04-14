package com.project.shopApp.reposistories;

import com.project.shopApp.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByPhoneNumber(String phoneNumber);

    //Select * FROM users where phoneNumber = ?
    Optional<User> findByPhoneNumber(String phoneNumber);
}
