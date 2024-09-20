import java.util.Scanner;

import com.mysql.cj.xdevapi.PreparableStatement;

import java.sql.*;

public class Email  {
  
  private String firstName;
  private String lastName;
  private int defaultPasswordLength = 10;
  private String password;
  private String department;
  private int mailboxCapacity = 500;
  private String alternateEmail;
  private String email;
  private String companySuffix = "company.com";
  Connection conn;
  Scanner scanner = new Scanner(System.in);
  boolean isEmployee;
  boolean isDirector;
  String queryUpdatePassword;
  
  public Email(Connection conn , String department) {
    this.conn = conn;
    this.department = department;
  }

  public Email(Connection conn){
    this.conn = conn;
  }

  public void addToTableEmails(String department , String role){

    System.out.print("First name:");
    this.firstName = scanner.nextLine();

    System.out.print("Last name:");
    this.lastName = scanner.nextLine();
    
    this.conn = conn;
    
    if (department.equals("")) {
      this.department = setDepartment();
    }else{
      this.department = department;
    }

    this.password = randomPassword(defaultPasswordLength);
    
    email = firstName.toLowerCase()+"."+lastName.toLowerCase()+"@"+this.department+"."+companySuffix;

    String query = "";
    String alert = "";

    if (role.equalsIgnoreCase("director") && !this.department.equals("none") && !this.department.equals("Unknown")) {

      query = "insert into directors (first_name , last_name , department , email , password) values(? , ? , ? , ? , ?) ";

      alert = "New Director of " + this.department +" was added";

    } else if (role.equalsIgnoreCase("employee") && !this.department.equals("none") && !this.department.equals("Unknown")) {

      query = "insert into emailsOfEmployees (first_name , last_name , department , email , password) values(? , ? , ? , ? , ?) ";

      alert = "New Employee of " + this.department +" was added";

    }

    try (PreparedStatement prep = conn.prepareStatement(query)){

      prep.setString(1, firstName);
      prep.setString(2, lastName);
      prep.setString(3, this.department);
      prep.setString(4, email);
      prep.setString(5, password);

      prep.executeUpdate();

      System.out.println(alert);

    } catch (Exception e) {
      System.out.println("Didn't Add to the Database");
      System.out.println(e);
    }

  } 
  
  private String setDepartment() {
    
    System.out.println("New worker: "+firstName);
      
    System.out.println("1.Sales");
    System.out.println("2.Development");
    System.out.println("3.Accounting");
    System.out.println("0.None");
    
    System.out.print("Departmanet code: ");
    
    Scanner in = new Scanner(System.in);
    
    int depChoice = in.nextInt();
    
    switch(depChoice){
    
    case 0:
      return "none";
    
    case 1:
      return "sales";
      
    case 2:
      return "dev";
      
    case 3:
      return "acct";

    default:
      return "Unknown";
      
    }
  }
  
  private String randomPassword(int length) {
    
    String passwordSet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%";
    
    char[] password = new char[length];
    
    for(int i = 0; i < length ; i++) {
      int rand = (int) (Math.random() * passwordSet.length());
      password[i] = passwordSet.charAt(rand);
    }
    
    return new String(password);
    
  }
  
  public void setAtlernateEmail(String emailCurrent,String role) {

    String queryAlternateEmail;

    System.out.print("Alternative Email:");
    String alternatEmail = scanner.nextLine();

    if (role.equalsIgnoreCase("director")) {
      queryAlternateEmail = "UPDATE directors SET alternative_email = ? WHERE email = ?";
    } else {
      queryAlternateEmail = "UPDATE emailsofemployees SET alternative_email = ? WHERE email = ?";
    }

    try (PreparedStatement prep = conn.prepareStatement(queryAlternateEmail)) {
      
      prep.setString(1, alternatEmail);
      prep.setString(2, emailCurrent);

      prep.executeUpdate();

      System.out.println("The alternative Email was added");

    } catch (Exception e) {
      System.out.println(e);
    }

  }
  
  public void changePassword(String workEmail , String oldPassword , String role) {

    System.out.print("New Password:");
    String newPassword = scanner.nextLine();

    while (newPassword.length() != 10) {
      System.out.println("New Password length must be 10. Please , Try again");
      System.out.print("New Password:");
      newPassword = scanner.nextLine();
    }

    if (role.equalsIgnoreCase("director")) {

      isDirector = checkDirector(oldPassword);
      queryUpdatePassword = "UPDATE directors SET password = ? WHERE email = ?";

    }else{

      isEmployee = checkEmployee(oldPassword);
      queryUpdatePassword = "UPDATE emailsOfEmployees SET password = ? WHERE email = ?";

    }

    try (PreparedStatement prep = conn.prepareStatement(queryUpdatePassword)) {

        prep.setString(1, newPassword);
        prep.setString(2, workEmail);

        if (isEmployee || isDirector) {
           prep.executeUpdate();
           System.out.println("The password was changed!"); 
        } else {
            System.out.println("Couldn't change the Password. Not Found in the Database");
        }

    } catch (Exception e) {
        System.out.println(e);
    }
  }

  public boolean checkEmployee(String oldPassword){

    String findEmployeeQuery = "select * from emailsOfEmployees where password = ?";

    try (PreparedStatement prep = conn.prepareStatement(findEmployeeQuery)) {
        
        prep.setString(1, oldPassword);

        prep.executeQuery();

        try (ResultSet rs = prep.executeQuery()) {

            if (rs.next()) {
              return true;
            }else{
              return false;
            }

        } catch (Exception e) {
            System.out.println(e);
            return false;
        }

    } catch (Exception e) {
        System.out.println(e);
        return false;
    }
  }

  public boolean checkDirector(String oldPassword){

    String findDirectorQuery = "select * from directors where password = ?";

    try (PreparedStatement prep = conn.prepareStatement(findDirectorQuery)) {
        
        prep.setString(1, oldPassword);

        prep.executeQuery();

        try (ResultSet rs = prep.executeQuery()) {

            if (rs.next()) {
              return true;
            }else{
              return false;
            }

        } catch (Exception e) {
            System.out.println(e);
            return false;
        }

    } catch (Exception e) {
        System.out.println(e);
        return false;
    }
  }

}

