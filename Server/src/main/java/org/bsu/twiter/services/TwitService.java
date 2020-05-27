package org.bsu.twiter.services;

import org.bsu.twiter.dao.*;
import org.bsu.twiter.forms.TwitCreateForm;
import org.bsu.twiter.forms.TwitUpdateForm;
import org.bsu.twiter.forms.TwitsFilterForm;
import org.bsu.twiter.models.Like;
import org.bsu.twiter.models.Tag;
import org.bsu.twiter.models.Twit;
import org.bsu.twiter.models.User;
import org.bsu.twiter.validators.TwitCreateFormValidator;
import org.bsu.twiter.validators.TwitUpdateFormValidator;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class TwitService {

    private static Logger logger;

    static {
        try {
            LogManager.getLogManager().readConfiguration(TwitService.class.getClassLoader().getResourceAsStream("logger.properties"));
            logger = Logger.getLogger(TwitDAOImpl.class.getName());
        } catch (Exception ignored) {
        }
    }

    private final TwitDAO twitDAO;
    private final LikeDAO likeDAO;
    private final UserDAO userDao;

    public TwitService() {
        twitDAO = new TwitDAOImpl();
        likeDAO = new LikeDAOImpl();
        userDao = new UserDAOImpl();
    }

    public Optional<Twit> saveTwit(TwitCreateForm twitCreateForm) {
        List<String> errors = new TwitCreateFormValidator().validate(twitCreateForm);
        if(errors.isEmpty()) {
            Optional<User> userOptional = userDao.findById(twitCreateForm.getAuthorId());

            if(!userOptional.isPresent()) {
                logger.log(Level.WARNING, "no such user");
                return Optional.empty();
            }

            Twit twit = new Twit(
                    twitCreateForm.getDescription(),
                    userOptional.get(),
                    twitCreateForm.getPhotoLink(),
                    twitCreateForm.getHashTags().stream().map(Tag::new).collect(Collectors.toList()),
                    new Date());

            return twitDAO.findById(twitDAO.save(twit));
        } else {
            String errorMessage =  "Illegal arguments in form:\n" + String.join(" ", errors);
            logger.log(Level.WARNING, errorMessage);

            return Optional.empty();
        }
    }

    public List<Twit> getTwits(TwitsFilterForm form) {
        form.setSkip(Objects.requireNonNullElse(form.getSkip(), 0));
        form.setTop(Objects.requireNonNullElse(form.getTop(), 10));
        return twitDAO.getTwits(form);
    }

    public Optional<Twit> getTwit(Long id) {
        return twitDAO.findById(id);
    }

    public Optional<Twit> updateTwit(TwitUpdateForm twitUpdateForm, Long userId) {
        Optional<Twit> twitOptional = twitDAO.findById(twitUpdateForm.getId());

        if(!twitOptional.isPresent() || twitOptional.get().getAuthor().getId() != userId) {
            return Optional.empty();
        }

        List<String> errors = new TwitUpdateFormValidator().validate(twitUpdateForm);

        if(errors.isEmpty()) {
            Twit twit = new Twit(
                    twitUpdateForm.getId(),
                    twitUpdateForm.getDescription(),
                    twitUpdateForm.getPhotoLink(),
                    twitUpdateForm.getHashTags().stream().map(Tag::new).collect(Collectors.toList()));

            twitDAO.update(twit);

            return twitDAO.findById(twit.getId());
        } else {
            String errorMessage =  "Illegal arguments in form:\n" + String.join(" ", errors);
            logger.log(Level.WARNING, errorMessage);

            return Optional.empty();
        }
    }

    public boolean deleteTwit(Long id, Long userId) {
        Optional<Twit> twitOptional = twitDAO.findById(id);

        if(!twitOptional.isPresent() || twitOptional.get().getAuthor().getId() != userId) {
            return false;
        }

        return twitDAO.delete(id);
    }

    public boolean postLike(Like like) {
        if(likeDAO.isLikePresent(like)) {
            likeDAO.deleteLike(like);
            return false;
        } else {
            likeDAO.saveLike(like);
            return true;
        }
    }
}
