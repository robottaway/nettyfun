package com.blueleftistconstructor.applug;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundMessageHandlerAdapter;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.util.Iterator;

import com.blueleftistconstructor.web.WebSession;

/**
 * Handle the messages from web socket requests.
 * 
 * Here is where we would hand off the websocket to an application
 * 
 * @author rob
 */
public class UserApplicationHandler extends ChannelInboundMessageHandlerAdapter<TextWebSocketFrame>
{
	static AppRunner r = null;
		
	boolean registered = false;
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception
	{
		cause.printStackTrace();
		super.exceptionCaught(ctx, cause);
	}

	synchronized static void createThread() {
		if (r != null) return;
		r = new AppRunner();
		Thread t = new Thread(r);
		t.start();
	}
	
	synchronized void register(ChannelHandlerContext ctx) {
		if (registered == true) return;
		r.addCtx(ctx);
		registered = true;
	}
	
	/**
	 * When a message is sent into the app by the connected user this is 
	 * invoked.
	 */
	@Override
	public void messageReceived(ChannelHandlerContext ctx, TextWebSocketFrame frame) throws Exception
	{
		if (r == null) {
			createThread();
		}
		if (registered == false) {
			register(ctx);
		}
		
		WebSession sess = ctx.channel().attr(WebSession.webSessionKey).get();
		if (sess != null) System.out.println("Got session id: "+sess.getSessionId());
		
		String talk = String.format("{ \"type\": \"talk\", \"value\": \"%s\" }", frame.text().replaceAll("\n", "\\\\n"));
		TextWebSocketFrame newframe = new TextWebSocketFrame(talk);
		for (Iterator<Channel> it = r.channelIterator(); it.hasNext();) {
			Channel ch = it.next();
			if (ch.equals(ctx.channel())) continue;
			ch.write(newframe);
		}
	}
}