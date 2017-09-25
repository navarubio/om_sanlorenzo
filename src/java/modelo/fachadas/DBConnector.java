/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.fachadas;

import java.sql.Connection;
import java.sql.SQLException;
/**
 *
 * @author miguel
 */
public class DBConnector {

    private static DBConnector instance = null;
    private final Connection connection;

    private DBConnector() throws Exception {
        javax.naming.InitialContext ctx = new javax.naming.InitialContext();
        javax.sql.DataSource ds = (javax.sql.DataSource) ctx.lookup("jdbc/om_optica");

        connection = ds.getConnection();
        connection.setAutoCommit(false);
    }

    public static DBConnector getInstance() throws Exception {
        if (instance == null || instance.getConnection() == null || instance.getConnection().isClosed()) {
            instance = new DBConnector();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }

    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
