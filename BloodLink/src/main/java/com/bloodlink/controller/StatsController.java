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

    @GetMapping("/blood-groups")
    @Operation(summary = "Get donor statistics by blood group")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved statistics")
    public ResponseEntity<List<BloodGroupStatsDTO>> getStats() {
        return ResponseEntity.ok(bloodGroupService.getBloodGroupStats());
    }
}
