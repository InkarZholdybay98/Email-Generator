import java.util.*;

import java.sql.*;

public class App {

    static Connection conn;
    static Scanner scanner;
    static Director director;
    static Employee employee;
    static String departmentDir = "";
    static String roleDir;
    static String emailDir;
    static String roleEmployee;
    static String passwordDir;
    
    public static void main(String[] args) throws Exception {

        conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/emailsDatabase", "root", "2641");

        scanner = new Scanner(System.in);

        System.out.println("Welcome to the Email Generator!");

        while (true) {

            System.out.println("State your role:");

            System.out.println("1.Director");
            System.out.println("2.Employee");
    
            int role = scanner.nextInt();
            scanner.nextLine();
    
            switch (role) {

                case 1:
                    directorMenu();
                    break;
    
                case 2:
                    employeeMenu();
                    break;
            
                default:
                    System.out.println("Invalid Option.Please , Try again");
                    break;
            }
        }

    }

    public static void directorMenu(){

        System.out.println("PLease , choose an option");

        while (true) {

            System.out.println("1.Registration");
            System.out.println("2.Log In");
    
            int choiceDirector = scanner.nextInt();
            scanner.nextLine();
    
            switch (choiceDirector) {
    
                case 1:
                    registration();
                    break;
    
                case 2:
                    logIn();
                    break;
            
                default:
                    System.out.println("Invalid Option. Please , Try again");
                    break;
            }
        }

    }

    public static void registration(){

        director = new Director(conn, departmentDir);
        
        roleDir = "director";
        
        director.addToTableEmails(departmentDir, roleDir);

    }

    public static void logIn(){

        boolean isDirectorExists;

        System.out.print("Email:");
        emailDir = scanner.nextLine();

        System.out.print("Password:");
        passwordDir = scanner.nextLine();

        isDirectorExists = checkDirector(emailDir , passwordDir);

        if (isDirectorExists) {
            departmentDir = returnDepartmentDir(emailDir , passwordDir);
        }

        if (isDirectorExists) {
            directorOptionsMenu();
        } else {
            System.out.println("This Director is not in the database");
        }
    }

    public static boolean checkDirector(String emailDir , String password){
        
        String queryCheckDirs = "select * from directors where TRIM(email) = ? AND TRIM(password) = ?";

        try (PreparedStatement prep = conn.prepareStatement(queryCheckDirs)) {
            
            prep.setString(1, emailDir);
            prep.setString(2, password);

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

    public static void directorOptionsMenu(){

        director = new Director(conn, departmentDir);

        System.out.println("Please , choose an option");

        while (true) {

            System.out.println("1.Add New Employee");
            System.out.println("2.Delete an Employee");
            System.out.println("3.Get an Employee Email");
            System.out.println("4.Show all Emails Employees");
            System.out.println("5.Change Password");
            System.out.println("6.Add an Alternative Email");
            System.out.println("7.Exit");
    
            int choice = scanner.nextInt();
            scanner.nextLine();
    
            switch (choice) {
    
                case 1:

                    String role = "employee";
                    director.addToTableEmails(departmentDir , role);
                    break;
                
                case 2:
                    director.deleteEmployee();
                    break;
    
                case 3:
                    director.getEmail(departmentDir);
                    break;
    
                case 4:
    
                    director.showEmails(departmentDir);
                    break;

                case 5:

                    roleDir = "director";
                    director.changePassword( emailDir, passwordDir , roleDir);
                    break;

                case 6:

                    roleDir = "director";
                    director.setAtlernateEmail(emailDir, roleDir);
                    break;
    

                case 7:
                    System.out.println("You exited the System");
                    System.exit(0);
                    break;
            
                default:
                    break;
            } 
        }

    }

    public static String returnDepartmentDir(String emailDir , String password){

        String queryDepartment = "select * from directors where email = ? AND password = ?";

        try (PreparedStatement prep = conn.prepareStatement(queryDepartment)){
            
            prep.setString(1, emailDir);
            prep.setString(2, password);

            try (ResultSet rs = prep.executeQuery()) {

                if (rs.next()) {
                    String department = rs.getString("department");
                    return department;
                }else{
                    return "";
                }

            } catch (Exception e) {
                System.out.println(e);
                return "";
            }

        } catch (Exception e) {
            System.out.println(e);
            return "";
        }

    }

    public static void employeeMenu(){

        employee = new Employee(conn);

        int choiceEmployee = 0;

        System.out.print("Email:");
        String emailEmployee = scanner.nextLine();

        System.out.print("Password:");
        String passwordEmployee = scanner.nextLine();

        boolean isEmployee = checkEmployee(emailEmployee , passwordEmployee);

        if (isEmployee) {
            
            System.out.println("Please , Choose an Option");

            while (true) {

                System.out.println("1.Change Password");
                System.out.println("2.Add an Alternative Email");
                System.out.println("3.Exit");

                choiceEmployee = scanner.nextInt();
                scanner.nextLine();
                
                switch (choiceEmployee) {

                    case 1:

                        roleEmployee = "employee";
                        employee.changePassword(emailEmployee , passwordEmployee ,roleEmployee);
                        break;

                    case 2:
                        roleEmployee = "employee";
                        employee.setAtlernateEmail(emailEmployee, roleEmployee);
                        break;

                    case 3:
                        System.out.println("You exited the System");
                        System.exit(0);
                        break;
                
                    default:
                        System.out.println("Invalid Option. Please , Try again");
                        break;
                }

            }

        }else{
            System.out.println("The Employee is not in the database");
        }
    }

    public static boolean checkEmployee(String emailEmployee , String passwordEmployee){

        String queryCheckEmployees = "select * from emailsofemployees where email = ? AND password = ?";

        try (PreparedStatement prep = conn.prepareStatement(queryCheckEmployees)) {
            
            prep.setString(1, emailEmployee);
            prep.setString(2, passwordEmployee);

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

