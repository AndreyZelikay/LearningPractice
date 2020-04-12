package org.bsu.twiter.validators;

import org.bsu.twiter.forms.TwitUpdateForm;

import java.util.ArrayList;
import java.util.List;

public class TwitUpdateFormValidator implements Validator<TwitUpdateForm> {

    private static final int MAX_DESCRIPTION_LENGTH = 250;

    @Override
    public List<String> validate(TwitUpdateForm object) {
        List<String> errors = new ArrayList<>();

        if(object.getDescription() == null || object.getDescription().isEmpty()) {
            errors.add("description field missed");
        }
        if(object.getDescription().length() >= MAX_DESCRIPTION_LENGTH) {
            errors.add("description too long");
        }

        return errors;
    }
}
