package com.amairovi.netty_in_action.chapter_10.combined;

import com.amairovi.netty_in_action.Listing;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

@Listing("10.9")
public class CharToByteEncoder extends MessageToByteEncoder<Character> {
    @Override
    protected void encode(final ChannelHandlerContext ctx, final Character msg, final ByteBuf out) throws Exception {
        out.writeChar(msg);
    }
}
