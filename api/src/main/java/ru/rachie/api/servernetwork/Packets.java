package ru.rachie.api.servernetwork;

import arc.net.Connection;
import arc.util.Log;
import arc.util.io.Reads;
import arc.util.io.Writes;

public class Packets {
    public static MessagePacket messagePacket = new MessagePacket();

    public static void load() {

    }

    public static class MessagePacket extends Packet {
        public boolean isGlobal;
        public String data;

        public MessagePacket() {
            super("MessagePacket");
        }

        @Override
        public void read(Reads read) {
            super.read(read);
            isGlobal = read.bool();
            data = read.str();
        }

        @Override
        public void write(Writes write) {
            super.write(write);
            write.bool(isGlobal);
            write.str(data);
        }

        @Override
        public void handleClient() {
            super.handleClient();
            Log.info(data);
        }

        @Override
        public void handleServer(Connection con) {
            super.handleServer(con);
            if (isGlobal) {
                HubServer hub = ServerState.as();
                for (Connection connection : hub.server.getConnections())
                    if (connection != con)
                        hub.sendPacket(this, connection);
            } else {
                Log.info(data);
            }
        }
    }
}
