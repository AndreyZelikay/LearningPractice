package org.bsu.twiter.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.bsu.twiter.models.User;
import org.bsu.twiter.services.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import java.util.stream.Collectors;

@WebServlet("/user")
public class UserServlet extends HttpServlet {

    private static UserService userService;

    @Override
    public void init() throws ServletException {
        userService = new UserService();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String json = req.getReader().lines().collect(Collectors.joining());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter("name");

        Optional<User> user = userService.findUserByName(name);

        resp.getWriter().write(new ObjectMapper().writeValueAsString(user.orElse(null)));
    }
}
