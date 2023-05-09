package ru.rachie.bot;

import arc.util.Log;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;

import java.io.File;
import java.io.IOException;

@SuppressWarnings("unused")
public record Config(String token, long adminChannel) {
    public static void load(File configFile) {
        try {
            ObjectMapper mapper = new ObjectMapper(new YAMLFactory().disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER));
            mapper.findAndRegisterModules();

            if (!configFile.exists()) {
                mapper.writeValue(configFile, new Config("token", 0));
                Log.info("The configuration file has been regenerated, make sure you fill it in before restarting the server.");
                System.exit(0);
            }

            Vars.config = mapper.readValue(configFile, Config.class);
        } catch (IOException exception) {
            Log.err(exception.getMessage());
            System.exit(1);
        }
    }
}