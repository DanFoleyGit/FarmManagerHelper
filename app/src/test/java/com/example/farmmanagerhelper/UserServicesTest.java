package com.example.farmmanagerhelper;

import org.junit.Test;
import static org.junit.Assert.*;


public class UserServicesTest {

    @Test
    public void checkPasswords() {
        String password1 ="password";
        String password2 ="wrongPassword";

        Boolean result = UserServices.checkPasswords(password1,password1);
        Boolean result2 = UserServices.checkPasswords(password1,password2);
        assertTrue(result);
        assertFalse(result2);
    }

    @Test
    public void checkPasswordLength() {
        Boolean result1 = UserServices.checkPasswordLength("12345678");
        Boolean result2 = UserServices.checkPasswordLength("1");
        assertTrue(result1);
        assertFalse(result2);
    }

    @Test
    public void checkEmailAndPasswordAreNotEmpty() {
        Boolean result1 = UserServices.checkEmailAndPasswordAreNotEmpty("email","password");
        Boolean result2 = UserServices.checkEmailAndPasswordAreNotEmpty("","password");
        Boolean result3 = UserServices.checkEmailAndPasswordAreNotEmpty("email","");
        Boolean result4 = UserServices.checkEmailAndPasswordAreNotEmpty("","");

        assertTrue(result1);
        assertFalse(result2);
        assertFalse(result3);
        assertFalse(result4);
    }

    @Test
    public void checkFieldsAreNotEmpty() {
        boolean result1 = UserServices.checkFieldsAreNotEmpty("email","password","confirmPassword");
        Boolean result2 = UserServices.checkFieldsAreNotEmpty("email","","confirmPassword");
        Boolean result3 = UserServices.checkFieldsAreNotEmpty("","","");

        assertTrue(result1);
        assertFalse(result2);
        assertFalse(result3);
    }

    @Test
    public void validateEmail() {
        Boolean result1 = UserServices.validateEmail("email@gmail.co.uk");
        Boolean result2 = UserServices.validateEmail("email@gmail.com");
        Boolean result3 = UserServices.validateEmail("email");
        Boolean result4 = UserServices.validateEmail("");

        assertTrue(result1);
        assertTrue(result2);
        assertFalse(result3);
        assertFalse(result4);
    }
}