package com.tom.seriennummer.sql;

import java.sql.*;

public class MySQL {

    private String HOST = "";
    private String DATABASE = "";
    private String USER = "";
    private String PASSWORD = "";

    private Connection con;

    public MySQL(String host, String database, String user, String password) {
        this.HOST = host;
        this.DATABASE = database;
        this.USER = user;
        this.PASSWORD = password;

        connect();
    }

    public boolean connect() {
        try {
            con = DriverManager.getConnection("jdbc:mysql://" + HOST + ":3306/" + DATABASE + "?autoReconnect=true", USER, PASSWORD);
            System.out.println("[MySQL] Die Verbindung zur MySQL wurde hergestellt!");
            return true;
        } catch (SQLException e) {
            System.out.println("[MySQL] Die Verbindung zur MySQL ist fehlgeschlagen! Fehler: " + e.getMessage());
            return false;
        }
    }

    public boolean close() {
        try {
            if(con != null) {
                con.close();
                System.out.println("[MySQL] Die Verbindung zur MySQL wurde Erfolgreich beendet!");
            }
            return true;
        } catch (SQLException e) {
            System.out.println("[MySQL] Fehler beim beenden der Verbindung zur MySQL! Fehler: " + e.getMessage());
            return false;
        }
    }

    public boolean update(String qry) {
        try {
            Statement st = con.createStatement();
            st.executeUpdate(qry);
            st.close();
            return true;
        } catch (SQLException e) {
            System.err.println(e);
            return false;
        }
    }

    public ResultSet query(String qry) {
        ResultSet rs = null;

        try {
            Statement st = con.createStatement();
            rs = st.executeQuery(qry);
        } catch (SQLException e) {
            System.err.println(e);
        }
        return rs;
    }

    public boolean hasConnection() {
        return con != null;
    }
}
