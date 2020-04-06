package org.bsu.twiter.models;

import java.util.Date;
import java.util.List;
import java.util.Objects;

public class Twit {
    private Long id;
    private String description;
    private String author;
    private String photoLink;
    private List<Tag> hashTags;
    private List<Like> likes;
    private Date createdAt;

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPhotoLink() {
        return photoLink;
    }

    public void setPhotoLink(String photoLink) {
        this.photoLink = photoLink;
    }

    public List<Tag> getHashTags() {
        return hashTags;
    }

    public void setHashTags(List<Tag> hashTags) {
        this.hashTags = hashTags;
    }

    public List<Like> getLikes() {
        return likes;
    }

    public void setLikes(List<Like> likes) {
        this.likes = likes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Twit)) return false;
        Twit twit = (Twit) o;
        return description.equals(twit.description) &&
                author.equals(twit.author) &&
                createdAt.equals(twit.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(description, author, createdAt);
    }
}
