package org.bsu.twiter.dao;

import org.bsu.twiter.models.Like;
import org.bsu.twiter.models.Tag;
import org.bsu.twiter.models.Twit;
import org.bsu.twiter.models.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Date;
import java.util.stream.Collectors;

public class DAOUtils {

    public static Twit parseTwitFromRS(ResultSet resultSet) throws SQLException {
        Twit twit = new Twit();

        twit.setId(resultSet.getLong("post_id"));
        twit.setAuthor(new User(resultSet.getLong("user_id"), resultSet.getString("name")));
        twit.setDescription(resultSet.getString("description"));
        twit.setCreatedAt(new Date(resultSet.getTimestamp("created_at").getTime()));
        twit.setPhotoLink(resultSet.getString("photo_link"));
        if(resultSet.getString("tags") != null) {
            twit.setHashTags(Arrays.stream(resultSet.getString("tags").split(" "))
                    .filter(tag -> !tag.isEmpty())
                    .map(Tag::new)
                    .collect(Collectors.toList()));
        }
        if (resultSet.getString("likes") != null) {
            twit.setLikes(Arrays.stream(resultSet.getString("likes").split(" "))
                    .map(value -> new Like(twit.getId(), Long.parseLong(value)))
                    .collect(Collectors.toList()));
        }

        return twit;
    }

    public static User parseUserFromRS(ResultSet resultSet) throws SQLException {
        User user = new User();

        user.setId(resultSet.getLong("user_id"));
        user.setName(resultSet.getString("name"));

        return user;
    }

    public static Tag parseTagFromRS(ResultSet resultSet) throws SQLException {
        Tag tag = new Tag();

        tag.setId(resultSet.getLong("tag_id"));
        tag.setBody(resultSet.getString("body"));

        return tag;
    }
}
