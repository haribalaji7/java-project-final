package com.bloodlink.dto;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DonationResponseDTO {
    private Long id;
    private Long donorId;
    private String donorName;
    private String bloodGroup;
    private LocalDate donationDate;
    private LocalDate nextEligibleDate;
}
