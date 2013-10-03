package com.blueleftistconstructor.applug;

/**
 * Operations that a client will need to communicate with other clients and the
 * application.
 * 
 * @author rob
 */
public interface ClientOps
{
	/**
	 * Send to all others connected to resource
	 */
	void sendToAllOthers(String val);
	
	/**
	 * Write back string to user
	 */
	void writeBack(String val);
}
