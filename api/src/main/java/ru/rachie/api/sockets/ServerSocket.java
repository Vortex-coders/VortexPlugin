package ru.rachie.api.sockets;

import arc.Events;
import arc.net.Connection;
import arc.net.DcReason;
import arc.net.NetListener;
import arc.net.Server;
import arc.util.Log;

import java.io.IOException;

public class ServerSocket implements SocketObject {
    private final int port;
    private final Server server;

    public ServerSocket(int port) {
        this.port = port;
        this.server = new Server(32768, 16384, new EventSerializer());

        this.server.addListener(new ServerSocketListener());
    }

    public int getPort() {
        return port;
    }

    public Server getServer() {
        return server;
    }

    @Override
    public void sendEvent(Object object) {
        server.sendToAllTCP(object);
    }

    @Override
    public void connect() throws IOException {
        server.bind(port);
        server.start();
    }

    @Override
    public void disconnect() {
        server.close();
    }

    protected static class ServerSocketListener implements NetListener {
        @Override
        public void connected(Connection connection) {
            if (!connection.getRemoteAddressTCP().getAddress().isLoopbackAddress()) {
                connection.close(DcReason.closed);
                Log.info("Client has kicked because not local. IP: @ ID: @", connection.getRemoteAddressTCP(), connection.getID());
                return;
            }

            Log.info("Client has connected. IP: @ ID: @", connection.getRemoteAddressTCP(), connection.getID());
        }

        @Override
        public void disconnected(Connection connection, DcReason reason) {
            Log.info("Client has disconnected. IP: @ ID: @ REASON: @", connection.getRemoteAddressTCP(), connection.getID(), reason);
        }

        @Override
        public void received(Connection connection, Object object) {
            Events.fire(object);
        }
    }
}
