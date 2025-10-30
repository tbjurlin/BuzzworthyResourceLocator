package com.buzzword;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThat;


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


    @Test
    public void testInvalidId() {
        Resource resource = new Resource();
        resource.setId(-1);
        Set<ConstraintViolation<Resource>> violations = validator.validate(resource);


        assertFalse(violations.isEmpty());


        resource.setCreatorId(-1);
        resource.setCreatorFirstName("");

       
        // Valid Input
        
        resource.setCreatorId(1);
        resource.setCreatorFirstName("FirstName");

        violations = validator.validate(resource);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void testValidId() {
        Resource resource = new Resource();
        resource.setId(1);
        Set<ConstraintViolation<Resource>> violations = validator.validate(resource);
        assertTrue(violations.isEmpty());
        ConstraintViolation<Resource> violation = violations.iterator().next();
        assertEquals("id must be non-negative", violation.getMessage());
    }
}

