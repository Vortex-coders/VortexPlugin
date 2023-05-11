package ru.rachie.vortex;

import arc.Core;
import arc.files.Fi;
import arc.util.Log;
import arc.util.serialization.Json;
import arc.util.serialization.JsonReader;
import arc.util.serialization.JsonWriter;
import mindustry.net.Administration;

public class Config {
    public String name;
    public String description;
    public int port;
    public String remote;
    public int remotePort;

    public Config() {

    }

    public Config(String n, String d, int p, String r, int rp) {
        name = n;
        description = d;
        port = p;
        remote = r;
        remotePort = rp;
    }

    public static void load(Fi configFile) {
        try {
            Json json = new Json();
            if (!configFile.exists()) {
                json.setWriter(new JsonWriter(configFile.writer(false)));
                json.writeValue(new Config("Survival", "A survival server", 6567, "localhost", 6466));
                json.getWriter().close();
                Log.info("The configuration file has been regenerated, make sure you fill it in before restarting the server.");
                System.exit(0);
            } else {
                Vars.config = json.fromJson(Config.class, configFile);
            }
        } catch (Exception exception) {
            Log.err("Configuration load error.", exception);
            System.exit(0);
        }
    }

    public static void setGameRules() {
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