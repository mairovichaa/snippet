package com.amairovi.netty_in_action.chapter_5;

import com.amairovi.netty_in_action.Listing;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.CompositeByteBuf;
import io.netty.buffer.Unpooled;

@Listing("5.4")
public class CompositeBufferPatternUsingCompositeByteBufferBuf {
    public static void main(String[] args) {
        CompositeByteBuf messageBuf = Unpooled.compositeBuffer();
        ByteBuf headerBuf = null; // can be backing or direct
        ByteBuf bodyBuff = null; // can be backing or direct
        messageBuf.addComponents(headerBuf, bodyBuff);
        // ...
        messageBuf.removeComponent(0); // remove the header
        for (ByteBuf buf : messageBuf) {
            System.out.println(buf.toString());
        }
    }
}
