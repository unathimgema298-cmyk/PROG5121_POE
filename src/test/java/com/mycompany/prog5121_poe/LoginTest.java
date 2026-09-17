/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package com.mycompany.prog5121_poe;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
/**
 *
 * @author Student
 */
public class LoginTest {

    @Test
    public void testUsernameCorrectlyFormatted() {
        Login login = new Login(
                "kyl_1",
                "Ch&sec@ke99!",
                "+27838968976",
                "Kyle",
                "Smith"
        );

        assertTrue(login.checkUserName());
    }

    @Test
    public void testUsernameIncorrectlyFormatted() {
        Login login = new Login(
                "kyle!!!!!!!",
                "Ch&sec@ke99!",
                "+27838968976",
                "Kyle",
                "Smith"
        );

        assertFalse(login.checkUserName());
    }

    @Test
    public void testPasswordMeetsComplexityRequirements() {
        Login login = new Login(
                "kyl_1",
                "Ch&sec@ke99!",
                "+27838968976",
                "Kyle",
                "Smith"
        );

        assertTrue(login.checkPasswordComplexity());
    }

    @Test
    public void testPasswordDoesNotMeetComplexityRequirements() {
        Login login = new Login(
                "kyl_1",
                "password",
                "+27838968976",
                "Kyle",
                "Smith"
        );

        assertFalse(login.checkPasswordComplexity());
    }

    @Test
    public void testCellPhoneNumberCorrectlyFormatted() {
        Login login = new Login(
                "kyl_1",
                "Ch&sec@ke99!",
                "+27838968976",
                "Kyle",
                "Smith"
        );

        assertTrue(login.checkCellPhoneNumber());
    }

    @Test
    public void testCellPhoneNumberIncorrectlyFormatted() {
        Login login = new Login(
                "kyl_1",
                "Ch&sec@ke99!",
                "0838968976",
                "Kyle",
                "Smith"
        );

        assertFalse(login.checkCellPhoneNumber());
    }

    @Test
    public void testLoginSuccessful() {
        Login login = new Login(
                "kyl_1",
                "Ch&sec@ke99!",
                "+27838968976",
                "Kyle",
                "Smith"
        );

        assertTrue(login.loginUser("kyl_1", "Ch&sec@ke99!"));
    }

    @Test
    public void testLoginFailed() {
        Login login = new Login(
                "kyl_1",
                "Ch&sec@ke99!",
                "+27838968976",
                "Kyle",
                "Smith"
        );

        assertFalse(login.loginUser("kyle", "password"));
    }
    
        @Test
    public void testUsernameCorrectlyFormattedMessage() {
        Login login = new Login(
                "kyl_1",
                "Ch&sec@ke99!",
                "+27838968976",
                "Kyle",
                "Smith"
        );

        assertEquals(
                "Welcome Kyle Smith, it is great to see you again.",
                login.returnLoginStatus("kyl_1", "Ch&sec@ke99!")
        );
    }

    @Test
    public void testUsernameIncorrectlyFormattedMessage() {
        Login login = new Login(
                "kyle!!!!!!!",
                "Ch&sec@ke99!",
                "+27838968976",
                "Kyle",
                "Smith"
        );

        assertEquals(
                "Username is not correctly formatted; please ensure that your username contains an underscore and is no more than five characters in length.",
                login.registerUser()
        );
    }

    @Test
    public void testPasswordSuccessfullyCapturedMessage() {
        Login login = new Login(
                "kyl_1",
                "Ch&sec@ke99!",
                "+27838968976",
                "Kyle",
                "Smith"
        );

        assertEquals(
                "User registered successfully.",
                login.registerUser()
        );
    }

    @Test
    public void testPasswordIncorrectlyFormattedMessage() {
        Login login = new Login(
                "kyl_1",
                "password",
                "+27838968976",
                "Kyle",
                "Smith"
        );

        assertEquals(
                "Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.",
                login.registerUser()
        );
    }

    @Test
    public void testCellPhoneSuccessfullyCapturedMessage() {
        Login login = new Login(
                "kyl_1",
                "Ch&sec@ke99!",
                "+27838968976",
                "Kyle",
                "Smith"
        );

        assertEquals(
                "User registered successfully.",
                login.registerUser()
        );
    }

    @Test
    public void testCellPhoneIncorrectlyFormattedMessage() {
        Login login = new Login(
                "kyl_1",
                "Ch&sec@ke99!",
                "08966553",
                "Kyle",
                "Smith"
        );

        assertEquals(
                "Cell phone number incorrectly formatted or does not contain international code.",
                login.registerUser()
        );
    }
}
