package com.amairovi.netty_in_action.chapter_9;

import com.amairovi.netty_in_action.Listing;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Listing("9.4")
class AbsIntegerEncoderTest {

    @Test
    public void testEncoded() {
        ByteBuf buf = Unpooled.buffer();
        for (int i = 1; i < 10; i++) {
            buf.writeInt(i);
        }

        EmbeddedChannel channel = new EmbeddedChannel(new AbsIntegerEncoder());
        assertThat(channel.writeOutbound(buf)).isTrue();
        assertThat(channel.finish()).isTrue();

        for (int i = 1; i < 10; i++) {
            assertThat((int) channel.readOutbound()).isEqualTo(i);
        }
        assertThat((Integer) channel.readOutbound()).isNull();
    }

}