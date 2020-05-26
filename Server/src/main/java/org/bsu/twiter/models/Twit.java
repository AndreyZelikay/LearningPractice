package org.bsu.twiter.models;

import java.util.Date;
import java.util.List;
import java.util.Objects;

public class Twit {
    private long id;
    private String description;
    private User author;
    private String photoLink;
    private List<Tag> hashTags;
    private List<Like> likes;
    private Date createdAt;

    public Twit() {}

    public Twit(String description, User author, String photoLink, List<Tag> hashTags, Date createdAt) {
        this.description = description;
        this.author = author;
        this.photoLink = photoLink;
        this.hashTags = hashTags;
        this.createdAt = createdAt;
    }

    public Twit(Long id, String description, String photoLink, List<Tag> hashTags) {
        this.id = id;
        this.description = description;
        this.photoLink = photoLink;
        this.hashTags = hashTags;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
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
