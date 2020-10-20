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
    
    public static void main(String[] args) {
        System.out.println("Enter name of the database: ");
        DBNAME = input.nextLine();
        System.out.println("Enter username: ");
        user = input.nextLine();
        System.out.println("Enter password: ");
        pass = input.nextLine();
        DB_URL = DB_URL + DBNAME + ";user=" + user + ";password=" + pass;
        Connection conn = null;
        Statement stmt = null;
        try {
            //need to establish driver
            Class.forName(JDBC_DRIVER);
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL);
            System.out.println("Connection Successful!");
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
}
