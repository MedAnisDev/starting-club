package com.example.startingclubbackend.repository;

import com.example.startingclubbackend.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface RoleRepository extends JpaRepository<Role ,Long> {

    @Query(value = "Select r from Role r where r.name= : name")
    Optional<Role> fetchRoleByName(@Param("role") String name) ;
}
