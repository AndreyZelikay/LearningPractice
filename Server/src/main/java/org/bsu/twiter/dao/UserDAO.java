package org.bsu.twiter.dao;

import org.bsu.twiter.models.User;

import java.util.Optional;

public interface UserDAO extends CRUD<User> {
    Optional<User> findUserByName(String name);
}