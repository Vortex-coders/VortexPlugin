package ru.rachie.api.servernetwork;

import arc.net.Connection;
import arc.struct.ObjectMap;
import arc.util.io.Reads;
import arc.util.io.Writes;

public abstract class Packet {
    public static ObjectMap<String, Packet> packets = new ObjectMap<>();

    public String name;

    public Packet(String name ) {
        this.name = name;
        packets.put(this.name, this);
    }

    public void read(Reads read){}
    public void write(Writes write){}

    public void handleClient(){}
    public void handleServer(Connection con){}

    public void handled(){}
}
