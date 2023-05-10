package ru.rachie.api.servernetwork;

import arc.net.Connection;
import arc.net.NetListener;
import arc.net.Server;
import arc.util.Log;

/**
 * Creates and binds the server.
 **/
public class HubServer implements ru.rachie.api.servernetwork.Server {
    public Server server;

    public HubServer(int port) {
        this(port, 32 * 1024, 32 * 1024);
    }

    public HubServer(int port, int wbs, int ibs) {
        try {
            server = new Server(wbs, ibs, new PacketSerializer());
            server.addListener(new NetListener() {
                @Override
                public void received(Connection connection, Object object) {
                    NetListener.super.received(connection, object);
                    if (object instanceof Packet packet) {
                        packet.handleServer(connection);
                        packet.handled();
                    }
                }
            });

            server.bind(port);
            server.start();

            ServerState.state = 1;
            ServerState.server = this;

            Log.info("Started server @", server);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendPacket(Packet packet, Connection connection) {
        server.sendToTCP(connection.getID(), packet);
    }

    public void sendPacketToAll(Packet packet) {
        server.sendToAllTCP(packet);
    }
}
