package com.bloodlink.controller;

import com.bloodlink.dto.BloodGroupStatsDTO;
import com.bloodlink.service.BloodGroupService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/stats")
@RequiredArgsConstructor
@Tag(name = "Statistics", description = "APIs for viewing blood bank statistics")
public class StatsController {

    private final BloodGroupService bloodGroupService;
    private final com.bloodlink.service.AdvancedStatsService advancedStatsService;

    @GetMapping("/blood-groups")
    @Operation(summary = "Get donor statistics by blood group")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved statistics")
    public ResponseEntity<List<BloodGroupStatsDTO>> getStats() {
        return ResponseEntity.ok(bloodGroupService.getBloodGroupStats());
    }

    @GetMapping("/summary")
    @Operation(summary = "Get overall statistics summary")
    public ResponseEntity<com.bloodlink.dto.StatsSummaryDTO> getSummary() {
        return ResponseEntity.ok(advancedStatsService.getSummary());
    }

    @GetMapping("/cities")
    @Operation(summary = "Get donor statistics by city")
    public ResponseEntity<List<com.bloodlink.dto.GroupCountDTO>> getDonorsPerCity() {
        return ResponseEntity.ok(advancedStatsService.getDonorsPerCity());
    }

    // Keep the other endpoints for backward compatibility or just in case
    @GetMapping("/donors/total")
    public ResponseEntity<com.bloodlink.dto.CountDTO> getTotalDonors() {
        return ResponseEntity.ok(advancedStatsService.getTotalDonors());
    }

    @GetMapping("/donors/available")
    public ResponseEntity<com.bloodlink.dto.CountDTO> getAvailableDonors() {
        return ResponseEntity.ok(advancedStatsService.getAvailableDonors());
    }

    @GetMapping("/donors/unavailable")
    public ResponseEntity<com.bloodlink.dto.CountDTO> getUnavailableDonors() {
        return ResponseEntity.ok(advancedStatsService.getUnavailableDonors());
    }

    @GetMapping("/donors/by-blood-group")
    public ResponseEntity<List<com.bloodlink.dto.GroupCountDTO>> getDonorsPerBloodGroup() {
        return ResponseEntity.ok(advancedStatsService.getDonorsPerBloodGroup());
    }

    @GetMapping("/donations/this-month")
    public ResponseEntity<com.bloodlink.dto.CountDTO> getDonationsThisMonth() {
        return ResponseEntity.ok(advancedStatsService.getDonationsThisMonth());
    }

    @GetMapping("/donations/most-requested-blood-group")
    public ResponseEntity<com.bloodlink.dto.StringValueDTO> getMostRequestedBloodGroup() {
        return ResponseEntity.ok(advancedStatsService.getMostRequestedBloodGroup());
    }
}
