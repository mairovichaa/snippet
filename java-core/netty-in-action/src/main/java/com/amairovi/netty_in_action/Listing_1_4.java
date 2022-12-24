package com.amairovi.netty_in_action;


public class Listing_1_4 {
    public static void main(String[] args) {
        //TODO fix when will know how to create channel
//        Channel channel = null;
//        ChannelFuture future = channel.connect(new InetSocketAddress("192.168.0.1", 1000));
//        future.addListener(new ChannelFutureListener() {
//            @Override
//            public void operationComplete(final ChannelFuture future) throws Exception {
//                if (future.isSuccess()) {
//                    ByteBuf buffer = Unpooled.copiedBuffer("Hello", Charset.defaultCharset());
//                    ChannelFuture wf = future.channel().writeAndFlush(buffer);
//                } else {
//                    Throwable cause = future.cause();
//                    cause.printStackTrace();
//                }
//            }
//        });
    }
}
