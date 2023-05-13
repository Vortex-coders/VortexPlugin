package ru.rachie.api.servernetwork;

public class ServerState {
    /**
     * 0 - disabled <br>
     * 1 - hub-server <br>
     * 2 - client-server
     **/
    public static int state = 0;
    public static Server server;

    public static boolean isHub() {
        return state == 1;
    }

    public static boolean isClient() {
        return state == 2;
    }

    public static boolean isAvailable() {
        return state != 0;
    }

    public static <T> T as() {
        return (T) server;
    }
}
