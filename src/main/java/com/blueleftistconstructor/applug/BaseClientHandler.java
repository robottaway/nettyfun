package com.blueleftistconstructor.applug;

/**
 * You should probably use this to create your client handler class.
 * 
 * If you do not you will need to make sure you either have properties named, or
 * setters for:
 * 
 * ops
 * appPlug
 * 
 * On your ClientHandler implementation class.
 * 
 * @author rob
 */
public abstract class BaseClientHandler<A extends AppPlug<?,?>> implements ClientHandler<A>
{
	private ClientOps ops;
	
	private A appPlug;
	
	public ClientOps getOps() {
		return ops;
	}
	
	public A getAppPlug() {
		return appPlug;
	}
}
