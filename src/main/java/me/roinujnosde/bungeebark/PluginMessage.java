package me.roinujnosde.bungeebark;

import com.velocitypowered.api.proxy.messages.ChannelIdentifier;

public class PluginMessage {

    private final ChannelIdentifier identifier;
    private final byte[] data;

    public PluginMessage(ChannelIdentifier identifier, byte[] data) {
        this.identifier = identifier;
        this.data = data;
    }

    public ChannelIdentifier getIdentifier() {
        return identifier;
    }

    public byte[] getData() {
        return data;
    }
}
