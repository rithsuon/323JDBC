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
 */
public class JDBC {
    public static String DB_URL = "jdc:derby://localhost:1527/";
    public static String DBNAME;
    static Scanner input = new Scanner(System.in);
    
    public static void main(String[] args) {
        System.out.println("Enter name of the database: ");
        DBNAME = input.nextLine();
        try {
            //need to establish driver
            System.out.println("Connecting to database...");
            Connection conn = DriverManager.getConnection(DB_URL);
            System.out.println("Connection Successful!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
