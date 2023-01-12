package com.amairovi.netty_in_action.chapter_10.encoder;

import com.amairovi.netty_in_action.Listing;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

@Listing("10.5")
public class ShortToByteEncoder extends MessageToByteEncoder<Short> {
    @Override
    protected void encode(final ChannelHandlerContext ctx, final Short msg, final ByteBuf out) throws Exception {
        out.writeShort(msg);
    }
}
