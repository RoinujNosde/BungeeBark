package me.roinujnosde.bungeebark.methods;

import com.google.common.io.ByteArrayDataInput;
import com.velocitypowered.api.proxy.ServerConnection;
import com.velocitypowered.api.proxy.messages.ChannelIdentifier;
import me.roinujnosde.bungeebark.BungeeBark;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

public class KickPlayer extends Method {

    public KickPlayer(BungeeBark plugin) {
        super(plugin);
    }

    @Override
    public void process(ChannelIdentifier identifier, ServerConnection connection, ByteArrayDataInput input) {
        plugin.getProxy().getPlayer(input.readUTF()).ifPresent(player -> {
            String reason = input.readUTF();
            player.disconnect(LegacyComponentSerializer.legacySection().deserialize(reason));
        });
    }
}
