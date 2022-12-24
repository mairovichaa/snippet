package com.amairovi.netty_in_action.chapter_4;

import com.amairovi.netty_in_action.Listing;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.ServerChannel;
import io.netty.channel.oio.OioEventLoopGroup;
import io.netty.channel.socket.oio.OioServerSocketChannel;

@Listing("4.3")
public class NettyOioServer extends NettyServer {

    @Override
    protected EventLoopGroup getEventLoopGroup() {
        return new OioEventLoopGroup();
    }

    @Override
    protected Class<? extends ServerChannel> getChannelClass() {
        return OioServerSocketChannel.class;
    }
}
