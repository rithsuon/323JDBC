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
        if(user.length() == 0 && pass.length() == 0)
        {
            DB_URL = DB_URL + DBNAME + ";user=" + null + ";password=" + null;
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
            
            String writingGroup = "CREATE TABLE WritingGroups"
                    + "( GroupName VARCHAR(20)NOT NULL,"
                    +"HeadWriter VARCHAR(20)NOT NULL,"
                    + "YearFormed int NOT NULL,"
                    + "Subject VARCHAR(20)NOT NULL);";
            stmt.executeUpdate(writingGroup);
            System.out.println("Created WritingGroup Table");
            
            String Publishers = "CREATE TABLE Publishers"
                    + "( PublisherName VARCHAR(20)NOT NULL,"
                    + " PublisherAddress VARCHAR(30)NOT NULL,"
                    + "PublisherPhone int NOT NULL,"
                    + "PublisherEmail VARCHAR(20)NOT NULL);";
            stmt.executeUpdate(Publishers);
            System.out.println("Created Publisher Table");
            
            String Books = "CREATE TABLE Books"
                    + "( GroupName VARCHAR(20)NOT NULL,"
                    + " Booktitle VARCHAR(20)NOT NULL,"
                    + "PublisherName VARCHAR(20)NOT NULL,"
                    + "YearsPublished int NOT NULL,"
                    + "NumberPages int NOT NULL);";
            stmt.executeUpdate(Books);
            System.out.println("Created Book Table");
            String pk1 = "ALTER TABLE WritingGroups ADD CONSTRAINT writinggroups_pk"
                    + "primary key (GroupName);";
            stmt.executeUpdate(pk1);
            System.out.println("Added pk to writingGroups Table");
            
            String pk2 = "ALTER TABLE Publishers ADD CONSTRAINT publishers_pk" +
"primary key (PublisherName);";
            stmt.executeUpdate(pk2);
            System.out.println("Added pk to publishers Table");
            
            String pk3 = "ALTER TABLE Books ADD CONSTRAINT books_pk" +
"primary key (GroupName, Booktitle);";
            stmt.executeUpdate(pk3);
            System.out.println("Added pk to books Table");
            
            String fk1 = "ALTER TABLE Books ADD CONSTRAINT books_fk" +
"foreign key (GroupName) REFERENCES WritingGroups (GroupName);";
            stmt.executeUpdate(fk1);
            System.out.println("Added fk to books Table");
            
            String fk2 = "ALTER TABLE Books ADD CONSTRAINT books_fk2" +
"foreign key (PublisherName) REFERENCES Publishers (PublisherName);";
            stmt.executeUpdate(fk2);
            System.out.println("Added fk2 to books Table");  
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
