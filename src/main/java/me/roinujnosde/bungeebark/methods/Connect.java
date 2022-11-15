package me.roinujnosde.bungeebark.methods;

import com.google.common.io.ByteArrayDataInput;
import com.velocitypowered.api.proxy.ServerConnection;
import com.velocitypowered.api.proxy.messages.ChannelIdentifier;
import me.roinujnosde.bungeebark.BungeeBark;

public class Connect extends Method {

    public Connect(BungeeBark plugin) {
        super(plugin);
    }

    @Override
    public void process(ChannelIdentifier identifier, ServerConnection connection, ByteArrayDataInput input) {
        String target = input.readUTF();
        plugin.getProxy().getServer(target).ifPresent(server ->
                connection.getPlayer().createConnectionRequest(server).fireAndForget());
    }
}
