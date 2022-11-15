package me.roinujnosde.bungeebark.methods;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.velocitypowered.api.proxy.ServerConnection;
import com.velocitypowered.api.proxy.messages.ChannelIdentifier;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import me.roinujnosde.bungeebark.BungeeBark;

import java.util.Optional;

public class PlayerCount extends Method {

    public PlayerCount(BungeeBark plugin) {
        super(plugin);
    }

    @SuppressWarnings("UnstableApiUsage")
    @Override
    public void process(ChannelIdentifier identifier, ServerConnection connection, ByteArrayDataInput input) {
        String target = input.readUTF();

        ByteArrayDataOutput output = ByteStreams.newDataOutput();
        output.writeUTF("PlayerCount");
        output.writeUTF(target);
        if ("ALL".equals(target)) {
            output.writeInt(plugin.getProxy().getPlayerCount());
        } else {
            Optional<RegisteredServer> server = plugin.getProxy().getServer(target);
            if (!server.isPresent()) {
                return;
            }
            output.writeInt(server.get().getPlayersConnected().size());
        }

        connection.sendPluginMessage(identifier, output.toByteArray());
    }
}
