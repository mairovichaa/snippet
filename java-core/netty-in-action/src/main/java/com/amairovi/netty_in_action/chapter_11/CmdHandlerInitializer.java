package com.amairovi.netty_in_action.chapter_11;

import com.amairovi.netty_in_action.Listing;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.handler.codec.LineBasedFrameDecoder;

import java.nio.charset.Charset;

@Listing("11.9")
public class CmdHandlerInitializer extends ChannelInitializer<Channel> {

    static final byte SPACE = (byte) ' ';

    @Override
    protected void initChannel(final Channel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new CmdDecoder(64 * 1024));
        pipeline.addLast(new CmdHandler());
    }

    public static final class Cmd {
        private final ByteBuf name;
        private final ByteBuf args;

        public Cmd(final ByteBuf name, final ByteBuf args) {
            this.name = name;
            this.args = args;
        }

        public ByteBuf name() {
            return name;
        }

        public ByteBuf args() {
            return args;
        }

        @Override
        public String toString() {
            return "Cmd{" +
                    "name=" + name.toString(Charset.defaultCharset()) +
                    ", args=" + args.toString(Charset.defaultCharset()) +
                    '}';
        }
    }

    public static final class CmdDecoder extends LineBasedFrameDecoder {
        public CmdDecoder(int maxLength) {
            super(maxLength);
        }

        @Override
        protected Object decode(final ChannelHandlerContext ctx, final ByteBuf buffer) throws Exception {
            ByteBuf frame = (ByteBuf) super.decode(ctx, buffer);
            if (frame == null) {
                return null;
            }
            int index = frame.indexOf(frame.readerIndex(), frame.writerIndex(), SPACE);
//            ByteBuf name = frame.slice(frame.readerIndex(), index);
//            ByteBuf args = frame.slice(index + 1, frame.writerIndex() - index - 1);
            ByteBuf name = frame.readBytes(index);
            ByteBuf args = frame.readBytes(frame.writerIndex() - index - 1);
            return new Cmd(name, args);
        }
    }

    public static final class CmdHandler extends SimpleChannelInboundHandler<Cmd> {

        @Override
        protected void channelRead0(final ChannelHandlerContext ctx, final Cmd msg) throws Exception {
            System.out.println("Receive a command: " + msg);
            // Do something with the command
        }
    }

}
