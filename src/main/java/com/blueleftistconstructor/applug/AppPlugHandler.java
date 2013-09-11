package com.blueleftistconstructor.applug;

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
import static io.netty.handler.codec.http.HttpVersion.*;

public class AppPlugHandler extends SimpleChannelInboundHandler<FullHttpRequest>
{
	public static final AttributeKey<AppRunner> appPlugKey = new AttributeKey<AppRunner>("app.plug");
	
	public AppPlugHandler() {
		super(false);
	}
	
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest req)
			throws Exception
	{
		String uri = req.getUri();
	
		System.out.println("Going to look up the app plug for uri: "+uri);
		
		AppRunner ar = AppRegistry.getApp(uri);
		
		if (ar == null) {
			FullHttpResponse response = new DefaultFullHttpResponse(
                    HTTP_1_1, HttpResponseStatus.NOT_FOUND, Unpooled.wrappedBuffer("NOT FOUND".getBytes()));
			ctx.channel().writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
		}
		
		ctx.channel().attr(appPlugKey).set(ar);
		
		// add websocket handler for the request uri
		ctx.pipeline().addAfter(ctx.name(), HttpSessionHandler.class.getName(), new WebSocketServerProtocolHandler(uri));
		
		// remove, app is attached and websocket handler in place
		ctx.pipeline().remove(this);
		
		// pass the request on
		ctx.fireChannelRead(req);
	}

}
