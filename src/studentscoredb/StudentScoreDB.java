package studentscoredb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class StudentScoreDB {

    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/"; // 3306 is default port
        String user = "root";
        String password = "";
        Connection con = null;
        Statement stmt = null;
        String query;
        ResultSet result = null;
        int input;
        boolean flag = true;
        Scanner sc = new Scanner(System.in);
        try {
            con = DriverManager.getConnection(url, user, password); // connect to MySQL
            stmt = con.createStatement();
            con.setAutoCommit(false);
            query = "DROP DATABASE IF EXISTS student_management;";
            stmt.executeUpdate(query);
            query = "CREATE DATABASE student_management;";
            stmt.executeUpdate(query);
            query = "USE student_management;";
            stmt.executeUpdate(query);
            query = """ 
            CREATE TABLE student_score ( 
            SUBJECT VARCHAR(32),
            SCORE FLOAT,
            PRIMARY KEY(SUBJECT)
            );
            """;
            stmt.executeUpdate(query);
            query = """
                  INSERT INTO student_score
                  (SUBJECT,SCORE)
                  VALUES
                  ("ENGLISH",95),
                  ("MATH",98),
                  ("SCIENCE",89);
                  """;
            stmt.executeUpdate(query);
            query = "SELECT * FROM student_score;";
            result = stmt.executeQuery(query);
            System.out.println("Subject\tScore");
            while (result.next()) {
                String subject = result.getString("SUBJECT");
                double score = result.getDouble("SCORE");
                System.out.println(subject + "\t" + score);
            }
            while (flag) {
                System.out.println("""
                               Press 1 to ADD new subject
                               Press 2 to delete subject: """);
                input = sc.nextInt();
                switch (input) {
                    case 1 -> {
                        System.out.println("Please enter the name of the subject: ");
                        String subj = sc.next();
                        System.out.println("Score in subject " + subj);
                        float marks = sc.nextFloat();
                        query = String.format("INSERT INTO student_score (SUBJECT, SCORE) VALUES ('%s','%s');", subj.toUpperCase(), marks);
                        stmt.executeUpdate(query);
                        con.commit();
                        query = "SELECT * FROM student_score;";
                        result = stmt.executeQuery(query);
                        System.out.println("""
                                                           Updated table
                                                           Subject\tScore""");
                        while (result.next()) {
                            String subject = result.getString("SUBJECT");
                            double score = result.getDouble("SCORE");
                            System.out.println(subject + "\t" + score);
                        }
                    }
                    case 2 -> {
                        System.out.println("Which subject you would like to delete: ");
                        String subj = sc.next();
                        query = "DELETE FROM student_score WHERE SUBJECT='" + subj.toUpperCase() + "';";
                        stmt.executeUpdate(query);
                        con.commit();
                        query = "SELECT * FROM student_score;";
                        result = stmt.executeQuery(query);
                        System.out.println("""
                                                           Updated table
                                                           Subject\tScore""");
                        while (result.next()) {
                            String subject = result.getString("SUBJECT");
                            double score = result.getDouble("SCORE");
                            System.out.println(subject + "\t" + score);
                        }
                    }
                    default -> {
                        System.out.println("Please input correct number.");
                    }
                }
                System.out.println("""
                               Enter y/Y to continue
                                and n/N to exit""");
                String choice = sc.next();
                if (choice.equals("y") || choice.equals("Y")) {
                    flag = true;
                } else if (choice.equals("n") || choice.equals("N")) {
                    flag = false;
                }
            }

        } catch (Exception ex) {
            System.out.println("SQLException caught: " + ex.getMessage());
        } finally {
            // Close all database objects
            try {
                if (result != null) {
                    result.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException ex) {
                System.out.println("SQLException caught: " + ex.getMessage());
            }

        }

    }

}
