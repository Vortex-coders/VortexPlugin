package ru.rachie.api.events;

import arc.func.Cons;
import arc.struct.Seq;
import ru.rachie.api.servernetwork.Packet;

public class Events {
    public static ReceivedPacketEvent receivedPacket = new ReceivedPacketEvent();

    public static abstract class Event {
        public Seq<Cons<Event>> runs = new Seq<>();

        public void fire() {
            runs.each(r -> r.get(this));
        }

        public void add(Cons<Event> run) {
            runs.add(run);
        }
    }

    public static class ReceivedPacketEvent extends Event {
        public Packet packet;
    }
}
