package com.george.vector.common.utils;

import static org.junit.Assert.*;

import org.junit.Test;

public class UtilsTest {

    @Test
    public void validate_field() {
        Utils utils = new Utils();

        assertTrue(utils.validateField("String", null));
        assertFalse(utils.validateField("", null));
    }

}