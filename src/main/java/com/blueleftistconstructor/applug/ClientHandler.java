package com.blueleftistconstructor.applug;

/**
 * Simple interface to hide details of the handling of websocket value.
 * 
 * @author rob
 *
 */
public interface ClientHandler<A extends AppPlug<?,?>>
{
	public void handle(String val);
		
	public A getAppPlug();
	
	public ClientOps getOps();
}
