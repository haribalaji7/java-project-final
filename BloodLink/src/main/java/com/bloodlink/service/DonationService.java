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
                .build();
        
        DonationRecord savedRecord = donationRecordRepository.save(record);

        // Update the donor's availability (mark them as unavailable)
        donorService.updateDonorAvailability(donor, false, donationDate);

        return DonationResponseDTO.builder()
                .id(savedRecord.getId())
                .donorId(donor.getId())
                .donorName(donor.getName())
                .bloodGroup(donor.getBloodGroup())
                .donationDate(savedRecord.getDonationDate())
                .nextEligibleDate(savedRecord.getDonationDate().plusDays(BloodLinkConstants.COOLDOWN_DAYS))
                .build();
    }
}
