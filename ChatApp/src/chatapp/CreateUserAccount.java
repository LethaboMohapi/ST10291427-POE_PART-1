/*
Module name: Progeamming 1A
Module code: PROG5121/p/w
Assessment type: Portfolio of Evidence (POE)- PART 1
Student Name: Lethabo
Student surname: Mohapi
Student username: ST10291427@rcconnect.edu.za
Student number: ST10291427

Part 1 Registration and Login feature - 2
CreateUserAccount.java - Handles user account creation and validation

*/
package chatapp;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CreateUserAccount 
{
   private String username, password, phoneNumber;
   
   public CreateUserAccount(String username, String password, String phoneNumber)
    {
      if (!isValidUsername(username))
      {
          throw new IllegalArgumentException("Invalid username. It must contain an underscore and be no more than five characters long. ");
      }
      if (!isValidPassword(password))
      {
          throw new IllegalArgumentException("Invalid password. It must be  at least eight characters long, must contain at least one capital letter, one number and one special character. ");
      }
      if (!isValidPhoneNumber(phoneNumber))
      {
          throw new IllegalArgumentException("Invalid phone number. It must include the country code and a number no more than 10 digits long. ");
      }
      
      this.username = username;
      this.password = password;
      this.phoneNumber = phoneNumber;
    }
   
   public String getUsername()
   {
       return username;
   }
   
   public String getPassword()
   {
       return password;
   }
   
   public String getPhoneNumber()
   {
       return phoneNumber;
   }
   
   private boolean isValidUsername(String username)
   {
       return username.length() <= 5 && username.contains("_");
   }
  
   private static boolean isValidPassword(String password)
   {
       String regex ="^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[`~!@#$%^&*()-_=+[{]}\\|:;\"'<,>.?/])" + "[A-Za-z\\d`~!@#$%^&*()-_=+[{]}\\|:;\"'<,>.?/]{8,20}$";
       Pattern pattern = Pattern.compile(regex);
       Matcher matcher = pattern.matcher(password);
       return matcher.matches();
   }
   
   private boolean isValidPhoneNumber(String phoneNumber)
   {
       String regex = "^\\+27\\d{9}$";
       return phoneNumber != null && phoneNumber.matches(regex);
   }
   
   @Override
   public String toString()
   {
       return "UserAccount{" + "username='" + username + '\'' + ", phoneNumber='" + phoneNumber + '\'' + '}';
   }
}
