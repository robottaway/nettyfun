package com.blueleftistconstructor.applug;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;

import com.blueleftistconstructor.web.SessionNotInCookieHeader;
import com.blueleftistconstructor.web.WebSession;

/**
 * Will implement layer connecting the HTTP layer to the websocket layer.
 * 
 * Will attempt to find the requestor's session cookie. If the cookie is not
 * found an error message will be returned with a HTTP 400 response code.
 *
 * @author rob
 */
public class HttpSessionHandler extends SimpleChannelInboundHandler<FullHttpRequest>
{
	
	public HttpSessionHandler() {
		super(false);
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception
	{
		cause.printStackTrace();
		ctx.close();
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest req)
			throws Exception
	{
		System.out.println("Looking for the session in the Cookie");
		if (req.headers().contains("Cookie")) {
			String cookie = req.headers().get("Cookie");
			System.out.println("Got Cookie: "+cookie);
			try {
				WebSession ws = new WebSession(cookie, "user", "/", ".blueleftistconstructor.com", 60*60*24, false, false);
				ctx.channel().attr(WebSession.webSessionKey).set(ws);
			}
			catch (SessionNotInCookieHeader e) {}
		}
		
		ctx.pipeline().remove(this); // remove after auth'd
		
		ctx.fireChannelRead(req);
	}
}