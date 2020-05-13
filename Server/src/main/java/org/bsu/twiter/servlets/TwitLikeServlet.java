package org.bsu.twiter.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.bsu.twiter.models.Like;
import org.bsu.twiter.services.TwitService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class TwitLikeServlet extends HttpServlet {

    private TwitService twitService;

    @Override
    public void init() {
        twitService = new TwitService();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String json = req.getReader().lines().reduce("", (accumulator, actual) -> accumulator + actual);
        ObjectMapper objectMapper = new ObjectMapper();

        Like like = objectMapper.readValue(json, Like.class);

        resp.getWriter().write(String.valueOf(twitService.postLike(like)));
    }
}
