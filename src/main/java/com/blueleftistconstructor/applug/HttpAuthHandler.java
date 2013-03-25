package com.blueleftistconstructor.applug;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundMessageHandlerAdapter;
import io.netty.handler.codec.http.FullHttpRequest;

import com.blueleftistconstructor.web.SessionNotInCookieHeader;
import com.blueleftistconstructor.web.WebSession;

/**
 * Will implement security layer for incoming requests to App Plug server.
 *
 * @author rob
 */
public class HttpAuthHandler extends ChannelInboundMessageHandlerAdapter<FullHttpRequest>
{
	@Override
	public void messageReceived(ChannelHandlerContext ctx, FullHttpRequest req)
			throws Exception
	{
		if (req.headers().contains("Cookie")) {
			String cookie = req.headers().get("Cookie");
			try {
				WebSession ws = new WebSession(cookie, "user", "/", ".blueleftistconstructor.com", 60*60*24, false, false);
				//System.out.println("cookie value: "+ws.getSessionId());
			}
			catch (SessionNotInCookieHeader e) {}
		}
		
		ctx.pipeline().remove(this); // remove after auth'd
		req.retain();
		ctx.nextInboundMessageBuffer().add(req); // pass http req on
	}
}