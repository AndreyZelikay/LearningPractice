package org.bsu.twiter.dao;

import org.bsu.twiter.forms.TwitsFilterForm;
import org.bsu.twiter.models.Twit;

import java.util.List;

public interface TwitDAO extends CRUD<Twit> {
    List<Twit> getTwits(TwitsFilterForm form);
}