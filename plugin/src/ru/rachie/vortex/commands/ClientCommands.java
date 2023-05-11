package ru.rachie.vortex.commands;

import arc.util.CommandHandler.CommandRunner;
import mindustry.gen.Player;
import ru.rachie.api.bundles.Bundles;
import ru.rachie.api.events.Events;
import ru.rachie.api.timeouts.Timeouts;
import ru.rachie.vortex.Vars;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

public class ClientCommands {
    public static void load() {
        register("login", (args, player) -> {
            /*if (Vars.socket != null && !player.admin) {
                Vars.socket.sendEvent(new Events.LoginRequestEvent(player.name, player.uuid()));
                Bundles.send(player, "commands.login.wait");
            } else Bundles.send(player, "commands.login.unavailable");*/
        });
    }

    private static void register(String name, CommandRunner<Player> runner) {
        Vars.client.<Player>register(
                name,
                Bundles.get("commands." + name + ".parameters"),
                Bundles.get("commands." + name + ".description"),
                (args, player) -> {
                    if (!Timeouts.isTimeoutExpired(player.name, name)) {
                        Bundles.send(player, "commands.timeout", name);
                        return;
                    }

                    runner.accept(args, player);
                    Timeouts.setTimeout(player.uuid(), name, Duration.of(30, ChronoUnit.SECONDS));
                });
    }
}
