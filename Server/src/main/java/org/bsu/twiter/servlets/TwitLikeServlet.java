package org.bsu.twiter.servlets;

import org.bsu.twiter.models.Like;
import org.bsu.twiter.services.TwitService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/tweets/like")
public class TwitLikeServlet extends HttpServlet {

    private TwitService twitService;

    @Override
    public void init() {
        twitService = new TwitService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        long twitId = Long.parseLong(req.getParameter("postId"));

        HttpSession session = req.getSession(false);

        Like like = new Like(twitId, (Long) session.getAttribute("userId"));

        resp.getWriter().write((twitService.postLike(like)) ? "inc" : "dec");
    }
}
