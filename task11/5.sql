select * from user where (select count(*) from post where post.user_id = user.user_id) > 3;