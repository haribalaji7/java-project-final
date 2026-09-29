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

    @org.springframework.data.jpa.repository.EntityGraph(attributePaths = {"bloodGroup"})
    List<Donor> findAll();

    boolean existsByPhone(String phone);

    // Find donors by blood group and city
    @Query("SELECT d FROM Donor d JOIN FETCH d.bloodGroup WHERE d.bloodGroup.name = :bloodGroup AND d.city = :city")
    List<Donor> findByBloodGroupAndCity(@Param("bloodGroup") String bloodGroup, @Param("city") String city);

    // Find only available donors
    @Query("SELECT d FROM Donor d JOIN FETCH d.bloodGroup WHERE d.available = true")
    List<Donor> findAvailableDonors();

    // Combination: Find available donors by blood group and city
    @Query("SELECT d FROM Donor d JOIN FETCH d.bloodGroup WHERE d.available = true " +
           "AND d.bloodGroup.name = :bloodGroup " +
           "AND d.city = :city " +
           "AND (d.lastDonationDate IS NULL OR d.lastDonationDate <= :eligibilityDate) " +
           "ORDER BY d.lastDonationDate DESC NULLS LAST")
    List<Donor> findAvailableDonorsByBloodGroupAndCity(@Param("bloodGroup") String bloodGroup, @Param("city") String city, @Param("eligibilityDate") LocalDate eligibilityDate);

    // Count donors by blood group
    @Query("SELECT COUNT(d) FROM Donor d WHERE d.bloodGroup.name = :bloodGroup")
    long countByBloodGroup(@Param("bloodGroup") String bloodGroup);
    
    // Count available donors by blood group
    @Query("SELECT COUNT(d) FROM Donor d WHERE d.bloodGroup.name = :bloodGroup AND d.available = true")
    long countAvailableByBloodGroup(@Param("bloodGroup") String bloodGroup);

    // Optimized bulk update for resetting availability
    @Modifying
    @Query("UPDATE Donor d SET d.available = true WHERE d.available = false AND d.lastDonationDate <= :eligibilityDate")
    int autoUpdateAvailability(@Param("eligibilityDate") LocalDate eligibilityDate);

    long countByAvailableTrue();

    long countByAvailableFalse();

    @Query("SELECT new com.bloodlink.dto.GroupCountDTO(d.bloodGroup.name, COUNT(d)) FROM Donor d GROUP BY d.bloodGroup.name")
    List<com.bloodlink.dto.GroupCountDTO> countDonorsByBloodGroup();

    @Query("SELECT new com.bloodlink.dto.GroupCountDTO(d.city, COUNT(d)) FROM Donor d GROUP BY d.city")
    List<com.bloodlink.dto.GroupCountDTO> countDonorsByCity();
}
