package com.example.startingclubbackend.repository;

import com.example.startingclubbackend.model.user.Athlete;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AthleteRepository extends JpaRepository<Athlete , Long> {
}
