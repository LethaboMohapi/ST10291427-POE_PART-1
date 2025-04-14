/*
Module name: Progeamming 1A
Module code: PROG5121/p/w
Assessment type: Portfolio of Evidence (POE)- PART 1
Student Name: Lethabo
Student surname: Mohapi
Student username: ST10291427@rcconnect.edu.za
Student number: ST10291427

Part 1 Registration and Login feature - 3
Login.java - Manages user authentication
 */
package chatapp;

public class Login 

{
    private CreateUserAccount createUserAccount;
    private boolean loggedIn = false;
    
    public Login(CreateUserAccount createUserAccount)
    {
        this.createUserAccount = createUserAccount;
    }
    
    public boolean checkUserName(String inputUsername)
    {
        return inputUsername.equals(createUserAccount.getUsername());
    }
    
    public boolean checkPassword(String inputPassword)
    {
        return inputPassword.equals(createUserAccount.getPassword());
    }
    
    public String registerUser()
    {
        return "User " + createUserAccount.getUsername() + " registered successfully! ";
    }
    
    public boolean loginUser(String username, String password)
    {
        if (checkUserName(username) && checkPassword(password))
        {
            loggedIn = true;
        }
        return loggedIn;
    }
    public String returnLoginStatus()
    {
        return loggedIn ? "Login successful!" : "Login failed: Incorrect username or password";
    }        
}

