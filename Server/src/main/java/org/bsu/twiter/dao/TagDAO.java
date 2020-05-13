package org.bsu.twiter.dao;

import org.bsu.twiter.models.Tag;

import java.util.Optional;

public interface TagDAO {
    Optional<Tag> findByBody(String body);
    Optional<Tag> findById(Long id);
}
