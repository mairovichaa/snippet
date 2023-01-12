package com.amairovi.netty_in_action.chapter_10;

import com.amairovi.netty_in_action.Listing;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import io.netty.handler.codec.http.websocketx.*;

import java.util.List;

import static com.amairovi.netty_in_action.chapter_10.WebSocketConvertHandler.MyWebSocketFrame.FrameType.*;

@Listing("10.7")
public class WebSocketConvertHandler extends MessageToMessageCodec<WebSocketFrame, WebSocketConvertHandler.MyWebSocketFrame> {

    @Override
    protected void encode(final ChannelHandlerContext ctx, final MyWebSocketFrame msg, final List<Object> out) throws Exception {
        ByteBuf payload = msg.getData().duplicate().retain();
        switch (msg.getType()) {
            case BINARY:
                out.add(new BinaryWebSocketFrame(payload));
                break;
            case TEXT:
                out.add(new TextWebSocketFrame(payload));
                break;
            case CLOSE:
                out.add(new CloseWebSocketFrame(true, 0, payload));
                break;
            case CONTINUATION:
                out.add(new ContinuationWebSocketFrame(payload));
                break;
            case PONG:
                out.add(new PongWebSocketFrame(payload));
                break;
            case PING:
                out.add(new PingWebSocketFrame(payload));
                break;
            default:
                throw new IllegalStateException("Unsupported websocket msg " + msg);
        }
    }

    @Override
    protected void decode(final ChannelHandlerContext ctx, final WebSocketFrame msg, final List<Object> out) throws Exception {
        ByteBuf payload = msg.content().duplicate().retain();
        if (msg instanceof BinaryWebSocketFrame) {
            out.add(new MyWebSocketFrame(BINARY, payload));
        } else if (msg instanceof CloseWebSocketFrame) {
            out.add(new MyWebSocketFrame(CLOSE, payload));
        } else if (msg instanceof PingWebSocketFrame) {
            out.add(new MyWebSocketFrame(PING, payload));
        } else if (msg instanceof PongWebSocketFrame) {
            out.add(new MyWebSocketFrame(PONG, payload));
        } else if (msg instanceof TextWebSocketFrame) {
            out.add(new MyWebSocketFrame(TEXT, payload));
        } else if (msg instanceof ContinuationWebSocketFrame) {
            out.add(new MyWebSocketFrame(CONTINUATION, payload));
        } else {
            throw new IllegalStateException("Unsupported websocket msg " + msg);
        }
    }

    public static final class MyWebSocketFrame {
        public enum FrameType {
            BINARY,
            CLOSE,
            PING,
            PONG,
            TEXT,
            CONTINUATION
        }

        private final FrameType type;
        private final ByteBuf data;

        public MyWebSocketFrame(FrameType type, ByteBuf data) {
            this.type = type;
            this.data = data;
        }

        public FrameType getType() {
            return type;
        }

        public ByteBuf getData() {
            return data;
        }
    }
}