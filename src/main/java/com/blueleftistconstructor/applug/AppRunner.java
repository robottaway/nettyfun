package com.blueleftistconstructor.applug;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.util.Iterator;

public class AppRunner implements Runnable
{
	final DefaultChannelGroup chGroup = new DefaultChannelGroup();
	
	// not exposed, can be modified through reflection if needed
	private boolean running = true;
	
	int ctr = 0;
	
	final String[] messages = {"toodles!", "siyonara!", "bonjour", "snoooowy", 
			"bada bing", "blimey", "cocooco", "knckrs", "sppemonkey", "zzzzzzzzz" };
	
	public void barkAtMoon() {
		System.out.println("bark!");
	}
	
	public Iterator<Channel> channelIterator() {
		return chGroup.iterator();
	}
	
	public void addCtx(final ChannelHandlerContext ctx) {
		chGroup.add(ctx.channel());
	}
	
	protected void send(String msg) {
		chGroup.write(new TextWebSocketFrame(msg));
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
				Thread.sleep(100);
			}
			catch (InterruptedException ie) {
				ie.printStackTrace();
				return;
			}
		}
	}
}