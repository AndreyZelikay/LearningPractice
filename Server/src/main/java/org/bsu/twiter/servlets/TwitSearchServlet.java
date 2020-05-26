package org.bsu.twiter.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.bsu.twiter.forms.TwitsFilterForm;
import org.bsu.twiter.models.Twit;
import org.bsu.twiter.services.TwitService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet("/tweets/search")
public class TwitSearchServlet extends HttpServlet {

    private TwitService twitService;

    @Override
    public void init() {
        twitService = new TwitService();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String json = req.getReader().lines().collect(Collectors.joining());
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));

        TwitsFilterForm form = objectMapper.readValue(json, TwitsFilterForm.class);
        List<Twit> result = twitService.getTwits(form);

        resp.getWriter().write(objectMapper.writeValueAsString(result));
    }
}
