package com.amairovi.netty_in_action.chapter_4;

import com.amairovi.netty_in_action.Listing;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.ServerChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

@Listing("4.4")
public class NettyNioServer extends NettyServer {
    @Override
    protected EventLoopGroup getEventLoopGroup() {
        return new NioEventLoopGroup();
    }

    @Override
    protected Class<? extends ServerChannel> getChannelClass() {
        return NioServerSocketChannel.class;
    }
}
