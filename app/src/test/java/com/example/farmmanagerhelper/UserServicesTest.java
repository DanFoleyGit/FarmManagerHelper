package com.example.farmmanagerhelper;

import org.junit.Test;
import static org.junit.Assert.*;


public class UserServicesTest {

    @Test
    public void checkPasswords() {
        String password1 ="password";
        String password2 ="wrongPassword";

        boolean result = UserServices.checkPasswords(password1,password1);
        boolean result2 = UserServices.checkPasswords(password1,password2);
        assertTrue(result);
        assertFalse(result2);
    }

    @Test
    public void checkPasswordLength() {
        boolean result1 = UserServices.checkPasswordLength("12345678");
        boolean result2 = UserServices.checkPasswordLength("1");
        assertTrue(result1);
        assertFalse(result2);
    }

    @Test
    public void checkEmailAndPasswordAreNotEmpty() {
        boolean result1 = UserServices.checkEmailAndPasswordAreNotEmpty("email","password");
        boolean result2 = UserServices.checkEmailAndPasswordAreNotEmpty("","password");
        boolean result3 = UserServices.checkEmailAndPasswordAreNotEmpty("email","");
        boolean result4 = UserServices.checkEmailAndPasswordAreNotEmpty("","");

        assertTrue(result1);
        assertFalse(result2);
        assertFalse(result3);
        assertFalse(result4);
    }

    @Test
    public void checkFieldsAreNotEmpty() {
        boolean result1 = UserServices.checkFieldsAreNotEmpty("email","password","confirmPassword");
        boolean result2 = UserServices.checkFieldsAreNotEmpty("email","","confirmPassword");
        boolean result3 = UserServices.checkFieldsAreNotEmpty("","","");

        assertTrue(result1);
        assertFalse(result2);
        assertFalse(result3);
    }

    @Test
    public void validateEmail() {
        boolean result1 = UserServices.validateEmail("email@gmail.co.uk");
        boolean result2 = UserServices.validateEmail("email@gmail.com");
        boolean result3 = UserServices.validateEmail("email");
        boolean result4 = UserServices.validateEmail("");

        assertTrue(result1);
        assertTrue(result2);
        assertFalse(result3);
        assertFalse(result4);
    }
}