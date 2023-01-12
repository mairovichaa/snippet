package com.amairovi.netty_in_action.chapter_12;

import com.amairovi.netty_in_action.Listing;
import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.stream.ChunkedNioFile;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

@Listing("12.1")
public class HttpRequestHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    private static final Logger log = Logger.getLogger(HttpRequestHandler.class.getName());

    // resources/chapter_12/index.html
    private static final String PATH_TO_INDEX_HTML = "<path-to-index.html>";
    private static final File INDEX;
    private final String wsUri;
    private final ChatService chatService;

    static {
        Path indexHtml = Paths.get(PATH_TO_INDEX_HTML);
        log.log(Level.INFO, "Path to index.html: " + indexHtml);
        INDEX = new File(indexHtml.toUri());
    }

    public HttpRequestHandler(final String wsUri, final ChatService chatService) {
        this.wsUri = wsUri;
        this.chatService = chatService;
    }

    @Override
    protected void channelRead0(final ChannelHandlerContext ctx, final FullHttpRequest request) throws Exception {
        if (request.uri().startsWith(wsUri)) {
            QueryStringDecoder decoder = new QueryStringDecoder(request.uri());
            String username = decoder.parameters().get("username").get(0);
            chatService.addUser(username, ctx.channel());

            ctx.fireChannelRead(request.retain());
        } else {
            if (HttpUtil.is100ContinueExpected(request)) {
                send100Continue(ctx);
            }
            RandomAccessFile file = new RandomAccessFile(INDEX, "r");
            DefaultHttpResponse response = new DefaultHttpResponse(request.protocolVersion(), HttpResponseStatus.OK);

            boolean keepAlive = HttpUtil.isKeepAlive(request);
            if (keepAlive) {
                response.headers()
                        .set(HttpHeaderNames.CONTENT_LENGTH, file.length());
                response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
            }

            ctx.write(response);

            if (ctx.pipeline().get(SslHandler.class) == null) {
                ctx.write(new DefaultFileRegion(file.getChannel(), 0, file.length()));
            } else {
                ctx.write(new ChunkedNioFile(file.getChannel()));
            }
            ChannelFuture future = ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);
            if (!keepAlive) {
                future.addListener(ChannelFutureListener.CLOSE);
            }
        }
    }

    private static void send100Continue(ChannelHandlerContext ctx) {
        DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.CONTINUE);
        ctx.writeAndFlush(response);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

}
