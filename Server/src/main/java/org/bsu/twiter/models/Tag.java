package org.bsu.twiter.models;

import java.util.Objects;

public class Tag {
    private long id;
    private String body;

    public Tag() {}

    public Tag(String body) {
        this.body = body;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Tag)) return false;
        Tag tag = (Tag) o;
        return body.equals(tag.body);
    }

    @Override
    public int hashCode() {
        return Objects.hash(body);
    }
}
