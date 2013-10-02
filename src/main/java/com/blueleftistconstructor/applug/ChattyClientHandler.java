package com.blueleftistconstructor.applug;

/**
 * Handle client state, dispatch messages interact with application.
 * 
 * @author rob
 *
 */
public class ChattyClientHandler implements ClientHandler
{
	ClientOps ops;
	
	public ChattyClientHandler(ClientOps ops) {
		this.ops = ops;
	}

	@Override
	public void handle(String val)
	{
		String talk = String.format("{ \"type\": \"talk\", \"value\": \"%s\" }", val);
		ops.sendToAllOthers(talk);
		talk = String.format("{ \"type\": \"talk\", \"value\": \"you sent: %s\" }", val);
		ops.writeBack(talk);
	}	
}
