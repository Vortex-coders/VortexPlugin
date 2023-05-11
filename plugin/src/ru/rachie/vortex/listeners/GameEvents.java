package ru.rachie.vortex.listeners;

import mindustry.gen.Groups;
import mindustry.gen.Player;
import ru.rachie.api.bundles.Bundles;

import java.util.Objects;

import static arc.Events.on;
import static ru.rachie.api.events.Events.LoginRequestEvent;

public class GameEvents {
    public static void load() {
        on(LoginRequestEvent.class, event -> {
            Player player = Groups.player.find(p -> Objects.equals(p.uuid(), event.uuid()));
            if (player == null) return;
            player.admin(true);
            Bundles.send(player, "commands.login.successfully");
        });
    }
}
