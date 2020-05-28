package org.bsu.twiter.dao;

public class SQLQueries {
    public static final String TWITS_SELECT =
            "select \n" +
            "  s1.name,\n" +
            "  s1.post_id,\n" +
            "  s1.description,\n" +
            "  s1.created_at,\n" +
            "  s1.photo_link,\n" +
            "  s1.user_id,\n" +
            "  s1.tags,\n" +
            "  group_concat(post_like.user_id separator ' ') as likes \n" +
            "from \n" +
            "  (select user.name,\n" +
            "    post.post_id,\n" +
            "    post.description,\n" +
            "    post.created_at,\n" +
            "    post.photo_link,\n" +
            "        user.user_id,\n" +
            "    group_concat(body, ' ' order by body separator '') as tags\n" +
            "  from\n" +
            "    user inner join post on post.user_id = user.user_id\n" +
            "    left join post_tag on post.post_id = post_tag.post_id\n" +
            "    left join hash_tag on hash_tag.tag_id = post_tag.tag_id\n" +
            "  group by post.post_id)\n" +
            "  s1 left join post_like on s1.post_id = post_like.post_id\n" +
            "group by s1.post_id";

    public static final String GET_MAX_TAG_ID = "select max(tag_id) as max from hash_tag";

    public static final String GET_MAX_TWIT_ID = "select max(post_id) as max from post";

    public static final String GET_MAX_USER_ID = "select max(user_id) as max from user";

    public static final String SAVE_TWIT = "insert into post (post_id, user_id, description, created_at, photo_link) value (?,?,?,?,?)";

    public static final String SAVE_TAG = "insert into hash_tag (tag_id, body) VALUE (?,?)";

    public static final String SAVE_POST_TAG_CONNECTION = "insert into post_tag (post_id, tag_id) value (?,?)";

    public static final String DELETE_POST_TAG_CONNECTION = "delete from post_tag where post_id = ? and tag_id = ?";

    public static final String GET_TAG_IDS_BY_POST_ID = "select * from post_tag where post_id = ?";

    public static final String FIND_TAG_BY_BODY = "select * from hash_tag where body = ?";

    public static final String FIND_TAG_BY_ID = "select * from hash_tag where tag_id = ?";

    public static final String UPDATE_POST_DESCRIPTION = "update post set description = ? where post_id = ?";

    public static final String DELETE_POST_BY_ID = "delete from post where post_id = ?";

    public static final String FIND_USER_BY_ID = "select * from user where user_id = ?";

    public static final String DELETE_USER_BY_ID = "delete from user where user_id = ?";

    public static final String UPDATE_USER = "update user set name = ? where user_id = ?";

    public static final String SAVE_USER = "insert into user (name) VALUE (?)";

    public static final String SAVE_LIKE = "insert into post_like (post_id, user_id) VALUE (?,?)";

    public static final String DELETE_LIKE = "delete from post_like where post_id = ? and user_id = ?";

    public static final String FIND_LIKE = "select * from post_like where post_id = ? and user_id = ?";

    public static final String FIND_USER_BY_NAME_AND_PASSWORD = "select * from user where name = ? and password = ?";
}
