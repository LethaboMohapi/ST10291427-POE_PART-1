/*
Module name: Progeamming 1A
Module code: PROG5121/p/w
Assessment type: Portfolio of Evidence (POE)- PART 1
Student Name: Lethabo
Student surname: Mohapi
Student username: ST10291427@rcconnect.edu.za
Student number: ST10291427

// Part 1 Registration and Login feature - 2
//ChatApp.java - Main class with program entry point
************************
 Test Data
username: kyl_1
password: Ch&&sec@ke99!
phonenumber: +27838968976
*************************
*/
package chatapp;
import java.util.Scanner;
public class ChatApp 
{

    public static void main(String[] args) 
    {
      Scanner scanner = new Scanner(System.in);
      
      System.out.println("Create a user account");
      System.out.print("Enter username: ");
      String username = scanner.nextLine();
      System.out.print("Enter password: ");
      String password = scanner.nextLine();
      System.out.print("Enter a phone number with international code (e.g., +27821234567): ");
      String phoneNumber = scanner.nextLine(); //**
      
      CreateUserAccount account;
      try
      {
          account = new CreateUserAccount(username, password, phoneNumber);
          System.out.println("\nAccount created successfully: " + account);
      }
      catch (IllegalArgumentException e)
      {
          System.out.println("\nFailed to create an account: " + e.getMessage());
          return;
      }
      
      Login login = new Login(account);
      System.out.println(login.registerUser());
      
      //Login
      System.out.println("\nLogin to your account");
      System.out.print("\nEnter username: ");
      String loginUsername = scanner.nextLine();
      System.out.print("Enter password: ");
      String loginPassword = scanner.nextLine();
      
      login.loginUser(loginUsername, loginPassword);
      System.out.println(login.returnLoginStatus());
      
      if (!login.returnLoginStatus().equals("\nLogin successful!"))
      {
          return;
      }
    }
    
}
