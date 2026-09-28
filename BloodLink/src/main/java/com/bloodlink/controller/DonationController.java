package com.bloodlink.controller;

import com.bloodlink.dto.DonationRequestDTO;
import com.bloodlink.dto.DonationResponseDTO;
import com.bloodlink.service.DonationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/donations")
@RequiredArgsConstructor
@Tag(name = "Donation Management", description = "APIs for recording blood donations")
public class DonationController {

    private final DonationService donationService;

    @PostMapping
    @Operation(summary = "Record a new blood donation")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Donation recorded successfully"),
            @ApiResponse(responseCode = "400", description = "Donor unavailable (in cooldown) or invalid request"),
            @ApiResponse(responseCode = "404", description = "Donor not found")
    })
    public ResponseEntity<DonationResponseDTO> recordDonation(@Valid @RequestBody DonationRequestDTO request) {
        DonationResponseDTO response = donationService.recordDonation(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
