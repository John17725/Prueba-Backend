package com.logistic.orders.repository;

import com.logistic.orders.entity.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DriverRepository extends JpaRepository<Driver, UUID> {
    List<Driver> findAllByActiveTrue();
    boolean existsByLicenseNumber(String licenseNumber);
}