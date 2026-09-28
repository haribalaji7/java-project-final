package com.bloodlink.repository;

import com.bloodlink.entity.DonationRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DonationRecordRepository extends JpaRepository<DonationRecord, Long> {
    
    // Find all donation records for a specific donor
    List<DonationRecord> findByDonorId(Long donorId);
}
