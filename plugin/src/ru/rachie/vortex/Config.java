package ru.rachie.vortex;

import arc.files.Fi;
import arc.struct.ObjectMap;
import arc.util.Log;
import mindustry.net.Administration;
import ru.rachie.api.config.GenericConfig;

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
        Administration.Config.autoPause.set(true);

        Administration.Config.serverName.set(Vars.config.name);
        Administration.Config.desc.set(Vars.config.description);

        Administration.Config.motd.set("off");
        Administration.Config.showConnectMessages.set(false);
        Administration.Config.logging.set(true);
        Administration.Config.strict.set(true);
        Administration.Config.antiSpam.set(true);

        Administration.Config.interactRateWindow.set(1);
        Administration.Config.interactRateLimit.set(15);
        Administration.Config.messageRateLimit.set(1);
        Administration.Config.packetSpamLimit.set(250);

        Administration.Config.interactRateKick.set(15);
        Administration.Config.messageSpamKick.set(5);

        Administration.Config.snapshotInterval.set(250);
    }
}