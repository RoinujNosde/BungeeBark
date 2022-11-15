package me.roinujnosde.bungeebark.methods;

import com.google.common.io.ByteArrayDataInput;
import com.velocitypowered.api.proxy.ServerConnection;
import com.velocitypowered.api.proxy.messages.ChannelIdentifier;
import me.roinujnosde.bungeebark.BungeeBark;

public class ConnectOther extends Method {

    public ConnectOther(BungeeBark plugin) {
        super(plugin);
    }

    @Override
    public void process(ChannelIdentifier identifier, ServerConnection connection, ByteArrayDataInput input) {
        plugin.getProxy().getPlayer(input.readUTF())
                .ifPresent(player -> plugin.getProxy().getServer(input.readUTF())
                        .ifPresent(server -> player.createConnectionRequest(server).fireAndForget()));
    }
}
