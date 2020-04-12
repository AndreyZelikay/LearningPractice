package org.bsu.twiter.dao;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.bsu.twiter.forms.TwitsFilterForm;
import org.bsu.twiter.models.Twit;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class TwitDAOImpl implements TwitDAO {

    private static final String PATH = "TwitList.txt";

    private static Logger logger;

    static {
        try {
            LogManager.getLogManager().readConfiguration(TwitDAOImpl.class.getClassLoader().getResourceAsStream("logger.properties"));
            logger = Logger.getLogger(TwitDAOImpl.class.getName());
        } catch (Exception ignored) {
        }
    }

    private static List<Twit> twitList;

    public TwitDAOImpl() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm a z"));
        try {
            twitList = objectMapper.readValue(TwitDAOImpl.class.getClassLoader().getResourceAsStream(PATH), new TypeReference<>() {
            });
        } catch (IOException | NullPointerException e) {
            logger.log(Level.SEVERE, "twit list read error " + e.getMessage());
        }
    }

    @Override
    public Optional<Twit> getTwitById(Long id) {
        Twit result = null;
        for(Twit twit: twitList) {
            if(twit.getId().equals(id)) {
                result = twit;
                break;
            }
        }
        return Optional.ofNullable(result);
    }

    @Override
    public List<Twit> getTwits(TwitsFilterForm form) {
        return twitList.stream().filter(twit -> (form.getAuthor() == null || twit.getAuthor().equals(form.getAuthor())) &&
                (form.getFromDate() == null || twit.getCreatedAt().after(form.getFromDate()))
                && (form.getUntilDate() == null || twit.getCreatedAt().before(form.getUntilDate()))
                && (form.getHashTags() == null || form.getHashTags().stream().filter(tag -> twit.getHashTags().contains(tag)).count() == form.getHashTags().size()))
                .skip(form.getSkip()).limit(form.getTop())
                .sorted(Comparator.comparing(Twit::getCreatedAt)).collect(Collectors.toList());
    }

    @Override
    public void saveTwit(Twit twit) {
        twit.setId(twitList.get(twitList.size() - 1).getId() + 1);
        twitList.add(twit);
    }

    @Override
    public boolean updateTwit(Twit twit) {
        Optional<Twit> toUpdateOptional = getTwitById(twit.getId());

        if (toUpdateOptional.isPresent()) {
            Twit toUpdate = toUpdateOptional.get();
            toUpdate.setAuthor(Objects.requireNonNullElse(twit.getAuthor(), toUpdate.getAuthor()));
            toUpdate.setDescription(Objects.requireNonNullElse(twit.getDescription(), toUpdate.getDescription()));
            toUpdate.setHashTags(Objects.requireNonNullElse(twit.getHashTags(), toUpdate.getHashTags()));
            toUpdate.setPhotoLink(Objects.requireNonNullElse(twit.getPhotoLink(), toUpdate.getPhotoLink()));
            toUpdate.setCreatedAt(Objects.requireNonNullElse(twit.getCreatedAt(), toUpdate.getCreatedAt()));

            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean deleteTwitById(Long id) {
        try {
            Optional<Twit> removable = twitList.stream().filter(twit -> twit.getId().equals(id)).findAny();
            removable.ifPresent(twitList::remove);

            return true;
        } catch (IndexOutOfBoundsException e) {
            return false;
        }
    }
}
