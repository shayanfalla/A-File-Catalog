/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaRMI.Server;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Database {

    private static final String TABLE_NAME = "accounts";
    private static final String TABLE_FILENAME = "filename";
    private static Connection connection;

    public Database() {
        try {
            Class.forName("org.apache.derby.jdbc.ClientXADataSource");
            connection = DriverManager.getConnection("jdbc:derby://localhost:1527//File-Catalog", "file", "file");
            createTables();
        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void createTables() throws SQLException {
        Statement stmt = connection.createStatement();
        if (!tableExists(true)) {
            stmt.executeUpdate(
                    "create table " + TABLE_NAME + " (username varchar(32) primary key, password varchar(32))");
        }
        if (!tableExists(false)) {
            stmt.executeUpdate(
                    "create table " + TABLE_FILENAME + " (filename varchar(32) primary key, owner varchar(32), permission varchar(32), readwrite varchar(32))");
        }
    }

    private boolean tableExists(boolean table) throws SQLException {
        DatabaseMetaData metaData = connection.getMetaData();
        ResultSet tableMetaData = metaData.getTables(null, null, null, null);
        while (tableMetaData.next()) {
            String tableName = tableMetaData.getString(3);
            if (tableName.equalsIgnoreCase(TABLE_NAME) && table) {
                return true;
            }
            if (tableName.equalsIgnoreCase(TABLE_FILENAME) && !table) {
                return true;
            }
        }
        return false;
    }

    public boolean checkUser(String username) {
        String sql = "SELECT username FROM accounts";
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                if (username.equals(rs.getString("username"))) {
                    return true;
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public void insertUser(String username, String password) {
        String sql = "INSERT INTO accounts (username, password) VALUES (?, ?)";
       // String query = "INSERT INTO Users (user_id,username,firstname,lastname, companyname, email_addr, want_privacy ) VALUES (null, ?, ?, ?, ?, ?, ?)";

        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setString(1, username);
            st.setString(2, password);
            st.executeUpdate();
            st.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
