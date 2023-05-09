package ru.rachie.api.events;

public class Events {
    public record LoginRequestEvent(String name, String uuid) {
    }
}
