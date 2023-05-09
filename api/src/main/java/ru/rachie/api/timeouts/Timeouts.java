package ru.rachie.api.timeouts;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public class Timeouts {
    private static final Map<String, Map<String, Instant>> timeouts = new HashMap<>();

    public static void setTimeout(String playerId, String command, Duration duration) {
        Instant expirationTime = Instant.now().plus(duration);
        timeouts.computeIfAbsent(playerId, k -> new HashMap<>()).put(command, expirationTime);
    }

    public static boolean isTimeoutExpired(String playerId, String command) {
        Map<String, Instant> playerTimeouts = timeouts.get(playerId);
        if (playerTimeouts == null) {
            return true;
        }

        Instant expirationTime = playerTimeouts.get(command);
        if (expirationTime == null) {
            return true;
        }

        return Instant.now().isAfter(expirationTime);
    }
}
