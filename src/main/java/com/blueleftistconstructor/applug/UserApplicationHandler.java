package com.blueleftistconstructor.applug;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundMessageHandlerAdapter;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

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
	
	@Override
	public void messageReceived(ChannelHandlerContext ctx, TextWebSocketFrame frame) throws Exception
	{
		if (r == null) {
			createThread();
		}
		if (registered == false) {
			register(ctx);
		}
	}
	
	public static class AppRunner implements Runnable
	{
		List<ChannelHandlerContext> ctxs = new ArrayList<ChannelHandlerContext>();
				
		int ctr = 0;
		
		String[] messages = {"toodles!", "siyonara!", "bonjour" };
		
		ReadWriteLock lk = new ReentrantReadWriteLock(true);
		
		private class Mo implements ChannelFutureListener {

			ChannelHandlerContext ctx;
			
			public Mo(ChannelHandlerContext ctx) {
				this.ctx = ctx;
			}
			
			@Override
			public void operationComplete(ChannelFuture future)
					throws Exception
			{
				lk.writeLock().lock();
				try {
					ctxs.remove(ctx);
				}
				catch (Exception e) {
					e.printStackTrace();
				}
				finally {
					lk.writeLock().unlock();
				}
			}
		}
		
		public void addCtx(final ChannelHandlerContext ctx) {
			lk.writeLock().lock();
			try {
				ctxs.add(ctx);
				ctx.channel().closeFuture().addListener(new Mo(ctx));	
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			finally {
				lk.writeLock().unlock();
			}
		}
		
		private boolean none = true;
		
		protected void send() {
			lk.readLock().lock();
			try {
				if (ctxs.size() == 0 && !none) {
					none = true;
					System.out.println("Back to nada!");
				}
				else if (ctxs.size() > 0 && none){
					none = false;
					System.out.println("Got some now!");
				}
				ctr++;
				for (ChannelHandlerContext ctx : ctxs) {
					ctx.channel().write(new TextWebSocketFrame(""+ctr));
					String msg = messages[ctr % messages.length];
					ctx.channel().write(new TextWebSocketFrame(msg));
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			finally {
				lk.readLock().unlock();
			}
		}
		
		@Override
		public void run()
		{
			while (true) {
				send();
				try
				{
					Thread.sleep(100);
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}
			}
		}
	}
}