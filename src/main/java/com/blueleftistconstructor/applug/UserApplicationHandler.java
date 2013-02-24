package com.blueleftistconstructor.applug;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundMessageHandlerAdapter;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

/**
 * Handle the messages from web socket requests.
 * 
 * Here is where we would hand off the 
 * 
 * @author rob
 */
public class UserApplicationHandler extends ChannelInboundMessageHandlerAdapter<TextWebSocketFrame>
{
	String noop = null;
	
	@Override
	public void messageReceived(ChannelHandlerContext ctx, TextWebSocketFrame frame) throws Exception
	{
		if (noop == null) {
			System.out.println("null noop");
			noop = "noop";
		}
		else {
			System.out.println("noop already init");
		}
		String request = frame.getText();
		ctx.channel().write(new TextWebSocketFrame(request.toUpperCase()));
	}
}