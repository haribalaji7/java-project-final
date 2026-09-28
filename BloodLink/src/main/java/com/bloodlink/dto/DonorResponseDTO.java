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
public class DonorResponseDTO {
    private Long id;
    private String name;
    private String phone;
    private String city;
    private String bloodGroup;
    private boolean available;
    private LocalDate lastDonationDate;
}
