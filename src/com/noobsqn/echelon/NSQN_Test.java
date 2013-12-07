package com.noobsqn.echelon;

import java.sql.SQLException;

/**
 * Created by Samuel on 05/12/13.
 */
public class NSQN_Test {

    public static void main( String args[] ){

        try {
            NSQN_SQLite sql = new NSQN_SQLite();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
