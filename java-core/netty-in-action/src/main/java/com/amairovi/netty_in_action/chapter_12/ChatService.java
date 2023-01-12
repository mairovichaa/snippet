package com.amairovi.netty_in_action.chapter_12;

import io.netty.channel.Channel;
import io.netty.channel.ChannelId;

import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

public class ChatService {
    private static final Logger log = Logger.getLogger(ChatService.class.getName());

    private final ConcurrentHashMap<ChannelId, String> channelToUsername = new ConcurrentHashMap<>();

    public void addUser(String username, Channel channel) {
        channelToUsername.put(channel.id(), username);
    }

    public String getUsername(Channel channel) {
        return channelToUsername.get(channel.id());
    }

    public void removeUser(final Channel channel) {
        String removedUsername = channelToUsername.remove(channel.id());
        log.info("Removed username " + removedUsername);
    }
}
