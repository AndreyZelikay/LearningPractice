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
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Long id = null;
        try {
            id = Long.parseLong(req.getParameter("id"));
        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }

        if (id != null) {
            Optional<Twit> twitOptional = twitService.getTwit(id);

            if (twitOptional.isPresent()) {
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm a z"));
                resp.getWriter().write(objectMapper.writeValueAsString(twitOptional.get()));
            } else {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String json = req.getReader().lines().collect(Collectors.joining());
        ObjectMapper objectMapper = new ObjectMapper();

        TwitCreateForm form = objectMapper.readValue(json, TwitCreateForm.class);

        resp.getWriter().write(objectMapper.writeValueAsString(twitService.saveTwit(form).orElse(null)));
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        Long id = Long.parseLong(req.getParameter("id"));

        if (twitService.deleteTwit(id)) {
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

        resp.getWriter().write(objectMapper.writeValueAsString(twitService.updateTwit(form).orElse(null)));
    }
}
