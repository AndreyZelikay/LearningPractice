package org.bsu.twiter.dao;

import org.bsu.twiter.models.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class UserDAOImpl implements UserDAO {

    private static Logger logger;

    static {
        try {
            LogManager.getLogManager().readConfiguration(TwitDAOImpl.class.getClassLoader().getResourceAsStream("logger.properties"));
            logger = Logger.getLogger(TwitDAOImpl.class.getName());
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    private ConnectionPool connectionPool;

    public UserDAOImpl() {
        connectionPool = ConnectionPool.getInstance();
    }

    @Override
    public Optional<User> findById(Long id) {
        try (Connection connection = connectionPool.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(SQLQueries.FIND_USER_BY_ID);
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return Optional.of(DAOUtils.parseUserFromRS(resultSet));
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "connection error " + e.getMessage());
        }

        return Optional.empty();
    }

    @Override
    public boolean save(User object) {
        try (Connection connection = connectionPool.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(SQLQueries.SAVE_USER);
            statement.setString(1, object.getName());

            return statement.execute();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "connection error " + e.getMessage());
        }

        return false;
    }

    @Override
    public boolean update(User object) {
        try (Connection connection = connectionPool.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(SQLQueries.UPDATE_USER);
            statement.setString(1, object.getName());
            statement.setLong(2, object.getId());

            return statement.execute();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "connection error " + e.getMessage());
        }

        return false;
    }

    @Override
    public boolean delete(Long id) {
        try (Connection connection = connectionPool.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(SQLQueries.DELETE_USER_BY_ID);
            statement.setLong(1, id);

            return statement.execute();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "connection error " + e.getMessage());
        }

        return false;
    }
}
