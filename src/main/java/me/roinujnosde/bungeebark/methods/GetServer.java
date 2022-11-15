package me.roinujnosde.bungeebark.methods;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.velocitypowered.api.proxy.ServerConnection;
import com.velocitypowered.api.proxy.messages.ChannelIdentifier;
import me.roinujnosde.bungeebark.BungeeBark;

public class GetServer extends Method {

    public GetServer(BungeeBark plugin) {
        super(plugin);
    }

    @SuppressWarnings("UnstableApiUsage")
    @Override
    public void process(ChannelIdentifier identifier, ServerConnection connection, ByteArrayDataInput input) {
        ByteArrayDataOutput output = ByteStreams.newDataOutput();
        output.writeUTF("GetServer");
        output.writeUTF(connection.getServerInfo().getName());

        connection.sendPluginMessage(identifier, output.toByteArray());
    }
}
