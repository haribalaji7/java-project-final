package com.bloodlink.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BloodGroupStatsDTO {
    private String bloodGroup;
    private long availableDonors;
    private long totalDonors;
}
