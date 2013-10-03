package com.blueleftistconstructor.applug;

/**
 * Handle client state, dispatch messages interact with application.
 * 
 * @author rob
 */
public class ChattyClientHandler implements ClientHandler<ChattyRunner>
{
	private ClientOps ops;

	private ChattyRunner cr;
	
	@Override
	public void handle(String val)
	{
		val = val.trim();
		if (val.startsWith("addmessage")) {
			String[] parts = val.split("addmessage");
			if (parts.length > 1 && parts[1].trim().length() > 0) {
				String message = parts[1].trim();
				if (cr.addMessage(message)) {
					String talk = String.format("{ \"type\": \"talk\", \"value\": \"added message '%s'.\" }", message);
					ops.writeBack(talk);
				}
				else {
					String talk = String.format("{ \"type\": \"talk\", \"value\": \"message '%s' already exists, try something new!\" }", message);
					ops.writeBack(talk);
				}
			}
			else {
				String talk = "{ \"type\": \"talk\", \"value\": \"you need to include a message.\" }";
				ops.writeBack(talk);
			}
			return;
		}
		
		String talk = String.format("{ \"type\": \"talk\", \"value\": \"%s\" }", val);
		ops.sendToAllOthers(talk);
		talk = String.format("{ \"type\": \"talk\", \"value\": \"you sent: %s\" }", val);
		ops.writeBack(talk);
	}

	/**
	 * Required so that we can set app?
	 */
	public void setAppPlug(ChattyRunner app) {
		this.cr = app;
	}
	
	@Override
	public ChattyRunner getAppPlug()
	{
		return cr;
	}	
}
