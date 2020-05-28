package org.bsu.twiter.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.bsu.twiter.forms.TwitCreateForm;
import org.bsu.twiter.forms.TwitUpdateForm;
import org.bsu.twiter.models.Twit;
import org.bsu.twiter.services.TwitService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Optional;
import java.util.stream.Collectors;

@WebServlet("/tweets")
public class TwitServlet extends HttpServlet {

    private TwitService twitService;

    @Override
    public void init() {
        twitService = new TwitService();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String json = req.getReader().lines().collect(Collectors.joining());
        ObjectMapper objectMapper = new ObjectMapper();

        TwitCreateForm form = objectMapper.readValue(json, TwitCreateForm.class);

        HttpSession session = req.getSession(false);

        form.setAuthorId((Long) session.getAttribute("userId"));

        resp.getWriter().write(objectMapper.writeValueAsString(twitService.saveTwit(form).orElse(null)));
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        Long id = Long.parseLong(req.getParameter("id"));

        HttpSession session = req.getSession(false);

        if (twitService.deleteTwit(id, (Long) session.getAttribute("userId"))) {
            resp.setStatus(HttpServletResponse.SC_OK);
        } else {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws  IOException {
        String json = req.getReader().lines().collect(Collectors.joining());
        ObjectMapper objectMapper = new ObjectMapper();

        TwitUpdateForm form = objectMapper.readValue(json, TwitUpdateForm.class);

        HttpSession session = req.getSession(false);

        resp.getWriter().write(objectMapper.writeValueAsString(twitService.updateTwit(form, (Long) session.getAttribute("userId")).orElse(null)));
    }
}
