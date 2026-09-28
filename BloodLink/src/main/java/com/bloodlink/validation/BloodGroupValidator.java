package com.bloodlink.validation;

import com.bloodlink.repository.BloodGroupRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BloodGroupValidator implements ConstraintValidator<ValidBloodGroup, String> {

    @Autowired
    private BloodGroupRepository bloodGroupRepository;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.trim().isEmpty()) {
            return false;
        }
        return bloodGroupRepository.existsByName(value);
    }
}
