package ru.rachie.api.bundles;

import arc.util.Strings;
import mindustry.gen.Player;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Bundles {
    private static final Locale defaultLocale = Locale.of("en");

    public static void send(Player player, String key, Object... args) {
        player.sendMessage(get(player.locale, key, args));
    }

    public static String get(String language, String key, Object... args) {
        Locale locale = Locale.of(language);
        try {
            return Strings.format(ResourceBundle.getBundle("bundle", locale).keySet().stream()
                    .filter(k -> k.equals(key))
                    .findFirst()
                    .map(k -> ResourceBundle.getBundle("messages", locale).getString(k))
                    .orElseGet(() -> ResourceBundle.getBundle("messages", defaultLocale).getString(key)), args);
        } catch (MissingResourceException exception) {
            return "";
        }
    }

    public static String get(String key, Object... args) {
        try {
            return Strings.format(ResourceBundle.getBundle("bundle", defaultLocale).getString(key), args);
        } catch (MissingResourceException exception) {
            return "";
        }
    }
}
