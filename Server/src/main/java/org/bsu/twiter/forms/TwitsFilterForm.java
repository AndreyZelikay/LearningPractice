package org.bsu.twiter.forms;

import org.bsu.twiter.models.Tag;

import java.util.Date;
import java.util.List;

public class TwitsFilterForm {
    private Integer skip;
    private Integer top;
    private String author;
    private List<Tag> hashTags;
    private Date fromDate;
    private Date untilDate;

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public List<Tag> getHashTags() {
        return hashTags;
    }

    public void setHashTags(List<Tag> hashTags) {
        this.hashTags = hashTags;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getUntilDate() {
        return untilDate;
    }

    public void setUntilDate(Date untilDate) {
        this.untilDate = untilDate;
    }

    public Integer getSkip() {
        return skip;
    }

    public void setSkip(Integer skip) {
        this.skip = skip;
    }

    public Integer getTop() {
        return top;
    }

    public void setTop(Integer top) {
        this.top = top;
    }
}
