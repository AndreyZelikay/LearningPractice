package org.bsu.twiter.dao;

import org.bsu.twiter.models.Tag;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class TagDAOImpl implements TagDAO {
    private static Logger logger;

    static {
        try {
            LogManager.getLogManager().readConfiguration(TwitDAOImpl.class.getClassLoader().getResourceAsStream("logger.properties"));
            logger = Logger.getLogger(TwitDAOImpl.class.getName());
        } catch (Exception ignored) {
        }
    }

    private final ConnectionPool connectionPool;

    public TagDAOImpl() {
        connectionPool = ConnectionPool.getInstance();
    }


    @Override
    public Optional<Tag> findByBody(String body) {
        try (Connection connection = connectionPool.getConnection()) {
            ResultSet resultSet = null;
            try (PreparedStatement statement = connection.prepareStatement(SQLQueries.FIND_TAG_BY_BODY)) {
                statement.setString(1, body);
                resultSet = statement.executeQuery();

                if (resultSet.next()) {
                    return Optional.of(DAOUtils.parseTagFromRS(resultSet));
                }
            } finally {
                if (resultSet != null) {
                    resultSet.close();
                }
            }
        } catch (SQLException exception) {
            logger.log(Level.SEVERE, "connection error " + exception.getMessage());
        }

        return Optional.empty();
    }

    @Override
    public Optional<Tag> findById(Long id) {
        try (Connection connection = connectionPool.getConnection()) {
            ResultSet resultSet = null;
            try (PreparedStatement statement = connection.prepareStatement(SQLQueries.FIND_TAG_BY_ID)) {
                statement.setLong(1, id);
                resultSet = statement.executeQuery();

                if (resultSet.next()) {
                    return Optional.of(DAOUtils.parseTagFromRS(resultSet));
                }
            } finally {
                if (resultSet != null) {
                    resultSet.close();
                }
            }
        } catch (SQLException exception) {
            logger.log(Level.SEVERE, "connection error " + exception.getMessage());
        }

        return Optional.empty();
    }
}
