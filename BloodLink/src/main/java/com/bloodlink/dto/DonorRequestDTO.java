package com.bloodlink.dto;

import com.bloodlink.validation.ValidBloodGroup;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DonorRequestDTO {

    @NotBlank(message = "Name cannot be blank")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String name;

    @NotBlank(message = "Phone number cannot be blank")
    @Pattern(regexp = "^[6-9]\\d{9}$", message = "Phone number must be a valid 10-digit Indian number")
    private String phone;

    @NotBlank(message = "City cannot be blank")
    @Size(min = 2, max = 100, message = "City name must be valid")
    private String city;

    @NotBlank(message = "Blood group cannot be blank")
    @ValidBloodGroup(message = "Blood group does not exist in the database")
    private String bloodGroup;
}
