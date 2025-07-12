package com.logistic.orders.repository;

import com.logistic.orders.entity.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AssignmentRepository extends JpaRepository<Assignment, UUID> {

}