package com.blueleftistconstructor.applug;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.EventExecutor;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * A place where the code for long running application being connected to by
 * one or more clients would go.
 * 
 * @author rob
 *
 */
public class AppRunner implements Runnable
{
	final DefaultChannelGroup chGroup;
	
	// not exposed, can be modified through reflection if needed
	private boolean running = true;
	
	int ctr = 0;
	
	final String[] messages = {"toodles!", "siyonara!", "bonjour", "snoooowy", 
			"bada bing", "blimey", "cocooco", "knckrs", "sppemonkey", "zzzzzzzzz" };
	
	public AppRunner(EventExecutor evtEx) {
		this.chGroup = new DefaultChannelGroup(evtEx);
	}
	
	public Iterator<Channel> channelIterator() {
		return chGroup.iterator();
	}
	
	/**
	 * Add given Netty context to channel group.
	 */
	public void addCtx(final ChannelHandlerContext ctx) {
		chGroup.add(ctx.channel());
	}
	
	/**
	 * Send given message to all members of channel group.
	 */
	protected void send(String msg) {
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
	
	@Override
	public void run()
	{
		System.out.println("We start running......");
		while (running) {
			try {
				ctr++;
				String num = String.format("{ \"type\":\"num\", \"value\": %s }", ctr);
				send(num);
				
				String word = String.format("{ \"type\":\"word\", \"value\": \"%s\" }", messages[ctr % messages.length]);
				send(word);
				
				chGroup.flush();
				
				Thread.sleep(1000);
			}
			catch (InterruptedException ie) {
				ie.printStackTrace();
				return;
			}
		}
	}
}