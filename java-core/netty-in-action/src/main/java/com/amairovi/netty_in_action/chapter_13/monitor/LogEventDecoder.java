package com.amairovi.netty_in_action.chapter_13.monitor;

import com.amairovi.netty_in_action.Listing;
import com.amairovi.netty_in_action.chapter_13.LogEvent;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Listing("13.6")
public class LogEventDecoder extends MessageToMessageDecoder<DatagramPacket> {
    @Override
    protected void decode(final ChannelHandlerContext ctx, final DatagramPacket datagramPacket, final List<Object> out) {
        ByteBuf content = datagramPacket.content();
        int idx = content.indexOf(0, content.readableBytes(), LogEvent.SEPARATOR);
        String filename = content.slice(0, idx).toString(StandardCharsets.UTF_8);
        String logMsg = content.slice(idx + 1, content.readableBytes()).toString(StandardCharsets.UTF_8);
        LogEvent logEvent = new LogEvent(datagramPacket.sender(), System.currentTimeMillis(), filename, logMsg);
        out.add(logEvent);
    }
}
