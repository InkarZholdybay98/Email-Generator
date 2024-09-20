import java.sql.*;
import java.util.*;

import com.mysql.cj.xdevapi.PreparableStatement;

public class Director extends Email{

  Connection conn;
  Scanner scanner = new Scanner(System.in);
  
  String querySetNewId = "set @new_id = 0";
  String querySetId = "update emailsofemployees set id = (@new_id := @new_id + 1)";
  String setAutoIncrementQuery = "ALTER TABLE emailsofemployees AUTO_INCREMENT = 1";

  public Director(Connection conn ,String department){
    super(conn , department);
    this.conn = conn;
  }

  public void deleteEmployee(){

    System.out.print("Please , write the first name of employee you want to delete:");
    String firstName = scanner.nextLine();

    System.out.print("Please , write the last name of employee you want to delete:");
    String lastName = scanner.nextLine();

    System.out.print("Write the email of employee:");
    String emailEmployee = scanner.nextLine();

    boolean isEmployee = checkEmployees(firstName , lastName , emailEmployee);

    String query = "delete from emailsOfEmployees where first_name = ? AND last_name = ? AND email = ?";

    if (isEmployee) {

      try (PreparedStatement prep = conn.prepareStatement(query)) {

        prep.setString(1, firstName);
        prep.setString(2, lastName);
        prep.setString(3, emailEmployee);

        prep.executeUpdate();
        changeId();

        System.out.println(firstName+" "+lastName+" was deleted from the database");

      } catch (Exception e) {
          System.out.println(e);
      }

    } else {
      System.out.println(firstName+" "+lastName+" is not in the Database");
    } 

}

  public void getEmail(String departmentDir){

  System.out.print("Please , write the name of the employee:");
  String name = scanner.nextLine();

  System.out.print("Please , write the last name of the employee:");
  String lastName = scanner.nextLine();

  String query = "select * from emailsOfEmployees where first_name = ? AND last_name = ? AND department = ?";

  try (PreparedStatement prep = conn.prepareStatement(query)) {

      prep.setString(1, name);
      prep.setString(2, lastName);
      prep.setString(3, departmentDir);

      try (ResultSet rs = prep.executeQuery()) {

          if (rs.next()) {

              System.out.println("The email of an employee "+rs.getString("first_name")+" "+rs.getString("last_name")+":"+rs.getString("email"));

          }else{

              System.out.println("This employee:"+name+" "+lastName + " " + " doesn't exit in the database");

          }

      } catch (Exception e) {
          System.out.println(e);
      }

  } catch (Exception e) {
      System.out.println(e);
  }
}

  public void showEmails(String departmentDir){

  String query = "select * from emailsOfEmployees where department = ?";

  try (PreparedStatement prep =conn.prepareStatement(query)) {

    prep.setString(1, departmentDir);

    try(ResultSet rs = prep.executeQuery()) {

      while (rs.next()) {
        System.out.println(" ");
        System.out.println("Full Name:"+rs.getString("first_name")+" "+rs.getString("last_name"));
        System.out.println("Email:"+rs.getString("email"));
        System.out.println(" ");
      }

    } catch (Exception e) {
      System.out.println(e);
    }
      
  } catch (Exception e) {
      System.out.println(e);
  }
}

  public void changeId(){

  try (PreparedStatement prep = conn.prepareStatement(querySetNewId)) {
    prep.executeUpdate();
  } catch (Exception e) {
    System.out.println(e);
  }

  try (PreparedStatement prep = conn.prepareStatement(querySetId)) {
    prep.executeUpdate();
  } catch (Exception e) {
    System.out.println(e);
  }

  try (PreparedStatement prep = conn.prepareStatement(setAutoIncrementQuery)) {
    prep.executeUpdate();
  } catch (Exception e) {
    System.out.println(e);
  }

}

  public boolean checkEmployees(String firstName ,String lastName ,String emailEmployee){
  
  String queryCheckEmployees = "select * from emailsofemployees where first_name = ? AND last_name = ? AND email = ?";

  try (PreparedStatement prep = conn.prepareStatement(queryCheckEmployees)) {

    prep.setString(1, firstName);
    prep.setString(2, lastName);
    prep.setString(3, emailEmployee);

    try (ResultSet rs = prep.executeQuery()) {
      
      if (rs.next()) {
        return true;
      } else {
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
