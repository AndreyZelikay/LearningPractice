package org.bsu.twiter.models;

import java.util.Objects;

public class Like {
    private Long id;
    private String author;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Like)) return false;
        Like like = (Like) o;
        return author.equals(like.author);
    }

    @Override
    public int hashCode() {
        return Objects.hash(author);
    }
}
