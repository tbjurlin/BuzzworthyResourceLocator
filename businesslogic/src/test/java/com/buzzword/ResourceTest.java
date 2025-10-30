package com.buzzword;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThat;


import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

/**
 * Unit test .
 */
public class ResourceTest {

    @BeforeEach
    void setup() {
    }


    @Test
    public void testInvalidId() {
        Resource resource = new Resource();
        resource.setId(-1);
        
    }

    @Test
    public void testValidId() {
        Resource resource = new Resource();
        resource.setId(1);

    }
}

