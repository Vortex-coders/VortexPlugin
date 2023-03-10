package org.ru.vortex.utils;

import mindustry.gen.Player;

import static org.ru.vortex.modules.Bundler.sendLocalized;
import static org.ru.vortex.utils.Timeouts.hasTimeout;

public class Checks
{

    public static boolean ifTimeoutCheck(Player player, String name)
    {
        if (hasTimeout(player, name))
        {
            sendLocalized(player, "has-timeout");
            return true;
        }
        else return false;
    }

    public static boolean notAdminCheck(Player player)
    {
        if (!player.admin)
        {
            sendLocalized(player, "not-admin");
            return true;
        }
        else return false;
    }
}
