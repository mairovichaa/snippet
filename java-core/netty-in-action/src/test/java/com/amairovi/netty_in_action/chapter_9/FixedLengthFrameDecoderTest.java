package com.amairovi.netty_in_action.chapter_9;

import com.amairovi.netty_in_action.Listing;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Listing("9.2")
class FixedLengthFrameDecoderTest {

    @Test
    public void testFramesDecoded() {
        ByteBuf buf = Unpooled.buffer();
        for (int i = 0; i < 9; i++) {
            buf.writeByte(i);
        }
        ByteBuf input = buf.duplicate();
        EmbeddedChannel channel = new EmbeddedChannel(new FixedLengthFrameDecoder(3));
        assertThat(channel.writeInbound(input.retain())).isTrue();
        assertThat(channel.finish()).isTrue();

        ByteBuf read = channel.readInbound();
        assertThat(read).isEqualTo(buf.readSlice(3));
        read.release();

        read = channel.readInbound();
        assertThat(read).isEqualTo(buf.readSlice(3));
        read.release();

        read = channel.readInbound();
        assertThat(read).isEqualTo(buf.readSlice(3));
        read.release();

        assertThat((ByteBuf) channel.readInbound()).isNull();
        buf.release();
    }

    @Test
    public void testFramesDecoded2() {
        ByteBuf buf = Unpooled.buffer();
        for (int i = 0; i < 9; i++) {
            buf.writeByte(i);
        }
        ByteBuf input = buf.duplicate();
        EmbeddedChannel channel = new EmbeddedChannel(new FixedLengthFrameDecoder(3));
        assertThat(channel.writeInbound(input.readBytes(2))).isFalse();
        assertThat(channel.writeInbound(input.readBytes(7))).isTrue();
        assertThat(channel.finish()).isTrue();

        ByteBuf read = channel.readInbound();
        assertThat(read).isEqualTo(buf.readSlice(3));
        read.release();

        read = channel.readInbound();
        assertThat(read).isEqualTo(buf.readSlice(3));
        read.release();

        read = channel.readInbound();
        assertThat(read).isEqualTo(buf.readSlice(3));
        read.release();

        assertThat((ByteBuf) channel.readInbound()).isNull();
        buf.release();
    }


}