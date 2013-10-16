package com.blueleftistconstructor.applug;

import io.netty.util.concurrent.EventExecutor;

import java.util.HashSet;
import java.util.Set;

/**
 * A place where the code for long running application being connected to by
 * one or more clients would go.
 * 
 * @author rob
 */
public class ChattyRunner extends AppPlug<ChattyRunner, ChattyClientHandler> implements Runnable
{	
	// not exposed, can be modified through reflection if needed
	private boolean running = true;
	
	private int ctr = 0;
	
	private final Set<String> messages = new HashSet<String>();
	
	private final String[] initMessages = new String[]{"toodles!", "siyonara!", "bonjour", "bada bing", "blimey"};
	
	public ChattyRunner(EventExecutor evtEx) {
		super(ChattyClientHandler.class, evtEx);
		for (String message : initMessages) {
			messages.add(message);
		}
	}
	
	public synchronized boolean addMessage(String message) {
		return messages.add(message);
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
				
				String message = messages.toArray(new String[]{})[ctr % messages.size()];
				String word = String.format("{ \"type\":\"word\", \"value\": \"%s\" }", message);
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
}