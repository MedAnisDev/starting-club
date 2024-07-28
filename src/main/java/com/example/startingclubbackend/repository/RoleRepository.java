package com.example.startingclubbackend.repository;

import com.example.startingclubbackend.model.role.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Repository
@Transactional(readOnly = true)
public interface RoleRepository extends JpaRepository<Role ,Long> {

    @Query(value = "Select r from Role r where r.name =:name")
    Optional<Role> fetchRoleByName(@Param("name") String name) ;
}
