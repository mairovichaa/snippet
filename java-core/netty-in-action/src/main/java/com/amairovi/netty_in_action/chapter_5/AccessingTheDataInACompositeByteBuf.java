package com.amairovi.netty_in_action.chapter_5;

import io.netty.buffer.CompositeByteBuf;
import io.netty.buffer.Unpooled;

public class AccessingTheDataInACompositeByteBuf {
    public static void main(String[] args) {
        CompositeByteBuf compBuf = Unpooled.compositeBuffer();
        int length = compBuf.readableBytes();
        byte[] array = new byte[length];
        compBuf.getBytes(compBuf.readerIndex(), array);
        handleArray(array, 0, array.length);
    }

    private static void handleArray(final byte[] array, final int i, final int length) {

    }
}
