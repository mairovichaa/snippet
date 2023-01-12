package com.amairovi.netty_in_action.chapter_12;

import com.amairovi.netty_in_action.Listing;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

@Listing("12.3")
public class ChatServerInitializer extends ChannelInitializer<Channel> {

    private final ChannelGroup group;
    private final ChatService chatService;

    public ChatServerInitializer(final ChannelGroup group, final ChatService chatService) {
        this.group = group;
        this.chatService = chatService;
    }

    @Override
    protected void initChannel(final Channel ch) throws Exception {
        System.out.println("init channel");
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new HttpServerCodec());
        pipeline.addLast(new ChunkedWriteHandler());
        pipeline.addLast(new HttpObjectAggregator(64 * 1024));
        pipeline.addLast(new HttpRequestHandler("/ws", chatService));
        pipeline.addLast(new WebSocketServerProtocolHandler("/ws", true));
        pipeline.addLast(new TextWebSocketFrameHandler(group, chatService));
    }
}
