package org.bsu.twiter.services;

import org.bsu.twiter.dao.UserDAO;
import org.bsu.twiter.dao.UserDAOImpl;
import org.bsu.twiter.models.User;

import java.util.Optional;

public class UserService {

    private final UserDAO userDAO;

    public UserService() {
        userDAO = new UserDAOImpl();
    }

    public Optional<User> saveUser(User user) {
        return userDAO.findById(userDAO.save(user));
    }

    public Optional<User> findUserById(Long id) {
        return userDAO.findById(id);
    }

    public Optional<User> findUserByName(String name) {
        return userDAO.findUserByName(name);
    }
}
