package com.amairovi.context.test1.scope;

import lombok.Getter;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.Scope;

import java.util.Map;
import java.util.Objects;

@Getter
public class CustomScope implements Scope {

    public final static String CUSTOM_SCOPE_NAME = "custom";

    private int counter = 0;

    @Override
    public Object get(String name, ObjectFactory<?> objectFactory) {
        if (Objects.equals(name, "counter")) {
            return counter;
        }

        counter++;
        return objectFactory.getObject();
    }

    @Override
    public Object remove(String name) {
        if (Objects.equals(name, "counter")) {
            int swap = counter;
            counter = 0;
            return swap;
        }
        return null;
    }

    @Override
    public void registerDestructionCallback(String name, Runnable callback) {

    }

    @Override
    public Object resolveContextualObject(String key) {
        return null;
    }

    @Override
    public String getConversationId() {
        return null;
    }

}
