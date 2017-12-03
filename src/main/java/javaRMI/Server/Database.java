/*
 * Copyright (C) 2017 Shayan Fallahian shayanf@kth.se
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package javaRMI.Server;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Database {

    private static final String TABLE_NAME = "accounts";
    private static final String TABLE_FILENAME = "files";
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

    public boolean checkPass(String username, String password) {
        String sql = "SELECT * FROM accounts";
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                if (username.equals(rs.getString("username")) && password.equals(rs.getString("password"))) {
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

    public ArrayList<String[]> getFilelist() {
        String sql = "SELECT * FROM files";

        Statement stmt;
        ResultSet rs;
        ArrayList<String[]> bigList = new ArrayList<String[]>();

        try {
            stmt = connection.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                String[] temp = new String[4];
                for (int i = 0; i < temp.length; i++) {
                    temp[i] = rs.getString(i + 1);
                }
                bigList.add(temp);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return bigList;
    }

    public boolean insertFile(String filename, String user, String permissions, String rw) {
        String sql = "INSERT INTO files (filename, owner, permission, readwrite) VALUES (?, ?, ?, ?)";
        String sqlCheck = "SELECT owner FROM files";

        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sqlCheck);
            while (rs.next()) {
                if (filename.equals(rs.getString(1))) {
                    return false;
                }
            }

            PreparedStatement st = connection.prepareCall(sql);
            st.setString(1, filename);
            st.setString(2, user);
            st.setString(3, permissions);
            st.setString(4, rw);
            st.executeUpdate();
            st.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return true;
    }

    public void unregister(String username) {
        String sql = "DELETE FROM accounts WHERE username = ?";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setString(1, username);
            st.executeUpdate();
            st.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void modifyFile(String filename, String attribute, String modification) {
        String sql = "UPDATE files SET " + attribute + " = ? WHERE filename = ?";

        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, modification);
            stmt.setString(2, filename);
            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

    }
}
