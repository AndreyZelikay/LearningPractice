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
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    private final ConnectionPool connectionPool;

    public TwitDAOImpl() {
        connectionPool = ConnectionPool.getInstance();
    }

    @Override
    public Optional<Twit> findById(Long id) {
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement statement = connection
                     .prepareStatement(String.format("select * from (%s) as s where s.post_id = ?", SQLQueries.TWITS_SELECT))) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return Optional.of(DAOUtils.parseTwitFromRS(resultSet));
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "connection error " + e.getMessage());
        }

        return Optional.empty();
    }

    @Override
    public List<Twit> getTwits(TwitsFilterForm form) {
        List<Twit> twitList = new ArrayList<>();

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

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql.toString())) {
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

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                twitList.add(DAOUtils.parseTwitFromRS(resultSet));
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "connection error " + e.getMessage());
        }

        return twitList;
    }

    @Override
    public boolean save(Twit twit) {
        try (Connection connection = connectionPool.getConnection()) {
            connection.setAutoCommit(false);

            long previousPostId = getMaxTwitId(connection);
            twit.setId(previousPostId + 1);

            saveTwit(twit, connection);

            saveTwitTags(twit, connection);

            connection.commit();

            return true;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "connection error " + e.getMessage());
        }

        return false;
    }

    @Override
    public boolean update(Twit twit) {
        try (Connection connection = connectionPool.getConnection()) {
            connection.setAutoCommit(false);

            try (PreparedStatement statement = connection.prepareStatement(SQLQueries.UPDATE_POST_DESCRIPTION)) {
                statement.setString(1, twit.getDescription());
                statement.setLong(2, twit.getId());
            }

            try (PreparedStatement statement = connection.prepareStatement(SQLQueries.GET_TAG_IDS_BY_POST_ID,
                    ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_UPDATABLE)) {
                statement.setLong(1, twit.getId());
                ResultSet resultSet = statement.executeQuery();

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
            }

            saveTwitTags(twit, connection);

            connection.commit();

            return true;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "connection error " + e.getMessage());
        }

        return false;
    }

    @Override
    public boolean delete(Long id) {
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQLQueries.DELETE_POST_BY_ID)) {
            statement.setLong(1, id);
            statement.execute();
            statement.close();

            return true;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "connection error " + e.getMessage());
        }

        return false;
    }


    private static void saveTwit(Twit twit, Connection connection) throws SQLException {
        try (PreparedStatement statement = connection
                .prepareStatement(SQLQueries.SAVE_TWIT)) {
            statement.setLong(1, twit.getId());
            statement.setLong(2, twit.getAuthor().getId());
            statement.setString(3, twit.getDescription());
            statement.setTimestamp(4, new Timestamp(twit.getCreatedAt().getTime()));
            statement.setString(5, twit.getPhotoLink());
            statement.execute();
        }
    }

    private static long getMaxTwitId(Connection connection) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(SQLQueries.GET_MAX_TWIT_ID)) {
            ResultSet resultSet = statement.executeQuery();
            return (resultSet.next()) ? resultSet.getLong("max") : 0;
        }
    }

    private static long getMaxTagId(Connection connection) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(SQLQueries.GET_MAX_TAG_ID)) {
            ResultSet resultSet = statement.executeQuery();
            return (resultSet.next()) ? resultSet.getLong("max") : 0;
        }
    }

    private static void saveTag(Tag tag, Connection connection) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(SQLQueries.SAVE_TAG)) {
            statement.setLong(1, tag.getId());
            statement.setString(2, tag.getBody());
            statement.execute();
        }
    }

    private static void saveTwitTagConnection(Twit twit, Tag tag, Connection connection) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(SQLQueries.SAVE_POST_TAG_CONNECTION)) {
            statement.setLong(1, twit.getId());
            statement.setLong(2, tag.getId());
            statement.execute();
        }
    }

    private static void saveTwitTags(Twit twit, Connection connection) throws SQLException {
        long previousTagId = getMaxTagId(connection);

        for (Tag tag : twit.getHashTags()) {
            TagDAO tagDAO = new TagDAOImpl();

            Optional<Tag> tagOptional = tagDAO.findByBody(tag.getBody());

            if (!tagOptional.isPresent()) {
                tag.setId(previousTagId + 1);
                saveTag(tag, connection);
                previousTagId++;
            } else {
                tag = tagOptional.get();
            }

            saveTwitTagConnection(twit, tag, connection);
        }
    }
}