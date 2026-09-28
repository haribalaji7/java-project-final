package com.bloodlink.service;

import com.bloodlink.dto.BloodGroupStatsDTO;
import com.bloodlink.entity.BloodGroup;
import com.bloodlink.repository.BloodGroupRepository;
import com.bloodlink.repository.DonorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BloodGroupService {

    private final DonorRepository donorRepository;
    private final BloodGroupRepository bloodGroupRepository;

    /**
     * Calculates statistics by fetching dynamic blood groups from the database, 
     * removing hardcoded lists.
     */
    @Transactional(readOnly = true)
    public List<BloodGroupStatsDTO> getBloodGroupStats() {
        return bloodGroupRepository.findAll().stream().map(bg -> {
            long total = donorRepository.countByBloodGroup(bg.getName());
            long available = donorRepository.countAvailableByBloodGroup(bg.getName());
            return new BloodGroupStatsDTO(bg.getName(), available, total);
        }).collect(Collectors.toList());
    }
}
