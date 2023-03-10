package org.ru.vortex.commands;

import arc.Events;
import arc.util.CommandHandler.CommandRunner;
import mindustry.Vars;
import mindustry.game.EventType;
import mindustry.game.Team;
import mindustry.gen.*;
import org.ru.vortex.modules.Bundler;

import static arc.util.Strings.format;
import static mindustry.gen.Call.openURI;
import static org.ru.vortex.PluginVars.*;
import static org.ru.vortex.modules.Bundler.*;
import static org.ru.vortex.utils.Checks.ifTimeoutCheck;
import static org.ru.vortex.utils.Oauth.getAuthLink;
import static org.ru.vortex.utils.Oauth.isAuthorized;
import static org.ru.vortex.utils.Timeouts.timeout;
import static org.ru.vortex.utils.Utils.findTranslatorLanguage;

public class ClientCommands
{

    public static void init()
    {
        register("discord", (args, player) -> openURI(player.con, serverLink));

        register(
                "register",
                (args, player) ->
                {
                    if (isAuthorized(player)) return;

                    openURI(player.con, getAuthLink(player));
                }
        );

        register(
                "tr",
                (args, player) ->
                {
                    var data = cachedPlayerData.get(player.uuid());

                    switch (args[0].toLowerCase())
                    {
                    case "off" ->
                    {
                        data.translatorLanguage = "off";
                        Bundler.sendLocalized(player, "commands.tr.off");
                    }
                    case "auto" ->
                    {
                        var lang = findTranslatorLanguage(player.locale);
                        data.translatorLanguage = lang == null ? "en" : lang;

                        Bundler.sendLocalized(player, "commands.tr.success", translatorLanguages.get(data.translatorLanguage));
                    }
                    default ->
                    {
                        var lang = findTranslatorLanguage(args[0]);
                        if (lang == null)
                        {
                            Bundler.sendLocalized(player, "commands.tr.not-found");
                            break;
                        }

                        data.translatorLanguage = lang;

                        Bundler.sendLocalized(player, "commands.tr.success", translatorLanguages.get(data.translatorLanguage));
                    }
                    }

                    cachedPlayerData.put(player.uuid(), data);
                }
        );

        register(
                "rtv",
                (args, player) ->
                {
                    if (player.admin())
                    {
                        rtvEnabled = args.length != 1 || !args[0].equals("off");
                    }

                    if (!rtvEnabled)
                    {
                        sendLocalized(player, "commands.rtv.disabled");
                        return;
                    }

                    if (rtvVotes.contains(player.uuid()))
                    {
                        sendLocalized(player, "commands.rtv.already-voted");
                        return;
                    }

                    rtvVotes.add(player.uuid());

                    int cur = rtvVotes.size();
                    int req = (int) Math.ceil(rtvRatio * Groups.player.size());

                    sendLocalizedAll("commands.rtv.change-map", player.name, cur, req);

                    if (cur < req) return;
                    rtvVotes.clear();

                    sendLocalizedAll("commands.rtv.vote-passed");
                    Events.fire(new EventType.GameOverEvent(Team.crux));
                }
        );

        register(
                "hub",
                (args, player) ->
                {
                    if (!config.hubIp.isEmpty())
                    {
                        Vars.net.pingHost(
                                config.hubIp,
                                config.hubPort,
                                host -> Call.connect(player.con, host.address, host.port),
                                e -> getLocalized(player, "commands.hub.error")
                        );
                    }
                }
        );
    }

    private static void register(String name, CommandRunner<Player> runner)
    {
        clientCommands.<Player>register(
                name,
                getLocalized(format("commands.@.parameters", name)),
                getLocalized(format("commands.@.description", name)),
                (args, player) ->
                {
                    if (ifTimeoutCheck(player, name)) return;
                    runner.accept(args, player);
                    timeout(player, name);
                }
        );
    }
}
