package com.example.demo.userRelationships.dao;

import com.example.demo.userRelationships.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author 86188
 */
@Repository
public interface UserDao extends JpaRepository<User, Long> {
    User findByUserId(Long userId);
}
