package ru.rachie.vortex;

import arc.util.CommandHandler;
import arc.util.Log;
import arc.util.Time;
import mindustry.mod.Plugin;
import ru.rachie.api.sockets.ClientSocket;
import ru.rachie.vortex.commands.AdminCommands;
import ru.rachie.vortex.commands.ClientCommands;
import ru.rachie.vortex.listeners.GameEvents;

import java.io.File;

public class Main extends Plugin {
    public void init() {
        Log.info("Initialising the plug-in...");
        Time.mark();

        Config.load(new File("vortex.yaml"));
        Config.setGameRules();

        GameEvents.load();

        try {
            Vars.socket = new ClientSocket(Vars.config.remote(), Vars.config.remotePort());
            Vars.socket.connect();
        } catch (Exception e) {
            Log.err(e);
        }

        Log.info("Plug-in initialised in @ ms", Time.elapsed());
    }

    @Override
    public void registerServerCommands(CommandHandler handler) {
        Vars.server = handler;
    }

    @Override
    public void registerClientCommands(CommandHandler handler) {
        Vars.client = handler;

        AdminCommands.load();
        ClientCommands.load();
    }
}