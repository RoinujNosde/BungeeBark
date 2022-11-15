package me.roinujnosde.bungeebark.methods;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.velocitypowered.api.proxy.ServerConnection;
import com.velocitypowered.api.proxy.messages.ChannelIdentifier;
import me.roinujnosde.bungeebark.BungeeBark;

public class ForwardToPlayer extends Method {

    public ForwardToPlayer(BungeeBark plugin) {
        super(plugin);
    }

    @SuppressWarnings("UnstableApiUsage")
    @Override
    public void process(ChannelIdentifier identifier, ServerConnection connection, ByteArrayDataInput input) {
        String target = input.readUTF();
        plugin.getProxy().getPlayer(target).ifPresent(player -> {
            String channel = input.readUTF();
            short len = input.readShort();
            byte[] data = new byte[len];
            input.readFully(data);

            ByteArrayDataOutput output = ByteStreams.newDataOutput();
            output.writeUTF(channel);
            output.writeShort(data.length);
            output.write(data);

            player.getCurrentServer().ifPresent(server -> server.sendPluginMessage(identifier, output.toByteArray()));
        });

    }
}
