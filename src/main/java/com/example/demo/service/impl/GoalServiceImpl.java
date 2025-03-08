package com.example.demo.service.impl;

import com.example.demo.model.Goal;
import com.example.demo.repository.GoalRepository;
import com.example.demo.service.GoalService;
import com.example.demo.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class GoalServiceImpl implements GoalService {

    private final GoalRepository goalRepository;

    @Autowired
    public GoalServiceImpl(GoalRepository goalRepository) {
        this.goalRepository = goalRepository;
    }

    @Override
    public List<Goal> findAllByUserId(String userId) {
        return goalRepository.findAllByUserId(userId);
    }

    @Override
    public Optional<Goal> findById(String id) {
        return goalRepository.findById(id);
    }

    @Override
    public Goal save(Goal goal) {
        goal.setCreatedAt(LocalDateTime.now());
        goal.setUpdatedAt(LocalDateTime.now());
        return goalRepository.save(goal);
    }

    @Override
    public Goal update(String id, Goal goal) {
        Goal existingGoal = goalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Goal not found"));
        
        existingGoal.setTitle(goal.getTitle());
        existingGoal.setDescription(goal.getDescription());
        existingGoal.setTargetDate(goal.getTargetDate());
        existingGoal.setProgress(goal.getProgress());
        existingGoal.setStatus(goal.getStatus());
        existingGoal.setTaskIds(goal.getTaskIds());
        existingGoal.setCategory(goal.getCategory());
        existingGoal.setUpdatedAt(LocalDateTime.now());
        
        return goalRepository.save(existingGoal);
    }

    @Override
    public void delete(String id) {
        if (!goalRepository.existsById(id)) {
            throw new ResourceNotFoundException("Goal not found");
        }
        goalRepository.deleteById(id);
    }
}
