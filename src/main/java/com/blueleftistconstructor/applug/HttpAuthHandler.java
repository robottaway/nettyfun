package com.blueleftistconstructor.applug;

import static io.netty.handler.codec.http.HttpHeaders.isKeepAlive;
import static io.netty.handler.codec.http.HttpResponseStatus.UNAUTHORIZED;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundMessageHandlerAdapter;
import io.netty.handler.codec.http.DefaultHttpResponse;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;

/**
 * Will implement security for BASIC, DIGEST and Cookie based auth.
 *
 * @author rob
 */
public class HttpAuthHandler extends ChannelInboundMessageHandlerAdapter<HttpRequest>
{
	@Override
	public void messageReceived(ChannelHandlerContext ctx, HttpRequest req)
			throws Exception
	{
		String uriString = req.getUri();
		System.out.println(uriString);
		
		if (req.getHeaderNames().contains("Cookie")) {
			// handle cookie based auth
			String cookie = req.getHeader("Cookie");
			System.out.printf("Got cookie: %s\n", cookie);
		}
		else if (req.getHeaderNames().contains("Authorization")) {
			String value = req.getHeader("Authorization");
			if (value.startsWith("Basic")) {
				
			}
			else if (value.startsWith("Digest")) {
				
			}
			else {
				sendHttpResponse(ctx, req, new DefaultHttpResponse(HTTP_1_1, UNAUTHORIZED));
				return;
			}
		}
		
		ctx.pipeline().remove(this); // remove after auth'd
		ctx.nextInboundMessageBuffer().add(req); // pass http req on
        ctx.fireInboundBufferUpdated();
	}
	
	private static void sendHttpResponse(ChannelHandlerContext ctx, HttpRequest req, HttpResponse res) {
        ChannelFuture f = ctx.channel().write(res);
        if (!isKeepAlive(req) || res.getStatus().getCode() != 200) {
            f.addListener(ChannelFutureListener.CLOSE);
        }
    }
}