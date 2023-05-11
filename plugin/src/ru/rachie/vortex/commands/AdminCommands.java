package ru.rachie.vortex.commands;

import arc.util.CommandHandler.CommandRunner;
import mindustry.gen.Player;
import ru.rachie.api.bundles.Bundles;
import ru.rachie.vortex.Vars;

public class AdminCommands {
    public static void load() {

    }

    private static void register(String name, CommandRunner<Player> runner) {
        Vars.client.<Player>register(
                name,
                Bundles.get("commands." + name + ".parameters"),
                Bundles.get("commands." + name + ".description"),
                (args, player) -> {
                    if (!player.admin) {
                        Bundles.send(player, "commands.permissions-denied");
                        return;
                    }

                    runner.accept(args, player);
                });
    }
}
