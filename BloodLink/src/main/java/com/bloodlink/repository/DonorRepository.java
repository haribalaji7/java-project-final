package com.bloodlink.repository;

import com.bloodlink.entity.Donor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DonorRepository extends JpaRepository<Donor, Long> {

    // Find donors by blood group and city
    @Query("SELECT d FROM Donor d WHERE d.bloodGroup = :bloodGroup AND d.city = :city")
    List<Donor> findByBloodGroupAndCity(@Param("bloodGroup") String bloodGroup, @Param("city") String city);

    // Find only available donors
    @Query("SELECT d FROM Donor d WHERE d.available = true")
    List<Donor> findAvailableDonors();

    // Combination: Find available donors by blood group and city
    @Query("SELECT d FROM Donor d WHERE d.available = true AND d.bloodGroup = :bloodGroup AND d.city = :city")
    List<Donor> findAvailableDonorsByBloodGroupAndCity(@Param("bloodGroup") String bloodGroup, @Param("city") String city);

    // Count donors by blood group
    @Query("SELECT COUNT(d) FROM Donor d WHERE d.bloodGroup = :bloodGroup")
    long countByBloodGroup(@Param("bloodGroup") String bloodGroup);
    
    // Count available donors by blood group
    @Query("SELECT COUNT(d) FROM Donor d WHERE d.bloodGroup = :bloodGroup AND d.available = true")
    long countAvailableByBloodGroup(@Param("bloodGroup") String bloodGroup);

    // Optimized bulk update for resetting availability
    @Modifying
    @Query("UPDATE Donor d SET d.available = true WHERE d.available = false AND d.lastDonationDate <= :eligibilityDate")
    int autoUpdateAvailability(@Param("eligibilityDate") LocalDate eligibilityDate);
}
