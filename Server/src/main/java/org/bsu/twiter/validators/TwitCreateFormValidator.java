package org.bsu.twiter.validators;

import org.bsu.twiter.forms.TwitCreateForm;

import java.util.ArrayList;
import java.util.List;

public class TwitCreateFormValidator implements Validator<TwitCreateForm> {

    private static final int MAX_DESCRIPTION_LENGTH = 250;

    @Override
    public List<String> validate(TwitCreateForm object) {
        List<String> errors = new ArrayList<>();

        if(object.getAuthorId() == null) {
            errors.add("author id field missed");
        }
        if(object.getDescription() == null || object.getDescription().isEmpty()) {
            errors.add("description field missed");
        }
        if(object.getDescription().length() >= MAX_DESCRIPTION_LENGTH) {
            errors.add("description too long");
        }

        return errors;
    }
}
