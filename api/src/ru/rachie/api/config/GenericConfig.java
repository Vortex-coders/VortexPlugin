package ru.rachie.api.config;

import arc.files.Fi;
import arc.struct.ObjectMap;
import arc.util.Log;
import arc.util.serialization.Json;
import arc.util.serialization.JsonReader;
import arc.util.serialization.JsonWriter;

import java.io.IOException;
import java.lang.reflect.Field;

public class GenericConfig {
    public GenericConfig(Fi configFile, ObjectMap<String, Object> defaultValues) {
        try {
            Json json = new Json();
            if (configFile.exists()) {
                json.readFields(this, new JsonReader().parse(configFile));
            } else {
                Log.warn("File @ was regenerated, all settings wasted.", configFile);
                json.setWriter(new JsonWriter(configFile.writer(false)));
                for (Field field : getClass().getFields()) {
                    field.set(this, defaultValues.get(field.getName()));
                }
                json.writeValue(this);
                json.getWriter().close();
            }
        } catch (IllegalAccessException | IOException e) {
            Log.err(e);
        } catch (RuntimeException e) {
            Log.warn("if the error is related to parsing, try deleting @", configFile);
            Log.err(e);
        }
    }

    public <T> T get(String fieldName) {
        try {
            return (T) getClass().getDeclaredField(fieldName).get(this);
        } catch (NoSuchFieldException e) {
            Log.warn("Field @ not founded.", fieldName);
        } catch (IllegalAccessException e) {
            Log.warn("Field @ not accessible from this scope.", fieldName);
        }
        return null;
    }

    public <T> void set(String fieldName, T value) {
        try {
            getClass().getDeclaredField(fieldName).set(this, value);
        } catch (NoSuchFieldException e) {
            Log.warn("Field @ not founded.", fieldName);
        } catch (IllegalAccessException e) {
            Log.warn("Field @ not accessible from this scope.", fieldName);
        }
    }
}
