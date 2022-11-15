package me.roinujnosde.bungeebark.methods;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.velocitypowered.api.proxy.ServerConnection;
import com.velocitypowered.api.proxy.messages.ChannelIdentifier;
import me.roinujnosde.bungeebark.BungeeBark;

public class IPOther extends Method {

    public IPOther(BungeeBark plugin) {
        super(plugin);
    }

    @SuppressWarnings("UnstableApiUsage")
    @Override
    public void process(ChannelIdentifier identifier, ServerConnection connection, ByteArrayDataInput input) {
        plugin.getProxy().getPlayer(input.readUTF()).ifPresent(player -> {
            ByteArrayDataOutput output = ByteStreams.newDataOutput();
            output.writeUTF("IPOther");
            output.writeUTF(player.getUsername());
            output.writeUTF(player.getRemoteAddress().getHostString());
            output.writeInt(player.getRemoteAddress().getPort());

            connection.sendPluginMessage(identifier, output.toByteArray());
        });
    }
}
