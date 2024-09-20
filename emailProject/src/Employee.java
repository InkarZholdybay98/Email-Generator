import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class Employee extends Email{

       Scanner scanner = new Scanner(System.in);
       boolean isEmployee = false;
       Connection conn;
       String newPassword;
       String login;

      public Employee(Connection conn){
        super(conn);
        this.conn = conn;
      }

}
