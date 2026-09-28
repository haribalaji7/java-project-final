package com.bloodlink.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "blood_group")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BloodGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 10)
    private String name;
}
