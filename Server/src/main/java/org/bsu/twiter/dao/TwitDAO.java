package org.bsu.twiter.dao;

import org.bsu.twiter.forms.TwitsFilterForm;
import org.bsu.twiter.models.Twit;

import java.util.List;
import java.util.Optional;

public interface TwitDAO {
    Optional<Twit> getTwitById(Long id);
    List<Twit> getTwits(TwitsFilterForm form);
    void saveTwit(Twit twit);
    boolean updateTwit(Twit twit);
    boolean deleteTwitById(Long id);
}
