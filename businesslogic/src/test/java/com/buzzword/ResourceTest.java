package com.buzzword;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;

/**
 * Unit test .
 */
public class ResourceTest {

    private Validator validator;

    @BeforeEach
    void setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    /**
     * 
     */
    @Test
    public void creatorFirstNameNotBlank() {
        Resource newResource = new Resource();
        newResource.setCreatorFirstName("FirstName");

        Set<ConstraintViolation<Resource>> violations = validator.validate(newResource);

        assertTrue(violations.isEmpty(), "Violations expected for invalid data");
    }
}
