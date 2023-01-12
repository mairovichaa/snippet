package com.amairovi.netty_in_action.chapter_10.decoder;

import com.amairovi.netty_in_action.Listing;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;

@Listing("10.3")
public class IntegerToStringDecoder extends MessageToMessageDecoder<Integer> {

    @Override
    protected void decode(final ChannelHandlerContext ctx, final Integer msg, final List<Object> out) throws Exception {
        out.add(String.valueOf(msg));
    }
}
