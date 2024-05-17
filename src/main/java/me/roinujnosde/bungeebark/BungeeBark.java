package me.roinujnosde.bungeebark;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PluginMessageEvent;
import com.velocitypowered.api.event.player.ServerPostConnectEvent;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.ServerConnection;
import com.velocitypowered.api.proxy.messages.ChannelIdentifier;
import com.velocitypowered.api.proxy.messages.LegacyChannelIdentifier;
import com.velocitypowered.api.proxy.messages.MinecraftChannelIdentifier;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import me.roinujnosde.bungeebark.methods.Method;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

@Plugin(id = "bungeebark", name = "BungeeBark", version = "0.0.1", authors = "RoinujNosde")
public class BungeeBark {

    private static final ChannelIdentifier LEGACY_CHANNEL = new LegacyChannelIdentifier("BungeeCord");
    private static final ChannelIdentifier MODERN_CHANNEL = MinecraftChannelIdentifier.from("bungeecord:main");

    private final ProxyServer server;
    private final Logger logger;
    private final Map<RegisteredServer, Queue<PluginMessage>> queueMap = new HashMap<>();

    @Inject
    public BungeeBark(ProxyServer server, Logger logger) {
        this.server = server;
        this.logger = logger;
    }

    public ProxyServer getProxy() {
        return server;
    }

    public void sendMessage(ChannelIdentifier identifier,
                            Collection<RegisteredServer> target,
                            ByteArrayDataOutput output) {
        sendMessage(identifier, target, output, true);
    }

    public void sendMessage(ChannelIdentifier identifier,
                            Collection<RegisteredServer> target,
                            ByteArrayDataOutput output,
                            boolean queue) {
        byte[] data = output.toByteArray();

        synchronized (queueMap) {
            for (RegisteredServer rs : target) {
                if (rs.getPlayersConnected().isEmpty() && queue) {
                    queueMap.computeIfAbsent(rs, k -> new LinkedList<>());
                    queueMap.get(rs).add(new PluginMessage(identifier, data));
                    continue;
                }
                rs.sendPluginMessage(identifier, data);
            }
        }
    }

    @Subscribe
    void onInitialize(ProxyInitializeEvent event) {
        server.getChannelRegistrar().register(LEGACY_CHANNEL, MODERN_CHANNEL);
    }

    @Subscribe
    void onMessage(PluginMessageEvent event) {
        if (!event.getIdentifier().equals(LEGACY_CHANNEL) && !event.getIdentifier().equals(MODERN_CHANNEL)) {
            return;
        }
        event.setResult(PluginMessageEvent.ForwardResult.handled());

        if (!(event.getSource() instanceof ServerConnection)) {
            return;
        }
        ServerConnection connection = (ServerConnection) event.getSource();
        ByteArrayDataInput input = event.dataAsDataStream();
        String methodName = input.readUTF();

        try {
            Method method = Class.forName("me.roinujnosde.bungeebark.methods." + methodName)
                    .asSubclass(Method.class).getConstructor(BungeeBark.class).newInstance(this);
            method.process(event.getIdentifier(), connection, input);
        } catch (ClassNotFoundException e) {
            logger.log(Level.SEVERE, String.format("Unknown method: %s", methodName));
        } catch (ReflectiveOperationException e) {
            logger.log(Level.SEVERE, String.format("Error processing method: %s", methodName), e);
        }
    }

    @SuppressWarnings("UnstableApiUsage")
    @Subscribe
    void onJoin(ServerPostConnectEvent event) {
        synchronized (queueMap) {
            Optional<ServerConnection> currentServer = event.getPlayer().getCurrentServer();
            if (!currentServer.isPresent()) {
                return;
            }
            ServerConnection serverConnection = currentServer.get();
            Queue<PluginMessage> queue = queueMap.get(serverConnection.getServer());
            if (queue != null) {
                while (!queue.isEmpty()) {
                    PluginMessage message = queue.poll();
                    serverConnection.sendPluginMessage(message.getIdentifier(), message.getData());
                }
            }
        }
    }

}