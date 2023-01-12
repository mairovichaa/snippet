package com.amairovi.netty_in_action.chapter_13.monitor;

import com.amairovi.netty_in_action.Listing;
import com.amairovi.netty_in_action.chapter_13.LogEvent;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

@Listing("13.7")
public class LogEventHandler extends SimpleChannelInboundHandler<LogEvent> {

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    protected void channelRead0(final ChannelHandlerContext ctx, final LogEvent event) throws Exception {
        System.out.printf("%d [%s] [%s] : %s%n", event.getReceivedTimestamp(), event.getSource(), event.getLogfile(), event.getMsg());
    }
}
