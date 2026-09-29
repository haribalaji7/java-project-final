package com.bloodlink.service;

import com.bloodlink.dto.DonationRequestDTO;
import com.bloodlink.dto.DonationResponseDTO;
import com.bloodlink.entity.DonationRecord;
import com.bloodlink.entity.Donor;
import com.bloodlink.exception.DonorUnavailableException;
import com.bloodlink.repository.DonationRecordRepository;
import com.bloodlink.util.BloodLinkConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class DonationService {

    private final DonationRecordRepository donationRecordRepository;
    private final DonorService donorService;

    /**
     * Records a successful blood donation and applies the 90-day cooldown constraint.
     */
    @Transactional
    public DonationResponseDTO recordDonation(DonationRequestDTO request) {
        // Fetch donor entity (throws ResourceNotFoundException if not found)
        Donor donor = donorService.getDonorEntity(request.getDonorId());
        
        LocalDate donationDate = request.getDonationDate() != null ? request.getDonationDate() : LocalDate.now();

        // Enforce the standard cooldown period
        if (donor.getLastDonationDate() != null) {
            long daysSinceLastDonation = ChronoUnit.DAYS.between(donor.getLastDonationDate(), donationDate);
            if (daysSinceLastDonation < BloodLinkConstants.COOLDOWN_DAYS) {
                long daysRemaining = BloodLinkConstants.COOLDOWN_DAYS - daysSinceLastDonation;
                throw new DonorUnavailableException("Donor is in cooldown period. Days remaining before eligible: " + daysRemaining);
            }
        }

        DonationRecord record = DonationRecord.builder()
                .donor(donor)
                .donationDate(donationDate)
                .hospitalName(request.getHospitalName())
                .doctorName(request.getDoctorName())
                .unitsDonated(request.getUnitsDonated())
                .remarks(request.getRemarks())
                .build();
        
        DonationRecord savedRecord = donationRecordRepository.save(record);

        // Update the donor's availability (mark them as unavailable)
        donorService.updateDonorAvailability(donor, false, donationDate);

        return mapToResponseDTO(savedRecord);
    }

    public java.util.List<DonationResponseDTO> getDonationHistory(Long donorId) {
        return donationRecordRepository.findByDonorIdOrderByDonationDateDesc(donorId).stream()
                .map(this::mapToResponseDTO)
                .collect(java.util.stream.Collectors.toList());
    }

    public java.util.List<DonationResponseDTO> getAllDonations() {
        return donationRecordRepository.findAll(org.springframework.data.domain.Sort.by(org.springframework.data.domain.Sort.Direction.DESC, "donationDate")).stream()
                .map(this::mapToResponseDTO)
                .collect(java.util.stream.Collectors.toList());
    }

    public DonationResponseDTO getLatestDonation() {
        DonationRecord record = donationRecordRepository.findFirstByOrderByDonationDateDesc();
        if (record == null) {
            throw new com.bloodlink.exception.ResourceNotFoundException("No donations found");
        }
        return mapToResponseDTO(record);
    }

    public java.util.List<DonationResponseDTO> getDonationsBetweenDates(LocalDate startDate, LocalDate endDate) {
        return donationRecordRepository.findByDonationDateBetweenOrderByDonationDateDesc(startDate, endDate).stream()
                .map(this::mapToResponseDTO)
                .collect(java.util.stream.Collectors.toList());
    }

    private DonationResponseDTO mapToResponseDTO(DonationRecord record) {
        return DonationResponseDTO.builder()
                .id(record.getId())
                .donorId(record.getDonor().getId())
                .donorName(record.getDonor().getName())
                .bloodGroup(record.getDonor().getBloodGroup().getName())
                .donationDate(record.getDonationDate())
                .nextEligibleDate(record.getDonationDate().plusDays(BloodLinkConstants.COOLDOWN_DAYS))
                .hospitalName(record.getHospitalName())
                .doctorName(record.getDoctorName())
                .unitsDonated(record.getUnitsDonated())
                .remarks(record.getRemarks())
                .createdAt(record.getCreatedAt())
                .build();
    }
}
