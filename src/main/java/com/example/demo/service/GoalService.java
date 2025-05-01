package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import com.example.demo.model.Goal;

public interface GoalService {
    List<Goal> findAll();
    Optional<Goal> findById(String id);
    Goal save(Goal goal);
    Goal update(String id, Goal goal);
    void delete(String id);
}