package com.noobsqn;

import java.sql.*;

import java.io.File;

/**
 * Created by Samuel on 05/12/13.
 */
public class NSQN_SQLite {

    public NSQN_SQLite(){

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

            String[] columnName = this.getColumnNameArray(rs);
            String[] columnType = this.getColumnTypeArray(rs);

            for (int i = 0; i < columnName.length; i++){
                //System.out.println(columnName[i]);
            }

            while ( rs.next() ) {
                for (int i = 0; i < columnName.length; i++){
                    //System.out.println(columnType[i]);
                    if(columnType[i].equals("INTEGER")){
                        System.out.println(columnName[i] + " " + String.valueOf(rs.getInt(columnName[i])));
                    }
                    if(columnType[i].equals("TEXT")){
                        System.out.println(columnName[i] + " " + String.valueOf(rs.getString(columnName[i])));
                    }
                    if(columnType[i].equals("REAL")){
                        System.out.println(columnName[i] + " " + String.valueOf(rs.getFloat(columnName[i])));
                    }
                }
                System.out.println("");
                /*int id = rs.getInt("id");
                String  name = rs.getString("name");
                int description  = rs.getInt("description");
                String  price = rs.getString("price");

                System.out.println( "id = " + id );
                System.out.println( "name = " + name );
                System.out.println( "description = " + description );
                System.out.println( "price = " + price );
                System.out.println();*/
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
    public String[] getColumnNameArray(ResultSet rs) {
        String sArr[] = null;
        try {
            ResultSetMetaData rm = rs.getMetaData();
            String sArray[] = new String[rm.getColumnCount()];
            for (int ctr = 1; ctr <= sArray.length; ctr++) {
                String s = rm.getColumnName(ctr);
                sArray[ctr - 1] = s;
            }
            return sArray;
        } catch (Exception e) {
            System.out.println(e);
            return sArr;
        }
    }
    public String[] getColumnTypeArray(ResultSet rs) {
        String sArr[] = null;
        try {
            ResultSetMetaData rm = rs.getMetaData();
            String sArray[] = new String[rm.getColumnCount()];
            for (int ctr = 1; ctr <= sArray.length; ctr++) {
                String s = rm.getColumnTypeName(ctr);
                sArray[ctr - 1] = s;
            }
            return sArray;
        } catch (Exception e) {
            System.out.println(e);
            return sArr;
        }
    }
}
