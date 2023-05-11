package ru.rachie.vortex;

import arc.files.Fi;
import arc.util.CommandHandler;
import arc.util.Log;
import arc.util.Time;
import mindustry.mod.Plugin;
import ru.rachie.api.servernetwork.ClientServer;
import ru.rachie.api.servernetwork.Packets;
import ru.rachie.vortex.commands.AdminCommands;
import ru.rachie.vortex.commands.ClientCommands;
import ru.rachie.vortex.listeners.GameEvents;

public class Main extends Plugin {
    public ClientServer client;

    public void init() {
        Log.info("Initialising the plug-in...");
        Time.mark();

        Config.load(new Fi("vortex.json"));
        Config.setGameRules();

        GameEvents.load();

        Log.info("Plug-in initialised in @ ms", Time.elapsed());
    }

    @Override
    public void registerServerCommands(CommandHandler handler) {
        Vars.server = handler;

        handler.register("message", "<arg1> <arg2>", "sends a message", s -> {
            Packets.messagePacket.data = s[0];
            Packets.messagePacket.isGlobal = s[1].equals("t");
            client.sendPacket(Packets.messagePacket);
        });
    }

    @Override
    public void registerClientCommands(CommandHandler handler) {
        Vars.client = handler;

        handler.register("message", "<arg1> <arg2>",  "sends a message", s -> {
            Packets.messagePacket.data = s[0];
            Packets.messagePacket.isGlobal = s[1].equals("t");
        });

        AdminCommands.load();
        ClientCommands.load();
    }
}