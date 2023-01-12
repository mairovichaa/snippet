package com.amairovi.netty_in_action.chapter_9;

import com.amairovi.netty_in_action.Listing;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.TooLongFrameException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Listing("9.6")
class FrameChunkDecoderTest {

    @Test
    public void testFramesDecoded() {
        ByteBuf buf = Unpooled.buffer();
        for (int i = 0; i < 9; i++) {
            buf.writeByte(i);
        }
        ByteBuf input = buf.duplicate();

        EmbeddedChannel channel = new EmbeddedChannel(new FrameChunkDecoder(3));

        assertThat(channel.writeInbound(input.readBytes(2))).isTrue();

        assertThatThrownBy(() -> channel.writeInbound(input.readBytes(4)))
                .isInstanceOf(TooLongFrameException.class);
        assertThat(channel.writeInbound(input.readBytes(3))).isTrue();
        assertThat(channel.finish()).isTrue();

        ByteBuf read = channel.readInbound();
        assertThat(read).isEqualTo(buf.readSlice(2));
        read.release();

        read = channel.readInbound();
        assertThat(read).isEqualTo(buf.skipBytes(4).readSlice(3));
        read.release();
        buf.release();
    }

}