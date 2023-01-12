package com.amairovi.netty_in_action.chapter_11;

import com.amairovi.netty_in_action.chapter_11.CmdHandlerInitializer.Cmd;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.jupiter.api.Test;

import java.nio.charset.Charset;

import static org.assertj.core.api.Assertions.assertThat;

class CmdHandlerInitializerTest {

    @Test
    public void testSingleCommandIsDecoded() {
        ByteBuf buf = Unpooled.buffer();
        buf.writeCharSequence("cmd1 arg1 arg2 arg3\n", Charset.defaultCharset());

        ByteBuf input = buf.duplicate();
        EmbeddedChannel channel = new EmbeddedChannel(new CmdHandlerInitializer.CmdDecoder(64 * 1024));

        assertThat(channel.writeInbound(input.retain())).isTrue();
        assertThat(channel.finish()).isTrue();

        Cmd cmd = channel.readInbound();
        assertThat(cmd).isNotNull();
        assertThat(cmd.name()).isEqualTo(buf.slice(0, "cmd1".length()));
        assertThat(cmd.args()).isEqualTo(buf.slice("cmd1".length(), "arg1 arg2 arg3".length()));

        buf.release();
    }

    @Test
    public void testSeveralCommandsAreDecoded() {
        ByteBuf buf = Unpooled.buffer();
        buf.writeCharSequence("cmd1 arg1 arg2 arg3\ncmd2 arg4 arg5 arg6\n", Charset.defaultCharset());

        ByteBuf input = buf.duplicate();
        EmbeddedChannel channel = new EmbeddedChannel(new CmdHandlerInitializer.CmdDecoder(64 * 1024));

        assertThat(channel.writeInbound(input.retain())).isTrue();
        assertThat(channel.finish()).isTrue();

        Cmd cmd = channel.readInbound();
        assertThat(cmd).isNotNull();
        assertThat(cmd.name()).isEqualTo(buf.slice(0, "cmd1".length()));
        assertThat(cmd.args()).isEqualTo(buf.slice("cmd1".length(), "arg1 arg2 arg3".length()));

        Cmd cmd2 = channel.readInbound();
        assertThat(cmd2).isNotNull();
        assertThat(cmd2.name()).isEqualTo(buf.slice("cmd1 arg1 arg2 arg3\n".length(), "cmd2".length()));
        assertThat(cmd2.args()).isEqualTo(buf.slice("cmd1 arg1 arg2 arg3\ncmd2".length(), "arg4 arg5 arg6".length()));
        buf.release();
    }

    @Test
    public void testMessageIsNotComplete() {
        ByteBuf buf = Unpooled.buffer();
        buf.writeCharSequence("cmd1 arg1 arg2 arg3\ncmd2 arg4", Charset.defaultCharset());

        ByteBuf input = buf.duplicate();
        EmbeddedChannel channel = new EmbeddedChannel(new CmdHandlerInitializer.CmdDecoder(64 * 1024));

        assertThat(channel.writeInbound(input.retain())).isTrue();
        assertThat(channel.finish()).isTrue();

        Cmd cmd = channel.readInbound();
        assertThat(cmd).isNotNull();
        assertThat(cmd.name()).isEqualTo(buf.slice(0, "cmd1".length()));
        assertThat(cmd.args()).isEqualTo(buf.slice("cmd1".length(), "arg1 arg2 arg3".length()));

        Cmd cmd2 = channel.readInbound();
        assertThat(cmd2).isNull();
        buf.release();
    }
}