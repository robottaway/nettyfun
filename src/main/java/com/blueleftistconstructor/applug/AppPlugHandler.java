package com.blueleftistconstructor.applug;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.util.AttributeKey;
import io.netty.util.CharsetUtil;
import static io.netty.handler.codec.http.HttpVersion.*;

public class AppPlugHandler extends SimpleChannelInboundHandler<FullHttpRequest>
{
	public static final AttributeKey<ChattyRunner> appPlugKey = new AttributeKey<ChattyRunner>("app.plug");
	
	public AppPlugHandler() {
		super(false);
	}
	
	private static final ByteBuf NOT_FOUND =
            Unpooled.unreleasableBuffer(Unpooled.copiedBuffer("NOT FOUND", CharsetUtil.US_ASCII));
	
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest req)
			throws Exception
	{
		String uri = req.getUri();
	
		System.out.println("Going to look up the app plug for uri: "+uri);
		
		ChattyRunner ar = AppRegistry.getApp(uri);
		
		if (ar == null) {
			FullHttpResponse response = new DefaultFullHttpResponse(
                    HTTP_1_1, HttpResponseStatus.NOT_FOUND, NOT_FOUND);
			ctx.channel().writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
			return;
		}
		
		ctx.channel().attr(appPlugKey).set(ar);
		
		// add websocket handler for the request uri if application found
		ctx.pipeline().addAfter(ctx.name(), HttpSessionHandler.class.getName(), new WebSocketServerProtocolHandler(uri));
		
		// remove, app is attached and websocket handler in place
		ctx.pipeline().remove(this);
		
		// pass the request on
		ctx.fireChannelRead(req);
	}

}
