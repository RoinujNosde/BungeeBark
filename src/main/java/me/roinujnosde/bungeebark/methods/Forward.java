package me.roinujnosde.bungeebark.methods;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.velocitypowered.api.proxy.ServerConnection;
import com.velocitypowered.api.proxy.messages.ChannelIdentifier;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import me.roinujnosde.bungeebark.BungeeBark;

import java.util.*;

public class Forward extends Method {

    public Forward(BungeeBark plugin) {
        super(plugin);
    }

    @SuppressWarnings("UnstableApiUsage")
    @Override
    public void process(ChannelIdentifier identifier, ServerConnection connection, ByteArrayDataInput input) {
        String target = input.readUTF();
        String channel = input.readUTF();
        short length = input.readShort();
        byte[] data = new byte[length];
        input.readFully(data);

        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF(channel);
        out.writeShort(data.length);
        out.write(data);

        Collection<RegisteredServer> allServers = new HashSet<>(plugin.getProxy().getAllServers());
        allServers.remove(connection.getServer());
        switch (target) {
            case "ALL":
                plugin.sendMessage(identifier, allServers, out);
                break;
            case "ONLINE":
                plugin.sendMessage(identifier, allServers, out, false);
                break;
            default:
                Optional<RegisteredServer> server = plugin.getProxy().getServer(target);
                server.ifPresent(s -> {
                    plugin.sendMessage(identifier, Collections.singleton(s), out);
                });
        }
    }
}
