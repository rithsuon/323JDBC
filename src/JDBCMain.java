/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.sql.*;
import java.util.Scanner;
/**
 *
 * @author Rith
 * 
 * Test andrew gitignore
 */
public class JDBCMain {
    public static String DB_URL = "jdbc:derby://localhost:1527/";
    public static String DBNAME;
    public static String user;
    public static String pass;
    static Scanner input = new Scanner(System.in);
    static final String JDBC_DRIVER = "org.apache.derby.jdbc.ClientDriver";
    static final String PRINT_FORMAT="%-25s%-25s%-25s%-25s\n";
    
    public static void main(String[] args) {
        String query;
        ResultSet rs;
        System.out.println("Enter name of the database: ");
        DBNAME = input.nextLine();
        System.out.println("Enter username: ");
        user = input.nextLine();
        System.out.println("Enter password: ");
        pass = input.nextLine();
        if(user.length() == 0 && pass.length() == 0)
        {
            DB_URL = DB_URL + DBNAME;
        }
        else
        {
            DB_URL = DB_URL + DBNAME + ";user=" + user + ";password=" + pass;
        }
        Connection conn = null;
        Statement stmt = null;
        try {
            //need to establish driver
            Class.forName(JDBC_DRIVER);
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL);
            System.out.println("Connection Successful!");
            stmt = conn.createStatement();
            
            //Main loop
            while(true){
                //Print menu
                System.out.println("What would you like to do?\n1.List all writing groups\n2.List all publishers\n"
                        + "3.List all book titles\nE.Exit");
                String choice = input.nextLine();
                
                //Evaluate user choice
                if (choice.equals("1")){
                    query = "select * from WRITINGGROUPS";
                    rs = stmt.executeQuery(query);
                    System.out.println("\nWriting Groups: ");
                    System.out.printf(PRINT_FORMAT, "GROUPNAME", "HEADWRITER", "YEARFORMED","SUBJECT");
                    while (rs.next()){
                        String groupName = rs.getString("GROUPNAME");
                        String headWriter = rs.getString("HEADWRITER");
                        String yearFormed = rs.getString("YEARFORMED");
                        String subject = rs.getString("SUBJECT");
                        System.out.printf(PRINT_FORMAT, dispNull(groupName), dispNull(headWriter), dispNull(yearFormed), dispNull(subject));
                    }
                    System.out.println("\n");
                }
                
                if(choice.equals("2")){
                    query = "select * from PUBLISHERS";
                    rs = stmt.executeQuery(query);
                    System.out.println("\nPublishers:");
                    String localFormat = "%-35s%-45s%-35s%-35s\n";
                    System.out.printf(localFormat, "PUBLISHERNAME", "PUBLISHERADDRESS", "PUBLISHERPHONE","PUBLISHEREMAIL");
                    while(rs.next()){
                        String publisherName = rs.getString("PUBLISHERNAME");
                        String address = rs.getString("PUBLISHERADDRESS");
                        String phone = rs.getString("PUBLISHERPHONE");
                        String email = rs.getString("PUBLISHEREMAIL");
                        System.out.printf(localFormat, dispNull(publisherName), dispNull(address), dispNull(phone), dispNull(email));
                    }
                    System.out.println("\n");
                }
                
                if(choice.equals("3")){
                    query = "select BOOKTITLE from BOOKS";
                    rs = stmt.executeQuery(query);
                    System.out.println("\nBooks:");
                    System.out.print("BOOKTITLE\n");
                    while(rs.next()){
                        String bookTitle = rs.getString("BOOKTITLE");
                        System.out.println(dispNull(bookTitle));
                    }
                    System.out.println("\n");
                }
                
                if(choice.equals("E") || choice.equals("e")) {
                    break;
                }
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException ce) {
            ce.printStackTrace();
        } finally {
            //finally block used to close resources
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException se2) {
            }// nothing we can do
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }//end finally try
        }//end try
        
        
    }
    
    public static String dispNull (String input) {
        //because of short circuiting, if it's null, it never checks the length.
        if (input == null || input.length() == 0)
            return "N/A";
        else
            return input;
    }

}
