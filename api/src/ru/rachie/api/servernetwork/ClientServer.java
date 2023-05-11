package ru.rachie.api.servernetwork;

import arc.net.Client;
import arc.net.Connection;
import arc.net.NetListener;
import arc.util.Log;
import arc.util.Threads;

import java.net.InetAddress;

/** Creates client.
 **/
public class ClientServer implements Server{
    public Client client;

    public ClientServer() {
        this(32 * 1024, 32 * 1024);
    }

    public ClientServer(int wbs, int ibs) {
        try {
            Packets.load();

            client = new Client(wbs, ibs, new PacketSerializer());
            client.addListener(new NetListener() {
                @Override
                public void received(Connection connection, Object object) {
                    NetListener.super.received(connection, object);
                    if (object instanceof Packet packet) {
                        packet.handleClient();
                        packet.handled();
                    }
                }
            });

            client.start();

            ServerState.state = 2;
            ServerState.server = this;

            Log.info("Client created.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void connect(InetAddress adr, int port) {
        Threads.daemon(() -> {
            try {
                client.connect(5000, adr, port);
                Log.info("Connected at @ to port @.", adr, port);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void sendPacket(Packet packet) {
        client.sendTCP(packet);
    }
}
