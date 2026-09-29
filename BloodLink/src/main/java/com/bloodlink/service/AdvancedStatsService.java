package com.bloodlink.service;

import com.bloodlink.dto.*;
import com.bloodlink.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdvancedStatsService {
    private final DonorRepository donorRepository;
    private final DonationRecordRepository donationRecordRepository;

    public CountDTO getTotalDonors() { return new CountDTO(donorRepository.count()); }
    public CountDTO getAvailableDonors() { return new CountDTO(donorRepository.countByAvailableTrue()); }
    public CountDTO getUnavailableDonors() { return new CountDTO(donorRepository.countByAvailableFalse()); }
    public List<GroupCountDTO> getDonorsPerBloodGroup() { return donorRepository.countDonorsByBloodGroup(); }
    public List<GroupCountDTO> getDonorsPerCity() { return donorRepository.countDonorsByCity(); }
    public CountDTO getDonationsThisMonth() {
        LocalDate now = LocalDate.now();
        LocalDate start = now.withDayOfMonth(1);
        LocalDate end = now.withDayOfMonth(now.lengthOfMonth());
        return new CountDTO(donationRecordRepository.countDonationsThisMonth(start, end));
    }
    public StringValueDTO getMostRequestedBloodGroup() {
        List<String> groups = donationRecordRepository.findMostRequestedBloodGroups(PageRequest.of(0, 1));
        return new StringValueDTO(groups.isEmpty() ? null : groups.get(0));
    }
    public StatsSummaryDTO getSummary() {
        LocalDate now = LocalDate.now();
        LocalDate start = now.withDayOfMonth(1);
        LocalDate end = now.withDayOfMonth(now.lengthOfMonth());
        
        return StatsSummaryDTO.builder()
                .totalDonors(donorRepository.count())
                .availableDonors(donorRepository.countByAvailableTrue())
                .totalDonations(donationRecordRepository.count())
                .donationsThisMonth(donationRecordRepository.countDonationsThisMonth(start, end))
                .build();
    }
}