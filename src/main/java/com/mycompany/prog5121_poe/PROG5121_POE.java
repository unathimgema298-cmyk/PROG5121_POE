/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.prog5121_poe;
import java.util.Scanner;
/**
 *
 * @author Student
 */
public class PROG5121_POE {

    public static void main(String[] args) {
        
        Scanner input = new Scanner(System.in);
        
        System.out.println("Enter your first name: ");
        String firstName = input.nextLine();
        
        System.out.println("Enter your last name: ");
        String lastName = input.nextLine();
        
        System.out.println("Enter your username: ");
        String username = input.nextLine();
        
        System.out.println("Enter your password: ");
        String password = input.nextLine();
        
        System.out.println("Enter your South African cellphone number (+27): ");
        String cellPhoneNumber = input.nextLine();
        
        Login user = new Login(username, password, cellPhoneNumber, 
        firstName, lastName);
        
        System.out.println(user.registerUser());
        
        System.out.println("\n--- LOGIN ---");
        
        System.out.println("Enter your username to login: ");
        String loginUsername = input.nextLine();
        
        System.out.println("Enter your password to login: ");
        String loginPassword = input.nextLine();
        
        System.out.println(user.returnLoginStatus(loginUsername, loginPassword));
    }
   
}
