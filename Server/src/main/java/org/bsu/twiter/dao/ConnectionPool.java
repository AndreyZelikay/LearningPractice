package org.bsu.twiter.dao;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class ConnectionPool {

    private static Logger logger;

    static {
        try {
            LogManager.getLogManager().readConfiguration(TwitDAOImpl.class.getClassLoader().getResourceAsStream("logger.properties"));
            logger = Logger.getLogger(TwitDAOImpl.class.getName());
        } catch (Exception ignored) {
        }
    }

    private DataSource dataSource;
    private static ConnectionPool instance = null;

    private ConnectionPool() {}

    public static ConnectionPool getInstance() {
        if (instance==null) {
            instance = new ConnectionPool();
        }

        return instance;
    }

    public Connection getConnection() throws SQLException {
        try {
            this.dataSource = (DataSource) new InitialContext().lookup("java:comp/env/jdbc/pool");
        } catch (NamingException e) {
            logger.log(Level.SEVERE, "jndi exception " + e.getMessage());
        }

        return dataSource.getConnection();
    }
}