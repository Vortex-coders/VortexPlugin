package ru.rachie.api.servernetwork;

import arc.net.ArcNetException;
import arc.net.FrameworkMessage;
import arc.net.NetSerializer;
import arc.util.Log;
import arc.util.io.ByteBufferInput;
import arc.util.io.ByteBufferOutput;
import arc.util.io.Reads;
import arc.util.io.Writes;

import java.nio.ByteBuffer;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

public class PacketSerializer implements NetSerializer {
    @Override
    public void write(ByteBuffer buffer, Object object) {
        Writes writes = new Writes(new ByteBufferOutput(buffer));
        if (object instanceof Packet packet) {
            writes.str(packet.name);
            packet.write(writes);
        } else if (object instanceof FrameworkMessage message) {
            writes.str("@frameworkMessage@");
            if (message instanceof FrameworkMessage.RegisterTCP tcp) {
                writes.i(0);
                writes.i(tcp.connectionID);
            } else if (message instanceof FrameworkMessage.RegisterUDP udp) {
                writes.i(1);
                writes.i(udp.connectionID);
            } else if (message instanceof FrameworkMessage.KeepAlive alive) {
                writes.i(2);
            } else {
                writes.i(-1);
                Log.info("Unknown framework message type: @" , message.getClass().getSimpleName());
            }
        } else {
            Log.err("Убери эту хуйню от меняяя~", new ArcNetException());
        }
    }

    @Override
    public Object read(ByteBuffer buffer) {
        Object out = null;
        Reads reads = new Reads(new ByteBufferInput(buffer));
        String name = reads.str();

        if (name.equals("@frameworkMessage@")) {
            int id = reads.i();
            if (id == 0) {
                FrameworkMessage.RegisterTCP tcp = new FrameworkMessage.RegisterTCP();
                tcp.connectionID = reads.i();
                out = tcp;
            } else if (id == 1) {
                FrameworkMessage.RegisterUDP udp = new FrameworkMessage.RegisterUDP();
                udp.connectionID = reads.i();
                out = udp;
            } else if (id == 2) {
                FrameworkMessage.KeepAlive alive = new FrameworkMessage.KeepAlive();
                out = alive;
            } else {
                Log.info("Received unknown framework message with id: @", id);
            }
        } else {
            AtomicReference<Packet> packet = new AtomicReference<>();
            Packet.packets.each((s, p) -> {
                //Log.info("s:@, n:@, s==n:@, p:", s, name, Objects.equals(s, name), p);
                if (Objects.equals(s, name)) {
                    packet.set(p);
                }
            });
            if (packet.get() == null) {
                Log.info("Received unknown packet: @", name);
            } else {
                packet.get().read(reads);
                out = packet.get();
            }
        }

        return out;
    }
}
