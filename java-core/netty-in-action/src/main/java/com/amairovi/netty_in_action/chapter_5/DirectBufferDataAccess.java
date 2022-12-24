package com.amairovi.netty_in_action.chapter_5;

import io.netty.buffer.ByteBuf;

public class DirectBufferDataAccess {
    public static void main(String[] args) {
        ByteBuf directBuf = null; // initialize properly
        if (!directBuf.hasArray()) {
            int length = directBuf.readableBytes();
            byte[] array = new byte[length];
            directBuf.getBytes(directBuf.readerIndex(), array);
            handleArray(array, 0, length);
        }
    }

    private static void handleArray(final byte[] array, final int i, final int length) {

    }
}
