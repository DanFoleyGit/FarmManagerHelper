package com.example.farmmanagerhelper;

import org.junit.Test;

import static org.junit.Assert.*;

public class GeneralServicesTest {

    @Test
    public void checkStringDoesNotContainForwardSlashCharacter() {
        boolean result = GeneralServices.checkStringDoesNotContainForwardSlashCharacter("String");
        boolean result2 = GeneralServices.checkStringDoesNotContainForwardSlashCharacter("Str/ing");

        assertTrue(result);
        assertFalse(result2);
    }
}