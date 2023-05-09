package ru.rachie.bot;

import arc.Events;
import discord4j.common.util.Snowflake;
import discord4j.core.DiscordClient;
import discord4j.core.GatewayDiscordClient;
import ru.rachie.api.sockets.ServerSocket;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

import static ru.rachie.api.events.Events.LoginRequestEvent;

public class Main {
    public static void main(String[] args) throws IOException {
        Config.load(new File("../bot.yaml"));
        GatewayDiscordClient gateway = DiscordClient.create(Vars.config.token()).login().block();

        Vars.socket = new ServerSocket(6466);
        Vars.socket.connect();

        var channel = Objects.requireNonNull(gateway, "Failed to load client")
                .getChannelById(Snowflake.of(Vars.config.adminChannel())).block();

        Events.on(LoginRequestEvent.class, event -> Objects.requireNonNull(channel, "Couldn't find channel").getRestChannel().createMessage(event.name()).block());
        Objects.requireNonNull(gateway, "Failed to load client").onDisconnect().block();
    }
}