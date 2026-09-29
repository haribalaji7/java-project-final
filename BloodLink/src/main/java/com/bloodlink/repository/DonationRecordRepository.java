package com.bloodlink.repository;

import com.bloodlink.entity.DonationRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DonationRecordRepository extends JpaRepository<DonationRecord, Long> {
    
    @org.springframework.data.jpa.repository.EntityGraph(attributePaths = {"donor", "donor.bloodGroup"})
    List<DonationRecord> findByDonorId(Long donorId);

    @Query("SELECT COUNT(r) FROM DonationRecord r WHERE r.donationDate >= :startOfMonth AND r.donationDate <= :endOfMonth")
    long countDonationsThisMonth(@org.springframework.data.repository.query.Param("startOfMonth") java.time.LocalDate startOfMonth, @org.springframework.data.repository.query.Param("endOfMonth") java.time.LocalDate endOfMonth);

    @Query("SELECT r.donor.bloodGroup.name FROM DonationRecord r GROUP BY r.donor.bloodGroup.name ORDER BY COUNT(r) DESC")
    List<String> findMostRequestedBloodGroups(org.springframework.data.domain.Pageable pageable);

    @org.springframework.data.jpa.repository.EntityGraph(attributePaths = {"donor", "donor.bloodGroup"})
    List<DonationRecord> findByDonorIdOrderByDonationDateDesc(Long donorId);

    @org.springframework.data.jpa.repository.EntityGraph(attributePaths = {"donor", "donor.bloodGroup"})
    DonationRecord findFirstByOrderByDonationDateDesc();

    @org.springframework.data.jpa.repository.EntityGraph(attributePaths = {"donor", "donor.bloodGroup"})
    List<DonationRecord> findByDonationDateBetweenOrderByDonationDateDesc(java.time.LocalDate startDate, java.time.LocalDate endDate);
    
    @org.springframework.data.jpa.repository.EntityGraph(attributePaths = {"donor", "donor.bloodGroup"})
    List<DonationRecord> findAll(org.springframework.data.domain.Sort sort);
}
