package org.bsu.twiter.dao;

import org.bsu.twiter.models.Like;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class LikeDAOImpl implements LikeDAO {
    private static Logger logger;

    static {
        try {
            LogManager.getLogManager().readConfiguration(TwitDAOImpl.class.getClassLoader().getResourceAsStream("logger.properties"));
            logger = Logger.getLogger(TwitDAOImpl.class.getName());
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    private final ConnectionPool connectionPool;

    public LikeDAOImpl() {
        connectionPool = ConnectionPool.getInstance();
    }

    @Override
    public boolean saveLike(Like like) {
        try (Connection connection = connectionPool.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(SQLQueries.SAVE_LIKE)) {
                statement.setLong(1, like.getTwitId());
                statement.setLong(2, like.getUserId());
                statement.execute();

                return true;
            }
        } catch (SQLException exception) {
            logger.log(Level.SEVERE, "connection error " + exception.getMessage());
        }

        return false;
    }

    @Override
    public boolean deleteLike(Like like) {
        try (Connection connection = connectionPool.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(SQLQueries.DELETE_LIKE)) {
                statement.setLong(1, like.getTwitId());
                statement.setLong(2, like.getUserId());
                statement.execute();

                return true;
            }
        } catch (SQLException exception) {
            logger.log(Level.SEVERE, "connection error " + exception.getMessage());
        }

        return false;
    }

    @Override
    public boolean isLikePresent(Like like) {
        try (Connection connection = connectionPool.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(SQLQueries.FIND_LIKE)) {
                statement.setLong(1, like.getTwitId());
                statement.setLong(2, like.getUserId());
                ResultSet rs = statement.executeQuery();

                return rs.next();
            }
        } catch (SQLException exception) {
            logger.log(Level.SEVERE, "connection error " + exception.getMessage());
        }

        return false;
    }
}
