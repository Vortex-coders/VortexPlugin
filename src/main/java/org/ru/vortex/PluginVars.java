package org.ru.vortex;

import arc.struct.ObjectMap;
import arc.struct.OrderedMap;
import arc.util.CommandHandler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.ru.vortex.modules.Config;
import org.ru.vortex.modules.database.models.PlayerData;

import java.util.HashSet;

import static com.google.gson.FieldNamingPolicy.LOWER_CASE_WITH_DASHES;

public class PluginVars
{

    public static final ObjectMap<String, PlayerData> cachedPlayerData = new ObjectMap<>();
    public static final OrderedMap<String, String> translatorLanguages = new OrderedMap<>();
    public static final String serverLink = "https://discord.gg/pTtQTUQM68";

    public static final String discordAuthString =
            "https://discord.com/api/oauth2/authorize?client_id=1063665247577198702&redirect_uri=http%3A%2F%2Flocalhost%3A8080&response_type=code&scope=identify&state=";

    public static final Gson gson = new GsonBuilder()
            .setFieldNamingPolicy(LOWER_CASE_WITH_DASHES)
            .setPrettyPrinting()
            .serializeNulls()
            .disableHtmlEscaping()
            .create();

    public static final double rtvRatio = 0.6;
    public static final HashSet<String> rtvVotes = new HashSet<>();
    public static boolean rtvEnabled = true;
    public static Config config;
    public static CommandHandler clientCommands, serverCommands;
}
