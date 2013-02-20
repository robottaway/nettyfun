package com.blueleftistconstructor.applug;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundMessageHandlerAdapter;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

/**
 * Handle the messages from web socket requests.
 * 
 * @author rob
 */
public class CustomTextFrameHandler extends ChannelInboundMessageHandlerAdapter<TextWebSocketFrame>
{
	@Override
	public void messageReceived(ChannelHandlerContext ctx, TextWebSocketFrame frame) throws Exception
	{
		String request = frame.getText();
		ctx.channel().write(new TextWebSocketFrame(request.toUpperCase()));
	}
}