package com.example.startingclubbackend.repository;

import com.example.startingclubbackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User ,Long> {

    @Query(value = "Select u from User u where u.email= :email ")
    Optional<User> fetchUserWithEmail(@Param("email") String email) ;
}
