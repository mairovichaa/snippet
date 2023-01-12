package com.amairovi.netty_in_action.chapter_11;

import com.amairovi.netty_in_action.Listing;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslHandler;

import javax.net.ssl.SSLEngine;

@Listing("11.1")
public class SslChannelInitializer extends ChannelInitializer<Channel> {
    private final SslContext sslContext;
    private final boolean startTls;

    public SslChannelInitializer(final SslContext sslContext, final boolean startTls) {
        this.sslContext = sslContext;
        this.startTls = startTls;
    }

    @Override
    protected void initChannel(final Channel ch) throws Exception {
        SSLEngine engine = sslContext.newEngine(ch.alloc());
        SslHandler handler = new SslHandler(engine, startTls);
        ch.pipeline().addFirst("ssl", handler);
    }
}
