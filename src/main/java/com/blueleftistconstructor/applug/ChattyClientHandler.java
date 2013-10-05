package com.blueleftistconstructor.applug;

/**
 * Handle client state, dispatch messages interact with application.
 * 
 * @author rob
 */
public class ChattyClientHandler extends BaseClientHandler<ChattyRunner>
{	
	@Override
	public void handle(String val)
	{
		val = val.trim();
		
		if (val.startsWith("addmessage")) {
			String[] parts = val.split("addmessage");
			if (parts.length > 1 && parts[1].trim().length() > 0) {
				String message = parts[1].trim();
				if (getAppPlug().addMessage(message)) {
					String talk = String.format("{ \"type\": \"talk\", \"value\": \"added message '%s'.\" }", message);
					getOps().writeBack(talk);
				}
				else {
					String talk = String.format("{ \"type\": \"talk\", \"value\": \"message '%s' already exists, try something new!\" }", message);
					getOps().writeBack(talk);
				}
			}
			else {
				String talk = "{ \"type\": \"talk\", \"value\": \"you need to include a message.\" }";
				getOps().writeBack(talk);
			}
			return;
		}
		
		String talk = String.format("{ \"type\": \"talk\", \"value\": \"%s\" }", val);
		getOps().sendToAllOthers(talk);
		talk = String.format("{ \"type\": \"talk\", \"value\": \"you sent: %s\" }", val);
		getOps().writeBack(talk);
	}	
}
