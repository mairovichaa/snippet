package com.amairovi.netty_in_action.chapter_13.broadcaster;

import com.amairovi.netty_in_action.Listing;
import com.amairovi.netty_in_action.chapter_13.LogEvent;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;

import java.io.File;
import java.io.RandomAccessFile;
import java.net.InetSocketAddress;

@Listing("13.3")
public class LogEventBroadcaster {
    private final EventLoopGroup group;
    private final Bootstrap bootstrap;
    private final File file;

    public LogEventBroadcaster(InetSocketAddress address, File file) {
        group = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioDatagramChannel.class)
                .option(ChannelOption.SO_BROADCAST, true)
                .handler(new LogEventEncoder(address));
        this.file = file;
    }

    public void run() throws Exception {
        System.out.println("run");
        Channel channel = bootstrap.bind(0).sync().channel();
        System.out.println("Bound");
        long pointer = 0;
        for (; ; ) {
            long len = file.length();
            System.out.println("file length: " + len);
            if (len < pointer) {
                System.out.println("len < pointer");
                // file was reset
                pointer = 0;
            } else if (len > pointer) {
                System.out.println("Read file");
                RandomAccessFile raf = new RandomAccessFile(file, "r");
                raf.seek(pointer);
                String line;
                System.out.println("Before loop");
                while ((line = raf.readLine()) != null) {
                    System.out.println("write");
                    System.out.println(line);
                    channel.writeAndFlush(new LogEvent(null, -1, file.getAbsolutePath(), line));
                }
                pointer = raf.getFilePointer();
                raf.close();
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.interrupted();
                break;
            }
        }
    }

    public void stop() {
        group.shutdownNow();
    }

    public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            throw new IllegalArgumentException();
        }
        LogEventBroadcaster broadcaster = new LogEventBroadcaster(
                new InetSocketAddress(
                        "255.255.255.255",
                        Integer.parseInt(args[0])
                ),
                new File(args[1])
        );

        try {
            broadcaster.run();
        } finally {
            broadcaster.stop();
        }
    }
}

