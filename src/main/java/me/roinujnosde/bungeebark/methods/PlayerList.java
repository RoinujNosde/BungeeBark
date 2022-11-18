package me.roinujnosde.bungeebark.methods;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ServerConnection;
import com.velocitypowered.api.proxy.messages.ChannelIdentifier;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import me.roinujnosde.bungeebark.BungeeBark;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

public class PlayerList extends Method {

    public PlayerList(BungeeBark plugin) {
        super(plugin);
    }

    @SuppressWarnings("UnstableApiUsage")
    @Override
    public void process(ChannelIdentifier identifier, ServerConnection connection, ByteArrayDataInput input) {
        String target = input.readUTF();

        ByteArrayDataOutput output = ByteStreams.newDataOutput();
        output.writeUTF("PlayerList");
        Collection<Player> players;

        if ("ALL".equals(target)) {
            output.writeUTF("ALL");
            players = plugin.getProxy().getAllPlayers();
        } else {
            Optional<RegisteredServer> server = plugin.getProxy().getServer(target);
            if (server.isPresent()) {
                output.writeUTF(target);
                players = server.get().getPlayersConnected();
            } else {
                return;
            }
        }
        output.writeUTF(players.stream().map(Player::getUsername).collect(Collectors.joining(", ")));

        connection.sendPluginMessage(identifier, output.toByteArray());
    }
}
