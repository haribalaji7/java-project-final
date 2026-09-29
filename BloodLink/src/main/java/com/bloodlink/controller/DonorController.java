package com.bloodlink.controller;

import com.bloodlink.dto.DonorRequestDTO;
import com.bloodlink.dto.DonorResponseDTO;
import com.bloodlink.service.DonorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/donors")
@RequiredArgsConstructor
@Tag(name = "Donor Management", description = "APIs for managing blood donors")
public class DonorController {

    private final DonorService donorService;

    @PostMapping
    @Operation(summary = "Register a new donor")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Donor registered successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    public ResponseEntity<DonorResponseDTO> registerDonor(@Valid @RequestBody DonorRequestDTO request) {
        DonorResponseDTO response = donorService.registerDonor(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Get all donors")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved list of all donors")
    public ResponseEntity<List<DonorResponseDTO>> getAllDonors() {
        List<DonorResponseDTO> donors = donorService.getAllDonors();
        return ResponseEntity.ok(donors);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get donor by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved donor"),
            @ApiResponse(responseCode = "404", description = "Donor not found")
    })
    public ResponseEntity<DonorResponseDTO> getDonorById(@PathVariable Long id) {
        return ResponseEntity.ok(donorService.getDonorById(id));
    }

    @GetMapping("/search")
    @Operation(summary = "Search available donors by city and blood group")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved available donors")
    public ResponseEntity<List<DonorResponseDTO>> searchDonors(
            @RequestParam String city,
            @RequestParam String bloodGroup) {
        List<DonorResponseDTO> donors = donorService.searchAvailableDonors(city, bloodGroup);
        return ResponseEntity.ok(donors);
    }
}
