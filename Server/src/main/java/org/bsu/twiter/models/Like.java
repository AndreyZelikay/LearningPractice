package org.bsu.twiter.models;

import java.util.Objects;

public class Like {
    private Long twitId;
    private Long userId;

    public Like() {}

    public Like(Long twitId, Long userId) {
        this.twitId = twitId;
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getTwitId() {
        return twitId;
    }

    public void setTwitId(Long twitId) {
        this.twitId = twitId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Like)) return false;
        Like like = (Like) o;
        return twitId.equals(like.twitId) &&
                userId.equals(like.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(twitId, userId);
    }
}
