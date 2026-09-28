package com.bloodlink.service;

import com.bloodlink.dto.DonorRequestDTO;
import com.bloodlink.dto.DonorResponseDTO;
import com.bloodlink.entity.Donor;
import com.bloodlink.exception.ResourceNotFoundException;
import com.bloodlink.repository.DonorRepository;
import com.bloodlink.util.BloodLinkConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DonorService {

    private final DonorRepository donorRepository;

    /**
     * Registers a new donor into the system. Default availability is true.
     */
    @Transactional
    public DonorResponseDTO registerDonor(DonorRequestDTO request) {
        Donor donor = Donor.builder()
                .name(request.getName())
                .phone(request.getPhone())
                .city(request.getCity())
                .bloodGroup(request.getBloodGroup())
                .available(true)
                .build();

        Donor savedDonor = donorRepository.save(donor);
        return mapToResponseDTO(savedDonor);
    }

    /**
     * Searches for currently available donors by city and blood group.
     */
    @Transactional(readOnly = true)
    public List<DonorResponseDTO> searchAvailableDonors(String city, String bloodGroup) {
        List<Donor> donors = donorRepository.findAvailableDonorsByBloodGroupAndCity(bloodGroup, city);
        return donors.stream().map(this::mapToResponseDTO).collect(Collectors.toList());
    }

    /**
     * Retrieves all registered donors (both available and unavailable).
     */
    @Transactional(readOnly = true)
    public List<DonorResponseDTO> getAllDonors() {
        return donorRepository.findAll().stream().map(this::mapToResponseDTO).collect(Collectors.toList());
    }

    /**
     * Internal method to fetch the Donor entity, throwing a custom exception if not found.
     */
    public Donor getDonorEntity(Long id) {
        return donorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Donor not found with ID: " + id));
    }

    /**
     * Modifies the donor's availability status and updates their last donation date.
     */
    @Transactional
    public void updateDonorAvailability(Donor donor, boolean isAvailable, LocalDate lastDonationDate) {
        donor.setAvailable(isAvailable);
        donor.setLastDonationDate(lastDonationDate);
        donorRepository.save(donor);
    }

    /**
     * Automatically enables donors after their cooldown period.
     * Uses an optimized bulk update query rather than fetching all records into memory.
     * Runs daily at midnight.
     */
    @Scheduled(cron = "0 0 0 * * ?") 
    @Transactional
    public void autoEnableDonors() {
        LocalDate eligibilityDate = LocalDate.now().minusDays(BloodLinkConstants.COOLDOWN_DAYS);
        int updatedCount = donorRepository.autoUpdateAvailability(eligibilityDate);
        // In a real application, you would log this metric: log.info("Auto-enabled {} donors", updatedCount);
    }

    private DonorResponseDTO mapToResponseDTO(Donor donor) {
        return DonorResponseDTO.builder()
                .id(donor.getId())
                .name(donor.getName())
                .phone(donor.getPhone())
                .city(donor.getCity())
                .bloodGroup(donor.getBloodGroup())
                .available(donor.isAvailable())
                .lastDonationDate(donor.getLastDonationDate())
                .build();
    }
}
