package org.bsu.twiter.forms;

import org.bsu.twiter.models.Tag;

import java.util.List;

public class TwitUpdateForm {

    private Long id;
    private String description;
    private List<Tag> hashTags;
    private String photoLink;

    public String getDescription() {
        return description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Tag> getHashTags() {
        return hashTags;
    }

    public void setHashTags(List<Tag> hashTags) {
        this.hashTags = hashTags;
    }

    public String getPhotoLink() {
        return photoLink;
    }

    public void setPhotoLink(String photoLink) {
        this.photoLink = photoLink;
    }
}
