package com.amairovi.netty_in_action.chapter_5;

import com.amairovi.netty_in_action.Listing;
import io.netty.buffer.ByteBuf;

@Listing("5.1")
public class BackingArray {
    public static void main(String[] args) {
        ByteBuf heapBuf = null; // init properly
        if (heapBuf.hasArray()) {
            byte[] array = heapBuf.array();
            int offset = heapBuf.arrayOffset() + heapBuf.readerIndex();
            int length = heapBuf.readableBytes();
            handleArray(array, offset, length);
        }
    }

    private static void handleArray(final byte[] array, final int offset, final int length) {

    }
}
