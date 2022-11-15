package me.roinujnosde.bungeebark.methods;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.velocitypowered.api.proxy.ServerConnection;
import com.velocitypowered.api.proxy.messages.ChannelIdentifier;
import me.roinujnosde.bungeebark.BungeeBark;

public class UUIDOther extends Method {

    public UUIDOther(BungeeBark plugin) {
        super(plugin);
    }

    @SuppressWarnings("UnstableApiUsage")
    @Override
    public void process(ChannelIdentifier identifier, ServerConnection connection, ByteArrayDataInput input) {
        plugin.getProxy().getPlayer(input.readUTF()).ifPresent(player -> {
            ByteArrayDataOutput output = ByteStreams.newDataOutput();
            output.writeUTF("UUIDOther");
            output.writeUTF(player.getUsername());
            output.writeUTF(player.getUniqueId().toString());

            connection.sendPluginMessage(identifier, output.toByteArray());
        });
    }
}
