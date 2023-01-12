package com.amairovi.netty_in_action.chapter_12;

import com.amairovi.netty_in_action.Listing;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.util.logging.Logger;

import static io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler.ServerHandshakeStateEvent.HANDSHAKE_COMPLETE;

@Listing("12.2")
public class TextWebSocketFrameHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    private static final Logger log = Logger.getLogger(TextWebSocketFrameHandler.class.getName());

    private final ChannelGroup group;
    private final ChatService chatService;

    public TextWebSocketFrameHandler(final ChannelGroup group, final ChatService chatService) {
        this.group = group;
        this.chatService = chatService;
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt == HANDSHAKE_COMPLETE) {
            ctx.pipeline().remove(HttpRequestHandler.class);
            group.writeAndFlush(new TextWebSocketFrame("Client " + ctx.channel() + " joined"));
            group.add(ctx.channel());
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }

    @Override
    public void channelInactive(final ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        log.info(ctx.channel().id() + "is inactive");
        chatService.removeUser(ctx.channel());
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) {
        String username = chatService.getUsername(ctx.channel()) + ": ";

        ByteBuf buffer = Unpooled.buffer(username.length() + msg.content().readableBytes());
        buffer.writeBytes(username.getBytes());
        buffer.writeBytes(msg.content());
        TextWebSocketFrame frameWithUsername = msg.replace(buffer);
        group.writeAndFlush(frameWithUsername.retain());
    }
}
