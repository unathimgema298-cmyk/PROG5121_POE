/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.prog5121_poe;
import java.util.regex.Pattern;
/**
 *
 * @author Student
 */
public class Login {
    
    private String username;
    private String password;
    private String cellPhoneNumber;
    private String firstName;
    private String lastName;
           
    public Login(String username, String password, String cellPhoneNumber, 
            String firstName, String lastName) {
        this.username = username;
        this.password = password;
        this.cellPhoneNumber = cellPhoneNumber;
        this.firstName = firstName;
        this.lastName = lastName;
    }
    //Checks that the username contains an underscore
    //and is no more than five characters long.
    public boolean checkUserName() {
        return username.contains("_") && username.length() <= 5;
    }
    //Checks whether the password meets the required complexity rules:
    //minimum eight characters, a capital letter, a number and a special character.
    public boolean checkPasswordComplexity() {
        
        boolean hasCapital = false;
        boolean hasNumber = false;
        boolean hasSpecialCharacter = false;
        
        for (int i = 0; i < password.length(); i++) {
            
            char character = password.charAt(i);
            
            if (Character.isUpperCase(character)) {
                hasCapital = true;
            }
            if (Character.isDigit(character)) {
                hasNumber = true;
            }
            if (!Character.isLetterOrDigit(character)) {
                hasSpecialCharacter = true;
            }
        }
        return password.length() >= 8
                && hasCapital
                && hasNumber
                && hasSpecialCharacter;
    }    
    //Regular expression implementation adapted using Oracle's Java
    //regular expression documentation:
    //https://docs.oracle.com/javase/tutorial/essential/regex/
    
    // Checks that the cellphone number contains the international
    // country code followed by exactly nine digits.
    public boolean checkCellPhoneNumber() {
        
        String regex = "^\\+27\\d{9}$";
        
        return Pattern.matches(regex, cellPhoneNumber);
    }
    
    // Registers the user after validating the username, password
    // and cellphone number, and returns the appropriate registration message.
    public String registerUser() {
        
        if (!checkUserName()) {
            return "Username is not correctly formatted; please ensure that your username contains "
                    + "an underscore and is no more than five characters in length.";
        }
        if (!checkPasswordComplexity()) {
            return "Password is not correctly formatted; please ensure that the password contains "
                    + "at least eight characters, a capital letter, a number, and a special character.";
        }
        if (!checkCellPhoneNumber()) {
            return "Cell phone number incorrectly formatted or does not contain international code.";
        }
        
        return "User registered successfully.";
    }
    
    public boolean loginUser(String enteredUsername, String enteredPassword) {
        
        return username.equals(enteredUsername)
                && password.equals(enteredPassword);
    }
    
    public String returnLoginStatus(String enteredUsername, String enteredPassword) {
        
        if (loginUser(enteredUsername, enteredPassword)) {
            return "Welcome " + firstName + " " + lastName
                    + ", it is great to see you again.";
        }
        
        return "Username or password incorrect, please try again.";
    }
}
    

