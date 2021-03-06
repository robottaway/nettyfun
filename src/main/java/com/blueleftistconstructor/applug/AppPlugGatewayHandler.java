package com.blueleftistconstructor.applug;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

/**
 * Before we even venture into processing the websocket we first check that an
 * application exists for the URI requested.
 * 
 * @author rob
 *
 */
public class AppPlugGatewayHandler extends SimpleChannelInboundHandler<FullHttpRequest>
{
	public static final AttributeKey<AppPlug<?,?>> appPlugKey = new AttributeKey<AppPlug<?,?>>("app.plug");
	
	public static final Logger logger = LoggerFactory.getLogger(AppPlugGatewayHandler.class);
	
	public AppPlugGatewayHandler() {
		super(false);
	}
	
	private static final ByteBuf NOT_FOUND =
            Unpooled.unreleasableBuffer(Unpooled.copiedBuffer("NOT FOUND", CharsetUtil.US_ASCII));
	
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest req)
			throws Exception
	{
		String uri = req.getUri();
			
		AppPlug<?,?> ap = AppRegistry.getApp(uri);
		
		if (ap == null) {
			logger.info("No AppPlug found for uri (id) '{}'", uri);
			FullHttpResponse response = new DefaultFullHttpResponse(
                    HTTP_1_1, HttpResponseStatus.NOT_FOUND, NOT_FOUND);
			ctx.channel().writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
			return;
		}
		
		ctx.channel().attr(appPlugKey).set(ap);
		
		// add websocket handler for the request uri where app lives
		ctx.pipeline().addLast(new WebSocketServerProtocolHandler(uri));
		
		// now add our application handler
		ctx.pipeline().addLast(new UserApplicationHandler());
		
		// remove, app is attached and websocket handler in place
		ctx.pipeline().remove(this);
		
		// pass the request on
		ctx.fireChannelRead(req);
	}

}
