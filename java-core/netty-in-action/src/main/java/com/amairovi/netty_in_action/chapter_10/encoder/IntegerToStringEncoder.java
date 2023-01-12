package com.amairovi.netty_in_action.chapter_10.encoder;

import com.amairovi.netty_in_action.Listing;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

@Listing("10.6")
public class IntegerToStringEncoder extends MessageToMessageEncoder<Integer> {
    @Override
    protected void encode(final ChannelHandlerContext ctx, final Integer msg, final List<Object> out) throws Exception {
        out.add(String.valueOf(msg));
    }
}
