/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.sql.*;
import java.util.InputMismatchException;
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
    static final String PRINT_FORMAT2 = "%-25s%-25s%-45s%-25s%-35s" + PRINT_FORMAT;
    
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
        PreparedStatement preparedStatement2 =null;
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
                System.out.println("\nWhat would you like to do?\n1.List all writing groups\n2.List all publishers\n"
                        + "3.List all book titles\n"+ "4.List all data for a writing group\n"+"5.Insert a new book\n"
                        + "6.List all data from a publisher\n"+
                        "7.List all data from a book\n"+"E.Exit");
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
                
                if(choice.equals("4")){
                    String LOCAL_FORMAT = "%-25s%-35s%-50s%-25s%-35s" + PRINT_FORMAT;
                    query = "select * from WRITINGGROUPS natural join books natural join publishers where groupname = ?";
                    preparedStatement2 = conn.prepareStatement(query);
                    System.out.println("What group would you like to get all data from?");
                    choice = input.nextLine();
                    preparedStatement2.setString(1, choice);
                    rs = preparedStatement2.executeQuery();
                    System.out.printf(LOCAL_FORMAT,"GROUPNAME","PUBLISHERNAME","PUBLISHERADDRESS","PUBLISHERPHONE","PUBLISHEREMAIL","HEADWRITER","YEARFORMED","SUBJECT","BOOKTITLE");
                    while(rs.next()){
                        String groupName = rs.getString("GROUPNAME");
                        String publisherName = rs.getString("PUBLISHERNAME");
                        String address = rs.getString("PUBLISHERADDRESS");
                        String phone = rs.getString("PUBLISHERPHONE");
                        String email = rs.getString("PUBLISHEREMAIL");
                        String headWriter = rs.getString("HEADWRITER");
                        String yearFormed = rs.getString("YEARFORMED");
                        String subject = rs.getString("SUBJECT");
                        String bookTitle = rs.getString("BOOKTITLE");
                        System.out.printf(LOCAL_FORMAT, dispNull(groupName), dispNull(publisherName),dispNull(address),dispNull(phone),
                                dispNull(email),dispNull(headWriter),dispNull(yearFormed),dispNull(subject),dispNull(bookTitle));
                    }
                    System.out.println("\n");
                }
                
                if(choice.equals("5")) {
                    try {
                    //inserting a new book || publisher exists, writinggroup exists, bookname doesn't already exist
                    query = "insert into books(groupname, publishername, booktitle, yearpublished, numberpages) values (?, ?, ?, ?, ?)";
                    preparedStatement2 = conn.prepareStatement(query);
                    System.out.println("Enter writing group name: ");
                    String group = input.nextLine();
                    System.out.println("Enter publisher name: ");
                    String pub = input.nextLine();
                    System.out.println("Enter book title: ");
                    String title = input.nextLine();
                    System.out.println("Enter year published: ");
                    int yearPub = input.nextInt();
                    System.out.println("Enter number of pages: ");
                    int numPages = input.nextInt();
                    preparedStatement2.setString(1, group);
                    preparedStatement2.setString(2, pub);
                    preparedStatement2.setString(3, title);
                    preparedStatement2.setInt(4, yearPub);
                    preparedStatement2.setInt(5, numPages);
                    
                    int rows = preparedStatement2.executeUpdate();
                    System.out.println("Executed. " + rows + " row(s) affected.\n");
                    
                    } catch (SQLIntegrityConstraintViolationException e) {
                        
                        System.out.println("Execution failed! Invalid input.");
                        System.out.println(e.getMessage());
                    } catch (InputMismatchException e) {
                        System.out.println("Execution failed! Invalid input type (int type required).");
                    }
                }
                
                if(choice.equals("6")){
                    query = "select * from PUBLISHERS natural join BOOKS natural join WRITINGGROUPS where publishername = ?";
                    preparedStatement2 = conn.prepareStatement(query);
                    System.out.println("What publisher would you like to get all data from?");
                    choice = input.nextLine();
                    preparedStatement2.setString(1, choice);
                    rs = preparedStatement2.executeQuery();
                    System.out.printf(PRINT_FORMAT2,"GROUPNAME","PUBLISHERNAME","PUBLISHERADDRESS","PUBLISHERPHONE","PUBLISHEREMAIL","HEADWRITER","YEARFORMED","SUBJECT","BOOKTITLE","YEARPUBLISHED","NUMBERPAGES");

                    while(rs.next()){
                        String groupName = rs.getString("GROUPNAME");
                        String publisherName = rs.getString("PUBLISHERNAME");
                        String address = rs.getString("PUBLISHERADDRESS");
                        String phone = rs.getString("PUBLISHERPHONE");
                        String email = rs.getString("PUBLISHEREMAIL");
                        String headWriter = rs.getString("HEADWRITER");
                        String yearFormed = rs.getString("YEARFORMED");
                        String subject = rs.getString("SUBJECT");
                        String bookTitle = rs.getString("BOOKTITLE");
                        String yearPublished = rs.getString("YEARPUBLISHED");
                        String numberPages = rs.getString("NUMBERPAGES");
                        System.out.printf(PRINT_FORMAT2, dispNull(groupName), dispNull(publisherName),dispNull(address),dispNull(phone),
                                dispNull(email),dispNull(headWriter),dispNull(yearFormed)
                                ,dispNull(subject),dispNull(bookTitle),dispNull(yearPublished),dispNull(numberPages));
                    }
                    System.out.println("\n");
                }
                
                if(choice.equals("7")){
                    query = "select * from books natural join publishers natural join WRITINGGROUPS where booktitle = ? and groupname = ?";
                    preparedStatement2 = conn.prepareStatement(query);
                    System.out.println("What book would you like to get all data from?");
                    choice = input.nextLine();
                    System.out.println("Please specify a writing group: ");
                    String group = input.nextLine();
                    preparedStatement2.setString(1, choice);
                    preparedStatement2.setString(2, group);
                    rs = preparedStatement2.executeQuery();
                    System.out.printf(PRINT_FORMAT2,"GROUPNAME","PUBLISHERNAME","PUBLISHERADDRESS","PUBLISHERPHONE","PUBLISHEREMAIL","HEADWRITER","YEARFORMED","SUBJECT","BOOKTITLE","YEARPUBLISHED","NUMBERPAGES");

                    while(rs.next()){
                        String groupName = rs.getString("GROUPNAME");
                        String publisherName = rs.getString("PUBLISHERNAME");
                        String address = rs.getString("PUBLISHERADDRESS");
                        String phone = rs.getString("PUBLISHERPHONE");
                        String email = rs.getString("PUBLISHEREMAIL");
                        String headWriter = rs.getString("HEADWRITER");
                        String yearFormed = rs.getString("YEARFORMED");
                        String subject = rs.getString("SUBJECT");
                        String bookTitle = rs.getString("BOOKTITLE");
                        String yearPublished = rs.getString("YEARPUBLISHED");
                        String numberPages = rs.getString("NUMBERPAGES");
                        System.out.printf(PRINT_FORMAT2, dispNull(groupName), dispNull(publisherName),dispNull(address),dispNull(phone),
                                dispNull(email),dispNull(headWriter),dispNull(yearFormed)
                                ,dispNull(subject),dispNull(bookTitle),dispNull(yearPublished),dispNull(numberPages));
                    }
                    System.out.println("\n");
                }
                                
                if(choice.equals("E") || choice.equals("e")) {
                    break;
                }
            }
            
        } catch (SQLNonTransientConnectionException e) {
            System.out.println("Connection to Database failed. (Database was not found)");
            System.out.println("Program will exit.");
        } catch (ClassNotFoundException ce) {
            ce.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
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
