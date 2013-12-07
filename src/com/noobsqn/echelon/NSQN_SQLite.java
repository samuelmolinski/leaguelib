package com.noobsqn.echelon;

import com.google.gson.JsonObject;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.noobsqn.util.Logg;

import java.sql.*;

import java.io.File;

/**
 * Created by Samuel on 05/12/13.
 */
public class NSQN_SQLite {
    public String fileName = "sqlite/gameStats_en_US.sqlite";
    public Connection c = null;
    public Statement stmt = null;

    public NSQN_SQLite() throws SQLException {

        NSQN_db mongoDB = new NSQN_db();
        c = DriverManager.getConnection("jdbc:sqlite:"+fileName);

        String[] tables = {"champions", "championSearchTags", "items", "itemItemCategories", "championItems", "itemRecipes", "championAbilities", "championSkins", "searchTags"};


        for (String table: tables){
            //mongoDB.purgeCollection(table);
            BasicDBList list = this.processTable(table);
            mongoDB.updateCollection(table, list);
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
    public BasicDBList processTable(String table){
        BasicDBList r = new BasicDBList();
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:"+fileName);
            c.setAutoCommit(false);
            System.out.println("Opened database successfully");

            stmt = c.createStatement();

            String query = "SELECT * FROM "+table+";";
            ResultSet rs = stmt.executeQuery( query );

            String[] columnName = this.getColumnNameArray(rs);
            String[] columnType = this.getColumnTypeArray(rs);

            while ( rs.next() ) {
                BasicDBObject doc = new BasicDBObject();
                for (int i = 0; i < columnName.length; i++){
                    //System.out.println(columnType[i]);
                    if(columnType[i].equals("INTEGER")){
                        //System.out.println(columnName[i] + " " + String.valueOf(rs.getInt(columnName[i])));
                        doc.append(columnName[i],rs.getInt(columnName[i]));
                    }
                    if(columnType[i].equals("TEXT")){
                        //System.out.println(columnName[i] + " " + String.valueOf(rs.getString(columnName[i])));
                        doc.append(columnName[i], rs.getString(columnName[i]));
                    }
                    if(columnType[i].equals("REAL")){
                        //System.out.println(columnName[i] + " " + String.valueOf(rs.getFloat(columnName[i])));
                        doc.append(columnName[i], rs.getFloat(columnName[i]));
                    }
                }
                r.add(doc);
                Logg.d("doc", doc.toString());
            }

            System.out.println("");
            rs.close();
            stmt.close();
            c.close();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
        System.out.println("Opened database." + table + " successfully");
        return r;
    }
    public void test(){

    }
}
