package me.roinujnosde.bungeebark.methods;

import com.google.common.io.ByteArrayDataInput;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ServerConnection;
import com.velocitypowered.api.proxy.messages.ChannelIdentifier;
import me.roinujnosde.bungeebark.BungeeBark;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

public class Message extends Method {

    public Message(BungeeBark plugin) {
        super(plugin);
    }

    @Override
    public void process(ChannelIdentifier identifier, ServerConnection connection, ByteArrayDataInput input) {
        String target = input.readUTF();
        String message = input.readUTF();
        TextComponent component = LegacyComponentSerializer.legacySection().deserialize(message);

        if ("ALL".equals(target)) {
            for (Player player : plugin.getProxy().getAllPlayers()) {
                player.sendMessage(component);
            }
        } else {
            plugin.getProxy().getPlayer(target).ifPresent(player -> player.sendMessage(component));
        }
    }
}
