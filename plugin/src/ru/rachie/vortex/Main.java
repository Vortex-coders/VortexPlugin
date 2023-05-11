package ru.rachie.vortex;

import arc.files.Fi;
import arc.struct.ObjectMap;
import arc.util.CommandHandler;
import arc.util.Log;
import arc.util.Time;
import mindustry.mod.Plugin;
import ru.rachie.api.servernetwork.ClientServer;
import ru.rachie.api.servernetwork.Packets;
import ru.rachie.api.servernetwork.ServerState;
import ru.rachie.vortex.commands.AdminCommands;
import ru.rachie.vortex.commands.ClientCommands;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;

public class Main extends Plugin {
    public ClientServer client;

    public void init() {
        Log.info("Initialising the plug-in...");
        Time.mark();

        Vars.config = new Config(new Fi("vortex.json"), ObjectMap.of(
                "name", "Survival",
                "description", "A survival server",
                "port", 6567,
                "remote", "localhost",
                "remotePort", 6466));
        Config.setRules();

        Log.info("Connecting to server at @ @", Vars.config.remote, Vars.config.remotePort);
        new ClientServer();
        try {
            ServerState.<ClientServer>as().connect(InetAddress.getByName(Vars.config.remote), Vars.config.remotePort);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

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