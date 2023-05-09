package ru.rachie.vortex;

import arc.Core;
import arc.util.Log;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;

import java.io.File;
import java.io.IOException;

import static mindustry.net.Administration.Config.*;

@SuppressWarnings("unused")
public record Config(String name, String description, int port, String remote, int remotePort) {
    public static void load(File configFile) {
        try {
            ObjectMapper mapper = new ObjectMapper(new YAMLFactory().disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER));
            mapper.findAndRegisterModules();

            if (!configFile.exists()) {
                mapper.writeValue(configFile, new Config("Survival", "A survival server", 6567, "localhost", 6466));
                Log.info("The configuration file has been regenerated, make sure you fill it in before restarting the server.");
                Core.app.exit();
            }

            Vars.config = mapper.readValue(configFile, Config.class);
        } catch (IOException exception) {
            Log.err(exception.getMessage());
            Core.app.exit();
        }
    }

    public static void setGameRules() {
        autoPause.set(true);

        serverName.set(Vars.config.name);
        desc.set(Vars.config.description);

        motd.set("off");
        showConnectMessages.set(false);
        logging.set(true);
        strict.set(true);
        antiSpam.set(true);

        interactRateWindow.set(1);
        interactRateLimit.set(15);
        messageRateLimit.set(1);
        packetSpamLimit.set(250);

        interactRateKick.set(15);
        messageSpamKick.set(5);

        snapshotInterval.set(250);
    }
}