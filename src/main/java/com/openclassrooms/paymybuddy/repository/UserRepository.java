package com.openclassrooms.paymybuddy.repository;

import com.openclassrooms.paymybuddy.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
//    User findByEmail(String email);
//    User findByUsername(String username);
//    boolean existsByEmail(String email);
//    boolean existsByUsername(String username);
}
