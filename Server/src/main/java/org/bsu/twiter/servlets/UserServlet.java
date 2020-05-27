package org.bsu.twiter.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.bsu.twiter.models.User;
import org.bsu.twiter.services.SecurityConstants;
import org.bsu.twiter.services.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Optional;
import java.util.stream.Collectors;

@WebServlet("/login")
public class UserServlet extends HttpServlet {

    private static UserService userService;

    @Override
    public void init() {
        userService = new UserService();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String json = req.getReader().lines().collect(Collectors.joining());

        ObjectMapper objectMapper = new ObjectMapper();

        User user = objectMapper.readValue(json, User.class);

        Optional<User> userOptional = userService.loginUser(user);

        if (userOptional.isPresent()) {
            HttpSession session = req.getSession(true);
            session.setAttribute("userId", userOptional.get().getId());
            session.setMaxInactiveInterval(SecurityConstants.MAX_COOKIE_AGE);
        }

        resp.getWriter().write(objectMapper.writeValueAsString(userOptional.orElse(null)));
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        session.invalidate();
    }
}
