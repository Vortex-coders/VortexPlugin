package ru.rachie.bot;

import arc.files.Fi;
import arc.struct.ObjectMap;
import ru.rachie.api.config.GenericConfig;

@SuppressWarnings("unused")
public class Config extends GenericConfig {
    public String token;
    public int permission;
    public int channel;
    public int port;

    public Config(Fi configFile, ObjectMap<String, Object> defaultValues) {
        super(configFile, defaultValues);
    }
}