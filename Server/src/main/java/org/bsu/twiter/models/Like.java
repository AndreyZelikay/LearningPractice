package org.bsu.twiter.models;

import java.util.Objects;

public class Like {
    private long twitId;
    private long userId;

    public Like() {}

    public Like(Long twitId, Long userId) {
        this.twitId = twitId;
        this.userId = userId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getTwitId() {
        return twitId;
    }

    public void setTwitId(long twitId) {
        this.twitId = twitId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Like)) return false;
        Like like = (Like) o;
        return twitId == like.twitId &&
                userId == like.userId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(twitId, userId);
    }
}
