package com.bloodlink.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StatsSummaryDTO {
    private long totalDonors;
    private long availableDonors;
    private long totalDonations;
    private long donationsThisMonth;
}
