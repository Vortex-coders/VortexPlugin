package ru.rachie.bot;

import arc.files.Fi;
import arc.struct.ObjectMap;
import arc.util.Strings;
import discord4j.common.util.Snowflake;
import discord4j.core.DiscordClient;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.object.entity.channel.Channel;
import ru.rachie.api.events.Events;
import ru.rachie.api.servernetwork.HubServer;
import ru.rachie.api.servernetwork.Packets;

import java.util.Objects;

public class Main {
    public static void main(String[] args) {
        Vars.config = new Config(new Fi("bot.json"), ObjectMap.of("port", 6466));

        new HubServer(Vars.config.port);

        GatewayDiscordClient gateway = DiscordClient.create(Vars.config.token).login().block();

        Channel channel = Objects.requireNonNull(gateway, "Failed to load client")
                .getChannelById(Snowflake.of(Vars.config.channel)).block();

        Events.receivedPacket.add(event -> {
            if (event instanceof Events.ReceivedPacketEvent p) {
                if (p.packet instanceof Packets.LoginPacket loginPacket) {
                    assert channel != null;
                    channel.getRestChannel().createMessage(
                            Strings.format("Player @ with uuid @ tried to connect to server at connection @.",
                                    loginPacket.name, loginPacket.uuid,
                                    loginPacket.from.getRemoteAddressTCP().getAddress()));
                }
            }
        });

        Objects.requireNonNull(gateway, "Failed to load client").onDisconnect().block();
    }
}