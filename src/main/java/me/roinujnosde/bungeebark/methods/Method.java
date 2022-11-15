package me.roinujnosde.bungeebark.methods;

import com.google.common.io.ByteArrayDataInput;
import com.velocitypowered.api.proxy.ServerConnection;
import com.velocitypowered.api.proxy.messages.ChannelIdentifier;
import me.roinujnosde.bungeebark.BungeeBark;

public abstract class Method {

    protected final BungeeBark plugin;

    public Method(BungeeBark plugin) {
        this.plugin = plugin;
    }

    public abstract void process(ChannelIdentifier identifier, ServerConnection connection, ByteArrayDataInput input);
}
