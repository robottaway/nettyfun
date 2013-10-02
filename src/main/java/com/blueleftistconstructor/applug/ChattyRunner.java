package com.blueleftistconstructor.applug;

import io.netty.channel.ChannelHandlerContext;
import io.netty.util.concurrent.EventExecutor;

/**
 * A place where the code for long running application being connected to by
 * one or more clients would go.
 * 
 * @author rob
 *
 */
public class ChattyRunner extends AppPlug implements Runnable
{	
	// not exposed, can be modified through reflection if needed
	private boolean running = true;
	
	int ctr = 0;
	
	final String[] messages = {"toodles!", "siyonara!", "bonjour", "snoooowy", 
			"bada bing", "blimey", "cocooco", "knckrs", "sppemonkey", "zzzzzzzzz" };
	
	public ChattyRunner(EventExecutor evtEx) {
		super(evtEx);
	}
	
	/**
	 * First we add the context to the plug making it avaible for group 
	 * messaging, then we create a handler and return it.
	 */
	@Override
	public ClientHandler getClientHandlerForContext(ChannelHandlerContext ctx)
	{
		ClientOps ops = this.registerClientContext(ctx);
		return new ChattyClientHandler(ops);
	}

	/**
	 * We make this a runnable which'll be able to respond to events and message
	 * connected users.
	 */
	@Override
	public void run()
	{
		System.out.println("We start running......");
		while (running) {
			try {
				ctr++;
				String num = String.format("{ \"type\":\"num\", \"value\": %s }", ctr);
				sendAll(num);
				
				String word = String.format("{ \"type\":\"word\", \"value\": \"%s\" }", messages[ctr % messages.length]);
				sendAll(word);
				
				flushAllClients();
				
				Thread.sleep(1000);
			}
			catch (InterruptedException ie) {
				ie.printStackTrace();
				return;
			}
		}
	}

	@Override
	protected void handleClientCommand(String val)
	{
		// TODO Auto-generated method stub
	}
}