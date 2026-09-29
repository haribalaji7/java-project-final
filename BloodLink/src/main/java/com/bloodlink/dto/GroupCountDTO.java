package com.bloodlink.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GroupCountDTO {
    private String group;
    private Long count;
}
