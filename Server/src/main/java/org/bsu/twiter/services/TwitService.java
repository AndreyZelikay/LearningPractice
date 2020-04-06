package org.bsu.twiter.services;

import org.bsu.twiter.dao.TwitDAO;
import org.bsu.twiter.dao.TwitDAOImpl;
import org.bsu.twiter.forms.GetObjectFromForm;
import org.bsu.twiter.forms.TwitCreateForm;
import org.bsu.twiter.forms.TwitUpdateForm;
import org.bsu.twiter.forms.TwitsFilterForm;
import org.bsu.twiter.models.Twit;
import org.bsu.twiter.validators.TwitCreateFormValidator;
import org.bsu.twiter.validators.TwitUpdateFormValidator;

import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class TwitService {

    private static Logger logger;

    static {
        try {
            LogManager.getLogManager().readConfiguration(TwitService.class.getClassLoader().getResourceAsStream("logger.properties"));
            logger = Logger.getLogger(TwitDAOImpl.class.getName());
        } catch (Exception ignored) {
        }
    }

    private TwitDAO twitDAO;

    public TwitService() {
        twitDAO = new TwitDAOImpl();
    }

    public boolean saveTwit(TwitCreateForm twitCreateForm) {
        List<String> errors = new TwitCreateFormValidator().validate(twitCreateForm);
        if(errors.isEmpty()) {
            Twit twit = GetObjectFromForm.getObject(twitCreateForm, Twit.class);
            twitDAO.saveTwit(twit);

            return true;
        } else {
            String errorMessage =  "Illegal arguments in form:\n" + String.join(" ", errors);
            logger.log(Level.WARNING, errorMessage);

            return false;
        }
    }

    public List<Twit> getTwits(TwitsFilterForm form) {
        return twitDAO.getTwits(form);
    }

    public Optional<Twit> getTwit(Long id) {
        return twitDAO.getTwitById(id);
    }

    public boolean updateTwit(TwitUpdateForm twitCreateForm) {
        List<String> errors = new TwitUpdateFormValidator().validate(twitCreateForm);

        if(errors.isEmpty()) {
            Twit twit = GetObjectFromForm.getObject(twitCreateForm, Twit.class);
            twitDAO.updateTwit(twit);

            return true;
        } else {
            String errorMessage =  "Illegal arguments in form:\n" + String.join(" ", errors);
            logger.log(Level.WARNING, errorMessage);

            return false;
        }
    }

    public boolean deleteTwit(Long id) {
        return twitDAO.deleteTwitById(id);
    }
}
