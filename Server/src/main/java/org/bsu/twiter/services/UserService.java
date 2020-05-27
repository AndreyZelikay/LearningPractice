package org.bsu.twiter.services;

import org.bsu.twiter.dao.UserDAO;
import org.bsu.twiter.dao.UserDAOImpl;
import org.bsu.twiter.models.User;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.Optional;

public class UserService {

    private final UserDAO userDAO;

    public UserService() {
        userDAO = new UserDAOImpl();
    }

    public Optional<User> findUserById(Long id) {
        return userDAO.findById(id);
    }

    public Optional<User> loginUser(User user) {
        return userDAO.findUserByNameAndPassword(user.getName(), DigestUtils.sha512Hex(user.getPassword()));
    }
}
