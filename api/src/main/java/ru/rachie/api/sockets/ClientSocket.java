package ru.rachie.api.sockets;

import arc.Events;
import arc.net.Client;
import arc.net.Connection;
import arc.net.DcReason;
import arc.net.NetListener;
import arc.util.Log;

import java.io.IOException;

public class ClientSocket implements SocketObject {
    private final int port;
    private final String remote;
    private final Client client;

    public ClientSocket(String remote, int port) {
        this.port = port;
        this.remote = remote;
        this.client = new Client(32768, 16384, new EventSerializer());

        this.client.addListener(new ClientSocketListener());
    }

    public int getPort() {
        return port;
    }

    public String getRemote() {
        return remote;
    }

    public Client getClient() {
        return client;
    }

    @Override
    public void sendEvent(Object object) {
        if (!client.isConnected()) return;
        client.sendTCP(object);
    }

    @Override
    public void connect() throws IOException {
        client.start();
        client.connect(5000, remote, port);
    }

    @Override
    public void disconnect() {
        client.close();
    }

    protected static class ClientSocketListener implements NetListener {
        @Override
        public void connected(Connection connection) {
            Log.info("Connected to the server. IP: @ ID: @", connection.getRemoteAddressTCP(), connection.getID());
        }

        @Override
        public void disconnected(Connection connection, DcReason reason) {
            Log.info("Disconnected from the server. IP: @ ID: @ REASON: @", connection.getRemoteAddressTCP(), connection.getID(), reason);
        }

        @Override
        public void received(Connection connection, Object object) {
            Events.fire(object);
        }
    }
}
