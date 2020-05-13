package org.bsu.twiter.dao;

import org.bsu.twiter.forms.TwitsFilterForm;
import org.bsu.twiter.models.Tag;
import org.bsu.twiter.models.Twit;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class TwitDAOImpl implements TwitDAO {

    private static Logger logger;

    static {
        try {
            LogManager.getLogManager().readConfiguration(TwitDAOImpl.class.getClassLoader().getResourceAsStream("logger.properties"));
            logger = Logger.getLogger(TwitDAOImpl.class.getName());
        } catch (Exception ignored) {
        }
    }

    private final ConnectionPool connectionPool;

    public TwitDAOImpl() {
        connectionPool = ConnectionPool.getInstance();
    }

    @Override
    public Optional<Twit> findById(Long id) {
        try (Connection connection = connectionPool.getConnection()) {
            ResultSet resultSet = null;
            try (PreparedStatement statement = connection
                    .prepareStatement("select * from " + "(" + SQLQueries.TWITS_SELECT + ") as s" + " where s.post_id = ?")) {
                statement.setLong(1, id);
                resultSet = statement.executeQuery();

                if (resultSet.next()) {
                    return Optional.of(DAOUtils.parseTwitFromRS(resultSet));
                }
            } finally {
                if (resultSet != null) {
                    resultSet.close();
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "connection error " + e.getMessage());
        }

        return Optional.empty();
    }

    @Override
    public List<Twit> getTwits(TwitsFilterForm form) {
        List<Twit> twitList = new ArrayList<>();

        try (Connection connection = connectionPool.getConnection()) {
            StringBuilder sql = new StringBuilder("select * from ");
            sql.append("(")
                    .append(SQLQueries.TWITS_SELECT)
                    .append(")")
                    .append(" as s").append(" where 1");
            if (form.getAuthor() != null) {
                sql.append(" and name = ?");
            }
            if (form.getFromDate() != null) {
                sql.append(" and created_at > ?");
            }
            if (form.getUntilDate() != null) {
                sql.append(" and created_at <= ?");
            }
            if (form.getHashTags() != null) {
                sql.append(" and tags like ?");
            }

            sql.append(" limit ")
                    .append(form.getTop())
                    .append(", ")
                    .append(form.getTop() + form.getSkip());

            ResultSet resultSet = null;
            try (PreparedStatement statement = connection.prepareStatement(sql.toString())) {
                int curr = 1;
                if (form.getAuthor() != null) {
                    statement.setString(curr, form.getAuthor());
                    curr++;
                }
                if (form.getFromDate() != null) {
                    statement.setTimestamp(curr, new Timestamp(form.getFromDate().getTime()));
                    curr++;
                }
                if (form.getUntilDate() != null) {
                    statement.setTimestamp(curr, new Timestamp(form.getUntilDate().getTime()));
                    curr++;
                }
                if (form.getHashTags() != null) {
                    statement.setString(curr,
                            "%" +
                                    form.getHashTags().stream().map(Tag::getBody).sorted().collect(Collectors.joining(" "))
                                    + " %");
                }

                resultSet = statement.executeQuery();

                while (resultSet.next()) {
                    twitList.add(DAOUtils.parseTwitFromRS(resultSet));
                }
            } finally {
                if (resultSet != null) {
                    resultSet.close();
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "connection error " + e.getMessage());
        }

        return twitList;
    }

    @Override
    public boolean save(Twit twit) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = connectionPool.getConnection();
            connection.setAutoCommit(false);

            long previousPostId;
            statement = connection.prepareStatement(SQLQueries.GET_MAX_TWIT_ID);
            resultSet = statement.executeQuery();
            previousPostId = (resultSet.next()) ? resultSet.getLong("max") : 0;
            resultSet.close();
            statement.close();

            statement = connection
                    .prepareStatement(SQLQueries.SAVE_TWIT);
            statement.setLong(1, previousPostId + 1);
            statement.setLong(2, twit.getAuthor().getId());
            statement.setString(3, twit.getDescription());
            statement.setTimestamp(4, new Timestamp(twit.getCreatedAt().getTime()));
            statement.setString(5, twit.getPhotoLink());
            statement.execute();
            statement.close();

            long previousTagId;
            statement = connection.prepareStatement(SQLQueries.GET_MAX_TAG_ID);
            resultSet = statement.executeQuery();
            previousTagId = (resultSet.next()) ? resultSet.getLong("max") : 0;
            resultSet.close();
            statement.close();

            for (Tag tag : twit.getHashTags()) {
                TagDAO tagDAO = new TagDAOImpl();

                Optional<Tag> tagOptional = tagDAO.findByBody(tag.getBody());

                long tagId;

                if (!tagOptional.isPresent()) {
                    statement = connection.prepareStatement(SQLQueries.SAVE_TAG);
                    statement.setLong(1, previousTagId + 1);
                    statement.setString(2, tag.getBody());
                    statement.execute();
                    statement.close();
                    tagId = previousTagId + 1;
                    previousTagId++;
                } else {
                    tagId = tagOptional.get().getId();
                }

                statement = connection.prepareStatement(SQLQueries.SAVE_POST_TAG_CONNECTION);
                statement.setLong(1, previousPostId + 1);
                statement.setLong(2, tagId);
                statement.execute();
                statement.close();
            }

            connection.commit();

            return true;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "connection error " + e.getMessage());

            try {
                if (connection != null) {
                    connection.rollback();
                }
            } catch (SQLException exception) {
                logger.log(Level.SEVERE, "connection error " + e.getMessage());
            }
        } finally {
            if (connection != null) {
                try {
                    if (statement != null) {
                        statement.close();
                    }
                    if (resultSet != null) {
                        resultSet.close();
                    }

                    connection.setAutoCommit(true);
                    connection.close();
                } catch (SQLException exception) {
                    logger.log(Level.SEVERE, "connection close error " + exception.getMessage());
                }
            }
        }

        return false;
    }

    @Override
    public boolean update(Twit twit) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = connectionPool.getConnection();
            connection.setAutoCommit(false);

            statement = connection.prepareStatement(SQLQueries.UPDATE_POST_DESCRIPTION);
            statement.setString(1, twit.getDescription());
            statement.setLong(2, twit.getId());

            if (statement.executeUpdate() == 0) {
                connection.rollback();
                return false;
            }

            statement.close();

            statement = connection.prepareStatement(SQLQueries.GET_MAX_TAG_ID);
            resultSet = statement.executeQuery();
            long previousTagId = (resultSet.next()) ? resultSet.getLong("max") : 0;
            resultSet.close();
            statement.close();

            statement = connection.prepareStatement(SQLQueries.GET_TAG_IDS_BY_POST_ID,
                    ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_UPDATABLE);
            statement.setLong(1, twit.getId());
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Optional<Tag> tagFromDB = new TagDAOImpl().findById(resultSet.getLong("tag_id"));
                if (tagFromDB.isPresent() && !twit.getHashTags().remove(tagFromDB.get())) {
                    try (PreparedStatement ps = connection.prepareStatement(SQLQueries.DELETE_POST_TAG_CONNECTION)) {
                        ps.setLong(1, twit.getId());
                        ps.setLong(2, tagFromDB.get().getId());
                        ps.execute();
                    }
                }
            }

            resultSet.close();
            statement.close();

            for (Tag tag : twit.getHashTags()) {
                TagDAO tagDAO = new TagDAOImpl();

                Optional<Tag> tagOptional = tagDAO.findByBody(tag.getBody());

                long tagId;

                if (!tagOptional.isPresent()) {
                    statement = connection.prepareStatement(SQLQueries.SAVE_TAG);
                    statement.setLong(1, previousTagId + 1);
                    statement.setString(2, tag.getBody());
                    statement.execute();
                    statement.close();
                    tagId = previousTagId + 1;
                    previousTagId++;
                } else {
                    tagId = tagOptional.get().getId();
                }

                statement = connection.prepareStatement(SQLQueries.SAVE_POST_TAG_CONNECTION);
                statement.setLong(1, twit.getId());
                statement.setLong(2, tagId);
                statement.execute();
                statement.close();
            }

            connection.commit();
            statement.close();

            return true;
        } catch (SQLException e) {
            try {
                if (connection != null) {
                    connection.rollback();
                }
            } catch (SQLException exception) {
                logger.log(Level.SEVERE, "connection error " + exception.getMessage());
            }
            logger.log(Level.SEVERE, "connection error " + e.getMessage());
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (resultSet != null) {
                    resultSet.close();
                }
                if (connection != null) {
                    connection.setAutoCommit(true);
                    connection.close();
                }
            } catch (SQLException exception) {
                logger.log(Level.SEVERE, "connection error " + exception.getMessage());
            }
        }

        return false;
    }

    @Override
    public boolean delete(Long id) {
        try (Connection connection = connectionPool.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(SQLQueries.DELETE_POST_BY_ID)) {
                statement.setLong(1, id);
                statement.execute();
                statement.close();

                return true;
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "connection error " + e.getMessage());
        }

        return false;
    }
}