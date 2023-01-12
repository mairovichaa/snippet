package com.amairovi.netty_in_action.chapter_13.broadcaster;

import com.amairovi.netty_in_action.Listing;
import com.amairovi.netty_in_action.chapter_13.LogEvent;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Listing("13.2")
public class LogEventEncoder extends MessageToMessageEncoder<LogEvent> {

    private final InetSocketAddress remoteAddress;

    public LogEventEncoder(final InetSocketAddress remoteAddress) {
        this.remoteAddress = remoteAddress;
    }


    @Override
    protected void encode(final ChannelHandlerContext ctx, final LogEvent logEvent, final List<Object> out) throws Exception {
        byte[] file = logEvent.getLogfile().getBytes(StandardCharsets.UTF_8);
        byte[] msg = logEvent.getMsg().getBytes(StandardCharsets.UTF_8);
        ByteBuf buf = ctx.alloc()
                .buffer(file.length + msg.length + 1);
        buf.writeBytes(file);
        buf.writeByte(LogEvent.SEPARATOR);
        buf.writeBytes(msg);
        out.add(new DatagramPacket(buf, remoteAddress));
    }
}
