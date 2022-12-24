package com.amairovi.netty_in_action.chapter_5;

import com.amairovi.netty_in_action.Listing;

import java.nio.ByteBuffer;

@Listing("5.3")
public class CompositeBufferPatternUsingByteBuffer {
    public static void main(String[] args) {
        ByteBuffer header = null;
        ByteBuffer body = null;
        // Use an array to hold the message parts
        ByteBuffer[] message = {header, body};
        // Create a new ByteBuffer and use copy to merge the header and body
        ByteBuffer message2 = ByteBuffer.allocate(header.remaining() + body.remaining());
        message2.put(header);
        message2.put(body);
        message2.flip();
    }
}
