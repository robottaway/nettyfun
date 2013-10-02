package com.blueleftistconstructor.applug;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;

import com.blueleftistconstructor.web.WebSession;

/**
 * Handle the messages from web socket requests.
 * 
 * Here is where we would hand off the websocket to an application
 * 
 * @author rob
 */
public class UserApplicationHandler extends SimpleChannelInboundHandler<TextWebSocketFrame>
{		
	ClientHandler ci;
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception
	{
		cause.printStackTrace();
		ctx.close();
	}

	/**
	 * We implement this to catch the websocket handshake completing 
	 * successfully. At that point we'll setup this client connection.
	 */
	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt)
			throws Exception
	{
		if (evt == WebSocketServerProtocolHandler.ServerHandshakeStateEvent.HANDSHAKE_COMPLETE) {
			configureClient(ctx);
		}
	}
	
	/**
	 * Should end up being called after websocket handshake completes. Will 
	 * configure this client for communication with the application. 
	 */
	protected void configureClient(ChannelHandlerContext ctx) {
		System.out.println("Checking auth");
		
		WebSession sess = ctx.channel().attr(WebSession.webSessionKey).get();
		
		if (sess == null) {
			System.out.println("Closing websocket connection, no session found");
			ctx.writeAndFlush(new CloseWebSocketFrame(400, "NO SESSION FOUND")).addListener(ChannelFutureListener.CLOSE);
			return;
		}
		
		System.out.println("Got session id: "+sess.getSessionId());
		
		// TODO how to work in the user model to all this
		
		AppPlug ap = ctx.channel().attr(AppPlugHandler.appPlugKey).get();		
		ci = ap.getClientHandlerForContext(ctx);
	}

	/**
	 * When a message is sent into the app by the connected user this is 
	 * invoked.
	 */
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame frame) throws Exception
	{
		ci.handle(frame.text());
	}
}