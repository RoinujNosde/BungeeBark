package me.roinujnosde.bungeebark.methods;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.velocitypowered.api.proxy.ServerConnection;
import com.velocitypowered.api.proxy.messages.ChannelIdentifier;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import com.velocitypowered.api.proxy.server.ServerInfo;
import me.roinujnosde.bungeebark.BungeeBark;

import java.util.stream.Collectors;

public class GetServers extends Method {

    public GetServers(BungeeBark plugin) {
        super(plugin);
    }

    @SuppressWarnings("UnstableApiUsage")
    @Override
    public void process(ChannelIdentifier identifier, ServerConnection connection, ByteArrayDataInput input) {
        ByteArrayDataOutput output = ByteStreams.newDataOutput();
        output.writeUTF("GetServers");
        String servers = plugin.getProxy().getAllServers().stream().map(RegisteredServer::getServerInfo)
                .map(ServerInfo::getName).collect(Collectors.joining(", "));
        output.writeUTF(servers);

        connection.sendPluginMessage(identifier, output.toByteArray());
    }
}
