package me.roinujnosde.bungeebark.methods;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.velocitypowered.api.proxy.ServerConnection;
import com.velocitypowered.api.proxy.messages.ChannelIdentifier;
import com.velocitypowered.api.proxy.server.ServerInfo;
import me.roinujnosde.bungeebark.BungeeBark;

public class ServerIP extends Method {
    public ServerIP(BungeeBark plugin) {
        super(plugin);
    }

    @SuppressWarnings("UnstableApiUsage")
    @Override
    public void process(ChannelIdentifier identifier, ServerConnection connection, ByteArrayDataInput input) {
        plugin.getProxy().getServer(input.readUTF()).ifPresent(server -> {
            ServerInfo info = server.getServerInfo();
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("ServerIP");
            out.writeUTF(info.getName());
            out.writeUTF(info.getAddress().getHostString());
            out.writeShort(info.getAddress().getPort());

            connection.sendPluginMessage(identifier, out.toByteArray());
        });

    }
}
