package com.blueleftistconstructor.applug;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
	private ClientHandler<?> ci;
		
	private static final Logger logger = LoggerFactory.getLogger(UserApplicationHandler.class);
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception
	{
		logger.error("Error caught while processing users applicaion.", cause);
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
		logger.debug("Checking auth");
		
		// TODO: we should support many different authentication standards here
		// building and auth token and passing it down where the AppPlug can
		// then build the actual user and bind it with the contexts channel.
		
		WebSession sess = ctx.channel().attr(WebSession.webSessionKey).get();
		
		// or basic, or digest..
		// possibly OAuth
		// even facebook or google?
		
		// we now have some principal for the user, from whatever authentication format we used.
		// call webservice to get user data such as authorities, and possibly any channel specific config (ignores, preferences)
		// when webservice is called it can register that the user is on the given server, later if the user model get's 
		// changed we should fire an event to this AppPlug and update the user model here (privileges comes to mind).
		// add user data to context.
		
		if (sess == null) {
			logger.info("Closing websocket connection, unable to authenticate");
			ctx.writeAndFlush(new CloseWebSocketFrame(400, "UNABLE TO AUTHENTICATE")).addListener(ChannelFutureListener.CLOSE);
			return;
		}
		
		logger.debug("Got session id: "+sess.getSessionId());

		AppPlug<?,?> ap = ctx.channel().attr(AppPlugGatewayHandler.appPlugKey).get();
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