package com.noobsqn;

import java.sql.*;

import java.io.File;

/**
 * Created by Samuel on 05/12/13.
 */
public class NSQN_SQLite {

    public static void main( String args[] ){

        String fileName = "sqlite/gameStats_en_US.sqlite";

        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:"+fileName);
            c.setAutoCommit(false);
            System.out.println("Opened database successfully");

            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery( "SELECT * FROM items;" );
            while ( rs.next() ) {
                int id = rs.getInt("id");
                String  name = rs.getString("name");
                int description  = rs.getInt("description");
                String  price = rs.getString("price");

                System.out.println( "id = " + id );
                System.out.println( "name = " + name );
                System.out.println( "description = " + description );
                System.out.println( "price = " + price );
                System.out.println();
            }
            rs.close();
            stmt.close();
            c.close();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
        System.out.println("Opened database successfully");
    }
}
