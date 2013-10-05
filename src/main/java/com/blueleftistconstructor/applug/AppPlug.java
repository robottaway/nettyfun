package com.blueleftistconstructor.applug;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.EventExecutor;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.azeckoski.reflectutils.ReflectUtils;

/**
 * Base class to extend for the application.
 * 
 * @author rob
 */
public abstract class AppPlug<A extends AppPlug<A, B>, B extends ClientHandler<A>>
{
	private final DefaultChannelGroup chGroup;
		
	private final Class<? extends B> clientHandlerClass;
	
	/**
	 * TODO: maybe these should be setters?
	 */
	public AppPlug(Class<? extends B> clientHandlerClass, EventExecutor evtEx) {
		this.clientHandlerClass = clientHandlerClass;
		this.chGroup = new DefaultChannelGroup(evtEx);
	}
	
	public Iterator<Channel> channelIterator() {
		return chGroup.iterator();
	}
	
	/**
	 * Add given Netty context to channel group and build operations to return
	 * for use in buidling a client handler.
	 */
	protected ClientOps registerClientContext(final ChannelHandlerContext ctx) {
		chGroup.add(ctx.channel());
		final AppPlug<A, B> ap = this;
		// TODO: probably want to add the ClientOps to the user mode too
		return new ClientOps()
		{
			@Override
			public void sendToAllOthers(String val) {
				ap.sendAllBut(val, ctx.channel());
			}
			
			@Override
			public void writeBack(String val) {
				ctx.writeAndFlush(new TextWebSocketFrame(val));
			}
		};
	}
	
	/**
	 * Send given message to all members of channel group.
	 */
	protected void sendAll(String msg) {
		chGroup.write(new TextWebSocketFrame(msg));
	}
	
	/**
	 * Allow sending a message to all members of group except those given.
	 */
	protected void sendAllBut(String msg, Channel...channels) {
		List<Channel> channelList = Arrays.asList(channels);
		for (Iterator<Channel> it = chGroup.iterator(); it.hasNext();) {
			Channel ch = it.next();
			if (channelList.contains(ch)) continue;
			ch.writeAndFlush(new TextWebSocketFrame(msg));
		}
	}

	/**
	 * Flush the connected clients.
	 */
	protected void flushAllClients() {
		chGroup.flush();
	}
	
	/**
	 * Get a client handler, responsible for processing the websocket values
	 * passed in by the client.
	 */
	protected B getClientHandlerForContext(ChannelHandlerContext ctx)
	{
		B inst = ReflectUtils.getInstance().constructClass(clientHandlerClass);
		ClientOps ops = this.registerClientContext(ctx);
		ReflectUtils ru = ReflectUtils.getInstance();
		ru.setFieldValue(inst, "ops", ops);
		ru.setFieldValue(inst, "appPlug", this);
		return inst;
	}
}
