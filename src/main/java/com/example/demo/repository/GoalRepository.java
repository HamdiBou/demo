package com.example.demo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.demo.model.Goal;

import java.util.List;

public interface GoalRepository extends MongoRepository<Goal, String> {
    List<Goal> findAllByUserId(String userId);
}
