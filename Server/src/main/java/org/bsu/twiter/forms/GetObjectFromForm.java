package org.bsu.twiter.forms;

import org.bsu.twiter.dao.TwitDAOImpl;

import java.lang.reflect.Field;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class GetObjectFromForm {

    private static Logger logger;

    static {
        try {
            LogManager.getLogManager().readConfiguration(GetObjectFromForm.class.getClassLoader().getResourceAsStream("logger.properties"));
            logger = Logger.getLogger(TwitDAOImpl.class.getName());
        } catch (Exception ignored) {
        }
    }

    private GetObjectFromForm() {}

    public static <T> T getObject(Object form, Class<T> objectClass) {
        try {
            T result = objectClass.getConstructor().newInstance();
            for (Field formField : form.getClass().getDeclaredFields()) {
                formField.setAccessible(true);
                Field objectField = objectClass.getDeclaredField(formField.getName());
                objectField.setAccessible(true);
                Object value = formField.get(form);
                objectField.set(result, value);
            }
            return result;
        } catch (Exception e) {
            logger.log(Level.WARNING, "Unable to parse " + form.getClass() + " to " + objectClass.getName() + e.getMessage());
        }
        return null;
    }
}
