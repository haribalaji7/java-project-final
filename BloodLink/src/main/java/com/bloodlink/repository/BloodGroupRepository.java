package com.bloodlink.repository;

import com.bloodlink.entity.BloodGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BloodGroupRepository extends JpaRepository<BloodGroup, Long> {
    boolean existsByName(String name);
    java.util.Optional<BloodGroup> findByName(String name);
}
