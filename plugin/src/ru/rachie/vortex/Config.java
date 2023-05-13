package ru.rachie.vortex;

import arc.files.Fi;
import arc.struct.ObjectMap;
import ru.rachie.api.config.GenericConfig;

import static mindustry.net.Administration.Config.*;

public class Config extends GenericConfig {
    public String name;
    public String description;
    public int port;
    public String remote;
    public int remotePort;

    public Config(Fi configFile, ObjectMap<String, Object> defaultValues) {
        super(configFile, defaultValues);
    }

    public static void setRules() {
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