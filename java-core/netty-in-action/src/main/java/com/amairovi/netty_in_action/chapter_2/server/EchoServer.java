package com.amairovi.netty_in_action.chapter_2.server;

import com.amairovi.netty_in_action.Listing;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

@Listing("2.2")
public class EchoServer {
    private final int port;

    public EchoServer(final int port) {
        this.port = port;
    }

    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.err.println("Usage: " + EchoServer.class.getSimpleName() + " <port>");
        }
        int port = Integer.parseInt(args[0]);
        new EchoServer(port).start();
    }

    private void start() throws Exception {
        final EchoServerHandler echoServerHandler = new EchoServerHandler();
        NioEventLoopGroup group = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(group)
                    .channel(NioServerSocketChannel.class)
                    .localAddress(port)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(final SocketChannel ch) {
                            ch.pipeline()
                                    .addLast(echoServerHandler);
                        }
                    });
            ChannelFuture f = b.bind().sync();
            System.out.println("Server is bound to port: " + port);
            f.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully().sync();
        }
    }
}
